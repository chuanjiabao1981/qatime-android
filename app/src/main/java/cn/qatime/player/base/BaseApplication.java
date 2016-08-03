package cn.qatime.player.base;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.qatime.player.bean.Profile;

public class BaseApplication extends Application {
    //    public static RequestQueue Queue= Volley.newRequestQueue(this);;
    private static Profile profile;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Profile getProfile() {
        return profile==null?new Profile():profile;
    }

    public static void setProfile(Profile profile) {
        BaseApplication.profile = profile;
    }
}