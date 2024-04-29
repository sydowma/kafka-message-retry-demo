package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.io.Input;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;

public class BillDeserializer implements Deserializer<Bill> {
    @Override
    public Bill deserialize(String topic, byte[] data) {

        if (data == null || data.length == 0) {
            return null; // or handle the case where no data is available
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Input input = new Input(inputStream);
        return KryoUtil.getKryo().readObject(input, Bill.class);
    }

}
