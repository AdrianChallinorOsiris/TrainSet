package uk.co.osiris;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.common.*;
import uk.co.osiris.config.*;

@Service
@Slf4j
public class SensorService {
	private final GpioController gpio;
	private static final  String CONFIG = "config.json" ;
	private Configuration configuration = null;
	private MessageSender sender;

	private ArrayList<GPIOSensor> pinMap;
	/**
	 * Initialise the Sensor service 
	 * 
	 * @param nodeService
	 */
	public SensorService(MessageSender sender) {
		this.sender = sender; 
		
		try {

			File configFile = new File(CONFIG);
			JsonMapper mapper = JsonMapper.builder()
					.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
			configuration = mapper.readValue(configFile, Configuration.class);
			log.info("Loaded {} Configuration", configuration.getLayout());
			
		} catch (IOException e) {
			log.error("Error reading configuration file: {}  = {}", CONFIG, e.getMessage());
			System.exit(1);
		}
		
		log.info("Initializing the GPIO");
		try {
			PlatformManager.setPlatform(Platform.ODROID);
			log.info("GPIO platform initialised for ODROID");
		} catch (PlatformAlreadyAssignedException e) {
			log.error("Error initiating GPIO platform {} ", e.getMessage());
		}

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
                sendSensorMessage(sensor.getId(), state.getName());
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
		sendSensorMessage("S01", "HIGH");
		Thread.sleep(2000);
		sendSensorMessage("S01", "LOW");
	}
	
	/**
	 * Send a sensor state change message to the fat controller 
	 * 
	 * @param id
	 * @param state
	 */
	public void sendSensorMessage(String id, String state) { 
		SensorMsg sm = SensorMsg.builder().id(id).state(state).build();
		sender.sendSensorMessage(configuration.getController(), sm);
		

	}
}
