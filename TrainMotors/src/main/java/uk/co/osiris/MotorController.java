package uk.co.osiris;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.common.MotorMsg;
import uk.co.osiris.config.Motor;

@RestController
@Slf4j
public class MotorController {
	private final MotorService svc;
	
	public MotorController(MotorService svc) { 
		this.svc = svc;
	}
	
	@GetMapping("/motors") 
	public ArrayList<Motor> configuration() { 
		log.debug("Fat controller probed for motors");
		return svc.getMotors();
	}
	
	@GetMapping("/crashstop") 
	@ResponseStatus(value = HttpStatus.OK)
	public void crashStopAll() { 
		log.debug("Fat controller request total shutdown of all motors");
		svc.crashStop();
	}

	@GetMapping("/crashstop/{id}") 
	public ResponseEntity<String> crashStopOne(@PathVariable("id") String id) { 
		log.debug("Fat controller request total shutdown of one motor: {}", id);
		try {
			svc.stopMotor(id);
			return new ResponseEntity<>("OK",HttpStatus.OK);

		} catch (Exception e) {
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/move/{id}")
	public ResponseEntity<String>  move(
			@PathVariable("id") String id, 
			@RequestParam(defaultValue="100") String speed, 
			@RequestParam(defaultValue="100") String acceleration,
			@RequestParam(defaultValue="forward") String direction) {
		try { 
			int requestedSpeed = Integer.parseInt(speed);
			int requestedAcceleration = Integer.parseInt(acceleration);
			boolean dir = (direction.equalsIgnoreCase("Forward"));
			svc.move(id, requestedSpeed, requestedAcceleration, dir);
			return new ResponseEntity<>("OK",HttpStatus.OK);
		} 
		catch (Exception e) { 
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@GetMapping("status/{id}")
	public ResponseEntity<MotorMsg>  status(@PathVariable("id") String id) { 
		return  new ResponseEntity<>(svc.getStatus(id), HttpStatus.OK);
	}


}
