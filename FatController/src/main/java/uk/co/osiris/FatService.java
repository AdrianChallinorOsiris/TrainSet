package uk.co.osiris;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.common.MessageSender;
import uk.co.osiris.config.*;

@Service
@Slf4j
public class FatService {
	private static final  String CONFIG = "config.json" ;
	private Configuration configuration = null;
	private MessageSender sender;

	ArrayList<Sensor> sensors;


	public FatService(MessageSender sender) {
		this.sender = sender; 
		
		try {

			File configFile = new File(CONFIG);
			JsonMapper mapper = JsonMapper.builder()
					.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
			configuration = mapper.readValue(configFile, Configuration.class);
			log.info("Loaded Configuration nodes");
			
			// TODO get config from nodes 
			sensors = sender.getSensors(configuration.getSensors());
			
		} catch (IOException e) {
			log.error("Error reading configuration file: {}  = {}", CONFIG, e.getMessage());
			System.exit(1);
		}
	}
	
	
	
}
