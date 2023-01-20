package uk.co.osiris;

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {
	
	public static Pin pinID(Integer pinnumber) { 
		String pinname = "GPIO_" + pinnumber.toString();
		log.debug("Creating pin: {} ", pinname);
		Pin pin = RaspiPin.getPinByName(pinname);
		return pin;
	}
	
	public static void init() { 
		// ####################################################################
		//
		// since we are not using the default Raspberry Pi platform, we should
		// explicitly assign the platform as the Odroid platform.
		//
		// ####################################################################
		try {
			PlatformManager.setPlatform(Platform.ODROID);
			log.info("GPIO platform initialised for ODROID");
		} catch (PlatformAlreadyAssignedException e) {
			log.error("Error initiating GPIO platform {} ", e.getMessage());
		}
	}
}
