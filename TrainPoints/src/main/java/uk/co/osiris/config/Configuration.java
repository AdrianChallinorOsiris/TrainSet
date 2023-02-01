package uk.co.osiris.config;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
	Integer	version;
	String date;
	String layout;
	Node controller; 
	ArrayList<Motor> motors;
	ArrayList<Point> points;
	ArrayList<Sensor> sensors;
	
	public int mcount() { 
		return (motors == null) ? 0 : motors.size();
	}
	
	public int pcount() { 
		return (points == null) ? 0 : points.size();
	}
	
	public int scount() { 
		return (sensors == null) ? 0 : sensors.size();
	}
}
