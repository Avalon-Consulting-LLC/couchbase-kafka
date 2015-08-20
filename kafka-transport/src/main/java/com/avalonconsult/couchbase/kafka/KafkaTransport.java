package com.avalonconsult.couchbase.kafka;

import com.couchbase.kafka.CouchbaseKafkaConnector;
import com.couchbase.kafka.CouchbaseKafkaEnvironment;
import com.couchbase.kafka.DefaultCouchbaseKafkaEnvironment;

/**
 * Created by kruthar on 8/11/15.
 */
public class KafkaTransport {
    private static final String FILTER_CLASS = "com.avalonconsult.couchbase.kafka.KafkaTransportFilter";
    private static final String ENCODER_CLASS = "com.avalonconsult.couchbase.kafka.KafkaTransportEncoder";
    private static final String COUCHBASE_NODE = "couchbase.vagrant";
    private static final String COUCHBASE_BUCKET = "transactions";
    private static final String COUCHBASE_BUCKET_PASSWORD = "";
    private static final String KAFKA_NODE = "kafka.vagrant";
    private static final String KAFKA_TOPIC = "transactions";

    public static void main(String args[]) {
        DefaultCouchbaseKafkaEnvironment.Builder builder =
            (DefaultCouchbaseKafkaEnvironment.Builder) DefaultCouchbaseKafkaEnvironment
                .builder()
                .kafkaFilterClass(FILTER_CLASS)
                .kafkaValueSerializerClass(ENCODER_CLASS)
                .dcpEnabled(true);

        CouchbaseKafkaEnvironment env = builder.build();
        CouchbaseKafkaConnector connector = CouchbaseKafkaConnector.create(
            env, COUCHBASE_NODE, COUCHBASE_BUCKET, COUCHBASE_BUCKET_PASSWORD, KAFKA_NODE, KAFKA_TOPIC);
        connector.run();
    }
}
