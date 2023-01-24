package uk.co.osiris;

import java.util.ArrayList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pi4j.io.gpio.*;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.config.Configuration;
import uk.co.osiris.config.Sensor;
import uk.co.osiris.messages.SensorMsg;

@Service
@Slf4j
public class SensorService {
	private final NodeService nodeService;  
	private final RestTemplate restTemplate;
	private final HttpHeaders headers;

	/**
	 * Initialise the Sensor service 
	 * 
	 * @param nodeService
	 */
	public SensorService(NodeService nodeService) {
		this.nodeService = nodeService;
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Accept", "application/json");
		restTemplate = new RestTemplate();
	}

	
	/**
	 * Handle the configuration 
	 * 
	 * @param cfg
	 * @return
	 */
	public Integer configure(Configuration cfg) {
		ArrayList<GPIOSensor> sensorList = new ArrayList<>();

		// final GpioController gpio = GpioFactory.getInstance();

		// Process Sensors
		for (Sensor s : cfg.getSensors()) {
			log.info("Build sensor for : {} ", s);
			// TODO build a sensor here, with a listener.

		}
		return sensorList.size();
	}

	/**
	 * Send a couple of test messages to the the fat controller. 
	 * 
	 * @throws InterruptedException
	 */
	public void doTest() throws InterruptedException {
		sendMessage("S01", "HIGH");
		Thread.sleep(2000);
		sendMessage("S01", "LOW");
	}
	
	/**
	 * Send a sensor state change message to the fat controller 
	 * 
	 * @param id
	 * @param state
	 */
	public void sendMessage(String id, String state) { 
		SensorMsg sm = SensorMsg.builder().id(id).state(state).build();
		Node controller = nodeService.getController();
		if (nodeService.isReachable(controller)) { 
			String url = controller.getUrl("sensor");
			HttpEntity<SensorMsg> requestEntity = new HttpEntity<>(sm, headers);

			restTemplate.postForEntity(url, requestEntity, null);
			log.info("Sent sensor message: {}", sm);
		}
		else { 
			log.warn("Sensor message not sent - controller is not reachable");
		}
	}
}
