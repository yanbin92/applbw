package com.chinausky.lanbowan.evideo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.evideo.voip.EvideoVoipFunctions;
import com.evideo.voip.EvideoVoipPreferences;

import com.chinausky.lanbowan.R;

public class AccountManageActivity extends FragmentActivity implements OnClickListener {

    private final static String TAG = AccountManageActivity.class.getCanonicalName();
    private EvideoVoipPreferences mPrefs;

    private EditText mAccount;
    private EditText mPassword;
    private EditText mDomain;
    private EditText mPort;

    private AccountAdapter mAccountAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_account_manage);

        mPrefs = EvideoVoipPreferences.instance();

        mAccount = (EditText) findViewById(R.id.usernameEditText);
        mPassword = (EditText) findViewById(R.id.passwordEditText);
        mDomain = (EditText) findViewById(R.id.domainEditText);
        mPort = (EditText) findViewById(R.id.portEditText);

        findViewById(R.id.addAccountButton).setOnClickListener(this);
        findViewById(R.id.accountBackButton).setOnClickListener(this);

        ListView accounts = (ListView) findViewById(R.id.accountListView);
        mAccountAdapter = new AccountAdapter();
        accounts.setAdapter(mAccountAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.addAccountButton == id) {
            String account = mAccount.getText().toString();
            String password = mPassword.getText().toString();
            String domain = mDomain.getText().toString();
            String port = mPort.getText().toString();
            Log.d(TAG, "account:" + account);
            Log.d(TAG, "password:" + password);
            Log.d(TAG, "domain:" + domain);
            Log.d(TAG, "port:" + port);

            if (TextUtils.isEmpty(port))
                EvideoVoipFunctions.getInstance().register(account, "", password, domain, -1);
            else
                EvideoVoipFunctions.getInstance().register(account, "", password, domain, Integer.valueOf(port));
            mAccountAdapter.notifyDataSetChanged();
            mAccount.setText("");
            mPassword.setText("");
            mDomain.setText("");
            mPort.setText("");
        } else if (R.id.accountBackButton == id) {
            finish();
        }
    }

    private class AccountAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPrefs.getAccountCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(AccountManageActivity.this);
                convertView = layoutInflater.inflate(R.layout.account_list_item, parent, false);
                holder = new Holder();
                holder.accountTxt = (TextView) convertView.findViewById(R.id.accountUriTextView);
                holder.delBtn = (Button) convertView.findViewById(R.id.accountDeleteButton);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            String username = mPrefs.getAccountUsername(position);
            String domain = mPrefs.getAccountDomain(position);
            holder.accountTxt.setText(username + "@" + domain);
            holder.delBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mPrefs.deleteAccount(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class Holder {
            TextView accountTxt;
            Button delBtn;
        }

    }
}
