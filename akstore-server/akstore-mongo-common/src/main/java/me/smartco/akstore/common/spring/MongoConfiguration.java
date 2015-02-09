package me.smartco.akstore.common.spring;

import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

/**
 * Created by libin on 14-11-7.
 */
@Configuration
@ComponentScan(basePackages="me.smartco.akstore")
@EnableMongoRepositories(basePackages={"me.smartco.akstore.store.mongodb"})
public class MongoConfiguration extends AbstractMongoConfiguration {


    @Autowired
    private List<Converter<?, ?>> converters;

    @Override
    protected String getDatabaseName() {
        return "estore";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        Mongo mongo = new Mongo("127.0.0.1");
        mongo.setWriteConcern(WriteConcern.SAFE);

        return mongo;
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(converters);
    }

    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
    }
}
