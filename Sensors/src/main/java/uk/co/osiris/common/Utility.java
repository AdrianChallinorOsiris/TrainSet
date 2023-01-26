package uk.co.osiris.common;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {
	public static Pin pinID(Integer pinnumber) { 
		String pinname = String.format("GPIO_%02d", pinnumber);
		log.debug("Creating pin: {} ", pinname);
		Pin pin = RaspiPin.getPinByName(pinname);
		return pin;
	}

}
