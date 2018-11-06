package company.lizhao.com.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import company.lizhao.com.history.HistoryActivity;
import company.lizhao.com.util.DBUtil;
import company.lizhao.com.util.HanZiToPinYinUtil;
import company.lizhao.com.write.WriteActivity;

import static android.content.ContentValues.TAG;





public class MainActivity extends UnityPlayerActivity implements View.OnClickListener {
    private TextView textView;

    private LinearLayout scan;
    private ImageView img1, img2, img3, img4;
    private ImageView xunhuan;

    @Override
    // TODO: 2017/7/23  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maina);
        init();
    }

    private void init() {
        scan = findViewById(R.id.lyw);
        View view = mUnityPlayer.getView();
        scan.addView(view);
        textView = findViewById(R.id.tvContent);
        xunhuan = findViewById(R.id.xunhuan);
        img1 = findViewById(R.id.history);
        img2 = findViewById(R.id.write);
        img3 = findViewById(R.id.recode);
        img4 = findViewById(R.id.photo);
        xunhuan.setOnClickListener(this);
        xunhuan.setVisibility(View.GONE);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //主界面循环
            case R.id.xunhuan:
                loop();
                break;
            //历史记录
            case R.id.history:
                UnityPlayer.UnitySendMessage("ARCamera", "ChangeSC1", "");
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivityForResult(intent, 1);
                break;
            //拼音或汉字输入
            case R.id.write:
                UnityPlayer.UnitySendMessage("ARCamera", "ChangeSC1", "");
                Intent intent1 = new Intent(MainActivity.this, WriteActivity.class);
                startActivityForResult(intent1, 2);
                break;
            //录音
            case R.id.recode:
                UnityPlayer.UnitySendMessage("ARCamera", "ChangeSC1", "");
                btnVoice();
                break;
            //开启AR识别
            case R.id.photo:
                Toast.makeText(this, "正在打开AR相机请稍等...", Toast.LENGTH_LONG).show();
                UnityPlayer.UnitySendMessage("Main Camera", "ChangeS", "");
                xunhuan.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
        }

    }

    public String loop() {
        CharSequence text = textView.getText();
        UnityPlayer.UnitySendMessage("Shou", "changeChar", text + "");
        return (String) text;
    }

    //返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        xunhuan.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        switch (requestCode) {
            //历史记录返回
            case 1:
                if (resultCode == RESULT_OK) {
                    UnityPlayer.UnitySendMessage("AR Camera", "ChangeSC1", "");
                    String returnedData = data.getStringExtra("data");
                    char[] chars = returnedData.toCharArray();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < chars.length; i++) {
                        sb.append(chars[i] + "-");
                    }
                    textView.setText(sb.deleteCharAt(sb.length() - 1));
                }
                break;
            case 2:
                //输入文本返回
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data");
                    Log.i(TAG, "onActivityResult:" + returnedData);
                    if (!returnedData.isEmpty()) {
                        char[] chars = returnedData.toCharArray();
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < chars.length; i++) {
                            sb.append(chars[i] + "-");
                        }
                        textView.setText(sb.deleteCharAt(sb.length() - 1));
                    }

                }
        }
    }
    //语音听写
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(this, null);
        dialog.setParameter(SpeechConstant.ASR_PTT, "0");

        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }

            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
    }

    private void printResult(RecognizerResult results) {
        String text = HanZiToPinYinUtil.getPingYin(parseIatResult(results.getResultString()));

        UnityPlayer.UnitySendMessage("Shou", "changeChar", text);
        if (!text.isEmpty()) {
            DBUtil dbUtil = new DBUtil();
            dbUtil.insertContent(text);
            char[] chars = text.toCharArray();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < chars.length; i++) {
                sb.append(chars[i] + "-");

            }

            textView.setText(sb.deleteCharAt(sb.length() - 1));
        }

    }


    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);
            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }


}
