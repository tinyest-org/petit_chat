package org.tyniest.config;

import javax.enterprise.context.ApplicationScoped;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;


@ApplicationScoped
public class DatabaseConfiguration {
    
    // set conf here

    @ApplicationScoped
    public CqlSession makeDatabaseConnection() {
        return new CqlSessionBuilder().build(); // TODO: stubbed
    }

}
