package uk.co.osiris;

import java.util.ArrayList;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;

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
	private final GpioController gpio;

	private ArrayList<GPIOSensor> pinMap;
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
		
		gpio = GpioFactory.getInstance();
	}

	
	/**
	 * Handle the configuration 
	 * 
	 * @param cfg
	 * @return
	 */
	public Integer configure(Configuration cfg) {
		pinMap = new ArrayList<>();
				
		/**
		 * Create a listener for the pins. 
		 */
		GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                log.debug(" --> GPIO PIN STATE CHANGE: {} ", event);
                PinState state = event.getState();
                GpioPin pin = event.getPin();
                String pinName = pin.getName();
                GPIOSensor sensor = pinMap.stream().filter(x -> x.getPin().getName().equals(pinName)).findFirst().get();
                sendMessage(sensor.getId(), state.getName());
            }
        };


		// Process Sensors
		for (Sensor s : cfg.getSensors()) {
			log.info("Build sensor for : {} ", s);
			Pin pin = Utility.pinID(s.getPin());
			GpioPinDigitalInput digitalInput =   gpio.provisionDigitalInputPin(pin, PinPullResistance.PULL_DOWN);
			pinMap.add(new GPIOSensor(s.getId(), digitalInput));
		}
		
		GpioPinDigitalInput[] pins = pinMap.stream().map(x -> x.getPin()).toArray(GpioPinDigitalInput[]::new);
		gpio.addListener(listener, pins);
		
		return pinMap.size();
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
