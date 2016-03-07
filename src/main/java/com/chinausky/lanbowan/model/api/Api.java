package com.chinausky.lanbowan.model.api;

import com.chinausky.lanbowan.model.conifg.RetrofitConfig;
import com.chinausky.lanbowan.model.bean.GetMyCamerasMessage;
import com.chinausky.lanbowan.model.bean.GetMyCarInfo;
import com.chinausky.lanbowan.model.bean.GetMyParkingCostMessage;
import com.chinausky.lanbowan.model.bean.GetMyResidenceInfo;
import com.chinausky.lanbowan.model.bean.GetPropertyAnnouncementMessage;
import com.chinausky.lanbowan.model.bean.MessageReturn;
import com.chinausky.lanbowan.model.bean.OwnerInfo;
import com.chinausky.lanbowan.model.bean.PostMyCar;
import com.chinausky.lanbowan.model.bean.PostPicRequestMessage;
import com.chinausky.lanbowan.model.bean.Register;
import com.chinausky.lanbowan.model.bean.RepairInfo;
import com.chinausky.lanbowan.model.bean.RequestMessage;
import com.chinausky.lanbowan.model.bean.SendMessageBean;
import com.chinausky.lanbowan.model.bean.SignInInfo;
import com.chinausky.lanbowan.model.bean.VisitorInfo;

import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by succlz123 on 15/10/28.
 */
public class Api {

    /**
     * 注册
     */
    public interface registerApi {
        @POST("v1/MyOwner/Register")
        Call<Register> onResult(@Body Register Register);
    }

    public static registerApi register() {
        registerApi registerApi = RetrofitConfig.build().create(registerApi.class);

        return registerApi;
    }

    /**
     * 登陆
     */
    public interface SignInApi {
        @Headers("Authorization: Basic MTIzNDo1Njc4")
        @FormUrlEncoded()
        @POST("v1/token")
        Call<SignInInfo> onResult(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password);
    }

    public static SignInApi signInApi() {
        SignInApi signInApi = RetrofitConfig.build().create(SignInApi.class);

        return signInApi;
    }

    /**
     * 获取对应房间在梯口机上注册的账号
     */
    public interface GetMyResidenceApi {
        @GET("v1/MyOwner/GetMyResidence")
        Call<GetMyResidenceInfo> onResult(@Header("Authorization") String authorization);
    }

    public static GetMyResidenceApi getMyResidenceApi() {
        GetMyResidenceApi getMyResidenceApi = RetrofitConfig.build().create(GetMyResidenceApi.class);

        return getMyResidenceApi;
    }


    /**
     * 查询车位
     */
    public interface GetMyCarApi {
        @GET("v1/MyCar/GetMyCars")
        Call<List<GetMyCarInfo>> onResult(@Header("Authorization") String authorization);
    }

    public static GetMyCarApi getMyCarApi() {
        GetMyCarApi getMyCarApi = RetrofitConfig.build().create(GetMyCarApi.class);

        return getMyCarApi;
    }

    /**
     * 添加车位
     */
    public interface PostMyCarApi {
        @POST("v1/MyCar/PostMyCar")
        Call<RequestMessage> onResult(@Header("Authorization") String authorization, @Body PostMyCar postMyCar);
    }

    public static PostMyCarApi postMyCarApi() {
        PostMyCarApi postMyCarApi = RetrofitConfig.build().create(PostMyCarApi.class);

        return postMyCarApi;
    }

    /**
     * 删除车位
     */
    public interface DeleteMyCarApi {
        @DELETE("v1/MyCar/DeleteMyCar")
        Call<RequestMessage> onResult(@Header("Authorization") String authorization, @QueryMap Map ownerCarId);
    }

    public static DeleteMyCarApi deleteMyCarApi() {
        DeleteMyCarApi deleteMyCarApi = RetrofitConfig.build().create(DeleteMyCarApi.class);

        return deleteMyCarApi;
    }

    /**
     * 查询用户信息
     */
    public interface GetOwnerInfo {
        @GET("v1/MyOwner/GetOwner")
        Call<OwnerInfo> onResult(@Header("Authorization") String authorization);
    }

    public static GetOwnerInfo getOwnerInfo() {
        GetOwnerInfo getOwnerInfo = RetrofitConfig.build().create(GetOwnerInfo.class);

        return getOwnerInfo;
    }


    /**
     * 获取访客记录数
     */
    public interface GetVisitorNum {
        @GET("v1/MyVisitor/GetTotal")
        Call<Integer> onResult(@Header("Authorization") String authorization);
    }

    public static GetVisitorNum getVisitorNum() {
        GetVisitorNum getVisitorNum = RetrofitConfig.build().create(GetVisitorNum.class);

        return getVisitorNum;
    }

    /**
     * 获取访客详细数据
     */
    public interface GetVisitorInfo {
        @GET("v1/MyVisitor/GetMyVisitors")
        Call<List<VisitorInfo>> onResult(@Header("Authorization") String authorization, @Query("page") String page);
    }

    public static GetVisitorInfo getVisitorInfo() {
        GetVisitorInfo getVisitorInfo = RetrofitConfig.build().create(GetVisitorInfo.class);

        return getVisitorInfo;
    }

    /**
     * 提交访客详细数据
     */
    public interface PostVisitorInfo {
        @POST("v1/MyVisitor/PostMyVisitor")
        Call<RequestMessage> onResult(@Header("Authorization") String authorization, @Body VisitorInfo visitorInfo);
    }

