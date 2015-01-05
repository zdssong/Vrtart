package com.vrtart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.vrtart.R;
import com.vrtart.adapter.CommentListAdapter;
import com.vrtart.adapter.NewsListAdapter;
import com.vrtart.application.ArtApplication;
import com.vrtart.artasynctask.ArtAddCareTask;
import com.vrtart.artasynctask.ArtCollectionTask;
import com.vrtart.baseActicity.BaseActivity;
import com.vrtart.contants.ArtContants;
import com.vrtart.models.Comment;
import com.vrtart.models.Details;
import com.vrtart.models.Information;
import com.vrtart.models.Member;
import com.vrtart.models.Details.OrdinaryEssayDetails;
import com.vrtart.view.ArtListView;
import com.vrtart.webServe.GetArtHttp;

public class ArtDetailsActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {

	private ScrollView mScrollView;
	private LinearLayout mInitLayout;

	private DisplayImageOptions mOptions;
	private ImageLoadingListener mLoadingListener;

	private GetArtHttp mGetArtHttp;

	private WebView webView;

	//
	private TextView mTitle;
	private TextView mPublishTime;
	private TextView mCommentNum;
	private ImageView mNewImage;
	private TextView mWriter;
	private ImageView mNew2;
	private String mAdString;
	private ImageView mAdView;
	private ArtListView mAboutNews;
	private ArtListView mAboutComment;
	private LinearLayout mCareLayout;
	private LinearLayout mErweimaLayout;

	private CommentListAdapter mCommentAdapter;
	private NewsListAdapter mNewAdapter;

	private List<Comment> mComments;
	private OrdinaryEssayDetails mEssayDetails;
	private Details details;
	private List<String> newList;
	private List<Member> mMembers;
	private List<Information> mInformations;

	private String mWebUrl;

	private TextView mBack;
	private TextView mShare;
	private TextView mCollection;
	private TextView mComment;
	private TextView mErWeiMa;
	private float start_y = 0;
	private boolean isDisappear = false;

