package uk.co.osiris.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MotorMsg {
	private String id; 
	private String section; 
	private Integer currentSpeed; 
	private Integer requestedSpeed; 
	private Boolean direction;
}
