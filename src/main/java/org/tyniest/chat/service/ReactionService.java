package org.tyniest.chat.service;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.tyniest.chat.entity.Reaction;

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

    public List<Reaction> getAll() {
        return Collections.emptyList(); // TODO: stub
    }
}
