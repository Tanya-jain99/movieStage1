package com.example.android.tanya.moviestage1.Model;



import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName ="movies")
public class Results implements Parcelable{
    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    private String poster_path;

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Integer id;

    @ColumnInfo(name = "isFavourite")
    private byte isFavourite;

    @Ignore
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;

    @Ignore
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;

    @Ignore
    @SerializedName("original_title")
    @Expose
    private String originalTitle;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    private String overview;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @Ignore
    public Results(String title,String overview, String releaseDate, Double voteAverage, String poster_path, byte isFavourite){
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.poster_path = poster_path;
        this.isFavourite = isFavourite;
    }

    public Results(Integer id, String title,String overview, String releaseDate, Double voteAverage, String poster_path, byte isFavourite) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.poster_path = poster_path;
        this.isFavourite = isFavourite;
    }


    public String getPoster_path() {
        return this.poster_path;
    }

    public void setPoster(String posterPath) {
        this.poster_path = poster_path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
public byte getIsFavourite() {
    return this.isFavourite;
}
public void setIsFavourite(byte isFavourite){
        this.isFavourite=isFavourite;
}
    public String getBackdropPath() {
        return this.backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public Double getVoteAverage() {
        return this.voteAverage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRate() {
        return this.voteAverage;
    }

    public void setRate(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setRelease(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    @Ignore
    private Results(Parcel in) {
        this.id = in.readInt();
        this.poster_path = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
        this.isFavourite = in.readByte() ;
    }
    public static final Parcelable.Creator<Results> CREATOR = new Parcelable.Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel source) {
            return new Results(source);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeByte( isFavourite);
    }
}
