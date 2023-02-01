package uk.co.osiris.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointMsg {
	private String id;
	private String name; 
	private boolean branch; 
	private String inSection; 
	private String thruSection;
	private String branchSection;
}
