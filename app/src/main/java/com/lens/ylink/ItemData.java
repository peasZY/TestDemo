package com.lens.ylink;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenhn on 2017/10/19.
 */

public class ItemData {

    private static int mStaticIndex = 0;

    private String mName;
    private int mIndex;
    private Class<?> mTargetFragment;
    private List<ItemData> mChildren = new ArrayList();

    private ItemData() {
        mIndex = mStaticIndex;
        mStaticIndex++;
    }

    public static synchronized ItemData create(String name){
        ItemData itemData = new ItemData();
        itemData.setName(name);
        mStaticIndex++;
        return itemData;
    }

    public String getName() {
        return mName;
    }

    public ItemData setName(String mName) {
        this.mName = mName;
        return this;
    }

    public int getIndex() {
        return mIndex;
    }

    public ItemData addChildItem(String child){
        mChildren.add(create(child));
        return this;
    }

    public<T extends Fragment> ItemData addChildItem(String child,Class<T> targetFragment){
        ItemData itemData = create(child);
        itemData.setTargetFragment(targetFragment);
        mChildren.add(itemData);
        return this;
    }

    public<T extends Fragment> ItemData setTargetFragment(Class<T> targetFragment) {
        this.mTargetFragment = targetFragment;
        return this;
    }

    public Class<?> getTargetFragment() {
        return mTargetFragment;
    }

    public List<ItemData> getChildren() {
        return mChildren;
    }
}
