package com.vrtart.adapter;

import java.util.List;

import com.vrtart.R;
import com.vrtart.application.ArtApplication;
import com.vrtart.models.Column;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DragAdapter extends BaseAdapter {
	/** TAG */
	private final static String TAG = "DragAdapter";
	/** �Ƿ���ʾ�ײ���ITEM */
	private boolean isItemShow = false;
	private Context context;
	/** ���Ƶ�postion */
	private int holdPosition;
	/** �Ƿ�ı� */
	private boolean isChanged = false;
	/** �б������Ƿ�ı� */
	private boolean isListChanged = false;
	/** �Ƿ�ɼ� */
	boolean isVisible = true;
	/** �����϶����б����û�ѡ���Ƶ���б� */
	public List<Column> channelList;
	/** TextView Ƶ������ */
	private TextView item_text;
	/** Ҫɾ����position */
	public int remove_position = -1;

	public DragAdapter(Context context, List<Column> channelList) {
		this.context = context;
		this.channelList = channelList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList == null ? 0 : channelList.size();
	}

	@Override
	public Column getItem(int position) {
		// TODO Auto-generated method stub
		if (channelList != null && channelList.size() != 0) {
			return channelList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.channel_item,
				null);
		item_text = (TextView) view.findViewById(R.id.text_item);
		Column channel = getItem(position);
		if (ArtApplication.mLogin != null && position == 1) {
			item_text.setText(ArtApplication.mLogin.getUname());
		} else {
			item_text.setText(channel.getTypeName());
		}
		if ((position == 0) || (position == 1)) {
			// item_text.setTextColor(context.getResources().getColor(R.color.black));
			item_text.setEnabled(false);
		}
		if (isChanged && (position == holdPosition) && !isItemShow) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
			isChanged = false;
		}
		if (!isVisible && (position == -1 + channelList.size())) {
			item_text.setText("");
			item_text.setSelected(true);
			item_text.setEnabled(true);
		}
		if (remove_position == position) {
			item_text.setText("");
		}
		return view;
	}

	/** ���Ƶ���б� */
	public void addItem(Column channel) {
		channelList.add(channel);
		isListChanged = true;
		notifyDataSetChanged();
	}

	/** �϶����Ƶ������ */
	public void exchange(int dragPostion, int dropPostion) {
		holdPosition = dropPostion;
		Column dragItem = getItem(dragPostion);
		if (dragPostion < dropPostion) {
			channelList.add(dropPostion + 1, dragItem);
			channelList.remove(dragPostion);
		} else {
			channelList.add(dropPostion, dragItem);
			channelList.remove(dragPostion + 1);
		}
		isChanged = true;
		isListChanged = true;
		notifyDataSetChanged();
	}

	/** ��ȡƵ���б� */
	public List<Column> getChannnelLst() {
		return channelList;
	}

	/** ����ɾ����position */
	public void setRemove(int position) {
		remove_position = position;
		notifyDataSetChanged();
	}

	/** ɾ��Ƶ���б� */
	public void remove() {
		channelList.remove(remove_position);
		remove_position = -1;
		isListChanged = true;
		notifyDataSetChanged();
	}

	/** ����Ƶ���б� */
	public void setListDate(List<Column> list) {
		channelList = list;
	}

	/** ��ȡ�Ƿ�ɼ� */
	public boolean isVisible() {
		return isVisible;
	}

	/** �����Ƿ����ı� */
	public boolean isListChanged() {
		return isListChanged;
	}

	/** �����Ƿ�ɼ� */
	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	/** ��ʾ���µ�ITEM */
	public void setShowDropItem(boolean show) {
		isItemShow = show;
	}
}