package org.ignite.bugreport.issue20181122;

import org.apache.ignite.Ignite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodesCountController implements ApplicationListener<ContextRefreshedEvent> {
	private Logger log = LoggerFactory.getLogger(IpFinderNodeServiceImpl.class);

	@Autowired
	private Ignite ignite;

	private boolean ready = true;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ready = true;
		log.info("ContextRefreshedEvent");
	}

	@GetMapping
	public int getCount() {
		return !ready ? -1 : ignite.cluster().nodes().size();
	}
}
