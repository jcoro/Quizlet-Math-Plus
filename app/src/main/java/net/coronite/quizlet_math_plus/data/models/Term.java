package net.coronite.quizlet_math_plus.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for Cards
 */
public class Term implements Parcelable {
    private String id;
    private String term;
    private String definition;
    private String image;
    private String rank;

    /**
     * Each flashcard Set consists of Terms which themselves consist of a term and a definition.
     * @param quizlet_id the Quizlet-generated id of the Set the Term belongs to
     * @param trm The term (i.e., question) of the term.
     * @param def The definition
     * @param img An image associated with a Term
     * @param rnk The order of the Term in the Set
     */

    public Term(String quizlet_id, String trm, String def, String img, String rnk){
        this.id = quizlet_id;
        this.term = trm;
        this.definition = def;
        this.image = img;
        this.rank = rnk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId(){
        return id;
    }

    public String getTerm(){
        return term;
    }

    public String getDefinition(){
        return definition;
    }

    public String getImage(){
        return image;
    }

    public String getRank(){
        return rank;
    }

    private Term(Parcel in){
        id = in.readString();
        term = in.readString();
        definition = in.readString();
        image = in.readString();
        rank = in.readString();
    }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(term);
        parcel.writeString(definition);
        parcel.writeString(image);
        parcel.writeString(rank);
    }
    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Term> CREATOR = new Parcelable.Creator<Term>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by Parcelable.writeToParcel().
         * @param parcel The Parcel containing the Term data.
         * @return the Parcel as a Term.
         */
        @Override
        public Term createFromParcel(Parcel parcel) {
            return new Term(parcel);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param i the size of the array (the number of terms).
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public Term[] newArray(int i) {
            return new Term[i];
        }

    };
}
