package com.vrtart.webServe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

import com.vrtart.application.ArtApplication;
import com.vrtart.models.Auction;
import com.vrtart.models.Column;
import com.vrtart.models.Commend;
import com.vrtart.models.Comment;
import com.vrtart.models.Details;
import com.vrtart.models.Information;
import com.vrtart.models.Login;
import com.vrtart.models.Member;
import com.vrtart.models.PersonalPage;
import com.vrtart.models.Register;
import com.vrtart.models.Details.OrdinaryEssayDetails;
import com.vrtart.models.Details.PictureSetDetails;
import com.vrtart.models.Details.ShowAndAuctionDetails;
import com.vrtart.models.PersonalPage.About;
import com.vrtart.models.PersonalPage.Cmtype;

/*
 * ������ʹ��Get���������ͺͽ�������
 * */

public class GetArtHttp {

	private GetArtJson m_GetArtJson;

	private List<Column> m_ColumnList;
	private List<Commend> m_CommendList;
	private List<Information> m_InformationList;
	private List<Register> m_RegisterList;

	private Details m_Details;
	private OrdinaryEssayDetails m_EssayDetail;
	private PictureSetDetails m_PictureSetDetail;
	private ShowAndAuctionDetails m_ShowDetail;
	private ShowAndAuctionDetails m_AuctionDetail;

	private List<Member> m_Members;

	private PersonalPage m_PersonalPage;
	private List<Cmtype> m_Cmtypes;
	private About m_Abouts;
	private Login m_Login;

	private boolean m_IsAuth;// �ж��Ƿ��Ѿ��������ֻ���֤��

	private String m_ResultString;
	private List<Comment> mComments;

	private Auction mAuction;

