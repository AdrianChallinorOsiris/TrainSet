package uk.co.osiris;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
	private String name;
	private String host;
	private Integer port;
	private boolean controller;
	
	
	public String getUrl(String endPoint) {
		String url = "http://" + host + ":" + port + "/" + endPoint;
		return url;
	}

}
