package me.smartco.akstore.common.spring;

import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.util.TypeInformation;

/**
 * Created by libin on 15-2-9.
 */
public class SeparatedBasicPersistentEntity<T> extends BasicMongoPersistentEntity {
    /**
     * Creates a new {@link org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity} with the given {@link org.springframework.data.util.TypeInformation}. Will default the
     * collection name to the entities simple type name.
     *
     * @param typeInformation
     */
    public SeparatedBasicPersistentEntity(TypeInformation<T> typeInformation) {
        super(typeInformation);
    }

    @Override
    public String getCollection() {
        String slug="akstore";
        TenancyContext tenancyContext=TenancyContext.tenancyContextThreadLocal.get();
        if(null!=tenancyContext){
            slug=tenancyContext.getSlug();
        }
        return slug+"_"+super.getCollection();
    }
}
