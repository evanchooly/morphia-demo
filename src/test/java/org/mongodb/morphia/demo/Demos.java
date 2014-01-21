package org.mongodb.morphia.demo;

import org.mongodb.morphia.query.MorphiaIterator;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Demos extends BaseTest {
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
    private GithubUser evanchooly;
    private Date date;

    public Demos() throws ParseException {
        date = sdf.parse("6-15-1987");
    }

    @Test
    public void basicUser() {
        evanchooly = new GithubUser("evanchooly");
        evanchooly.fullName = "Justin Lee";
        evanchooly.memberSince = date;
        evanchooly.following = 1000;

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
        ((MorphiaIterator)fetch).close();
        
        Iterator<Repository> iterator = fetch.iterator();
        while(iterator.hasNext()) {
            iterator.next();
        }
        
        iterator = fetch.iterator();
        while(iterator.hasNext()) {
            iterator.next();
        }

        query.field("owner").equal(evanchooly).get();

        GithubUser memberSince = datastore.createQuery(GithubUser.class).field("memberSince").equal(date).get();
        System.out.println("memberSince = " + memberSince);
        GithubUser since = datastore.createQuery(GithubUser.class).field("since").equal(date).get();
        System.out.println("since = " + since);
    }

    @Test(dependsOnMethods = {"repositories"})
    public void updates() {
        evanchooly.followers = 12;
        evanchooly.following = 678;
        datastore.save(evanchooly);
    }
    
    @Test(dependsOnMethods = {"repositories"})
    public void massUpdates() {
        UpdateOperations<GithubUser> update = datastore.createUpdateOperations(GithubUser.class)
                                                       .inc("followers")
                                                       .set("following", 42);
        Query<GithubUser> query = datastore.createQuery(GithubUser.class).field("followers").equal(0);
        datastore.update(query, update);
    }

    @Test(dependsOnMethods = {"repositories"}, expectedExceptions = {ConcurrentModificationException.class})
    public void versioned() {
        Organization organization = datastore.createQuery(Organization.class).get();
        Organization organization2 = datastore.createQuery(Organization.class).get();

        Assert.assertEquals(organization.version, 1L);                
        datastore.save(organization);
        
        Assert.assertEquals(organization.version, 2L);        
        datastore.save(organization);
        
        Assert.assertEquals(organization.version, 3L);
        datastore.save(organization2);
    }
}
