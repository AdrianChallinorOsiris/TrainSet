package uk.co.osiris;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.co.osiris.config.Sensor;

@RestController
public class SensorController {
	private final SensorService svc;

	public SensorController(SensorService svc) {
		this.svc = svc; 
	}
	
	@GetMapping("/sensors")
	public ArrayList<Sensor> configuration( ) { 
		return svc.getSensors();
	}
	
	@GetMapping("/test") 
	public String tester() throws InterruptedException { 
		svc.doTest(); 
		return "Test done";
	}
}
