package com.eeducationgo.markoupdrivers.features.request.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PassangerUser implements Parcelable {
    private String Uid;
    private String Name;
    private String Image;
    private String Email;
    private String Phone;
    private String Type;
    private String station;
    private String destination;
    private String tripTime;
    private String tripDate;
    private double Late;
    private double Long;
    private String Token;

    public PassangerUser() {
    }

    public PassangerUser(String uid, String name, String image, String email, String phone, String type, String station, String destination, String tripTime, String tripDate, double late, double aLong, String token) {
        Uid = uid;
        Name = name;
        Image = image;
        Email = email;
        Phone = phone;
        Type = type;
        this.station = station;
        this.destination = destination;
        this.tripTime = tripTime;
        this.tripDate = tripDate;
        Late = late;
        Long = aLong;
        Token = token;
    }

    protected PassangerUser(Parcel in) {
        Uid = in.readString();
        Name = in.readString();
        Image = in.readString();
        Email = in.readString();
        Phone = in.readString();
        Type = in.readString();
        station = in.readString();
        destination = in.readString();
        tripTime = in.readString();
        tripDate = in.readString();
        Late = in.readDouble();
        Long = in.readDouble();
        Token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Uid);
        dest.writeString(Name);
        dest.writeString(Image);
        dest.writeString(Email);
        dest.writeString(Phone);
        dest.writeString(Type);
        dest.writeString(station);
        dest.writeString(destination);
        dest.writeString(tripTime);
        dest.writeString(tripDate);
        dest.writeDouble(Late);
        dest.writeDouble(Long);
        dest.writeString(Token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PassangerUser> CREATOR = new Creator<PassangerUser>() {
        @Override
        public PassangerUser createFromParcel(Parcel in) {
            return new PassangerUser(in);
        }

        @Override
        public PassangerUser[] newArray(int size) {
            return new PassangerUser[size];
        }
    };

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public double getLate() {
        return Late;
    }

    public void setLate(double late) {
        Late = late;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
