package uk.co.osiris.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SinglePoint  {
	public String		id;
	public Integer		thruPin;
	public Integer		branchPin;
	public Section		section;
	public Direction	direction;
}
