package uk.co.osiris.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node {
	String node;
	Integer port;
	Integer timeout;
}
