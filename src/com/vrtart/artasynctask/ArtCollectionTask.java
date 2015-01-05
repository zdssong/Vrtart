package com.vrtart.artasynctask;

import com.vrtart.application.ArtApplication;
import com.vrtart.webServe.PostArtHttp;

import android.os.AsyncTask;
import android.widget.Toast;

public class ArtCollectionTask extends AsyncTask<Void, Void, Void> {

	private String aid;
	private int code;

	public ArtCollectionTask(String aid) {
		// TODO Auto-generated constructor stub
		this.aid = aid;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		PostArtHttp postArtHttp = new PostArtHttp();
		code = postArtHttp.postCollectionHttp(aid);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		if (code == 0) {
			Toast toast = Toast.makeText(ArtApplication.getArtApplication(),
					"�ղسɹ�", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast.makeText(ArtApplication.getArtApplication(),
					"�ղ�ʧ��", Toast.LENGTH_SHORT);
			toast.show();
		}

		super.onPostExecute(result);
	}
}
