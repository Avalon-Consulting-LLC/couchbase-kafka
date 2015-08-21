package com.avalonconsult.couchbase.kafka;

import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.json.JSONObject;

import java.nio.charset.Charset;

/**
 * Created by kruthar on 8/20/15.
 */
public class KafkaAlertConsumer implements Runnable{
    private static final Double ALERT_LIMIT = 9000.00;

    private final KafkaStream<byte[], byte[]> stream;
    private final int threadNum;

    public KafkaAlertConsumer(KafkaStream<byte[], byte[]> s, int t) {
        stream = s;
        threadNum = t;
    }

    public void run() {
        for (MessageAndMetadata<byte[], byte[]> message : stream) {
            String content = new String(message.message(), Charset.forName("UTF-8"));
            JSONObject json = new JSONObject(content);
            Double amount = json.getDouble("amount");

            if (amount > ALERT_LIMIT) {
                System.out.println("Thread " + threadNum + " caught amount limit: " + content);
            }
        }
    }
}