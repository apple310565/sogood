package flag.com.gooood1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileInputStream;
import android.content.Context;

public class StdDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Class";
    private static final int DATABASE_VERSION = 1;
    public StdDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE Data(_date Text primary key, "+
                "flow int , color int , quality int , isStart int , isEnd int)");

        db.execSQL("CREATE TABLE EAT(_name Text primary key, "+
                "subset Text ,effect Text , method Text , who Text , main Text , main2 Text ,favor int,P int,D int,Source Text)");

        db.execSQL("CREATE TABLE ACUP(_name Text primary key, "+
                "subset Text ,effect Text , who Text , main Text , main2 Text, method Text ,theory Text,Source Text, favor int,P int,D int,note Text)");

        db.execSQL("CREATE TABLE SPORT(_name Text primary key, "+
                "subset1 Text , subset2 Text, effect Text , who Text , main Text , main2 Text, method Text ,theory Text,Source Text, favor int,P int,D int)");

        db.execSQL("CREATE TABLE HABIT(_name Text primary key, "+
                "subset Text , priority int ,P int,D int)");

        db.execSQL("CREATE TABLE Day(_ID Text primary key,name Text, article Text ,P int,D int,priority int,max int, selected int)");

        db.execSQL("CREATE TABLE DayIn(_ID Text , _date Text, progress int, complete real, d1 Text, d2 Text, d3 Text, d4 Text, d5 Text, d6 Text, d7 Text"+
                ",primary key(_ID,_date))");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int Version){
        db.execSQL("DROP TABLE IF EXISTS Data");
        onCreate(db);
    }
}

