package uk.co.osiris.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KafkaTopicConfig {

	@Value("${kafka.bootstrap-servers}") private String bootstrapServers;

	@Value("${kafka.topic-sensor}") 	private String sensorTopic;
	@Value("${kafka.topic-motor}") 	private String motorTopic;
	@Value("${kafka.topic-point}") 	private String pointTopic;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();

		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topicSensor() {
		return register(sensorTopic);
	}
	
	@Bean
	public NewTopic motorSensor() {
		return register(motorTopic);
	}
	
	@Bean
	public NewTopic topicPoint() {
		return register(pointTopic);
	}
	
	private NewTopic register(String topic) {
		log.info("Registering topic: {} ", topic);
		return new NewTopic(topic, 1, (short) 1);

	}
}
