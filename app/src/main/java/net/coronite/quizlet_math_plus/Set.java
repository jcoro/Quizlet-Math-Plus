package net.coronite.quizlet_math_plus;

import java.util.UUID;

/**
 * The model containing all of the information to produce a flashcard
 */
public class Set {
    private UUID mId;
    private String mTitle;

    public Set(){
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
