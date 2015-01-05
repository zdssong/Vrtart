package com.vrtart.application;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.vrtart.contants.ArtContants;
import com.vrtart.db.DatabaseManager;
import com.vrtart.models.Column;
import com.vrtart.models.Login;
import com.vrtart.webServe.ArtCookieStore;
import com.vrtart.webServe.GetArtHttp;
import com.vrtart.webServe.PostArtHttp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

public class ArtApplication extends Application {

	public static List<Column> mUserChannelList = new ArrayList<Column>();

	private static ArtApplication m_ArtApplication;
	private DatabaseManager m_DatabaseManager;
	private GetArtHttp m_GetArtHttp;
	private PostArtHttp m_PostArtHttp;
	public static Login mLogin;
	
	//����ָʾ�Ƿ��֪ͨ������
	public static boolean OpenNew = false; 
	// �໬�˵���

	public static ArtCookieStore mArtCookieStore;

	@SuppressWarnings("unused")
	public void onCreate() {
		// TODO Auto-generated method stub
		if (ArtContants.Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}
		super.onCreate();
		mArtCookieStore = new ArtCookieStore();

		m_ArtApplication = this;
		initImageLoader(getApplicationContext());
	}

	public static ArtApplication getArtApplication() {
		return m_ArtApplication;
	}

	public DatabaseManager getDatabaseManger() {
		if (m_DatabaseManager == null)
			m_DatabaseManager = new DatabaseManager(m_ArtApplication);
		return m_DatabaseManager;
	}

	public GetArtHttp getGetArtHttp() {
		if (m_GetArtHttp == null)
			m_GetArtHttp = new GetArtHttp();
		return m_GetArtHttp;
	}

	public PostArtHttp getPostArtHttp() {
		if (m_PostArtHttp == null)
			m_PostArtHttp = new PostArtHttp();
		return m_PostArtHttp;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(5).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				.memoryCache(new WeakMemoryCache())
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(getApplicationContext()) //
		// .memoryCacheExtraOptions(480, 800)//���û���ͼƬʱ��Ŀ�����ֵ��Ĭ��Ϊ��Ļ���
		// .discCacheExtraOptions(480, 800, CompressFormat.JPEG,75
		// )//����Ӳ�̻��棬Ĭ�ϸ�ʽjpeg��ѹ������70
		// .threadPoolSize(5) //�����̳߳ص�����߳�����
		// .threadPriority(Thread.NORM_PRIORITY)//�����߳����ȼ�
		// .denyCacheImageMultipleSizesInMemory()//�Զ�����
		// .memoryCache(new UsingFreqLimitedMemoryCache(4*1024*1024))//���û����С
		// ��UsingFrgLimitMemoryCache�������չ //
		// .discCache(new UnlimitedDiscCache(mContext.getCacheDir())) //����Ӳ�̻���
		// .discCacheFileNameGenerator(new HashCodeFileNameGenerator())//unkown
		// .imageDownloader(new BaseImageDownloader(mContext, 3000, 8000))
		// .defaultDisplayImageOptions(options);//�����Ҫ�򿪻�����ƣ���Ҫ�Լ�buildeһ��option,������DisplayImageOptions
		// .createSimple() //.enableLogging();4
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
