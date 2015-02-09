package me.smartco.akstore.user.model;

import me.smartco.akstore.common.model.AbstractDocument;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by libin on 14-11-7.
 */
@Document
public class Role extends AbstractDocument {



    @Indexed(unique = true)
    private String name;

    private Set<Resource> Resources = new HashSet<Resource>();

    @PersistenceConstructor
    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Resource> resources() {
        return Resources;
    }

    public void setResources(Set<Resource> resources) {
        Resources = resources;
    }
}
