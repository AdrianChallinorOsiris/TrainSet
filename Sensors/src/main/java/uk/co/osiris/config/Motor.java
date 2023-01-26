package uk.co.osiris.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Motor {
	private String id;
	private String name;
	private Integer ena;
	private Integer dir;
}
