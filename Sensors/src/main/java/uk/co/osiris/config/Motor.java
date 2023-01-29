package uk.co.osiris.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Motor {
	private String id;
	private String section;
	private Integer ena;
	private Integer dir;
	
}
