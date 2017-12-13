package com.lens.ylink;


import com.lens.ylink.regist.CloudRegistByAccountFragment;
import com.lens.ylink.regist.CloudRegistByPincodeFragment;
import com.lens.ylink.regist.SipRegistFrgament;
import com.lens.ylink.regist.YmsRegistFragment;
import com.lens.ylink.talk.IncomingFragment;
import com.lens.ylink.talk.MeetNowFragment;
import com.lens.ylink.talk.TalkFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhn on 2017/10/19.
 */

public class DataSource {

    private static DataSource mDataSource;

    private List<ItemData> mDatas;

    private DataSource() {
        mDatas = new ArrayList();

//        ItemData init = ItemData.create("初始化").setTargetFragment(InitFragment.class);

        ItemData regist = ItemData.create("注册")
                .addChildItem("Sip注册", SipRegistFrgament.class)
                .addChildItem("Cloud注册 PINCODE方式", CloudRegistByPincodeFragment.class)
                .addChildItem("Cloud注册 账号密码方式", CloudRegistByAccountFragment.class)
                .addChildItem("Yms注册", YmsRegistFragment.class);
//                .addChildItem("H323注册", H323RegistFragment.class);
        mDatas.add(regist);

        ItemData talk = ItemData.create("通话")
                .addChildItem("去电", TalkFragment.class)
                .addChildItem("发起及时会议", MeetNowFragment.class)
//                .addChildItem("参加会议", JoinMeetFragment.class)
                .addChildItem("来电接收", IncomingFragment.class);
        mDatas.add(talk);

//        ItemData contact = ItemData.create("联系人")
//                .addChildItem("组织架构(新服务器)", TreeContactFragment.class)
//                .addChildItem("联系人列表(旧服务器)", ListContactFragment.class);
//        mDatas.add(contact);

    }

    public synchronized static DataSource instance(){
        if(mDataSource == null){
            mDataSource = new DataSource();
        }
        return mDataSource;
    }

    public List<ItemData> getDatas() {
        return mDatas;
    }
}
