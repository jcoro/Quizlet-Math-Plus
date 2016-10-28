package net.coronite.quizlet_math_plus;

import java.util.UUID;

/**
 * Model for Cards
 */
public class Card {
    private UUID mId;
    private String mTitle;

    public Card(){
        mId = UUID.randomUUID();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public UUID getId() {
        return mId;
    }
}
