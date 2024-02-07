package org.example.hackaton.batch.clientproduct;

import lombok.val;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = {"org.example.hackaton.repository"})
@ComponentScan(basePackages = {"org.example.hackaton.batch.clientproduct"})
@EntityScan(basePackages = {"org.example.hackaton.entity"})
class ImportClientProductBatchConfigTest {

    @Primary
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("init-h2-batch-test/init-database.sql")
                .addScript("init.sql")
                .build();
    }

    @Bean
    @Qualifier("transactionManager")
    public JpaTransactionManager transactionManager() {
        val jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return jpaTransactionManager;
    }


    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan("org.example.hackaton.entity");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("Client");
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("Product");
        val hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        val jobrepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobrepositoryFactoryBean.setDataSource(dataSource());
        jobrepositoryFactoryBean.setTransactionManager(transactionManager());
        jobrepositoryFactoryBean.afterPropertiesSet();
        return jobrepositoryFactoryBean.getObject();
    }
}