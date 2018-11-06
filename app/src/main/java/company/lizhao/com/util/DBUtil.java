package company.lizhao.com.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import company.lizhao.com.main.MyApplication;

/**
 * Created by Administrator on 2017/7/15.
 */

public class DBUtil {

    private DbOpenHelp help;

    public DBUtil() {
        help =new DbOpenHelp(MyApplication.getContext());
    }


    //插入
    public void insertContent(String content){
        SQLiteDatabase sql=help.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DbOpenHelp._CON,content);
        sql.insert(DbOpenHelp.TABLE_NAME,null,values);
    }
    //删除
    public void deleteContent(){
        SQLiteDatabase sql=help.getWritableDatabase();
        sql.delete(DbOpenHelp.TABLE_NAME,null,null);

    }
    //查找
    public List queryContent(){
        List<String> list=new ArrayList<>();
        SQLiteDatabase sql=help.getWritableDatabase();
        Cursor cursor=sql.query(DbOpenHelp.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String content=cursor.getString(cursor.getColumnIndex(DbOpenHelp._CON));
            list.add(content);
        }
        return list;
    }
}
