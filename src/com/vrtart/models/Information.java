package com.vrtart.models;

import java.io.Serializable;

/**
 * ��Ѷ��Ϣ���ղ��б��û����º��������ص�ʵ����
 * */

public class Information implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String typeId; // ������ĿID
	private String sendDate;
	private String flag; // ���Զ��壬���Կ��Զ���������Ӣ�Ķ���","�ָ�.�� flag=h,j ��ʾ ͷ������ת
	private String isMake;
	private String channel; // ����ģ�Ͷ���
	private String arcRank; // ���״̬
	private String click; // .�����
	private String title; // ����
	private String color; // ��ɫ
	private String litpic; // �б�����ͼ��ַ
	private String pubdate; // ����ʱ���
	private String mid; // �����û�id
	private String typeName; // ������Ŀ����
	private String channelName; // ����ģ������
	private String redirecturl; // ��ת��ַ
	private String url;
	private String[] imgurls;
	private String fc;
	
	//չ�����е��ֶ�
	private String address;
	private String leftTime;
	private String endTime;
	private String startTime;

	public Information() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Information(String id, String typeId, String sendDate, String flag,
			String isMake, String channel, String arcRank, String click,
			String title, String color, String litpic, String pubdate,
			String mid, String typeName, String channelName,
			String redirecturl, String url, String[] imgurls, String fc,
			String address, String leftTime, String endTime, String startTime) {
		super();
		this.id = id;
		this.typeId = typeId;
		this.sendDate = sendDate;
		this.flag = flag;
		this.isMake = isMake;
		this.channel = channel;
		this.arcRank = arcRank;
		this.click = click;
		this.title = title;
		this.color = color;
		this.litpic = litpic;
		this.pubdate = pubdate;
		this.mid = mid;
		this.typeName = typeName;
		this.channelName = channelName;
		this.redirecturl = redirecturl;
		this.url = url;
		this.imgurls = imgurls;
		this.fc = fc;
		this.address = address;
		this.leftTime = leftTime;
		this.endTime = endTime;
		this.startTime = startTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLeftTime() {
		return leftTime;
	}

	public void setLeftTime(String leftTime) {
		this.leftTime = leftTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIsMake() {
		return isMake;
	}

	public void setIsMake(String isMake) {
		this.isMake = isMake;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getArcRank() {
		return arcRank;
	}

	public void setArcRank(String arcRank) {
		this.arcRank = arcRank;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}

	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getRedirecturl() {
		return redirecturl;
	}

	public void setRedirecturl(String redirecturl) {
		this.redirecturl = redirecturl;
	}

	public String[] getImgurls() {
		return imgurls;
	}

	public void setImgurls(String[] imgurls) {
		this.imgurls = imgurls;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFc() {
		return fc;
	}

	public void setFc(String fc) {
		this.fc = fc;
	}

}
