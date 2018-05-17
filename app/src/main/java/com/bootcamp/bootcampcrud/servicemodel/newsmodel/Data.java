
package com.bootcamp.bootcampcrud.servicemodel.newsmodel;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable, Parcelable
{

    @SerializedName("bootcampnews")
    @Expose
    private List<Bootcampnews> bootcampnews = null;
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
    ;
    private final static long serialVersionUID = 4720328089679593618L;

    protected Data(Parcel in) {
        in.readList(this.bootcampnews, (Bootcampnews.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param bootcampnews
     */
    public Data(List<Bootcampnews> bootcampnews) {
        super();
        this.bootcampnews = bootcampnews;
    }

    public List<Bootcampnews> getBootcampnews() {
        return bootcampnews;
    }

    public void setBootcampnews(List<Bootcampnews> bootcampnews) {
        this.bootcampnews = bootcampnews;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(bootcampnews);
    }

    public int describeContents() {
        return  0;
    }

}
