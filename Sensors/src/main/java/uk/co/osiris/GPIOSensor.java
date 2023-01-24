package uk.co.osiris;


import com.pi4j.io.gpio.GpioPinDigitalInput;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GPIOSensor {
	String	id;
	GpioPinDigitalInput gpioPin; 
	
}
