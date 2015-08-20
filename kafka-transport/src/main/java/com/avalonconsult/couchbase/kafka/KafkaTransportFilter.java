package com.avalonconsult.couchbase.kafka;

import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.kafka.DCPEvent;
import com.couchbase.kafka.filter.Filter;

/**
 * Created by kruthar on 8/11/15.
 */
public class KafkaTransportFilter implements Filter {
    public boolean pass(final DCPEvent dcpEvent) {
        return dcpEvent.message() instanceof MutationMessage;
    }
}
