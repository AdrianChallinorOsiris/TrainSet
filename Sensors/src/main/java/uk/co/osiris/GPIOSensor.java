package uk.co.osiris;



import com.pi4j.io.gpio.GpioPinDigitalInput;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GPIOSensor {
	private String id;
	private GpioPinDigitalInput pin;
}
