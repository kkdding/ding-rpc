package com.ding.dingrpc.protocol;

/**
 * 协议信息常量
 * @author: Dding
 * @date: 2024/09/23
 **/
public class ProtocolConstant {
    /**
     * 消息头长度
     */
    public static final int MESSAGE_HEADER_LENGTH = 17;

    /**
     * 协议魔数
     */
    public static final byte PROTOCOL_MAGIC = 0x1;

    /**
     * 协议版本号
     */
    public static final byte PROTOCOL_VERSION = 0x1;
}
