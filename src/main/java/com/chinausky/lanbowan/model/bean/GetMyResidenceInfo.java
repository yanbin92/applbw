package com.chinausky.lanbowan.model.bean;

/**
 * Created by Woody on 2015/11/1.
 */
public class GetMyResidenceInfo {

    /**
     * ResidenceId : 2
     * ResidenceAddress : 南泊湾A座303
     * ResidenceInfo : null
     * StarnetAccount : M0591_001_Dshow_1_46_1_3_3vu1
     * StarnetPassword : starnet
     * StarnetServer : 121.40.95.209
     * StarnetServerPort : 6050
     * StarnetElevatorAccount : T0591_001_Dshow_1_46_1vu133
     * Cars : null
     * LinkedOwners : null
     */

    private int ResidenceId;
    private String ResidenceAddress;
    private String ResidenceInfo;
    private String StarnetAccount;
    private String StarnetPassword;
    private String StarnetServer;
    private String StarnetServerPort;
    private String StarnetElevatorAccount;
    private String Cars;
    private String LinkedOwners;

    public void setResidenceId(int ResidenceId) {
        this.ResidenceId = ResidenceId;
    }

    public void setResidenceAddress(String ResidenceAddress) {
        this.ResidenceAddress = ResidenceAddress;
    }

    public void setResidenceInfo(String ResidenceInfo) {
        this.ResidenceInfo = ResidenceInfo;
    }

    public void setStarnetAccount(String StarnetAccount) {
        this.StarnetAccount = StarnetAccount;
    }

    public void setStarnetPassword(String StarnetPassword) {
        this.StarnetPassword = StarnetPassword;
    }

    public void setStarnetServer(String StarnetServer) {
        this.StarnetServer = StarnetServer;
    }

    public void setStarnetServerPort(String StarnetServerPort) {
        this.StarnetServerPort = StarnetServerPort;
    }

    public void setStarnetElevatorAccount(String StarnetElevatorAccount) {
        this.StarnetElevatorAccount = StarnetElevatorAccount;
    }

    public void setCars(String Cars) {
        this.Cars = Cars;
    }

    public void setLinkedOwners(String LinkedOwners) {
        this.LinkedOwners = LinkedOwners;
    }

    public int getResidenceId() {
        return ResidenceId;
    }

    public String getResidenceAddress() {
        return ResidenceAddress;
    }

    public String getResidenceInfo() {
        return ResidenceInfo;
    }

    public String getStarnetAccount() {
        return StarnetAccount;
    }

    public String getStarnetPassword() {
        return StarnetPassword;
    }

    public String getStarnetServer() {
        return StarnetServer;
    }

    public String getStarnetServerPort() {
        return StarnetServerPort;
    }

    public String getStarnetElevatorAccount() {
        return StarnetElevatorAccount;
    }

    public String getCars() {
        return Cars;
    }

    public String getLinkedOwners() {
        return LinkedOwners;
    }
}
