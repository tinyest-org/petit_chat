package org.tyniest.security.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.user.entity.User;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class IdentityService {
    
    protected UUID userId = UUID.fromString("43c0db5c-d829-4929-8efc-5e4a13bb202f"); // TODO: stub

    public User getCurrentUser() {
        return null; // TODO: stubed
    }

    public Set<String> getCurrentRoles() {
        return new HashSet<>();
    }

    public UUID getCurrentUserId() {
        return userId; // TODO: Stub
    }
    
    public Uni<UUID> getCurrentUserIdAsync() {
        return Uni.createFrom().item(userId); // TODO: Stub
    }
}
