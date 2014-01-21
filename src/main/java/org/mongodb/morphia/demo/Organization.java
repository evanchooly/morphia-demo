package org.mongodb.morphia.demo;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Version;
import org.mongodb.morphia.utils.IndexDirection;

import java.util.Date;

@Entity("orgs")
public class Organization {
    @Id
    public String name;
    @Indexed(value = IndexDirection.ASC, name = "", unique = false, dropDups = false, background = false, sparse = false, 
             expireAfterSeconds = -1)
    public Date created;    
    @Version("v")
    public long version;

    public Organization() {
    }

    public Organization(final String name) {
        this.name = name;
    }
}
