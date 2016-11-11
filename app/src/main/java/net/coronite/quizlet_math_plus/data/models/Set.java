package net.coronite.quizlet_math_plus.data.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import net.coronite.quizlet_math_plus.UserSetFragment;

/**
 * The model for Sets
 */
public class Set implements Parcelable {
    String quizlet_id;
    String url;
    String title;

    public Set (String id, String url, String title){
        this.quizlet_id = id;
        this.url = url;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return(title);
    }

    public String getQuizletSetId(){
        return(quizlet_id);
    }

    public String getUrl() { return(url); }

    public static Set fromCursor(Cursor cursor){
        Set set= null;
        // if Cursor is contains results
        if(cursor != null) {
            String quizlet_id = cursor.getString(UserSetFragment.INDEX_COLUMN_SET_ID);
            String url = cursor.getString(UserSetFragment.INDEX_SET_URL);
            String title = cursor.getString(UserSetFragment.INDEX_SET_TITLE);
            set = new Set(quizlet_id, url, title);
        }
        return set;
    }

    private Set(Parcel in) {
        quizlet_id = in.readString();
        url = in.readString();
        title = in.readString();
    }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quizlet_id);
        parcel.writeString(url);
        parcel.writeString(title);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<Set> CREATOR = new Parcelable.Creator<Set>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by Parcelable.writeToParcel().
         * @param parcel The Parcel containing the Set data.
         * @return the Parcel as a Set.
         */
        @Override
        public Set createFromParcel(Parcel parcel) {
            return new Set(parcel);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param i the size of the array (the number of sets).
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public Set[] newArray(int i) {
            return new Set[i];
        }

    };
}
