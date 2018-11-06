package company.lizhao.com.main;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=596c5e55");
    }
    public static Context getContext(){
        return context;
    }
}
