package org.mongodb.morphia.demo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.testng.annotations.BeforeTest;

import java.net.UnknownHostException;

public class BaseTest {

    protected Morphia morphia;
    protected Datastore datastore;

    @BeforeTest
    public void connect() throws UnknownHostException {
        morphia = new Morphia();
        datastore = morphia.createDatastore(new MongoClient(), "morphia-demo");
        datastore.getDB().dropDatabase();
    }
}
