package uk.co.osiris.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Point  {
	public String		id;
	public Integer		thruPin;
	public Integer		branchPin;
	public Section		clockwise;
	public Section		anticlockwise;
}
