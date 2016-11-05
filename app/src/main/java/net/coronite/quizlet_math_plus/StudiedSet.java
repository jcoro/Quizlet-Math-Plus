package net.coronite.quizlet_math_plus;


import android.os.Parcel;
import android.os.Parcelable;

public class StudiedSet implements Parcelable {
    String id;
    Set set;

    @Override
    public int describeContents() {
        return 0;
    }

    public Set getSet() {
        return(set);
    }

    public String getId(){
        return(id);
    }

    private StudiedSet(Parcel in) {
        id = in.readString();
        set = in.readParcelable(Set.class.getClassLoader());
    }

    /**
     * Flatten this object into a Parcel.
     * @param parcel the parcel in which the object should be written
     * @param i Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(set, i);
    }

    /**
     * Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
     */
    public static final Parcelable.Creator<StudiedSet> CREATOR = new Parcelable.Creator<StudiedSet>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by Parcelable.writeToParcel().
         * @param parcel The Parcel containing the StudiedSet data.
         * @return the Parcel as a StudiedSet.
         */
        @Override
        public StudiedSet createFromParcel(Parcel parcel) {
            return new StudiedSet(parcel);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param i the size of the array (the number of sets).
         * @return an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public StudiedSet[] newArray(int i) {
            return new StudiedSet[i];
        }

    };
}
