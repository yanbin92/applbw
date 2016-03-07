package com.chinausky.lanbowan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by succlz123 on 15/12/2.
 */
public class GetMyParkingCostMessage implements Parcelable {

    /**
     * ParkingCostId : 1
     * ParkingCostName : 一个月
     * ParkingCostMonths : 1
     * ParkingCostCharge : 0.01
     */

    private int ParkingCostId;
    private String ParkingCostName;
    private int ParkingCostMonths;
    private double ParkingCostCharge;

    public void setParkingCostId(int ParkingCostId) {
        this.ParkingCostId = ParkingCostId;
    }

    public void setParkingCostName(String ParkingCostName) {
        this.ParkingCostName = ParkingCostName;
    }

    public void setParkingCostMonths(int ParkingCostMonths) {
        this.ParkingCostMonths = ParkingCostMonths;
    }

    public void setParkingCostCharge(double ParkingCostCharge) {
        this.ParkingCostCharge = ParkingCostCharge;
    }

    public int getParkingCostId() {
        return ParkingCostId;
    }

    public String getParkingCostName() {
        return ParkingCostName;
    }

    public int getParkingCostMonths() {
        return ParkingCostMonths;
    }

    public double getParkingCostCharge() {
        return ParkingCostCharge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ParkingCostId);
        dest.writeString(this.ParkingCostName);
        dest.writeInt(this.ParkingCostMonths);
        dest.writeDouble(this.ParkingCostCharge);
    }

    public GetMyParkingCostMessage() {
    }

    protected GetMyParkingCostMessage(Parcel in) {
        this.ParkingCostId = in.readInt();
        this.ParkingCostName = in.readString();
        this.ParkingCostMonths = in.readInt();
        this.ParkingCostCharge = in.readDouble();
    }

    public static final Creator<GetMyParkingCostMessage> CREATOR = new Creator<GetMyParkingCostMessage>() {
        public GetMyParkingCostMessage createFromParcel(Parcel source) {
            return new GetMyParkingCostMessage(source);
        }

        public GetMyParkingCostMessage[] newArray(int size) {
            return new GetMyParkingCostMessage[size];
        }
    };
}
