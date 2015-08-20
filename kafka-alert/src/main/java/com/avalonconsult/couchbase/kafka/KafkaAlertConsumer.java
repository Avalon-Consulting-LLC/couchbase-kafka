package com.avalonconsult.couchbase.kafka;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by kruthar on 8/20/15.
 */
public class KafkaAlertConsumer implements Runnable{
    private final Double ALERT_LIMIT = 9000.00;

    private KafkaStream stream;
    private int threadNum;
    private JSONParser parser;

    public KafkaAlertConsumer(KafkaStream s, int t) {
        stream = s;
        threadNum = t;
        parser = new JSONParser();
    }

    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

        while (iterator.hasNext()) {
            String message = new String(iterator.next().message());
            JSONObject json = null;
            Double amount = null;

            try {
                json = (JSONObject) parser.parse(message);
            } catch (ParseException e) {
                System.out.println("Thread " + threadNum + " failed to parse message: '" + message  + "' with error: " + e);
            }

            try {
                amount = Double.parseDouble(json.get("amount").toString());
            } catch (NumberFormatException e) {
                System.out.println("Thread " + threadNum + " failed to parse amount: '" + message  + "' with error: " + e);
            }

            if (amount > ALERT_LIMIT) {
                System.out.println("Thread " + threadNum + " caught amount limit: " + message);
            }
        }
    }
}