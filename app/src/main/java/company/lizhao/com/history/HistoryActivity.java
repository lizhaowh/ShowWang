package company.lizhao.com.history;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.unity3d.player.UnityPlayer;

import company.lizhao.com.util.HanZiToPinYinUtil;
import company.lizhao.com.main.MainActivity;
import company.lizhao.com.main.R;
import company.lizhao.com.util.DBUtil;

/**
 * 历史记录界面
 */
public class HistoryActivity extends AppCompatActivity {
    private ListView listView;
    private Toolbar tl_head;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainc);
        init();
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,
                new DBUtil().queryContent()));
        //删除记录
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBUtil dbUtil=new DBUtil();
                dbUtil.deleteContent();
                listView.setAdapter(new ArrayAdapter<String>(HistoryActivity.this,
                        android.R.layout.simple_expandable_list_item_1, new DBUtil().queryContent()));
            }
        });
        //点击列表
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DBUtil dbUtil=new DBUtil();
                String o = (String)dbUtil.queryContent().get(i);
                UnityPlayer.UnitySendMessage("Shou","changeChar", HanZiToPinYinUtil.getPingYin(o));
                Intent intent=new Intent(HistoryActivity.this,MainActivity.class);
                intent.putExtra("data",HanZiToPinYinUtil.getPingYin(o));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void init() {
        listView= (ListView) findViewById(R.id.lv);
        tl_head= (Toolbar) findViewById(R.id.tl_head);
        imageView= (ImageView) findViewById(R.id.imgde);
        tl_head.setTitle("历史记录");
    }
}
