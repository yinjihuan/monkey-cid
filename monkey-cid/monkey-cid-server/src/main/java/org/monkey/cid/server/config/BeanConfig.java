package org.monkey.cid.server.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cxytiandi.jdbc.CxytiandiJdbcTemplate;

@Configuration
public class BeanConfig {
	
	/**
	 * JDBC
	 * @return
	 */
	@Bean(autowire=Autowire.BY_NAME)
	public CxytiandiJdbcTemplate cxytiandiJdbcTemplate() {
		return new CxytiandiJdbcTemplate("org.monkey.cid.server.po");
	}
	
}
