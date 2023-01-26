package uk.co.osiris.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotorMsg {
	private String id; 
	private String name;
	private String section; 
	private Integer currentSpeed; 
	private Integer requestedSpeed; 
}
