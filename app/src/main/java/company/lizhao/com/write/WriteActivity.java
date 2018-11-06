package company.lizhao.com.write;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.unity3d.player.UnityPlayer;

import company.lizhao.com.util.HanZiToPinYinUtil;
import company.lizhao.com.main.MainActivity;
import company.lizhao.com.main.R;
import company.lizhao.com.util.DBUtil;


public class WriteActivity extends AppCompatActivity {
    private EditText editText;
    private TextView tv_num;
    private ImageView imgtv;
    private String strContent;
    private Toolbar tl_head;
    private int num = 63;//限制的最大字数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainb);
        init();
        imgtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取内容
                strContent = editText.getText().toString();
                //存入数据库
                addSQL(strContent);
                //播放动画
                play(strContent);
                //结束Activity
                Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                intent.putExtra("data", HanZiToPinYinUtil.getPingYin(strContent));
                intent.putExtra("dataContent", HanZiToPinYinUtil.getPingYin(strContent));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //字数跟踪
        editText.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
                System.out.println("s=" + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                tv_num.setText((63 - number) + "");
                selectionStart = editText.getSelectionStart();
                selectionEnd = editText.getSelectionEnd();
                //System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    editText.setText(s);
                    editText.setSelection(tempSelection);//设置光标在最后
                }
            }
        });


    }
    private void init() {
        tl_head = (Toolbar) findViewById(R.id.tl_head);
        tl_head.setTitle("请输入..");
        editText = (EditText) findViewById(R.id.etContent);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_num.setText("0");
        imgtv = (ImageView) findViewById(R.id.img);
    }



    //把strContent存入数据库
    public void addSQL(String str) {
        if (!str.isEmpty()) {

            DBUtil dbUtil = new DBUtil();
            dbUtil.insertContent(str);
        }

    }

    //播放动画
    public void play(String str) {
        str = HanZiToPinYinUtil.getPingYin(str);

        UnityPlayer.UnitySendMessage("Shou", "changeChar", str);

    }
}



