package net.coronite.quizlet_math_plus;

import java.util.UUID;

/**
 * The model containing all of the information to produce a flashcard
 */
public class Card {
    private UUID mId;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    private String mTitle;

    public Card (){
        mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }
}
