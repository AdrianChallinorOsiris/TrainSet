package uk.co.osiris.common;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {
	public static Pin pinID(Integer pinnumber) { 
		String pinname = String.format("GPIO_%02d", pinnumber);
		log.debug("Creating pin: {} ", pinname);
		Pin pin = RaspiPin.getPinByAddress(pinnumber);
		if (pin == null) { 
			log.error("PIN was found to be null");
			
			Pin pin2 = RaspiPin.GPIO_02;
			log.error("pin2 = {}", pin2.getName());
			
			Pin pin3 = RaspiPin.getPinByName( pin2.getName());
			log.error("Pin3 = {}", pin3);
			
			Pin pin4 = RaspiPin.getPinByAddress(04);
			log.error("Pin4 = {}", pin4);
			System.exit(0);
		}
		return pin;
	}

}
