package net.coronite.quizlet_math_plus;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A temporary Singleton to Make a list of cards
 */
public class CardMaker {
    private static CardMaker sCardMaker;

    private ArrayList<Set> mSets;

    public static CardMaker get(Context context) {
        if (sCardMaker == null) {
            sCardMaker = new CardMaker(context);
        }
        return sCardMaker;
    }

    private CardMaker(Context context){
        mSets = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            Set set = new Set();
            set.setTitle("Card # " + i);
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
