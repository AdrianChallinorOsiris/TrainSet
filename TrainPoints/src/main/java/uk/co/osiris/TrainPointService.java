package uk.co.osiris;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.common.MotorMsg;
import uk.co.osiris.common.Utility;
import uk.co.osiris.config.Configuration;
import uk.co.osiris.config.Motor;

@Service
@Slf4j
public class TrainPointService {
	private static final  String CONFIG = "config.json" ;
	// private final GpioController gpio;
	private Configuration configuration = null;
	// private HashMap<String, GPIOMotor> motors;
	
	/**
	 * Service to handle the motors. These are defined in a config file. Each motor requires 
	 * two pins: 
	 *    1 = The power switch. We use a software PWM to control this. 
	 *    2 = The direction. 
	 *    
	 * We hold a hash map of the GPIO motors for easy access.
	 * 
	 * @param sender
	 */
	public TrainPointService() { 
		
		try {

			File configFile = new File(CONFIG);
			JsonMapper mapper = JsonMapper.builder()
					.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
			configuration = mapper.readValue(configFile, Configuration.class);
			log.info("Loaded {} Configuration", configuration.getLayout());
			log.info("Points: {},  Sensors: {},  Motors: {}", 
					configuration.pcount(), 
					configuration.scount(),
					configuration.mcount());
			
		} catch (IOException e) {
			log.error("Error reading configuration file: {}  = {}", CONFIG, e.getMessage());
			System.exit(1);
		}
		
	}
	

	/**
	 * Respond to the Fat Controller when he asks what motors we have 
	 * @return
	 */
	public ArrayList<Motor> getMotors() {
		return configuration.getMotors();

	}


}
