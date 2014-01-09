package org.mongodb.morphia.demo;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(value = "users", noClassnameStored = true)
@Indexes({
     @Index(value ="userName, -followers", name = "popular"),
     @Index(value ="lastActive", name = "idle", expireAfterSeconds = 1000000000)
})
public class GithubUser {
    @Id
    public String userName;
    public String fullName;
    @Property("since")
    public Date memberSince;
    public Date lastActive;
    @Reference(lazy = true)
    public List<Repository> repositories = new ArrayList<>();
    public int followers = 0;
    public int following = 0;

    public GithubUser() {
    }

    public GithubUser(final String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GithubUser{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", memberSince=").append(memberSince);
        sb.append('}');
        return sb.toString();
    }
}