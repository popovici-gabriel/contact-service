package com.ionos.domains.contact.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.ionos.domains.contact.service")
@EnableTransactionManagement
@EntityScan("com.ionos.domains.contact.model")
public class JpaConfiguration {

	@Bean
	public PlatformTransactionManager transactionManager(final DataSource dataSource,
			final EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		txManager.setDataSource(dataSource);
		return txManager;
	}

}
