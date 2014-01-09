package org.mongodb.morphia.demo;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("orgs")
public class Organization {
    @Id
    public String name;

    public Organization() {
    }

    public Organization(final String name) {
        this.name = name;
    }
}
