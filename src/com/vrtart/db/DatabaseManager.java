package com.vrtart.db;

import java.util.ArrayList;
import java.util.List;

import com.vrtart.models.Column;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseManager {

	private DatabaseHelper dataBaseHelper;
	private SQLiteDatabase db;

	public DatabaseManager(Context context) {
		dataBaseHelper = new DatabaseHelper(context);
		db = dataBaseHelper.getWritableDatabase();
	}

	// ���ӳɼ���¼
	public void addColumn(List<Column> columns) {
		db.beginTransaction();
		try {
			for (Column column : columns) {
				db.execSQL("INSERT INTO " + DatabaseHelper.COLUMN_TABLE
						+ " VALUES(null, ?, ?, ?, ?, ?, ?)",
						new Object[] { column.getTypeId(),
								column.getTypeName(), column.getChannel(),
								column.getChannelName(), column.getOrderId(),
								column.getSelected() });
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void deleteColumn() {
		db.execSQL("delete from " + DatabaseHelper.COLUMN_TABLE);
	}

	// ���¼�¼
	public void update() {

	}

	public List<Column> queryColumnBySelected(int selected) {
		ArrayList<Column> columns = new ArrayList<Column>();
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.COLUMN_TABLE
				+ " WHERE selected = ? ", new String[] { selected + "" });
		while (c.moveToNext()) {
			Column column = new Column();
			column.setTypeId(c.getInt(c.getColumnIndex("typeId")));
			column.setTypeName(c.getString(c.getColumnIndex("typeName")));
			column.setChannel(c.getInt(c.getColumnIndex("channel")));
			column.setChannelName(c.getString(c.getColumnIndex("channelName")));
			column.setOrderId(c.getInt(c.getColumnIndex("orderId")));
			column.setSelected(c.getInt(c.getColumnIndex("selected")));
			columns.add(column);
		}
		Log.e("size", columns.size() + "3");
		return columns;
	}

	// ��ѯ��¼
	public List<Column> queryAllColumns() {
		ArrayList<Column> columns = new ArrayList<Column>();
		Cursor c = queryCourseCursor();
		while (c.moveToNext()) {
			Column column = new Column();
			column.setTypeId(c.getInt(c.getColumnIndex("typeId")));
			column.setTypeName(c.getString(c.getColumnIndex("typeName")));
			column.setChannel(c.getInt(c.getColumnIndex("channel")));
			column.setChannelName(c.getString(c.getColumnIndex("channelName")));
			column.setOrderId(c.getInt(c.getColumnIndex("orderId")));
			column.setSelected(c.getInt(c.getColumnIndex("selected")));
			columns.add(column);
		}
		return columns;
	}

	// ����α�
	public Cursor queryCourseCursor() {
		Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.COLUMN_TABLE,
				null);
		return c;
	}

	public boolean isNull() {
		Cursor cursor = queryCourseCursor();
		if (cursor.getCount() == 0 && !cursor.moveToNext()) {
			return true;
		} else {
			return false;
		}
	}
}
