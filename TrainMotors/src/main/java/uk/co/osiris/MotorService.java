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
public class MotorService {
	private static final  String CONFIG = "config.json" ;
	private final GpioController gpio;
	private Configuration configuration = null;
	private HashMap<String, GPIOMotor> motors;
	
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
	public MotorService() { 
		
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
		
		gpio = GpioFactory.getInstance();

		// Build the configuration
		for (Motor m : configuration.getMotors()) { 
			String id = m.getId();
			log.info("Building GPIO Motor {} ", id);
			GpioPinDigitalOutput enable = gpio.provisionDigitalOutputPin(Utility.pinID(m.getEna()));
			GpioPinDigitalOutput direction = gpio.provisionDigitalOutputPin(Utility.pinID(m.getDir()));
			GPIOMotor gm = new GPIOMotor(id, enable, direction, m.getSection());
			motors.put(id, gm);
		}
	}
	

	/**
	 * Respond to the Fat Controller when he asks what motors we have 
	 * @return
	 */
	public ArrayList<Motor> getMotors() {
		return configuration.getMotors();

	}

	/**
	 * Stop all motors immediately. 
	 */
	public void crashStop() {
		for (HashMap.Entry<String, GPIOMotor> entry : motors.entrySet()) {
	        entry.getValue().immediateStop();
	    }
	}

	/** 
	 * Stop a specific a specific motor
	 * 
	 * An exception is thrown if the Fat Controller asks to manipulate a non-existent motor. 
	 * 
	 * @param id				- The motor to change 
	 */
	public void stopMotor(String id) throws Exception{
		GPIOMotor m = motors.get(id);
		if (m != null) { 
			m.setAcceleration(-25);
			m.setRequestedSpeed(0);
		}
		else 
			throw new Exception("Fat controller tried to change a non-existent motor");
	}

	/** 
	 * Move a motor. Not that the motor may already be moving, in which case we just change the 
	 * values. Be aware that this does allow you to change direction instantly, which may not be 
	 * desired or aesthetic. 
	 * 
	 * An exception is thrown if the Fat Controller asks to manipulate a non-existent motor. 
	 * 
	 * @param id				- The motor to change 
	 * @param speed				- The speed we want to achieve
	 * @param accelation		- The accelaration 
	 * @param forward			- Forward (or reverse)
	 * @throws Exception		
	 */
	public void move(String id, int speed, int accelation, boolean forward) 
			throws Exception {
		GPIOMotor m = motors.get(id);
		if (m != null) { 
			m.move(speed, accelation, forward);
		}
		else 
			throw new Exception("Fat controller tried to change a non-existent motor");
		
	}


	/**
	 * Gte a motors status 
	 * 
	 * @param id
	 * @return
	 */
	public MotorMsg getStatus(String id) {
		GPIOMotor m = motors.get(id);
		if (m != null)
			return m.getStatus();
		else
			return  null;
	}

}
