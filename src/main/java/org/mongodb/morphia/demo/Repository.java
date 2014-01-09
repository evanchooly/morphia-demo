package org.mongodb.morphia.demo;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@Entity("repos")
public class Repository {
    @Id
    public String name;
    @Reference
    public Organization organization;
    @Reference
    public GithubUser owner;
    public Settings settings = new Settings();

    public Repository() {
    }

    public Repository(final Organization organization, final String name) {
        this.organization = organization;
        this.name = organization.name + "/" + name;
    }
    
    public Repository(final GithubUser owner, final String name) {
        this.owner = owner;
        this.name = owner.userName + "/" + name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Repository{");
        sb.append("name='").append(name).append('\'');
        if(organization != null) {
            sb.append(", organization=").append(organization);
        }
        if(owner != null) {
            sb.append(", owner=").append(owner);
        }
        sb.append(", settings=").append(settings);
        sb.append('}');
        return sb.toString();
    }
}
