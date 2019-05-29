package com.netflix.conductor.contribs.kafka;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.conductor.core.config.Configuration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Objects;
import java.util.Properties;

public class KafkaProducerManager {

	public static final String KAFKA_PUBLISH_REQUEST_TIMEOUT_MS = "kafka.publish.request.timeout.ms";
	public static final String STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
	public String defaultRequestTimeOut = "100";

	public KafkaProducerManager(Configuration configuration) {
		this.defaultRequestTimeOut = configuration.getProperty(KAFKA_PUBLISH_REQUEST_TIMEOUT_MS, defaultRequestTimeOut);
	}


	public Producer getProducer(KafkaPublishTask.Input input) {

		Properties configProperties = getProducerProperties(input);
		Producer producer = new KafkaProducer<String, String>(configProperties);
		return producer;

	}

	@VisibleForTesting
	Properties getProducerProperties(KafkaPublishTask.Input input) {

		Properties configProperties = new Properties();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, input.getBootStrapServers());

		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, input.getKeySerializer());

		String requestTimeoutMs = defaultRequestTimeOut;

		if (Objects.nonNull(input.getRequestTimeoutMs())) {
			requestTimeoutMs = String.valueOf(input.getRequestTimeoutMs());
		}

		configProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER);
		return configProperties;
	}
}
