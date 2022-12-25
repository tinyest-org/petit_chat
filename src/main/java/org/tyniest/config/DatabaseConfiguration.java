package org.tyniest.config;

import java.net.InetSocketAddress;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;


@ApplicationScoped
public class DatabaseConfiguration {
    
    // set conf here
    @ConfigProperty(name = "db.host", defaultValue = "app.tinyest.org")
    String host;

    @ApplicationScoped
    public CqlSession makeDatabaseConnection() {
        return new CqlSessionBuilder()
        .addContactPoint(InetSocketAddress.createUnresolved(host, 9042))
        .withLocalDatacenter("datacenter1")
        .withKeyspace("chat2")
        .build(); // TODO: stubbed
    }

}
