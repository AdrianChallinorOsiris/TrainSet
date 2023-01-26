package uk.co.osiris;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.common.SensorMsg;
import uk.co.osiris.messages.*;

@RestController
@Slf4j
public class FatController {

	@PostMapping("/sensor")
	@ResponseStatus(value = HttpStatus.OK)
	public void Sensor(@RequestBody SensorMsg sensorMsg) {
		log.info("Sensor state change {} ", sensorMsg);
		
		
		
	}

}