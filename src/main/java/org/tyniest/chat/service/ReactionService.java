package org.tyniest.chat.service;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;


@ApplicationScoped
@RequiredArgsConstructor
public class ReactionService {
    
    public void createReactionType(final String reaction) {

    }

    public void deleteReactionType(final String reaction) {
        
    }

    public boolean checkReactionType(final String reaction) {
        return true; // TODO: stub
    }
}
