package org.ignite.bugreport.issue20181122;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteSpringBean;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.log4j.Log4JLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.ssl.SslContextFactory;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Issue20181122Application {
	public static void main(String[] args) {
		SpringApplication.run(Issue20181122Application.class, args);
	}

	@Value("${IGNITE_ID}")
	private String igniteId;

	@Bean
	public Ignite ignite() {
		IgniteConfiguration cfg = new IgniteConfiguration();

		SslContextFactory ssl = new SslContextFactory();
		// NOTE: Keystore was created using command: keytool -genkey -keyalg RSA -alias
		// ignite -keystore keystore.jks -storepass 112233
		// -validity 36000 -storetype pkcs12
		ssl.setKeyStoreFilePath("keystore.jks");
		ssl.setKeyStorePassword("112233".toCharArray());
		ssl.setTrustManagers(SslContextFactory.getDisabledTrustManager());
		cfg.setSslContextFactory(ssl);

		TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
		discoSpi.setIpFinder(new IpFinderNodeServiceImpl());
		cfg.setDiscoverySpi(discoSpi);

		IgniteSpringBean ret = new IgniteSpringBean();
		cfg.setConsistentId(igniteId);
		Log4JLogger log = new Log4JLogger(false);
		log.setLevel(Level.TRACE);
		cfg.setGridLogger(log);
		ret.setConfiguration(cfg);
		return ret;
	}
}
