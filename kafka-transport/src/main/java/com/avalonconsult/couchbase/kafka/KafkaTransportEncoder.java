package com.avalonconsult.couchbase.kafka;

import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.deps.io.netty.util.CharsetUtil;
import com.couchbase.kafka.DCPEvent;
import com.couchbase.kafka.coder.AbstractEncoder;
import kafka.utils.VerifiableProperties;

/**
 * Created by kruthar on 8/11/15.
 */
public class KafkaTransportEncoder extends AbstractEncoder {
    public KafkaTransportEncoder(final VerifiableProperties properties) {
        super(properties);
    }

    @Override
    public byte[] toBytes(final DCPEvent dcpEvent) {
        MutationMessage message = (MutationMessage) dcpEvent.message();
        return message.content().toString(CharsetUtil.UTF_8).getBytes();
    }
}
