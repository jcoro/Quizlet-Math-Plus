package net.coronite.quizlet_math_plus;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A temporary Singleton to Make a list of Sets
 */
public class SetMaker {
    private static SetMaker sSetMaker;

    private ArrayList<Set> mSets;

    public static SetMaker get(Context context) {
        if (sSetMaker == null) {
            sSetMaker = new SetMaker(context);
        }
        return sSetMaker;
    }

    private SetMaker(Context context){
        mSets = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            Set set = new Set();
            set.setTitle("Set # " + i);
            mSets.add(set);
        }
    }

    public List<Set> getSets() {
        return mSets;
    }

    public Set getSet(UUID id) {
        for (Set set : mSets){
            if (set.getId().equals(id)){
                return set;
            }
        }
        return null;
    }
}
