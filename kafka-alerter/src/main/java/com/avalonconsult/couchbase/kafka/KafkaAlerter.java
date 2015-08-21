package com.avalonconsult.couchbase.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kruthar on 8/20/15.
 */
public class KafkaAlerter {
    private static final String ZOOKEEPER_ADDRESS = "kafka.vagrant:2181";
    private static final String CONSUMER_GROUP = "alerters";
    private static final String KAFKA_TOPIC = "transactions";
    private static final int NUM_THREADS = 4;

    public static void main(String args[]) {
        Properties props = new Properties();
        props.put("zookeeper.connect", ZOOKEEPER_ADDRESS);
        props.put("group.id", CONSUMER_GROUP);

        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        List<KafkaStream<byte[], byte[]>> consumerStreams = consumer.createMessageStreamsByFilter(new Whitelist("^" + KAFKA_TOPIC + "$"), NUM_THREADS);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < consumerStreams.size(); i++) {
            executor.submit(new KafkaAlertConsumer(consumerStreams.get(i), i));
        }
    }
}
