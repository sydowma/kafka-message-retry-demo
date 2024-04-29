package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.io.Output;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.tomcat.util.security.KeyStoreUtil;

import java.io.ByteArrayOutputStream;

public class BillSerializer implements Serializer<Bill> {
    @Override
    public byte[] serialize(String topic, Bill data) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        KryoUtil.getKryo().writeObject(output, data);
        output.close(); // Ensure all data is flushed and stream is closed
        return outputStream.toByteArray();
    }
}
