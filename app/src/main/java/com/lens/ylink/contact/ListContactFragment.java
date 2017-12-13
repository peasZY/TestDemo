package com.lens.ylink.contact;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.common.data.Contact;
import com.yealink.sdk.YealinkApi;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表
 * Created by chenhn on 2017/10/25.
 */

public class ListContactFragment extends BaseFragment {

    private ListView mListView;
    private List<Contact> mData = new ArrayList<Contact>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_list,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mLoadTask.execute();
    }

    private AsyncTask<Void,Void,List<Contact>> mLoadTask = new AsyncTask<Void,Void,List<Contact>>() {

        @Override
        protected List<Contact> doInBackground(Void... params) {
            return YealinkApi.instance().searchCloudContact("",false);
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            mData.clear();
            mData.addAll(contacts);
            mAdapter.notifyDataSetChanged();
        }
    };

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Contact getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.contact_list_item,parent,false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView number = (TextView) convertView.findViewById(R.id.number);
            final Button video = (Button) convertView.findViewById(R.id.video);
            Button audio = (Button) convertView.findViewById(R.id.audio);
            final Contact contact = getItem(position);
            if(!TextUtils.isEmpty(contact.getName())){
                name.setText("姓名：" + contact.getName());
            }
            if(!TextUtils.isEmpty(contact.getSerialNumber())){
                number.setText("号码：" + contact.getSerialNumber());
            }
            video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YealinkApi.instance().call(getActivity(),contact.getSerialNumber(),true);
                }
            });
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YealinkApi.instance().call(getActivity(),contact.getSerialNumber(),false);
                }
            });
            return convertView;
        }
    };
}