    public static PostVisitorInfo postVisitorInfo() {
        PostVisitorInfo postVisitorInfo = RetrofitConfig.build().create(PostVisitorInfo.class);

        return postVisitorInfo;
    }

    /**
     * 删除访客信息
     */
    public interface DeleteVisitorInfo {
        @DELETE("v1/MyVisitor/DeleteMyVisitor")
        Call<RequestMessage> onResult(@Header("Authorization") String authorization, @QueryMap Map ownerCarId);
    }

    public static DeleteVisitorInfo deleteVisitorInfo() {
        DeleteVisitorInfo deleteVisitorInfo = RetrofitConfig.build().create(DeleteVisitorInfo.class);

        return deleteVisitorInfo;
    }


    /**
     * 获取访客记录数
     */
    public interface GetRepairNum {
        @GET("v1/MyRepair/GetTotal")
        Call<Integer> onResult(@Header("Authorization") String authorization);
    }

    public static GetRepairNum getRepairNum() {
        GetRepairNum getRepairNum = RetrofitConfig.build().create(GetRepairNum.class);

        return getRepairNum;
    }

    /**
     * 获取访客详细数据
     */
    public interface GetRepairInfo {
        @GET("v1/MyRepair/GetMyRepairs")
        Call<List<RepairInfo>> onResult(@Header("Authorization") String authorization, @Query("page") String page);
    }

    public static GetRepairInfo getRepairInfo() {
        GetRepairInfo getRepairInfo = RetrofitConfig.build().create(GetRepairInfo.class);

        return getRepairInfo;
    }


    /**
     * 提交维修详细数据
     */
    public interface PostRepairInfo {
        @POST("v1/MyRepair/PostMyRepair")
        Call<RequestMessage> onResult(@Header("Authorization") String authorization, @Body RepairInfo repairInfo);
    }

    public static PostRepairInfo postRepairInfo() {
        PostRepairInfo postRepairInfo = RetrofitConfig.build().create(PostRepairInfo.class);

        return postRepairInfo;
    }

    /**
     * 删除维修信息
     */
    public interface DeleteRepairInfo {
        @DELETE("v1/MyRepair/DeleteMyRepair")
        Call<RequestMessage> onResult(@Header("Authorization") String authorization, @QueryMap Map myRepairId);
    }

    public static DeleteRepairInfo deleteRepairInfo() {
        DeleteRepairInfo deleteRepairInfo = RetrofitConfig.build().create(DeleteRepairInfo.class);

        return deleteRepairInfo;
    }

    /**
     * 提交图片
     */
    public interface PostPic {
        @Multipart
        @POST("v1/FileUpload/FromDataUpload")
        Call<PostPicRequestMessage> onResult(@Header("Authorization") String authorization, @PartMap() Map files);
    }

    public static PostPic postPic() {
        PostPic postPic = RetrofitConfig.build().create(PostPic.class);

        return postPic;
    }

    /**
     * 获取我的车位的价格
     */
    public interface GetMyParkingCost {

        @GET("v1/MyParkingCost/GetMyParkingCost")
        Call<List<GetMyParkingCostMessage>> onResult(@Header("Authorization") String authorization);
    }

    public static GetMyParkingCost getMyParkingCost() {
        GetMyParkingCost getMyParkingCost = RetrofitConfig.build().create(GetMyParkingCost.class);

        return getMyParkingCost;
    }

    /**
     * 获取视频监控
     */
    public interface GetMyCameras {

        @GET("v1/MyCamera/GetMyCameras")
        Call<List<GetMyCamerasMessage>> onResult(@Header("Authorization") String authorization);
    }

    public static GetMyCameras getMyCameras() {
        GetMyCameras getMyCameras = RetrofitConfig.build().create(GetMyCameras.class);

        return getMyCameras;
    }

    /**
     * 获取注册短信
     */
    public interface SendMessage {

        @POST("v1/MyOwner/SendMessage")
        Call<MessageReturn> onResult(@Body SendMessageBean sendMessageBean);
    }

    public static SendMessage sendMessage() {
        SendMessage sendMessage = RetrofitConfig.build().create(SendMessage.class);

        return sendMessage;
    }

    /**
     * 获取重置密码短信
     */
    public interface ForGetPWD {

        @POST("v1/MyOwner/ForGetPWD")
        Call<MessageReturn> onResult(@Body SendMessageBean sendMessageBean);
    }

    public static ForGetPWD forGetPWD() {
        ForGetPWD forGetPWD = RetrofitConfig.build().create(ForGetPWD.class);

        return forGetPWD;
    }

    /**
     * 重置密码短信
     */
    public interface PostForgetPWD {

        @POST("v1/MyOwner/PostForgetPWD")
        Call<RequestMessage> onResult(@Body Register register);
    }

    public static PostForgetPWD postForgetPWD() {
        PostForgetPWD postForgetPWD = RetrofitConfig.build().create(PostForgetPWD.class);

        return postForgetPWD;
    }

    /**
     * 获取首页物业公告
     */
    public interface GetPropertyAnnouncement {

        @GET("v1/MyNotice/GetMyNotice")
        Call<GetPropertyAnnouncementMessage> onResult(@Header("Authorization") String authorization);
    }

    public static GetPropertyAnnouncement getPropertyAnnouncement() {
        GetPropertyAnnouncement getPropertyAnnouncement = RetrofitConfig.build().create(GetPropertyAnnouncement.class);

        return getPropertyAnnouncement;
    }
}
