package uk.co.osiris.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.config.Node;

@Service
@Slf4j
public class MessageSender {
	private final RestTemplate restTemplate;
	private final HttpHeaders headers;

	public MessageSender() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Accept", "application/json");
		restTemplate = new RestTemplate();

	}

	public void sendSensorMessage(Node controller, SensorMsg sm) {
		if (isReachable(controller)) { 
			String url = buildURL(controller, "sensor");
			HttpEntity<SensorMsg> requestEntity = new HttpEntity<>(sm, headers);

			restTemplate.postForEntity(url, requestEntity, null);
			log.info("Sent sensor message: {}", sm);
		}
		else { 
			log.warn("Sensor message not sent - controller is not reachable");
		}		
	}
	
	private String buildURL(Node node, String endpoint) {
		return node.getNode() + ":" + node.getPort() + "/" + endpoint;
	}
	
	private boolean isReachable(Node node) { 
		boolean reachable = true;
		
		try {
			try (Socket soc = new Socket()) {
				soc.connect(new InetSocketAddress(node.getNode(), node.getPort()), node.getTimeout());
			}
		} catch (IOException ex) {
			reachable = false;
		}
		return reachable;
	}
}
