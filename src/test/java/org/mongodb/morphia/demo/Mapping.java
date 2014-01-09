package org.mongodb.morphia.demo;

import org.mongodb.morphia.query.Query;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Mapping extends BaseTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    private GithubUser evanchooly;
    private Date date;

    public Mapping() throws ParseException {
        date = sdf.parse("6-15-1987");
    }

    @Test
    public void basicUser() {
        evanchooly = new GithubUser("evanchooly");
        evanchooly.fullName = "Justin Lee";
        evanchooly.memberSince = date;

        datastore.save(evanchooly);
    }

    @Test(dependsOnMethods = {"basicUser"})
    public void repositories() throws ParseException {
        Organization org = new Organization("mongodb");
        datastore.save(org);
        
        Repository morphia1 = new Repository(org, "morphia");
        Repository morphia2 = new Repository(evanchooly, "morphia");
        
        datastore.save(morphia1);
        datastore.save(morphia2);
        
        evanchooly.repositories.add(morphia1);
        evanchooly.repositories.add(morphia2);
        
        datastore.save(evanchooly);
    }
    
    @Test(dependsOnMethods = {"repositories"})
    public void query() {
        Query<Repository> query = datastore.createQuery(Repository.class);
        
        Repository repository = query.get();
        
        List<Repository> repositories = query.asList();

        Iterable<Repository> fetch = query.fetch();

        query.field("owner").equal(evanchooly).get();

        GithubUser memberSince = datastore.createQuery(GithubUser.class).field("memberSince").equal(date).get();
        System.out.println("memberSince = " + memberSince);
        GithubUser since = datastore.createQuery(GithubUser.class).field("since").equal(date).get();
        System.out.println("since = " + since);
    }
}
