package vn.hcmute.edu.vn.cuoiky.foody.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Agencys implements Parcelable{
    String diachi;
    double latitude, longitude, khoangcach;

    public Agencys(){

    }

    protected Agencys(Parcel in) {
        diachi = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        khoangcach = in.readDouble();
    }

    public static final Creator<Agencys> CREATOR = new Creator<Agencys>() {
        @Override
        public Agencys createFromParcel(Parcel in) {
            return new Agencys(in);
        }

        @Override
        public Agencys[] newArray(int size) {
            return new Agencys[size];
        }
    };

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(double khoangcach) {
        this.khoangcach = khoangcach;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(diachi);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(khoangcach);
    }
}
