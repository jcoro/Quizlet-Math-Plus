package net.coronite.quizlet_math_plus;

/**
 * Model for Cards
 */
public class Card {
    private int id;
    private int term;
    private String definition;
    private String image;
    private int rank;

    public int getId(){
        return id;
    }

    public int getTerm(){
        return term;
    }

    public String getDefinition(){
        return definition;
    }

    public String getImage(){
        return image;
    }

    public int getRank(){
        return rank;
    }
}
