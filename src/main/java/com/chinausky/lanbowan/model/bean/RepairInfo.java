package com.chinausky.lanbowan.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by succlz123 on 15/11/6.
 */
public class RepairInfo implements Parcelable {
    /**
     * RepairId : 16
     * RepairType : 修马桶
     * RepairMessage : 呵呵
     * RepairStatus : 已申请
     * RepairRate : null
     * ApplyDate : 2015-11-08T00:00:00
     * RepairDate : 2015-11-08T00:00:00
     * FinishDate : null
     * UrlPaths : [{"UrlPathId":29,"RelativeUrl":"oa.usky.cn/Upload/Images/20151108_0675a7da-59aa-4e01-8b28-7db159170f63.JPEG","FileType":""},{"UrlPathId":30,"RelativeUrl":"oa.usky.cn/Upload/Images/20151108_0675a7da-59aa-4e01-8b28-7db159170f63.JPEG","FileType":""}]
     */

    private int RepairId;
    private String RepairType;
    private String RepairMessage;
    private String RepairStatus;
    private String RepairRate;
    private String ApplyDate;
    private String RepairDate;
    private String FinishDate;
    /**
     * UrlPathId : 29
     * RelativeUrl : oa.usky.cn/Upload/Images/20151108_0675a7da-59aa-4e01-8b28-7db159170f63.JPEG
     * FileType :
     */

    private List<UrlPathsEntity> UrlPaths;

    public void setRepairId(int RepairId) {
        this.RepairId = RepairId;
    }

    public void setRepairType(String RepairType) {
        this.RepairType = RepairType;
    }

    public void setRepairMessage(String RepairMessage) {
        this.RepairMessage = RepairMessage;
    }

    public void setRepairStatus(String RepairStatus) {
        this.RepairStatus = RepairStatus;
    }

    public void setRepairRate(String RepairRate) {
        this.RepairRate = RepairRate;
    }

    public void setApplyDate(String ApplyDate) {
        this.ApplyDate = ApplyDate;
    }

    public void setRepairDate(String RepairDate) {
        this.RepairDate = RepairDate;
    }

    public void setFinishDate(String FinishDate) {
        this.FinishDate = FinishDate;
    }

    public void setUrlPaths(List<UrlPathsEntity> UrlPaths) {
        this.UrlPaths = UrlPaths;
    }

    public int getRepairId() {
        return RepairId;
    }

    public String getRepairType() {
        return RepairType;
    }

    public String getRepairMessage() {
        return RepairMessage;
    }

    public String getRepairStatus() {
        return RepairStatus;
    }

    public String getRepairRate() {
        return RepairRate;
    }

    public String getApplyDate() {
        return ApplyDate;
    }

    public String getRepairDate() {
        return RepairDate;
    }

    public String getFinishDate() {
        return FinishDate;
    }

    public List<UrlPathsEntity> getUrlPaths() {
        return UrlPaths;
    }

    public static class UrlPathsEntity implements Parcelable {
        private int UrlPathId;
        private String RelativeUrl;
        private String FileType;

        public void setUrlPathId(int UrlPathId) {
            this.UrlPathId = UrlPathId;
        }

        public void setRelativeUrl(String RelativeUrl) {
            this.RelativeUrl = RelativeUrl;
        }

        public void setFileType(String FileType) {
            this.FileType = FileType;
        }

        public int getUrlPathId() {
            return UrlPathId;
        }

        public String getRelativeUrl() {
            return RelativeUrl;
        }

        public String getFileType() {
            return FileType;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.UrlPathId);
            dest.writeString(this.RelativeUrl);
            dest.writeString(this.FileType);
        }

        public UrlPathsEntity() {
        }

        protected UrlPathsEntity(Parcel in) {
            this.UrlPathId = in.readInt();
            this.RelativeUrl = in.readString();
            this.FileType = in.readString();
        }

        public static final Creator<UrlPathsEntity> CREATOR = new Creator<UrlPathsEntity>() {
            public UrlPathsEntity createFromParcel(Parcel source) {
                return new UrlPathsEntity(source);
            }

            public UrlPathsEntity[] newArray(int size) {
                return new UrlPathsEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.RepairId);
        dest.writeString(this.RepairType);
        dest.writeString(this.RepairMessage);
        dest.writeString(this.RepairStatus);
        dest.writeString(this.RepairRate);
        dest.writeString(this.ApplyDate);
        dest.writeString(this.RepairDate);
        dest.writeString(this.FinishDate);
        dest.writeTypedList(UrlPaths);
    }

    public RepairInfo() {
    }

    protected RepairInfo(Parcel in) {
        this.RepairId = in.readInt();
        this.RepairType = in.readString();
        this.RepairMessage = in.readString();
        this.RepairStatus = in.readString();
        this.RepairRate = in.readString();
        this.ApplyDate = in.readString();
        this.RepairDate = in.readString();
        this.FinishDate = in.readString();
        this.UrlPaths = in.createTypedArrayList(UrlPathsEntity.CREATOR);
    }

    public static final Creator<RepairInfo> CREATOR = new Creator<RepairInfo>() {
        public RepairInfo createFromParcel(Parcel source) {
            return new RepairInfo(source);
        }

        public RepairInfo[] newArray(int size) {
            return new RepairInfo[size];
        }
    };
}
