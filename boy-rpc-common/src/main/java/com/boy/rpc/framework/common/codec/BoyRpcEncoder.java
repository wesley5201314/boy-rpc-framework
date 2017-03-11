package com.boy.rpc.framework.common.codec;

import com.boy.rpc.framework.common.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by wesley on 2017-03-10.
 * rpc 编码,基于netty
 */
public class BoyRpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public BoyRpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
