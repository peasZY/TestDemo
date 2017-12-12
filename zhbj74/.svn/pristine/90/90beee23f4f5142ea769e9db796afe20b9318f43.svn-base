package com.itheima.slidingmenudemo;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 使用slidingmenu 
 * 1. 引入slidingmenu库 
 * 2. 继承SlidingFragmentActivity 
 * 3. onCreate改成public
 * 4. 调用相关api
 * 
 * @author Kevin
 * @date 2015-10-17
 */
public class MainActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 设置侧边栏
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		// 设置右侧侧边栏
		slidingMenu.setSecondaryMenu(R.layout.right_menu);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);// 设置模式,左右都有侧边栏

		// 设置全屏触摸
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		// 设置侧边栏宽度
		slidingMenu.setBehindOffset(200);// 设置屏幕预留宽度

	}
}
