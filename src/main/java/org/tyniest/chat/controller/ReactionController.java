package org.tyniest.chat.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.tyniest.chat.entity.Reaction;
import org.tyniest.chat.service.ReactionService;

import lombok.RequiredArgsConstructor;

@Path("/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @GET
    public List<Reaction> getAll() {
        return reactionService.getAll();
    }

    @POST
    @Path("/{name}")
    public void create(
        @PathParam("name") final String name
    ) {
        reactionService.createReactionType(name);
    }

    @DELETE
    @Path("/{name}")
    public void delete(
        @PathParam("name") final String name
    ) {
        reactionService.deleteReactionType(name);
    }
}
