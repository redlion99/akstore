package me.smartco.akstore.common.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.util.TypeInformation;

/**
 * Created by libin on 15-2-9.
 */
public class SeparatedMongoMappingContext extends MongoMappingContext {

    private ApplicationContext context;

    public SeparatedMongoMappingContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected <T> BasicMongoPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
        BasicMongoPersistentEntity<T> entity = new SeparatedBasicPersistentEntity<T>(typeInformation);

        if (context != null) {
            entity.setApplicationContext(context);
        }

        return entity;
    }
}
