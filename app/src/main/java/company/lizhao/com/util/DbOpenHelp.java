package company.lizhao.com.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by Administrator on 2017/7/15.
 */

public class DbOpenHelp extends SQLiteOpenHelper {
    //数据库名字
    public static final String DB_NAME="contact.db";
    //数据库版本
    public static final int DB_VERSION=1;
    //表名
    public static final String TABLE_NAME="info";
    //表内容字段
    public static final String _ID="_id";
    public static final String _CON="con";
    //创建数据库表
    public static final String DB_TANLE="create table "+TABLE_NAME+"("+_ID+" integer primary key autoincrement,"+_CON+" varchar(200) not null);";
    public DbOpenHelp(Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_TANLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
