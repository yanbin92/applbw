package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/10/29.
 */
public class GetMyCarInfo {

    /**
     * OwnerCarId : 103
     * OwnerId : 5
     * PlateName : 我的车牌1
     * PlateNumber : 沪h12345
     * CarType : null
     * PaymentType : null
     * ExpiringDate : null
     * ParkingStatus : null
     */

    private int OwnerCarId;
    private int OwnerId;
    private String PlateName;
    private String PlateNumber;
    private Object CarType;
    private Object PaymentType;
    private Object ExpiringDate;
    private Object ParkingStatus;
    private boolean showMore;

    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public void setOwnerCarId(int OwnerCarId) {
        this.OwnerCarId = OwnerCarId;
    }

    public void setOwnerId(int OwnerId) {
        this.OwnerId = OwnerId;
    }

    public void setPlateName(String PlateName) {
        this.PlateName = PlateName;
    }

    public void setPlateNumber(String PlateNumber) {
        this.PlateNumber = PlateNumber;
    }

    public void setCarType(Object CarType) {
        this.CarType = CarType;
    }

    public void setPaymentType(Object PaymentType) {
        this.PaymentType = PaymentType;
    }

    public void setExpiringDate(Object ExpiringDate) {
        this.ExpiringDate = ExpiringDate;
    }

    public void setParkingStatus(Object ParkingStatus) {
        this.ParkingStatus = ParkingStatus;
    }

    public int getOwnerCarId() {
        return OwnerCarId;
    }

    public int getOwnerId() {
        return OwnerId;
    }

    public String getPlateName() {
        return PlateName;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public Object getCarType() {
        return CarType;
    }

    public Object getPaymentType() {
        return PaymentType;
    }

    public Object getExpiringDate() {
        return ExpiringDate;
    }

    public Object getParkingStatus() {
        return ParkingStatus;
    }
}
