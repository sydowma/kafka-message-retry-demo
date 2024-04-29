package com.magaofei.kafkamessageretrydemo;

import com.esotericsoftware.kryo.Kryo;

import java.math.BigDecimal;

public class KryoUtil {

    private static final Kryo kryo = new Kryo();

    static {
        kryo.register(BigDecimal.class);
        kryo.register(Bill.class);
    }

    public static Kryo getKryo() {
        return kryo;
    }


}
