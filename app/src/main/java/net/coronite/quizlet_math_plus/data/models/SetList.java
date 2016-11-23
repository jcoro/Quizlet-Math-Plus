package net.coronite.quizlet_math_plus.data.models;

import java.util.List;

/**
 * A SetList is created because of the way data is returned from the Quizlet API.
 * We can separate user sets and studied sets.
 */
public class SetList {
    public List<Set> sets;
    public List<StudiedSet> studied;

    public List<Set> getSets() {
        return sets;
    }

    public List<StudiedSet> getStudiedSets(){
        return studied;
    }
}
