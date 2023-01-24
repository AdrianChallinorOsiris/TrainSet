package uk.co.osiris;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import uk.co.osiris.config.Configuration;

@RestController
public class SensorController {
	private final SensorService svc;

	public SensorController(SensorService svc) {
		this.svc = svc; 
	}
	
	@PostMapping("/configuration")
	public Integer configuration(@RequestBody Configuration cfg ) { 
		return svc.configure(cfg);
	}
	
	@GetMapping("/test") 
	public String tester() throws InterruptedException { 
		svc.doTest(); 
		return "Test done";
	}
}
