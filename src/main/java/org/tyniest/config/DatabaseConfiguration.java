package org.tyniest.config;

import java.net.InetSocketAddress;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;


@ApplicationScoped
public class DatabaseConfiguration {
    
    @ConfigProperty(name = "db.host")
    String host;

    @ConfigProperty(name = "db.port")
    Integer port;

    @ConfigProperty(name = "db.datacenter")
    String datacenter;
    
    @ConfigProperty(name = "db.keyspace")
    String keyspace;

    @ApplicationScoped
    public CqlSession makeDatabaseConnection() {
        return new CqlSessionBuilder()
            .addContactPoint(InetSocketAddress.createUnresolved(host, port))
            .withLocalDatacenter(datacenter)
            .withKeyspace(keyspace)
            .build();
    }

}
