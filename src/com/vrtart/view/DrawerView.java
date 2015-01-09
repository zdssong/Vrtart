package com.vrtart.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrtart.AboutActivity;
import com.vrtart.CareActivity;
import com.vrtart.CollectActivity;
import com.vrtart.LoginActivity;
import com.vrtart.PaimaiActivity;
import com.vrtart.R;
import com.vrtart.SettingsActivity;
import com.vrtart.contants.ArtContants;
import com.wt.slidingmenu.lib.SlidingMenu;
import com.wt.slidingmenu.lib.SlidingMenu.OnClosedListener;

/**
 * �Զ���SlidingMenu �����˵���
 * */
public class DrawerView implements OnClickListener {
	private final Activity activity;
	private static SlidingMenu localSlidingMenu;
	private static View setting_btn, guanzhu_btn, shoucang_btn, chengjiao_btn,
			lingxian_btn, weichujia_btn, luohou_btn, guanyu_btn;
	public static View login_btn;
	public static ImageView mHead;
	public static TextView mLogin;

	public DrawerView(Activity activity) {
		this.activity = activity;
	}

	public SlidingMenu initSlidingMenu() {
			localSlidingMenu = new SlidingMenu(activity);
			localSlidingMenu.setMode(SlidingMenu.LEFT);// �������һ��˵�
			localSlidingMenu.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);// ����Ҫʹ�˵�������������Ļ�ķ�Χ
			// localSlidingMenu.setTouchModeBehind(SlidingMenu.SLIDING_CONTENT);//������������ȡ�����˵�����Ľ��㣬������ע�͵�
			localSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);// ������ӰͼƬ�Ŀ��
			localSlidingMenu.setShadowDrawable(R.drawable.shadow);// ������ӰͼƬ
			localSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu����ʱ��ҳ����ʾ��ʣ����
			localSlidingMenu.setFadeDegree(0.35F);// SlidingMenu����ʱ�Ľ���̶�
			localSlidingMenu.attachToActivity(activity, SlidingMenu.LEFT);// ʹSlidingMenu������Activity�ұ�
			// localSlidingMenu.setBehindWidthRes(R.dimen.left_drawer_avatar_size);//����SlidingMenu�˵��Ŀ��
			localSlidingMenu.setMenu(R.layout.left_drawer_fragment);// ����menu�Ĳ����ļ�
			// localSlidingMenu.toggle();//��̬�ж��Զ��رջ���SlidingMenu
			// localSlidingMenu.setSecondaryMenu(R.layout.channel);
			// localSlidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
			localSlidingMenu
					.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
						public void onOpened() {

						}
					});
			localSlidingMenu.setOnClosedListener(new OnClosedListener() {

				@Override
				public void onClosed() {
					// TODO Auto-generated method stub

				}
			});
			initView1();
			initView2();
			initView3();
			initView4();
			initView5();
			initView6();
			initView7();
			initView8();
			initView9();
		return localSlidingMenu;
	}

	private void initView1() {
		setting_btn = localSlidingMenu.findViewById(R.id.setting_btn);
		setting_btn.setOnClickListener(this);
	}

	private void initView2() {
		guanzhu_btn = localSlidingMenu.findViewById(R.id.guanzhu_btn);
		guanzhu_btn.setOnClickListener(this);
	}

	private void initView3() {
		shoucang_btn = localSlidingMenu.findViewById(R.id.shoucang_btn);
		shoucang_btn.setOnClickListener(this);
	}

	private void initView4() {
		chengjiao_btn = localSlidingMenu.findViewById(R.id.chengjiao_btn);
		chengjiao_btn.setOnClickListener(this);
	}

	private void initView5() {
		lingxian_btn = localSlidingMenu.findViewById(R.id.lingxian_btn);
		lingxian_btn.setOnClickListener(this);
	}

	private void initView6() {
		weichujia_btn = localSlidingMenu.findViewById(R.id.weichujia_btn);
		weichujia_btn.setOnClickListener(this);
	}

	private void initView7() {
		luohou_btn = localSlidingMenu.findViewById(R.id.luohou_btn);
		luohou_btn.setOnClickListener(this);
	}

	private void initView8() {
		login_btn = localSlidingMenu.findViewById(R.id.login_btn);
		mHead = (ImageView) localSlidingMenu.findViewById(R.id.touxiang);
		mHead.setBackgroundResource(R.drawable.default_round_head);
		mLogin = (TextView) localSlidingMenu.findViewById(R.id.login);
		login_btn.setOnClickListener(this);
	}

	public static void setLogin() {

	}

	private void initView9() {
		guanyu_btn = localSlidingMenu.findViewById(R.id.guanyu_btn);
		guanyu_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.setting_btn:
//			activity.startActivity(new Intent(activity, SettingsActivity.class));
//			activity.overridePendingTransition(R.anim.slide_in_right,
//					R.anim.slide_out_left);
			break;
		case R.id.guanzhu_btn:
			activity.startActivity(new Intent(activity, CareActivity.class));
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.shoucang_btn:
			activity.startActivity(new Intent(activity, CollectActivity.class));
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.chengjiao_btn:
			intent = new Intent(activity, PaimaiActivity.class);
			intent.putExtra(ArtContants.ACTIVITY_TAG, ArtContants.CHENG_JIAO);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.lingxian_btn:
			intent = new Intent(activity, PaimaiActivity.class);
			intent.putExtra(ArtContants.ACTIVITY_TAG, ArtContants.LING_XIAN);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.weichujia_btn:
			intent = new Intent(activity, PaimaiActivity.class);
			intent.putExtra(ArtContants.ACTIVITY_TAG, ArtContants.MEI_CHU_JIA);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.luohou_btn:
			intent = new Intent(activity, PaimaiActivity.class);
			intent.putExtra(ArtContants.ACTIVITY_TAG, ArtContants.LUO_HOU);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.login_btn:
			intent = new Intent(activity, LoginActivity.class);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.guanyu_btn:
//			activity.startActivity(new Intent(activity, AboutActivity.class));
//			activity.overridePendingTransition(R.anim.slide_in_right,
//					R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
}
