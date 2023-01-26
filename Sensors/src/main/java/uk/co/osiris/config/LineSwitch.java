package uk.co.osiris.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LineSwitch  {
	public String		id;
	public Integer		thruPin;
	public Integer		branchPin;
	public Section clockwise;
	public Section anticlockwise;
}
