package com.ding.dingrpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.ding.dingrpc.config.RegistryConfig;
import com.ding.dingrpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * etcd 注册中心实现
 * @author: Dding
 * @date: 2024/09/21
 **/
@Slf4j
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;

    /**
     * 本机注册的节点集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeSet = new HashSet<>();

    /**
     * 注册中心服务元信息缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 正在监听的 key 集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();


    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc-etcd/";

    /**
     * 初始化
     *
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    /**
     * 注册服务（服务端）
     *
     * @param serviceMetaInfo
     */
    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 创建 Lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();

        // 创建一个 30 秒的租约
        long leaseId = leaseClient.grant(30).get().getID();

        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        // 将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        // 添加节点信息到本地缓存
        localRegisterNodeSet.add(registerKey);
    }

    /**
     * 注销服务（服务端）
     *
     * @param serviceMetaInfo
     */
    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registryKey, StandardCharsets.UTF_8)).get();
        // 将节点信息从本地缓存删除
        localRegisterNodeSet.remove(registryKey);
    }

    /**
     * 服务发现（获取某服务的所有节点，消费端）
     *
     * @param serviceKey 服务键名
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        // 优先读取本地缓存
        List<ServiceMetaInfo> cacheList = registryServiceCache.readCache(serviceKey);
        if (cacheList != null) {
            log.info("从本地缓存读取服务元信息");
            return cacheList;
        }

        String searchPrefix = ETCD_ROOT_PATH + serviceKey + '/';
        log.info("searchPrefix: " + searchPrefix);
        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                            ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                            getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        // 监听节点key变化
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);
                        log.info("监听节点：" + key);

                        // 获取服务信息
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());
            // 写入本地缓存
            registryServiceCache.writeCache(serviceKey, serviceMetaInfoList);

            log.info("从注册中心读取服务元信息");
            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败", e);
        }
    }

    /**
     * 服务销毁
     */
    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        // 下线节点
        for (String key : localRegisterNodeSet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RuntimeException(key + "下线失败", e);
            }
        }

        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }

    /**
     * 心跳检测（服务端）
     */
    @Override
    public void heartBeat() {

        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String key : localRegisterNodeSet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        // 该节点已过期（需要重启节点才能重新注册）
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        // 节点未过期，重新注册（相当于续签）
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(key + "续签失败", e);
                    }
                }
            }
        });

        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 监听服务（消费端）
     *
     * @param serviceNodeKey
     */
    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();

        // 未被监听的节点,开启监听
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                for (WatchEvent event : watchResponse.getEvents()) {
                    switch (event.getEventType()) {
                        case DELETE:
                            String serviceKey = serviceNodeKey.split("/")[2];
                            log.info("检测serviceKey: {}", serviceKey);
                            registryServiceCache.removeCache(serviceKey);
                            log.info("节点{}本地缓存被删除", event.getKeyValue().getKey().toString(StandardCharsets.UTF_8));
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }
}