	// �������Ĳ���
	private String idString;
	private String titleString;
	private String channelString;
	private String channelnameString;

	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.details);

		init();
		getData();
		initView();
		initWebView();
		initListener();
		initTitle();

		removePlatform();
		addQQPlatform();
		addWXPlatform();

		new getAboutNewTask().execute();
	}

	private void addWXPlatform() {
		// ���΢��ƽ̨
		UMWXHandler wxHandler = new UMWXHandler(this, ArtContants.WXAPPID,
				ArtContants.WXAPPSECRET);
		wxHandler.addToSocialSDK();
		// ֧��΢������Ȧ
		UMWXHandler wxCircleHandler = new UMWXHandler(this,
				ArtContants.WXAPPID, ArtContants.WXAPPSECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	private void addQQPlatform() {
		// ���QQ֧��, ��������QQ�������ݵ�target url

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		// mController.openShare(this, false);
	}

	private void removePlatform() {
		mController.getConfig().removePlatform(SHARE_MEDIA.SINA,
				SHARE_MEDIA.TENCENT);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (ArtApplication.OpenNew) {
			Intent intent = new Intent();
			intent.setClass(ArtDetailsActivity.this, MainActivity.class);
			startActivity(intent);
			ArtApplication.OpenNew = false;
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			this.finish();
		} else {
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		}
	}

	private void init() {
		mGetArtHttp = new GetArtHttp();
		mComments = new ArrayList<Comment>();
		details = new Details();
		mEssayDetails = details.getEssayDetails();
		mMembers = new ArrayList<Member>();
		mInformations = new ArrayList<Information>();
		newList = new ArrayList<String>();

		mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.nopicture)
				.showImageForEmptyUri(R.drawable.nopicture)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		mLoadingListener = new LoadingListener();
	}

	private void buttonAppearAnimation() {
		Animation backInAnimation = AnimationUtils.loadAnimation(this,
				R.anim.slide_in_left);
		backInAnimation.setFillAfter(true);
		backInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mBack.setClickable(true);
			}
		});
		Animation otherInAnimation = AnimationUtils.loadAnimation(this,
				R.anim.slide_in_right);
		otherInAnimation.setFillAfter(true);
		otherInAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mCollection.setClickable(true);
				mComment.setClickable(true);
				mShare.setClickable(true);
				mErWeiMa.setClickable(true);
			}
		});
		mBack.setAnimation(backInAnimation);
		mCollection.setAnimation(otherInAnimation);
		mComment.setAnimation(otherInAnimation);
		mShare.setAnimation(otherInAnimation);
		mErWeiMa.setAnimation(otherInAnimation);
	}

	private void buttonDisappearAnimation() {
		Animation backOutAnimation = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_left);
		backOutAnimation.setFillAfter(true);
		backOutAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mBack.setClickable(false);
			}
		});
		Animation otherOutAnimation = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_right);
		otherOutAnimation.setFillAfter(true);
		otherOutAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mCollection.setClickable(true);
				mComment.setClickable(true);
				mShare.setClickable(true);
				mErWeiMa.setClickable(true);
			}
		});
		mBack.setAnimation(backOutAnimation);
		mCollection.setAnimation(otherOutAnimation);
		mComment.setAnimation(otherOutAnimation);
		mShare.setAnimation(otherOutAnimation);
		mErWeiMa.setAnimation(otherOutAnimation);

	}

	/* ��ȡ���ݹ��������� */
	private void getData() {
		Intent intent = getIntent();
		titleString = intent.getStringExtra(ArtContants.TITLE);
		channelString = intent.getStringExtra(ArtContants.CHANNEL);
		channelnameString = intent.getStringExtra(ArtContants.CHANNEL_NAME);
		idString = intent.getStringExtra(ArtContants.ID);
		mWebUrl = "http://m.vrtart.com/html/mobile/frame/" + idString + ".html";
	}

	private void initCommentList() {
		mCommentAdapter = new CommentListAdapter(mComments, this);
		mAboutComment.setAdapter(mCommentAdapter);
	}

	private void initNewList() {
		if (channelString == "-2" || "-2".equals(channelString)) {
			if (mMembers.size() >= 2) {
				for (int i = 0; i < 2; i++) {
					newList.add(mMembers.get(i).getUname());
				}
			} else {
				for (int i = 0; i < mMembers.size(); i++) {
					newList.add(mMembers.get(i).getUname());
				}
			}
		} else {
			if (mInformations.size() >= 2) {
				for (int i = 0; i < 2; i++) {
					newList.add(mInformations.get(i).getTitle());
				}
			} else {
				for (int i = 0; i < mInformations.size(); i++) {
					newList.add(mInformations.get(i).getTitle());
				}
			}
		}
		mNewAdapter = new NewsListAdapter(newList, this);
		mAboutNews.setAdapter(mNewAdapter);
		mAboutNews.setOnItemClickListener(this);
	}

	private void initWebView() {
		webView = (WebView) findViewById(R.id.wb_details);
		if (!TextUtils.isEmpty(mWebUrl)) {
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);// ���ÿ�������JS�ű�
			settings.setJavaScriptCanOpenWindowsAutomatically(true);
			settings.setTextZoom(120);// Sets the text zoom of the page in
										// percent. The default is 100.
			settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			settings.setUseWideViewPort(true); // ��ҳ��ʱ�� ����Ӧ��Ļ
			settings.setLoadWithOverviewMode(true);// ��ҳ��ʱ�� ����Ӧ��Ļ
			settings.setSupportZoom(false);// ��������webview�Ŵ�
			settings.setBuiltInZoomControls(false);

			settings.setPluginState(PluginState.ON);
			settings.setAllowFileAccess(true);
			webView.setBackgroundResource(R.color.transparent);
			// ���js�����ӿ��࣬������� imagelistner
			webView.addJavascriptInterface(new JavascriptInterface(
					getApplicationContext()), "imagelistner");
			webView.setWebChromeClient(new MyWebChromeClient());
			webView.setWebViewClient(new MyWebViewClient());
			webView.loadUrl(mWebUrl);
		}
	}

	private void initView() {
		mNewImage = (ImageView) findViewById(R.id.newsImage);
		mTitle = (TextView) findViewById(R.id.title);
		mPublishTime = (TextView) findViewById(R.id.publish_time);
		mCommentNum = (TextView) findViewById(R.id.comment_count);
		mWriter = (TextView) findViewById(R.id.title1);
		mNew2 = (ImageView) findViewById(R.id.new2);
		mAdView = (ImageView) findViewById(R.id.adImage);
		mCareLayout = (LinearLayout) findViewById(R.id.care);
		mErweimaLayout = (LinearLayout) findViewById(R.id.erweimalayout);
		mAboutNews = (ArtListView) findViewById(R.id.newsListLayout);
		mAboutComment = (ArtListView) findViewById(R.id.commentListLayout);

		mBack = (TextView) findViewById(R.id.back);
		mShare = (TextView) findViewById(R.id.share);
		mCollection = (TextView) findViewById(R.id.collection);
		mComment = (TextView) findViewById(R.id.action_comment_count);
		mErWeiMa = (TextView) findViewById(R.id.erweima);

		mInitLayout = (LinearLayout) findViewById(R.id.initLayout);
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					start_y = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE
						&& start_y != 0) {
					if (event.getY() - start_y < 0 && !isDisappear) {
						buttonDisappearAnimation();
						isDisappear = true;
						return false;
					}
					if (event.getY() - start_y > 0 && isDisappear) {
						buttonAppearAnimation();
						isDisappear = false;
					}
				}
				return false;
			}
		});
	}

	private void initListener() {
		mBack.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mCollection.setOnClickListener(this);
		mComment.setOnClickListener(this);
		mErWeiMa.setOnClickListener(this);
		mNewImage.setOnClickListener(this);

		mCareLayout.setOnClickListener(this);
		mErweimaLayout.setOnClickListener(this);
	}

	private void initTitle() {
		mTitle.setText(titleString);
		mPublishTime.setText(mEssayDetails.getPubDate());
		mCommentNum.setText(mEssayDetails.getFc());
	}

	private void initData() {
		ImageLoader.getInstance().displayImage(mEssayDetails.getFace(),
				mNewImage, mOptions, mLoadingListener);
		mWriter.setText(mEssayDetails.getWriter());
		ImageLoader.getInstance().displayImage(mEssayDetails.getQrcode(),
				mNew2, mOptions, mLoadingListener);
		ImageLoader.getInstance().displayImage(mAdString, mAdView, mOptions,
				mLoadingListener);

	}

	// ע��js��������
	private void addImageClickListner() {
		// ���js�����Ĺ��ܾ��ǣ��������е�img���㣬�����onclick�������ڻ���ִ�е�ʱ����ñ��ؽӿڴ���url��ȥ
		webView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\");"
				+ "var imgurl=''; " + "for(var i=0;i<objs.length;i++)  " + "{"
				+ "imgurl+=objs[i].src+',';"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(imgurl);  "
				+ "    }  " + "}" + "})()");
	}

	// jsͨ�Žӿ�
	public class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img) {

			//
			String[] imgs = img.split(",");
			ArrayList<String> imgsUrl = new ArrayList<String>();
			for (String s : imgs) {
				imgsUrl.add(s);
			}
			Intent intent = new Intent();
			intent.putStringArrayListExtra("infos", imgsUrl);
			intent.setClass(context, ArtImageShowActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	// ����
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			// html�������֮����Ӽ���ͼƬ�ĵ��js����
			addImageClickListner();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.getSettings().setJavaScriptEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			if (newProgress == 100) {
			}
		}
	}

	private class getAboutNewTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			mGetArtHttp.getCommentsHttp(idString);
			mGetArtHttp.getEssayDetails(idString);
			mGetArtHttp.getNewsHttp(channelString, channelnameString);
			mAdString = mGetArtHttp.getAdHttp(ArtContants.AD_ID);
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mComments = mGetArtHttp.getmComments();
			mEssayDetails = mGetArtHttp.getM_EssayDetail();
			if (channelString == "-2" || "-2".equals(channelString)) {
				mMembers = mGetArtHttp.getM_Members();
			} else {
				mInformations = mGetArtHttp.getM_InformationList();
			}
			initCommentList();
			initData();
			initNewList();
			mPublishTime.setText(mEssayDetails.getPubDate());
			mCommentNum.setText(mEssayDetails.getFc());
			mInitLayout.setVisibility(View.GONE);
			mScrollView.setVisibility(View.VISIBLE);
			super.onPostExecute(result);
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (view.getId()) {
		case R.id.newsImage:
			intent.putExtra(ArtContants.NAME, mEssayDetails.getWriter());
			intent.putExtra(ArtContants.MID, mEssayDetails.getmId());
			intent.setClass(this, ArtMemberActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			onBackPressed();
			break;
		case R.id.share:
			mController.setShareContent(mEssayDetails.getTitle() + "\n"
					+ mEssayDetails.getDescription() + "\n"
					+ mEssayDetails.getShareUrl());
			mController.openShare(this, false);
			break;
		case R.id.collection:
			new ArtCollectionTask(idString).execute();
			break;
		case R.id.action_comment_count:
			intent.putExtra(ArtContants.COMMENT_ID, idString);
			intent.setClass(ArtDetailsActivity.this, CommentActivity.class);
			startActivity(intent);
			break;
		case R.id.erweima:
			break;
		case R.id.care:
			new ArtAddCareTask(mEssayDetails.getmId()).execute();
			break;
		case R.id.erweimalayout:
			intent.putExtra(ArtContants.ER_WEI_MA, mEssayDetails.getQrcode());
			intent.setClass(ArtDetailsActivity.this,
					ArtShowTdcodeActivity.class);
			startActivity(intent);
			break;
		}
	}

	private static class LoadingListener extends SimpleImageLoadingListener {

		private static List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			super.onLoadingStarted(imageUri, view);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean isFristDisplay = !displayedImages.contains(imageUri);
				if (isFristDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String channel;
		if (channelString == "-2" || "-2".equals(channelString)) {
			// Intent intent = new Intent();
			// if (mMembers.get(position)..equals(ArtContants.ESSAY_CHANNEL)
			// || channel == ArtContants.ESSAY_CHANNEL) {
			// essayDetail = getArtHttp.getM_EssayDetail();
			// if (essayDetail.getChannel() != null
			// || essayDetail.getChannel() != ""
			// || !essayDetail.getChannel().equals("")) {
			// intent.putExtra(ArtContants.ID, essayDetail.getId());
			// intent.putExtra(ArtContants.TITLE, essayDetail.getTitle());
			// intent.putExtra(ArtContants.CHANNEL,
			// essayDetail.getChannel());
			// intent.putExtra(ArtContants.CHANNEL_NAME,
			// essayDetail.getChannelName());
			// intent.setClass(ArtApplication.getArtApplication(),
			// ArtDetailsActivity.class);
			// ArtApplication.getArtApplication().startActivity(intent);
			// }
			// }
			// if (channel == ArtContants.AUCTION_CHANNEL
			// || channel.equals(ArtContants.AUCTION_CHANNEL)) {
			// mShowAndAuctionDetails = getArtHttp.getM_AuctionDetail();
			// if (mShowAndAuctionDetails.getChannel() != null
			// || mShowAndAuctionDetails.getChannel() != ""
			// || !mShowAndAuctionDetails.getChannel().equals("")) {
			// intent.setClass(ArtApplication.getArtApplication(),
			// ArtAuctionActivity.class);
			// intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
			// mShowAndAuctionDetails.getId());
			// ArtApplication.getArtApplication().startActivity(intent);
			// }
			// }
			// if (channel == ArtContants.SHOW_CHANNEL
			// || channel.equals(ArtContants.SHOW_CHANNEL)) {
			// mShowAndAuctionDetails = getArtHttp.getM_ShowDetail();
			// if (mShowAndAuctionDetails.getChannel() != null
			// || mShowAndAuctionDetails.getChannel() != ""
			// || !mShowAndAuctionDetails.getChannel().equals("")) {
			// intent.setClass(ArtApplication.getArtApplication(),
			// ArtShowActivity.class);
			// intent.putExtra(ArtContants.ID,
			// mShowAndAuctionDetails.getId());
			// intent.putExtra(ArtContants.TITLE,
			// mShowAndAuctionDetails.getTitle());
			// intent.putExtra(ArtContants.CHANNEL,
			// mShowAndAuctionDetails.getChannel());
			// intent.putExtra(ArtContants.CHANNEL_NAME,
			// mShowAndAuctionDetails.getChannelName());
			// ArtApplication.getArtApplication().startActivity(intent);
			// }
			// }
			// if (channel == ArtContants.PICTURE_CHANNEL
			// || channel.equals(ArtContants.PICTURE_CHANNEL)) {
			// mPictureSetDetails = getArtHttp.getM_PictureSetDetail();
			// if (mPictureSetDetails.getChannel() != null
			// || mPictureSetDetails.getChannel() != ""
			// || !mPictureSetDetails.getChannel().equals("")) {
			// intent.setClass(ArtApplication.getArtApplication(),
			// PictureActivity.class);
			// intent.putExtra(ArtContants.ID, mPictureSetDetails.getId());
			// intent.putExtra(ArtContants.TITLE,
			// mPictureSetDetails.getTitle());
			// intent.putExtra(ArtContants.CHANNEL,
			// mPictureSetDetails.getChannel());
			// intent.putExtra(ArtContants.CHANNEL_NAME,
			// mPictureSetDetails.getChannelName());
			// ArtApplication.getArtApplication().startActivity(intent);
			// }
			// }
		} else {
			channel = mInformations.get(position).getChannel();
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (channel.equals(ArtContants.ESSAY_CHANNEL)
					|| channel == ArtContants.ESSAY_CHANNEL) {
				intent.putExtra(ArtContants.ID, mInformations.get(position)
						.getId());
				intent.putExtra(ArtContants.TITLE, mInformations.get(position)
						.getTitle());
				intent.putExtra(ArtContants.CHANNEL, mInformations
						.get(position).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position).getChannelName());
				intent.setClass(ArtApplication.getArtApplication(),
						ArtDetailsActivity.class);
				ArtApplication.getArtApplication().startActivity(intent);
			}
			if (channel == ArtContants.AUCTION_CHANNEL
					|| channel.equals(ArtContants.AUCTION_CHANNEL)) {
				intent.setClass(ArtApplication.getArtApplication(),
						ArtAuctionActivity.class);
				intent.putExtra(ArtContants.ZHAN_LAN_OR_PAI_MAI_AID,
						mInformations.get(position).getId());
				ArtApplication.getArtApplication().startActivity(intent);
			}
			if (channel == ArtContants.SHOW_CHANNEL
					|| channel.equals(ArtContants.SHOW_CHANNEL)) {
				intent.setClass(ArtApplication.getArtApplication(),
						ArtShowActivity.class);
				intent.putExtra(ArtContants.ID, mInformations.get(position)
						.getId());
				intent.putExtra(ArtContants.TITLE, mInformations.get(position)
						.getTitle());
				intent.putExtra(ArtContants.CHANNEL, mInformations
						.get(position).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position).getChannelName());
				ArtApplication.getArtApplication().startActivity(intent);
			}
			if (channel == ArtContants.PICTURE_CHANNEL
					|| channel.equals(ArtContants.PICTURE_CHANNEL)) {
				intent.setClass(ArtApplication.getArtApplication(),
						PictureActivity.class);
				intent.putExtra(ArtContants.ID, mInformations.get(position)
						.getId());
				intent.putExtra(ArtContants.TITLE, mInformations.get(position)
						.getTitle());
				intent.putExtra(ArtContants.CHANNEL, mInformations
						.get(position).getChannel());
				intent.putExtra(ArtContants.CHANNEL_NAME,
						mInformations.get(position).getChannelName());
				ArtApplication.getArtApplication().startActivity(intent);
			}
		}
	}
}
