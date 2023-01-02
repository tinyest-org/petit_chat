package org.tyniest.common.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/utils")
public class UtilsController {
    @GET
    @Path("/ping")
    public String pong() {
        return "PONG";
    }
}
