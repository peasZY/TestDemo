package com.lens.ylink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yealink.base.CommonAdapter;
import com.yealink.callscreen.MeetingNowInviteFragment;
import com.yealink.callscreen.function.OnInviteListener;

import java.util.List;

/**
 * Created by chenhn on 2017/10/19.
 */

public class FunctionFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private CommonAdapter mMainAdapter;
    private List<ItemData> itemDatas;


    public synchronized static void show(FragmentManager manager, String tag , List<ItemData> itemDatas){
        FragmentTransaction transaction = manager.beginTransaction();
        FunctionFragment fragment = new FunctionFragment();
        fragment.setDatas(itemDatas);
        transaction.add(fragment, tag);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.root,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.listview);
        mMainAdapter = new CommonAdapter<ItemData>(getActivity()){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item,parent,false);
                }
                TextView text = (TextView) convertView.findViewById(R.id.item_text);
                text.setText(getItem(position).getName());
                return convertView;
            }
        };
        mListView.setAdapter(mMainAdapter);
        mMainAdapter.setData(itemDatas);
        mListView.setOnItemClickListener(this);
    }

    public void setDatas(List<ItemData> itemDatas) {
        this.itemDatas = itemDatas;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemData data = (ItemData) mMainAdapter.getItem(position);
        if(data.getChildren() != null && data.getChildren().size() > 0){
            FunctionFragment.show(getFragmentManager(),data.getName(),data.getChildren());
            return;
        }
        Fragment fragment = Fragment.instantiate(getActivity(),data.getTargetFragment().getName(),null);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(fragment, data.getName());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
