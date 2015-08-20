package com.avalonconsult.couchbase.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        Map<String, Integer> topicThreadMap = new HashMap<String, Integer>();
        topicThreadMap.put(KAFKA_TOPIC, NUM_THREADS);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreamMap = consumer.createMessageStreams(topicThreadMap);
        List<KafkaStream<byte[], byte[]>> consumerStreams = consumerStreamMap.get(KAFKA_TOPIC);

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < consumerStreams.size(); i++) {
            executor.submit(new KafkaAlertConsumer(consumerStreams.get(i), i));
        }
    }
}
