package com.chinausky.lanbowan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by succlz123 on 15/11/4.
 */
public class VisitorInfo implements Parcelable {

    /**
     * BookingId : 18
     * OwnerId : 5
     * MyAddress : 0
     * BookingDate : 2015-11-04T09:45:57.09
     * BookingDateTimestamp : null
     * AppointmentDate : null
     * AppointmentDateTimestamp : null
     * Reason : 0
     * Remark : 吃饭
     * VisitorStatus : 0
     * ArriveDate : 2015-11-02T14:31:13.267
     * ArriveDateTimestamp : null
     * LeaveDate : 2015-11-02T14:31:13.267
     * LeaveDateTimestamp : null
     * Visitors : [{"VisitorId":35,"IdentityCard":"0","VisitorName":"张三 123","MobileNumber":"13123123","PlateNumber":"沪-123122"},{"VisitorId":36,"IdentityCard":"0","VisitorName":"张三 123范德萨分手费","MobileNumber":"13123123","PlateNumber":"沪-123122"}]
     */

    private String BookingId;
    private int OwnerId;
    private String MyAddress;
    private String BookingDate;
    private String BookingDateTimestamp;
    private String AppointmentDate;
    private String AppointmentDateTimestamp;
    private String Reason;
    private String Remark;
    private int VisitorStatus;
    private String ArriveDate;
    private String ArriveDateTimestamp;
    private String LeaveDate;
    private String LeaveDateTimestamp;
    /**
     * VisitorId : 35
     * IdentityCard : 0
     * VisitorName : 张三 123
     * MobileNumber : 13123123
     * PlateNumber : 沪-123122
     */

    private List<VisitorsEntity> Visitors;

    public void setBookingId(String BookingId) {
        this.BookingId = BookingId;
    }

    public void setOwnerId(int OwnerId) {
        this.OwnerId = OwnerId;
    }

    public void setMyAddress(String MyAddress) {
        this.MyAddress = MyAddress;
    }

    public void setBookingDate(String BookingDate) {
        this.BookingDate = BookingDate;
    }

    public void setBookingDateTimestamp(String BookingDateTimestamp) {
        this.BookingDateTimestamp = BookingDateTimestamp;
    }

    public void setAppointmentDate(String AppointmentDate) {
        this.AppointmentDate = AppointmentDate;
    }

    public void setAppointmentDateTimestamp(String AppointmentDateTimestamp) {
        this.AppointmentDateTimestamp = AppointmentDateTimestamp;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public void setVisitorStatus(int VisitorStatus) {
        this.VisitorStatus = VisitorStatus;
    }

    public void setArriveDate(String ArriveDate) {
        this.ArriveDate = ArriveDate;
    }

    public void setArriveDateTimestamp(String ArriveDateTimestamp) {
        this.ArriveDateTimestamp = ArriveDateTimestamp;
    }

    public void setLeaveDate(String LeaveDate) {
        this.LeaveDate = LeaveDate;
    }

    public void setLeaveDateTimestamp(String LeaveDateTimestamp) {
        this.LeaveDateTimestamp = LeaveDateTimestamp;
    }

    public void setVisitors(List<VisitorsEntity> Visitors) {
        this.Visitors = Visitors;
    }

    public String getBookingId() {
        return BookingId;
    }

    public int getOwnerId() {
        return OwnerId;
    }

    public String getMyAddress() {
        return MyAddress;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public String getBookingDateTimestamp() {
        return BookingDateTimestamp;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getAppointmentDateTimestamp() {
        return AppointmentDateTimestamp;
    }

    public String getReason() {
        return Reason;
    }

    public String getRemark() {
        return Remark;
    }

    public int getVisitorStatus() {
        return VisitorStatus;
    }

    public String getArriveDate() {
        return ArriveDate;
    }

    public String getArriveDateTimestamp() {
        return ArriveDateTimestamp;
    }

    public String getLeaveDate() {
        return LeaveDate;
    }

    public String getLeaveDateTimestamp() {
        return LeaveDateTimestamp;
    }

    public List<VisitorsEntity> getVisitors() {
        return Visitors;
    }

    public static class VisitorsEntity implements Parcelable {
        private int VisitorId;
        private String IdentityCard;
        private String VisitorName;
        private String MobileNumber;
        private String PlateNumber;

        public void setVisitorId(int VisitorId) {
            this.VisitorId = VisitorId;
        }

        public void setIdentityCard(String IdentityCard) {
            this.IdentityCard = IdentityCard;
        }

        public void setVisitorName(String VisitorName) {
            this.VisitorName = VisitorName;
        }

        public void setMobileNumber(String MobileNumber) {
            this.MobileNumber = MobileNumber;
        }

        public void setPlateNumber(String PlateNumber) {
            this.PlateNumber = PlateNumber;
        }

        public int getVisitorId() {
            return VisitorId;
        }

        public String getIdentityCard() {
            return IdentityCard;
        }

        public String getVisitorName() {
            return VisitorName;
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public String getPlateNumber() {
            return PlateNumber;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.VisitorId);
            dest.writeString(this.IdentityCard);
            dest.writeString(this.VisitorName);
            dest.writeString(this.MobileNumber);
            dest.writeString(this.PlateNumber);
        }

        public VisitorsEntity() {
        }

        protected VisitorsEntity(Parcel in) {
            this.VisitorId = in.readInt();
            this.IdentityCard = in.readString();
            this.VisitorName = in.readString();
            this.MobileNumber = in.readString();
            this.PlateNumber = in.readString();
        }

        public static final Creator<VisitorsEntity> CREATOR = new Creator<VisitorsEntity>() {
            public VisitorsEntity createFromParcel(Parcel source) {
                return new VisitorsEntity(source);
            }

            public VisitorsEntity[] newArray(int size) {
                return new VisitorsEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.BookingId);
        dest.writeInt(this.OwnerId);
        dest.writeString(this.MyAddress);
        dest.writeString(this.BookingDate);
        dest.writeString(this.BookingDateTimestamp);
        dest.writeString(this.AppointmentDate);
        dest.writeString(this.AppointmentDateTimestamp);
        dest.writeString(this.Reason);
        dest.writeString(this.Remark);
        dest.writeInt(this.VisitorStatus);
        dest.writeString(this.ArriveDate);
        dest.writeString(this.ArriveDateTimestamp);
        dest.writeString(this.LeaveDate);
        dest.writeString(this.LeaveDateTimestamp);
        dest.writeTypedList(Visitors);
    }

    public VisitorInfo() {
    }

    protected VisitorInfo(Parcel in) {
        this.BookingId = in.readString();
        this.OwnerId = in.readInt();
        this.MyAddress = in.readString();
        this.BookingDate = in.readString();
        this.BookingDateTimestamp = in.readString();
        this.AppointmentDate = in.readString();
        this.AppointmentDateTimestamp = in.readString();
        this.Reason = in.readString();
        this.Remark = in.readString();
        this.VisitorStatus = in.readInt();
        this.ArriveDate = in.readString();
        this.ArriveDateTimestamp = in.readString();
        this.LeaveDate = in.readString();
        this.LeaveDateTimestamp = in.readString();
        this.Visitors = in.createTypedArrayList(VisitorsEntity.CREATOR);
    }

    public static final Creator<VisitorInfo> CREATOR = new Creator<VisitorInfo>() {
        public VisitorInfo createFromParcel(Parcel source) {
            return new VisitorInfo(source);
        }

        public VisitorInfo[] newArray(int size) {
            return new VisitorInfo[size];
        }
    };
}
