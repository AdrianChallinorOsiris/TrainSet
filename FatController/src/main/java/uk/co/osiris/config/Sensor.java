package uk.co.osiris.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Sensor {
	private String id; 
	private Integer pin; 
	private String section; 
	private String description;
}
