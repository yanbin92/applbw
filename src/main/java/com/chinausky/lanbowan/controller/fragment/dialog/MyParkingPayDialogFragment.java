package com.chinausky.lanbowan.controller.fragment.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chinausky.lanbowan.MyApplication;
import com.chinausky.lanbowan.R;
import com.chinausky.lanbowan.model.bean.GetMyParkingCostMessage;
import com.chinausky.lanbowan.model.utils.base.GlobalUtils;
import com.chinausky.lanbowan.model.utils.pay.PayResult;
import com.chinausky.lanbowan.model.utils.pay.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by succlz123 on 15/8/28.
 */
public class MyParkingPayDialogFragment extends DialogFragment {

    public static void startFragment(AppCompatActivity activity, ArrayList<GetMyParkingCostMessage> list) {
        MyParkingPayDialogFragment fragment = new MyParkingPayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("myParkingCostList", list);
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), "MyParkingPayDialogFragment");
    }

    // 商户PID
    public static final String PARTNER = "2088121343022224";
    // 商户收款账号
    public static final String SELLER = "neal.chen@chinausky.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK4DCGD3+M8Jimi99yo47vuFeIzEako+/ZfeT4f79vJGv/RcuPX5GAHrGTW+jSZKTZUjifdGHUXlbh9BlH8UnQHmRUY/HdbLQyQ6cZXiLw50YPevAt2vSEOxgy4Z8wazsnayqGRkk3pmwHWNE5p65BhFQZRgfK/x7l/nAKPgdDTJAgMBAAECgYBd9LE/emTTlTkwmPGt0JPpJwYSK7OgZyaSK60RpZCO/HiEvtP3rAm0b3ZrOHoKJLCDSG8kHq2Lofe11LkTbNPg4JDkcvg4VN6OEpQfBnLAtSDfZBwsTOSFNTLo0b4o9UWtrOoLatATvq7ZwOIwt0wYrWXPGUw6Q7bSZ0HhaCfsAQJBANvl8aP9UOq5e7m7uInBYhMrrc0tlvhsc6apsTnMfAnQs5cUiezuow7jbQDWlB5H05crEq287oSs2z1nI4hwFcECQQDKlIpFwU66VUnAZste7TWPqBxeu4/VOR9dDA6LdNfCL2kQF1hihdg0sw5AIcDxdivtXy5KedDLFFwP2ZgdubEJAkBubxegxOorR4WkbGsr+4uWlxwM6/Viw4x9ScFwhopQn5hhqnXuIJaRNpQalKkEYAwaYg5aZITg6rKcLr0Am1HBAkBk4jY96LeJKcIRs99hhaXfxatjLerCF7FbyA5SOrzb8npSzh1w6xCxpd3Z6DVKyjuz+Wt3IVTuXVQ/fHA5lWjpAkEAhoVYwlxBDavX1tWx326h/iODqEG2omRiCFPt00WzVJnlf7QRUS8CLrrijtczakiqYBl32rleTB1819+PnmTVPA==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MyApplication.getInstance().getApplicationContext(), "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "支付失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(getActivity(), "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private List<GetMyParkingCostMessage> list = new ArrayList<>();
    private RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialogfragment_my_parking_renew, container);

        list = getArguments().getParcelableArrayList("myParkingCostList");

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.my_parking_ll);
        Button button = (Button) view.findViewById(R.id.ma_parking_pay_btn);

        radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams radioGroupLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        radioGroup.setLayoutParams(radioGroupLayoutParams);
        radioGroup.setOrientation(LinearLayout.VERTICAL);

        for (GetMyParkingCostMessage myParkingCost : list) {
            RadioButton radioButton = new RadioButton(getActivity());

            RadioGroup.LayoutParams radioButtonLayoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            int top = GlobalUtils.dip2pix(getActivity(), 10);
            int left = GlobalUtils.dip2pix(getActivity(), 30);
            radioButtonLayoutParams.setMargins(left, top, 0, top);
            radioButton.setText(myParkingCost.getParkingCostName() + "  " + myParkingCost.getParkingCostCharge() + "元");
            radioButton.setTextSize(17);
            radioButton.setTag(myParkingCost);
            radioGroup.addView(radioButton, radioButtonLayoutParams);
        }

        linearLayout.addView(radioGroup);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = radioGroup.getChildAt(radioGroup.getCheckedRadioButtonId() - 1).getTag();
                if (tag == null) {
                    return;
                }
                GetMyParkingCostMessage myParkingCost = (GetMyParkingCostMessage) tag;

                pay(myParkingCost.getParkingCostName(), myParkingCost.getParkingCostCharge());
            }
        });
        return view;
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(String info, double price) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    getDialog().dismiss();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo("我的车位缴费", info, String.valueOf(price));

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(getActivity());
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(getActivity());
        String version = payTask.getVersion();
        Toast.makeText(getActivity(), version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://lbw.usky.cn/notify_url.aspx"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
