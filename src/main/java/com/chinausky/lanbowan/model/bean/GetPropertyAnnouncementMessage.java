package com.chinausky.lanbowan.model.bean;

/**
 * Created by succlz123 on 15/12/25.
 */
public class GetPropertyAnnouncementMessage {

    /**
     * NoticeId : 1
     * Title : 物业公告
     * Context : 物业公告：恳请小区内养犬的业主，要自觉遵守《江西市严格限制养犬规定》，做到在园区内遛狗要挂狗牌、束狗链，并由成年人牵领拴狗绳，及时清理狗粪，不得侵扰他人的正常生活，定期为爱犬注射狂犬疫苗。
     */

    private int NoticeId;
    private String Title;
    private String Context;

    public void setNoticeId(int NoticeId) {
        this.NoticeId = NoticeId;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public void setContext(String Context) {
        this.Context = Context;
    }

    public int getNoticeId() {
        return NoticeId;
    }

    public String getTitle() {
        return Title;
    }

    public String getContext() {
        return Context;
    }
}