	public GetArtHttp() {
		m_ColumnList = new ArrayList<Column>();
		m_CommendList = new ArrayList<Commend>();
		m_InformationList = new ArrayList<Information>();
		m_RegisterList = new ArrayList<Register>();

		m_GetArtJson = new GetArtJson();
		m_Details = new Details();
		m_PictureSetDetail = m_Details.getPictureSetDetails();
		m_ShowDetail = m_Details.getShowDetails();
		m_AuctionDetail = m_Details.getShowDetails();
		m_Members = new ArrayList<Member>();

		m_PersonalPage = new PersonalPage();
		m_Cmtypes = new ArrayList<PersonalPage.Cmtype>();
		m_Abouts = m_PersonalPage.getAbout();
		mComments = new ArrayList<Comment>();
		mAuction = new Auction();
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		} else {
			Log.e("ERROR", "û���ڴ濨");
		}
		return sdDir.toString();
	}

	public List<Member> getCareHttp() {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/follow.php";
		HttpGet httpGet = new HttpGet(uriAPI);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(ArtApplication.mArtCookieStore);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_Members = m_GetArtJson.getMembersJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m_Members;
	}

	public Auction getAuction(String aid, String wid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/auction.php?wid="
				+ wid + "&aid=" + aid;
		// String uriAPI =
		// "http://m.yi71.com/plus/mobile/apps/auction.php?wid=7&aid=35820";
		HttpGet httpGet = new HttpGet(uriAPI);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				mAuction = m_GetArtJson.getAuction(json);
				return mAuction;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mAuction;
	}

	public String getAdHttp(String aid) {
		String uriAPI = "http://m.vrtart.com/plus/ad_js.php?aid=" + aid
				+ "&format=json";
		HttpGet httpGet = new HttpGet(uriAPI);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				String imgString = json.getString("img");
				return imgString;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// ��ù�ע�б�
	public void getCollection() {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/stow.php";
		HttpGet httpGet = new HttpGet(uriAPI);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setCookieStore(ArtApplication.mArtCookieStore);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_InformationList = m_GetArtJson.getCollectionJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getNewsHttp(String channelid, String keyword) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/search.php?channelid="
				+ channelid + "&keyword=" + keyword;
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				if (channelid == "-2" || channelid.equals("-2")) {
					m_Members = m_GetArtJson.getMembersJson(json);
				} else {
					m_InformationList = m_GetArtJson.getInformationJson(json);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getCommentsHttp(String aid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/feedback.php?aid="
				+ aid;
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject jsonObject = new JSONObject(m_ResultString);
				mComments = m_GetArtJson.getCommentsJson(jsonObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Information> getShowItemHttp(String mod, String typeid) {
		String uriAPI = "http://m.vrtart.com//plus/mobile/apps/view.php?mod="
				+ mod + "&typeid=" + typeid + "&page=1&row=10";
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_InformationList = m_GetArtJson.getShowItemJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m_InformationList;
	}

	// ����Get����Ļ�ȡ�ֻ���֤�뷽��
	public void sendAuthCodeRequest(final String phoneNum) {
		Thread sendAuthCodeThread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String uriAPI = "http://m.vrtart.com/plus/mobile/apps/signup.php?act=identify&phone="
						+ phoneNum;
				HttpGet httpGet = new HttpGet(uriAPI);
				HttpResponse httpResponse = null;
				try {
					httpResponse = new DefaultHttpClient().execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						m_ResultString = EntityUtils.toString(
								httpResponse.getEntity(), HTTP.UTF_8);
					}
					try {
						JSONObject json = new JSONObject(m_ResultString);
						m_IsAuth = m_GetArtJson.getAuthCodeRequest(json);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		sendAuthCodeThread.start();
	}

	// ��ȡ��Ŀ��Get����
	public void getColumnHttp(String imei) {
		// TODO Auto-generated method stub
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/navs.php?imei="
				+ imei;

		HttpGet httpGet = new HttpGet(uriAPI);

		HttpResponse httpResponse;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			System.out.println("��Ŀ"+m_ResultString);
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_ColumnList = m_GetArtJson.getColumnJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// �����Ŀ��Ϣ�ķ���
	public void getInformationHttp(int typeId, int page) {
		String uriAPI = "";
		if (typeId == 0) {
			uriAPI = "http://m.vrtart.com/plus/mobile/apps/view.php?page="
					+ page + "&row=20";
		} else {
			uriAPI = "http://m.vrtart.com/plus/mobile/apps/view.php?typeid="
					+ typeId + "&page=" + page + "&row=20";
		}
		HttpGet httpGet = new HttpGet(uriAPI);

		HttpResponse httpResponse;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_CommendList = m_GetArtJson.getCommendJson(json);
				m_InformationList = m_GetArtJson.getInformationJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ��û�Ա��Ŀ��Ϣ�ķ���
	public void getMemberInformationHttp(String mid, String mtypeid) {
		String uriApi = "http://m.vrtart.com/plus/mobile/apps/member.php?act=archieve&mid="
				+ mid + "&mtypeid=" + mtypeid + "&row=20";
		HttpGet httpGet = new HttpGet(uriApi);

		HttpResponse httpResponse;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_InformationList = m_GetArtJson.getMenberInformationJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getEssayDetails(String aid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/view.php?act=detail&aid="
				+ aid;
		System.out.println(aid);
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_EssayDetail = m_GetArtJson.getEssayDetails(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getPictureSetDetails(String aid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/view.php?act=detail&aid="
				+ aid;
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_PictureSetDetail = m_GetArtJson.getPictureSetDetails(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getShowDetails(String aid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/view.php?act=detail&aid="
				+ aid;
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_ShowDetail = m_GetArtJson.getShowDetails(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAuctionDetails(String aid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/view.php?act=detail&aid="
				+ aid;
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_AuctionDetail = m_GetArtJson.getAuctionDetails(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getCmtype(String mid) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/member.php?act=space&mid="
				+ mid;
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
			System.out.println("��Ա��Ŀ"+m_ResultString);
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_CommendList = m_GetArtJson.getCommendJson(json);
				m_Cmtypes = m_GetArtJson.getCmtype(json);
				m_Abouts = m_GetArtJson.getAboutJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getMember() {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/member.php";
		HttpGet httpGet = new HttpGet(uriAPI);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(httpEntity, HTTP.UTF_8);
			}
			System.out.println("��Ա"+m_ResultString);
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_Members = m_GetArtJson.getMembersJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getSearchResult(String channelid, String keyword) {
		String uriAPI = "http://m.vrtart.com/plus/mobile/apps/search.php?channelid="
				+ channelid + "&keyword=" + keyword;
		HttpGet httpGet = new HttpGet(uriAPI);

		HttpResponse httpResponse;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				m_ResultString = EntityUtils.toString(entity, HTTP.UTF_8);
			}
			try {
				JSONObject json = new JSONObject(m_ResultString);
				m_InformationList = m_GetArtJson.getInformationJson(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Comment> getmComments() {
		return mComments;
	}

	public void setmComments(List<Comment> mComments) {
		this.mComments = mComments;
	}

	public Login getM_Login() {
		return m_Login;
	}

	public void setM_Login(Login m_Login) {
		this.m_Login = m_Login;
	}

	public GetArtJson getM_GetArtJson() {
		return m_GetArtJson;
	}

	public void setM_GetArtJson(GetArtJson m_GetArtJson) {
		this.m_GetArtJson = m_GetArtJson;
	}

	public Details getM_Details() {
		return m_Details;
	}

	public void setM_Details(Details m_Details) {
		this.m_Details = m_Details;
	}

	public PersonalPage getM_PersonalPage() {
		return m_PersonalPage;
	}

	public void setM_PersonalPage(PersonalPage m_PersonalPage) {
		this.m_PersonalPage = m_PersonalPage;
	}

	public List<Cmtype> getM_Cmtypes() {
		return m_Cmtypes;
	}

	public void setM_Cmtypes(List<Cmtype> m_Cmtypes) {
		this.m_Cmtypes = m_Cmtypes;
	}

	public About getM_Abouts() {
		return m_Abouts;
	}

	public void setM_Abouts(About m_Abouts) {
		this.m_Abouts = m_Abouts;
	}

	public List<Member> getM_Members() {
		return m_Members;
	}

	public void setM_Members(List<Member> m_Members) {
		this.m_Members = m_Members;
	}

	public PictureSetDetails getM_PictureSetDetail() {
		return m_PictureSetDetail;
	}

	public void setM_PictureSetDetail(PictureSetDetails m_PictureSetDetail) {
		this.m_PictureSetDetail = m_PictureSetDetail;
	}

	public ShowAndAuctionDetails getM_ShowDetail() {
		return m_ShowDetail;
	}

	public void setM_ShowDetail(ShowAndAuctionDetails m_ShowDetail) {
		this.m_ShowDetail = m_ShowDetail;
	}

	public ShowAndAuctionDetails getM_AuctionDetail() {
		return m_AuctionDetail;
	}

	public void setM_AuctionDetail(ShowAndAuctionDetails m_AuctionDetail) {
		this.m_AuctionDetail = m_AuctionDetail;
	}

	public List<Column> getM_ColumnList() {
		return m_ColumnList;
	}

	public void setM_ColumnList(List<Column> m_ColumnList) {
		this.m_ColumnList = m_ColumnList;
	}

	public List<Commend> getM_CommendList() {
		return m_CommendList;
	}

	public void setM_CommendList(List<Commend> m_CommendList) {
		this.m_CommendList = m_CommendList;
	}

	public List<Information> getM_InformationList() {
		return m_InformationList;
	}

	public void setM_InformationList(List<Information> m_InformationList) {
		this.m_InformationList = m_InformationList;
	}

	public List<Register> getM_RegisterList() {
		return m_RegisterList;
	}

	public void setM_RegisterList(List<Register> m_RegisterList) {
		this.m_RegisterList = m_RegisterList;
	}

	public String getM_ResultString() {
		return m_ResultString;
	}

	public void setM_ResultString(String m_ResultString) {
		this.m_ResultString = m_ResultString;
	}

	public boolean isM_IsAuth() {
		return m_IsAuth;
	}

	public void setM_IsAuth(boolean m_IsAuth) {
		this.m_IsAuth = m_IsAuth;
	}

	public OrdinaryEssayDetails getM_EssayDetail() {
		return m_EssayDetail;
	}

	public void setM_EssayDetail(OrdinaryEssayDetails m_EssayDetail) {
		this.m_EssayDetail = m_EssayDetail;
	}

}
