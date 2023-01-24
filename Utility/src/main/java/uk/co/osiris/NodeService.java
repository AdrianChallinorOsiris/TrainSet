package uk.co.osiris;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Getter
public class NodeService {
	// @formatter:off

	@Value("${nodes}") String NODES;
	@Value("${timeout}") int TIMEOUT;
	
	// @formatter:on
	
	private List<Node> nodeList; 
	private boolean initialised = false;

	/**
	 * Startup the service 
	 * @return
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void startup() { 
		if (initialised) return;
		log.info("Starting node service");
		File file = new File(NODES);
		if (!file.exists()) { 
			log.error("Cant find the nodes file: {}", NODES);
			System.exit(1);
		}
		try {
			JsonMapper mapper = JsonMapper.builder().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
			nodeList = mapper.readValue(file, new TypeReference<List<Node>>(){});
			log.info("Loaded {} Node list", nodeList.size());
			
			
		} catch (IOException e) {
			log.error("Error parsing nodelist {} - {}", NODES, e.getMessage());
			System.exit(1);
		}
		initialised = true;
	}
	
	public boolean isReachable(Node node) { 
		boolean reachable = true;
		
		try {
			try (Socket soc = new Socket()) {
				soc.connect(new InetSocketAddress(node.getHost(), node.getPort()), TIMEOUT);
			}
		} catch (IOException ex) {
			reachable = false;
		}
		return reachable;
	}
	
	public Node getController() { 
		return nodeList.stream().filter(n -> n.isController()).findFirst().get();
	}


}
