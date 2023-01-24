package uk.co.osiris;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;
import uk.co.osiris.config.Configuration;

@Service
@Slf4j
public class FatService {
	// @formatter:off
	@Value("${layout}") 	String LAYOUT;
	@Autowired 				NodeService nodeService;

	// @formatter:on
	
	private Configuration config = null;
	private final RestTemplate restTemplate = new RestTemplate();
	private final HttpHeaders headers;

	public FatService() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Accept", "application/json");
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startup() {
		File file = new File(LAYOUT);
		if (file.exists())
			try {
				JsonMapper mapper = JsonMapper.builder()
						.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).build();
				config = mapper.readValue(file, Configuration.class);
				log.info("Loaded {} Configuration", config.getLayout());

				HttpEntity<Configuration> requestEntity = new HttpEntity<>(config, headers);
				if (nodeService.getNodeList() == null) {
					nodeService.startup();
				}
				for (Node n : nodeService.getNodeList()) {
					if (!n.isController()) {
						if (nodeService.isReachable(n)) {
							String url = n.getUrl("configuration");
							ResponseEntity<Integer> count = restTemplate.postForEntity(url, requestEntity,
									Integer.class);
							log.info("Sent configuration to {} - responded with {}", n.getName(), count.getBody());
						}
						else {
							log.error("Node is not reachable: {} ", n);
						}
					}
				}

			} catch (IOException e) {
				log.error("Error parsing Layout : {}", e.getMessage());
			}
		else {
			log.error("Layout file not found: {}", LAYOUT);
			System.exit(1);
		}
	}

}
