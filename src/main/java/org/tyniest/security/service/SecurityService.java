package org.tyniest.security.service;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.user.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class SecurityService {
    

    public User getCurrentUser() {
        return null; // TODO: stubed
    }
}
