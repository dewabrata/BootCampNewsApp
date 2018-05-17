
package com.bootcamp.bootcampcrud.servicemodel.newsmodel;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.bootcamp.bootcampcrud.ApplicationBootCamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = ApplicationBootCamp.class)
public class Bootcampnews extends BaseModel implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;
    @SerializedName("title")
    @Expose
    @Column
    private String title;
    @SerializedName("content")
    @Expose
    @Column
    private String content;
    @SerializedName("author")
    @Expose
    @Column
    private String author;
    @SerializedName("image")
    @Expose
    @Column
    private String image;
    public final static Creator<Bootcampnews> CREATOR = new Creator<Bootcampnews>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Bootcampnews createFromParcel(Parcel in) {
            return new Bootcampnews(in);
        }

        public Bootcampnews[] newArray(int size) {
            return (new Bootcampnews[size]);
        }

    }
    ;
    private final static long serialVersionUID = -6632937261547135264L;

    protected Bootcampnews(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((String) in.readValue((String.class.getClassLoader())));
        this.author = ((String) in.readValue((String.class.getClassLoader())));
        this.image = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public Bootcampnews() {
    }

    /**
     * 
     * @param content
     * @param id
     * @param author
     * @param title
     * @param image
     */
    public Bootcampnews(String id, String title, String content, String author, String image) {
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(content);
        dest.writeValue(author);
        dest.writeValue(image);
    }

    public int describeContents() {
        return  0;
    }

}
