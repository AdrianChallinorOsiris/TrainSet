package uk.co.osiris.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Point {
	private String id;
	private String name; 
	private boolean branch; 
	private String inSection; 
	private String thruSection;
	private String branchSection;
}
