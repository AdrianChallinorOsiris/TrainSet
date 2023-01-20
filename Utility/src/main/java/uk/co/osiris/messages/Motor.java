package uk.co.osiris.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Motor {
	private String id; 
	private String name;
	private String section; 
	private Integer currentSpeed; 
	private Integer requestedSpeed; 
}
