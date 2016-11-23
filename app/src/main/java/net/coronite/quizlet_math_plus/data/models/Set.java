package net.coronite.quizlet_math_plus.data.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import net.coronite.quizlet_math_plus.UserSetFragment;

/**
 * Model for Flash Card Sets
 */
public class Set implements Parcelable {
    String id;
    String url;
    String title;
    String created_by;


    /**
     * Constructor for Sets
     * @param id The Quizlet-generated id for the set
     * @param url The Quizlet url
     * @param title The title for the set
     * @param created_by The creator of the set
     */
    public Set (String id, String url, String title, String created_by){
        this.id = id;
        this.url = url;
        this.title = title;
        this.created_by = created_by;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return(title);
    }

    public String getQuizletSetId(){
        return(id);
    }

    public String getUrl() { return(url); }

    public String getCreatedBy() {return (created_by);}

    /**
     * Method for creating Set objects from a cursor
     * @param cursor the cursor from the set query
     * @return the Set
     */
    public static Set fromCursor(Cursor cursor){
        Set set= null;
        // if Cursor contains results
        if(cursor != null) {
            //String _id = cursor.getString(UserSetFragment.INDEX_COLUMN_AUTO_ID);
            String id = cursor.getString(UserSetFragment.INDEX_COLUMN_SET_ID);
            String url = cursor.getString(UserSetFragment.INDEX_COLUMN_SET_URL);
            String title = cursor.getString(UserSetFragment.INDEX_COLUMN_SET_TITLE);
            String created_by = cursor.getString(UserSetFragment.INDEX_COLUMN_SET_CREATED_BY);
            set = new Set(id, url, title, created_by);
        }
        return set;
    }

    private Set(Parcel in) {
        id = in.readString();
        url = in.readString();
        title = in.readString();
        created_by = in.readString();
    }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeString(title);
        parcel.writeString(created_by);
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
