package net.coronite.quizlet_math_plus.data.models;

import java.util.List;


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
