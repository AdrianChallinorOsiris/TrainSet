package uk.co.osiris.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.messages.SensorMsg;

@Slf4j
public class Receiver {
	@Value("${kafka.topic-sensor}") private String sensorTopic;
	@Value("${kafka.topic-motor}") 	private String motorTopic;
	@Value("${kafka.topic-point}") 	private String pointTopic;
	
 
    @KafkaListener(topics = "${kafka.topic-sensor}")
    public void receive(SensorMsg sensor) {
        log.info("received Sensor='{}'", sensor);
    }
}