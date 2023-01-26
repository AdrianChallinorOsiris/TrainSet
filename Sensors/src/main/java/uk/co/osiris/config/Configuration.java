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
	ArrayList<LineSwitch> lineSwitches;
	ArrayList<SinglePoint> singlePoints;
	ArrayList<Sensor> sensors;
}
