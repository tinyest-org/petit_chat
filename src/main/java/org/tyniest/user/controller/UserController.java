package org.tyniest.user.controller;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.tyniest.chat.entity.Chat;
import org.tyniest.user.entity.User;
import org.tyniest.user.service.UserService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Path("/user")
public class UserController {
    
    private final UserService userService;
    protected UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub

    @Path("/me")
    @GET
    public void me() {
        // ;
    }
    
    @Path("/me/chats")
    @GET
    public List<Chat> getChats() {
        return userService.getChats(userId, 0);
    }

    @Path("/find")
    @GET
    public List<User> getUsersByName(@QueryParam("q") final String query) {
        return userService.getUserByName(query);
    }
}
