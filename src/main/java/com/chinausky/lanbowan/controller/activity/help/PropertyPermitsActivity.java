package com.chinausky.lanbowan.controller.activity.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;


import com.chinausky.lanbowan.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PropertyPermitsActivity extends AppCompatActivity {

    public static void startActivity(Context activity) {
        Intent intent = new Intent(activity, PropertyPermitsActivity.class);
        activity.startActivity(intent);
    }

    private String content = "房产证办理流程：\n" +
            "一、提供所需材料 \n" +
            "二、进行产权登记 \n" +
            "三、契税税率及缴款书的办理 \n" +
            "1.提供户主身份证复印件。 \n" +
            "2.提供销售发票复印件(带原件校验)。\n" +
            "3.提供销售合同及最终补充合同复印件(带原件校验)。\n" +
            "4.若开发公司代为办理，则派遣员工时应出具公司盖章的证明书。\n" +
            "四、注意事项：保证合同﹑发票﹑测绘报告等各个凭证资料的内容一致性， 认真核对，确保措辞﹑各项数据﹑日期等一致无误。\n" +
            "房产证办理所需材料：\n" +
            "1.房屋购销合同原件及补充合同原件。\n" +
            "2.商品房销售统一发票原件。\n" +
            "3.房屋的外业测绘调查表及分层分户平面图原件。\n" +
            "(若房屋测绘报告为旧格式的，则还需出具竣工验收单及填写房地产登记申请书，新格式只需提供房屋的外业测绘调查表及分层分户平面图原件即可。) \n" +
            "4.完税凭证(契税缴款书)。\n" +
            "5.⑴购房人已婚\n" +
            "①双方身份证复印件(原则上需校验原件)。\n" +
            "②婚姻证明复印件(带原件校验)\n" +
            "③双方任何一方不能到场办理，则需提供私章，若委托第三方代办则需同时提供夫妻双方私章\n" +
            "⑵购房人单身(必须本人到场，不可代办)。\n" +
            "①身份证复印件(带原件校验)\n" +
            "②户口簿或户籍证明复印件(带原件校验)\n" +
            "③单身声明具结书 购房者在本地则到民政局办理 购房者在异地则到公证处办理 购房者在国外则到大使馆办理\n" +
            "⑶购房人为单位\n" +
            "①营业执照(或组织机构代码证)复印件(校验原件)，无法提交原件校验，则需提交工商出具的彩印副本。\n" +
            "房产证办理所需费用：\n" +
            "居民住宅每套80元，如有共有权证增收工本费10元/本\n" +
            "其它房产建筑面积500(含500)㎡以下的每宗200元，500~1000㎡的为300元，1000~2000㎡的为500元，2000~5000㎡的为800元，5000㎡以上的为1000元;\n" +
            "如共有权证增收工本费10元/本。其他税费具体如下： \n" +
            "1、商品房 契税——1.5%(非普通住宅3%) 印花税——0.1% 营业税——如果房产证下发不满两年的，" +
            "需要交纳成交价格5.5%的营业税。已经满两年的普通住宅免交，非普通住宅缴纳出售价格与购买价格差额部分的5.5%的营业税。\n" +
            "2、房改房 契税(同上) 印花税(同上)\n " +
            "土地出让金：当年成本价乘以建筑面积乘以1%如果是标准价或者优惠价的房改房那么还需要交纳当年成本价乘以建筑面积乘以6%的价款，然后再缴纳税费过户\n" +
            "3、经济适用房 契税(同上) 印花税(同上)";

    @Bind(R.id.toolbar_left_btn)
    ImageView mLeftBtn;

    @Bind(R.id.toolbar_right_btn)
    ImageView mRightBtn;

    @Bind(R.id.toolbar_title)
    TextView mTitleTv;

    @Bind(R.id.property_permits_text)
    TextView mPropertyPermitsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_permits);
        ButterKnife.bind(this);
        mTitleTv.setText("帮助中心");
        mPropertyPermitsText.setText(content);

    }

    @OnClick(R.id.toolbar_left_btn)
    void onClickToolbarLeftBtn() {
        onBackPressed();
    }
}
