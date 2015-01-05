package com.vrtart.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.vrtart.models.Login;
import com.vrtart.webServe.ArtCookieStore;

import android.app.Activity;
import android.util.DisplayMetrics;

public class BaseTools {

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public final static void saveLoginCookie(ArtCookieStore cookieStore) {
		try {
			FileOutputStream foStream = new FileOutputStream("LoginCookie.out");
			ObjectOutputStream ooStream = new ObjectOutputStream(foStream);
			ooStream.writeObject(cookieStore);
			ooStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public final static ArtCookieStore getLoginCookie() {
		ArtCookieStore cookieStore = new ArtCookieStore();
		try {
			FileInputStream fis = new FileInputStream("LoginCookie.out");
			ObjectInputStream ois = new ObjectInputStream(fis);
			cookieStore = (ArtCookieStore) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookieStore;
	}

	public final static void saveLoginInf(Login login) {
		try {
			FileOutputStream foStream = new FileOutputStream("LoginInf.out");
			ObjectOutputStream ooStream = new ObjectOutputStream(foStream);
			ooStream.writeObject(login);
			ooStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public final static Login getLoginInf() {
		Login login = new Login();
		try {
			FileInputStream fis = new FileInputStream("LoginInf.out");
			ObjectInputStream ois = new ObjectInputStream(fis);
			login = (Login) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return login;
	}
}
