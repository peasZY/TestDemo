package com.lens.ylink.contact;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lens.ylink.BaseFragment;
import com.lens.ylink.R;
import com.yealink.common.data.OrgNode;
import com.yealink.sdk.YealinkApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhn on 2017/10/25.
 */

public class TreeContactFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private List<OrgNode> mData = new ArrayList<OrgNode>();
    private boolean mIsRoot = true;
    private String mParentId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_tree,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        if(mIsRoot){
            mLoadRootTask.execute();
        } else {
            mLoadChilidrenTask.execute(mParentId);
        }
    }

    public void setParentId(String parentId) {
        mIsRoot = false;
        mParentId = parentId;
    }

    private AsyncTask<String,Void,List<OrgNode>> mLoadChilidrenTask = new AsyncTask<String,Void,List<OrgNode>>() {

        @Override
        protected List<OrgNode> doInBackground(String... params) {

            String orgId = params[0];
            return YealinkApi.instance().getOrgChildNode(true,orgId);
        }

        @Override
        protected void onPostExecute(List<OrgNode> contacts) {
            if(contacts.size() == 0){
                getActivity().onBackPressed();
            }
            mData.clear();
            mData.addAll(contacts);
            mAdapter.notifyDataSetChanged();
        }
    };

    private AsyncTask<Void,Void,OrgNode> mLoadRootTask = new AsyncTask<Void,Void,OrgNode>() {

        @Override
        protected OrgNode doInBackground(Void... params) {
            return YealinkApi.instance().getOrgRoot(true);
        }

        @Override
        protected void onPostExecute(OrgNode root) {
            mLoadChilidrenTask.execute(root.getNodeId());
        }
    };

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public OrgNode getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.contact_tree_item,parent,false);
            }
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView number = (TextView) convertView.findViewById(R.id.number);
            Button video = (Button) convertView.findViewById(R.id.video);
            Button audio = (Button) convertView.findViewById(R.id.audio);
            final OrgNode contact = getItem(position);
            if(!TextUtils.isEmpty(contact.getName())){
                name.setText("姓名：" + contact.getName());
            }
            if(!TextUtils.isEmpty(contact.getSerialNumber())){
                number.setText("号码：" + contact.getSerialNumber());
            }
            if(contact.getType() == OrgNode.TYPE_ORG){
                video.setVisibility(View.GONE);
                audio.setVisibility(View.GONE);
            } else {
                video.setVisibility(View.VISIBLE);
                audio.setVisibility(View.VISIBLE);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrgNode orgNode = (OrgNode) mAdapter.getItem(position);
        if(orgNode.getType() == OrgNode.TYPE_ORG){
            TreeContactFragment fragment = (TreeContactFragment) Fragment.instantiate(getActivity(),TreeContactFragment.class.getName(),null);
            fragment.setParentId(orgNode.getNodeId());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(fragment, orgNode.getName());
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }
}
