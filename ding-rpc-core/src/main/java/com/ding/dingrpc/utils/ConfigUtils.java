package com.ding.dingrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * 配置工具类
 *
 * @author: Dding
 * @date: 2024/09/18
 **/
public class ConfigUtils {

    private static Props props;

    private ConfigUtils(){
        throw new AssertionError();
    };

    /**
     * 加载配置对象
     *
     * @param tClass
     * @param prefix
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        loadConfig(tClass, prefix, "");
        return props.toBean(tClass);
    }

    /**
     * 加载配置对象，支持区分环境
     *
     * @param tClass
     * @param prefix
     * @param environment
     * @param <T>
     * @return
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        // 拼接配置文件名
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".yml");
        String ymlFile = configFileBuilder.toString();

        props = new Props(ymlFile);
        return props.toBean(tClass, prefix);
    }

}
