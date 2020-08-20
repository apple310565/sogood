package flag.com.gooood1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView m1DisplayDate;
    private static String DATABASE_TABLE="Data";
    private SQLiteDatabase db;
    private StdDBHelper dbHelper;
    private DatePickerDialog.OnDateSetListener m1DataSetListerner;
    int [] Mon = {0,31,59,90,120,151,181,212,243,273,304,334};
    int [] Mon1={0,31,59,90,120,151,181,212,243,273,304,334};
    int [] Mon2={0,31,60,91,121,152,182,213,244,274,305,335};
    int Y,M,D,peroid=29,flag=0,a,flag2=0,day1=0,day2=0,i_M;
    int w=90;
    String account,passWD,email;
    LinearLayout Ch2=null,Ch3=null,Ch4=null,Ch5=null,Ch6=null;
    CheckBox [] ch2_v =new CheckBox[7];
    CheckBox [] ch3_v =new CheckBox[3];
    CheckBox [] ch4_v =new CheckBox[3];
    CheckBox [] ch5_v =new CheckBox[4];
    CheckBox [] ch6_v =new CheckBox[3];
    LinearLayout LL;
    String [] str2={"腰臀部\n脹痛","乳房\n脹痛","面目\n浮腫","肢軟\n無力","肢體腫\n脹不適","肢體麻\n木疼痛","關節\n疼痛"};
    String []str3={"皮膚\n起疹","膚色\n焮紅","身癢"};
    String [] str4={"口糜\n舌爛","口\n臭","口燥\n咽乾"};
    String [] str5={"吐\n血","衄\n血","齒\n衄","咯\n血"};
    String [] str6={"無故\n悲傷","煩躁\n易怒","神志\n不清"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String line="User not exist";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //建立SQLOHleper物件
        dbHelper = new StdDBHelper(this);
        db =dbHelper.getWritableDatabase();//開啟資料庫
        //若是第一次登入沒有帳密，則切換成註冊頁面
        try{
            FileInputStream in = openFileInput("passwd.txt");
            byte[] data = new byte[128];
            in.read(data);
            in.close();
            line = new String(data);

            String[] name = line.split(",");
            String [] Date = name[3].split("/");
            Y=Integer.parseInt(Date[0]);
            M=Integer.parseInt(Date[1]);
            D=Integer.parseInt(Date[2]);
            peroid = Integer.parseInt(name[2]);
            flag=1;
            account=name[0];
            passWD=name[1];
            i_M=Integer.parseInt(name[4]);
            email=name[5];
        }
        catch (IOException e){
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(R.drawable.ic_launcher_background)
                    .setTitle("Message")
                    .setMessage("第一次使用請先設置帳密喔")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, Main2Activity.class);
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                    })
                    .show();
        }
        //選擇日期
        /*參考網址: https://www.youtube.com/watch?v=hwe1abDO2Ag*/
        if(flag==1){
            LL=(LinearLayout)findViewById(R.id.Else_V);
            m1DisplayDate = (TextView)findViewById(R.id.tvDate);
            Calendar cal = Calendar.getInstance();
            int year =cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            IsLeapYear(year);

            String date =year + "/" ;
            if((month+1)<10)date+="0";
            date+=(month+1)+"/";
            if(day<10)date+="0";
            date+=day;
            m1DisplayDate.setText(date);
            isExist(date);

            day1 = Mon[month]+day;day2 = Mon[M-1]+D;
            int days = (year-Y)*365+(day1-day2);
            int mense =peroid - (days%peroid);
            if(mense>=peroid)mense=mense-peroid;
            TextView Days = (TextView)findViewById(R.id.tvDay);
            Days.setText(Integer.toString(mense));
            if(is_mense()==0)out_memse();
            else in_memse();


            m1DisplayDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    Calendar cal = Calendar.getInstance();
                    int year =cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            MainActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            m1DataSetListerner,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            m1DataSetListerner = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //New();
                    String date =year + "/" ;
                    if((month+1)<10)date+="0";
                    date+=(month+1)+"/";
                    if(dayOfMonth<10)date+="0";
                    date+=dayOfMonth;
                    m1DisplayDate.setText(date);
                    IsLeapYear(year);

                    day1 = Mon[month] + dayOfMonth;
                    day2 = Mon[M - 1] + D;
                    int days = (year - Y) * 365 + (day1 - day2);
                    int mense = peroid - (days % peroid);
                    if (mense >= peroid) mense = mense - peroid;
                    TextView Days = (TextView) findViewById(R.id.tvDay);
                    if(is_mense()==0)Days.setText(Integer.toString(mense));
                    else Days.setText(Integer.toString(days+1));

                    isExist(date);
                }
            };

            //監聽症狀是否被選取
            final CheckBox ch2 = (CheckBox)findViewById(R.id.checkBox2);
            ch2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ch2.isChecked()){
                            produce_Ch2();
                    }
                    else {
                        if(Ch2!=null){
                            LL.removeView(Ch2);
                            Ch2=null;
                        }
                    }
                }
            });
            final CheckBox ch3 = (CheckBox)findViewById(R.id.checkBox3);
            ch3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ch3.isChecked()){
                        produce_Ch3();
                    }
                    else {
                        if(Ch3!=null){
                            LL.removeView(Ch3);
                            Ch3=null;
                        }
                    }
                }
            });
            final CheckBox ch4 = (CheckBox)findViewById(R.id.checkBox4);
            ch4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ch4.isChecked()){
                        produce_Ch4();
                    }
                    else {
                        if(Ch4!=null){
                            LL.removeView(Ch4);
                            Ch4=null;
                        }
                    }
                }
            });
            final CheckBox ch5 = (CheckBox)findViewById(R.id.checkBox5);
            ch5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ch5.isChecked()){
                        produce_Ch5();
                    }
                    else {
                        if(Ch5!=null){
                            LL.removeView(Ch5);
                            Ch5=null;
                        }
                    }
                }
            });
            final CheckBox ch6 = (CheckBox)findViewById(R.id.checkBox6);
            ch6.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(ch6.isChecked()){
                        produce_Ch6();
                    }
                    else {
                        if(Ch6!=null){
                            LL.removeView(Ch6);
                            Ch6=null;
                        }
                    }
                }
            });

        }

    }
    @Override
    protected void onStop(){
        super.onStop();
        db.close();
    }
    //紀錄
    public void submit_click(View view){
        //基本選項(Data1)
        int f=-1,c=-1,q=-1,ca=-1,i_S=0,i_E=0;
        RadioGroup flow = (RadioGroup)findViewById(R.id.RGflow);
        if(flow.getCheckedRadioButtonId()==R.id.f1)f=1;
        else if(flow.getCheckedRadioButtonId()==R.id.f2)f=2;
        else if(flow.getCheckedRadioButtonId()==R.id.f3)f=3;
        else if(flow.getCheckedRadioButtonId()==R.id.f4)f=4;
        else if(flow.getCheckedRadioButtonId()==R.id.f5)f=5;
        SeekBar sb =(SeekBar)findViewById(R.id.seekBar);
        if(sb.getProgress()==0)c=1;
        else if(sb.getProgress()==1)c=2;
        else if(sb.getProgress()==2)c=3;
        else if(sb.getProgress()==3)c=4;
        else if(sb.getProgress()==4)c=5;

        RadioGroup quality = (RadioGroup)findViewById(R.id.RGqaulity);
        if(quality.getCheckedRadioButtonId()==R.id.q1)q=1;
        else if(quality.getCheckedRadioButtonId()==R.id.q2)q=2;
        else if(quality.getCheckedRadioButtonId()==R.id.q3)q=3;
        RadioGroup carry = (RadioGroup)findViewById(R.id.RGcarry);
        if(carry.getCheckedRadioButtonId()==R.id.ca1)ca=1;
        else if(carry.getCheckedRadioButtonId()==R.id.ca2)ca=2;
        else if(carry.getCheckedRadioButtonId()==R.id.ca3)ca=3;
        Switch isStart = (Switch)findViewById(R.id.switch3);
        Switch isEnd = (Switch)findViewById(R.id.end);
        if(isStart.isChecked())i_S=1;
        if(isEnd.isChecked())i_E=1;

        ContentValues cv = new ContentValues();
        if(flag2==0)cv.put("_date",m1DisplayDate.getText().toString());
        cv.put("flow",f);
        cv.put("color",c);
        cv.put("carry",ca);
        cv.put("quality",q);
        cv.put("isStart",i_S);
        cv.put("isEnd",i_E);
        String id =m1DisplayDate.getText().toString();
        if(flag2==0)db.insert(DATABASE_TABLE,null,cv);
        else {
            try {
                db.update(DATABASE_TABLE,cv, "_date='"+id+"'",null);
            } catch (Exception e) {
                Toast.makeText(this,"更新失敗，我也很絕望啊orz"+e.toString(),Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        //CheckBox(Data2)
        ContentValues cv2 = new ContentValues();
        cv2.put("_date",m1DisplayDate.getText().toString());
        CheckBox ch1=(CheckBox)findViewById(R.id.checkBox);
        if(ch1.isChecked())cv2.put("ch1",true);
        else cv2.put("ch1",false);
        CheckBox ch2=(CheckBox)findViewById(R.id.checkBox2);
        if(ch2.isChecked()){
            cv2.put("ch2",true);
            sumbit_Ch2();
        }
        else cv2.put("ch2",false);
        CheckBox ch3=(CheckBox)findViewById(R.id.checkBox3);
        if(ch3.isChecked()){
            cv2.put("ch3",true);
            sumbit_Ch3();
        }
        else cv2.put("ch3",false);
        CheckBox ch4=(CheckBox)findViewById(R.id.checkBox4);
        if(ch4.isChecked()){
            cv2.put("ch4",true);
            sumbit_Ch4();
        }
        else cv2.put("ch4",false);
        CheckBox ch5=(CheckBox)findViewById(R.id.checkBox5);
        if(ch5.isChecked()){
            cv2.put("ch5",true);
            sumbit_Ch5();
        }
        else cv2.put("ch5",false);
        CheckBox ch6=(CheckBox)findViewById(R.id.checkBox6);
        if(ch6.isChecked()){
            cv2.put("ch6",true);
            sumbit_Ch6();
        }
        else cv2.put("ch6",false);
        CheckBox ch7=(CheckBox)findViewById(R.id.checkBox7);
        if(ch7.isChecked())cv2.put("ch7",true);
        else cv2.put("ch7",false);
        CheckBox ch8=(CheckBox)findViewById(R.id.checkBox8);
        if(ch8.isChecked())cv2.put("ch8",true);
        else cv2.put("ch8",false);
        CheckBox ch9=(CheckBox)findViewById(R.id.checkBox9);
        if(ch9.isChecked())cv2.put("ch9",true);
        else cv2.put("ch9",false);
        CheckBox ch10=(CheckBox)findViewById(R.id.checkBox10);
        if(ch10.isChecked())cv2.put("ch10",true);
        else cv2.put("ch10",false);
        db.insert("Data2",null,cv2);
        db.update("Data2",cv2, "_date='"+id+"'",null);

        if(((day2+i_M)==day1)&&i_E==0)cancel_END();
        if(i_S==1&&i_M!=-1)firstDay();
        if(i_E==1)END();
        Toast.makeText(this,"感謝親的紀錄~ (,,・ω・,,)",Toast.LENGTH_SHORT).show();
        //SqlQuery("SELECT * FROM "+DATABASE_TABLE);
    }
    public int is_mense(){
        if(day1>=day2){
            if(i_M==-1)return 1;
            else if(i_M>=(day1-day2))return 1;
        }
        return 0;
    }
    public void in_memse(){
        TextView text1 = (TextView)findViewById(R.id.text1);
        text1.setText("經期已經開始");
        TextView tvDay = (TextView)findViewById(R.id.tvDay);
        int days=day1-day2;
        tvDay.setText(Integer.toString(days+1));
        Switch i_S = (Switch)findViewById(R.id.switch3);
        i_S.setChecked(false);
        i_S.setEnabled(false);
        Switch i_E = (Switch)findViewById(R.id.end);
        if(day1==day2||(i_M!=-1&&day2+i_M!=day1))i_E.setEnabled(false);
        else i_E.setEnabled(true);
    }
    public void out_memse(){
        TextView text1 = (TextView)findViewById(R.id.text1);
        text1.setText("經期倒數");;
        Switch i_S = (Switch)findViewById(R.id.switch3);
        if(day1>day2)i_S.setEnabled(true);
        else i_S.setEnabled(false);
        i_S.setChecked(false);
        Switch i_E = (Switch)findViewById(R.id.end);
        i_E.setChecked(false);
        i_E.setEnabled(false);

        if(day1-day2>peroid&&day1-day2<peroid+15){
            text1.setText("經期已經晚到");
            text1.setTextSize(18);
            TextView tv = (TextView)findViewById(R.id.tvDay);
            tv.setText(Integer.toString(day1-day2-peroid));
        }

    }
    public void firstDay(){
        peroid=day1-day2;
        String str=account+","+passWD+","+peroid+","+m1DisplayDate.getText().toString()+","+"-1,"+email+",";
        try {
            FileOutputStream out = openFileOutput("passwd.txt",MODE_PRIVATE);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(this,"In_memse",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"文件创建失败"+e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        String [] Date = m1DisplayDate.getText().toString().split("/");
        Y=Integer.parseInt(Date[0]);
        M=Integer.parseInt(Date[1]);
        D=Integer.parseInt(Date[2]);
        day2=Mon[M-1]+D;
        i_M=-1;

        ContentValues cv = new ContentValues();
        cv.put("_date",m1DisplayDate.getText().toString());
        cv.put("p",peroid);
        db.insert("Period",null,cv);

        in_memse();
    }
    public void END(){
        i_M=day1-day2;
        String str=account+","+passWD+","+peroid+","+Y+"/"+M+"/"+D+","+i_M+","+email+",";
        try {
            FileOutputStream out = openFileOutput("passwd.txt",MODE_PRIVATE);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(this,"成功寫入檔案",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"文件创建失败"+e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    public void New(){
        RadioGroup flow = (RadioGroup)findViewById(R.id.RGflow);
        flow.clearCheck();
        SeekBar sb=(SeekBar)findViewById(R.id.seekBar);
        sb.setProgress(1);
        RadioGroup quality = (RadioGroup)findViewById(R.id.RGqaulity);
        quality.clearCheck();
        RadioGroup carry = (RadioGroup)findViewById(R.id.RGcarry);
        carry.clearCheck();
        Check_clear();
    }
    public void Check_clear(){
        CheckBox ch1=(CheckBox)findViewById(R.id.checkBox);
        ch1.setChecked(false);
        CheckBox ch2=(CheckBox)findViewById(R.id.checkBox2);
        ch2.setChecked(false);
        CheckBox ch3=(CheckBox)findViewById(R.id.checkBox3);
        ch3.setChecked(false);
        CheckBox ch4=(CheckBox)findViewById(R.id.checkBox4);
        ch4.setChecked(false);
        CheckBox ch5=(CheckBox)findViewById(R.id.checkBox5);
        ch5.setChecked(false);
        CheckBox ch6=(CheckBox)findViewById(R.id.checkBox6);
        ch6.setChecked(false);
        CheckBox ch7=(CheckBox)findViewById(R.id.checkBox7);
        ch7.setChecked(false);
        CheckBox ch8=(CheckBox)findViewById(R.id.checkBox8);
        ch8.setChecked(false);
        CheckBox ch9=(CheckBox)findViewById(R.id.checkBox9);
        ch9.setChecked(false);
        CheckBox ch10=(CheckBox)findViewById(R.id.checkBox10);
        ch10.setChecked(false);
        if(Ch2!=null){
            LL.removeView(Ch2);
            Ch2=null;
        }
        if(Ch3!=null){
            LL.removeView(Ch3);
            Ch3=null;
        }
        if(Ch4!=null){
            LL.removeView(Ch4);
            Ch4=null;
        }
        if(Ch5!=null){
            LL.removeView(Ch5);
            Ch5=null;
        }
        if(Ch6!=null){
            LL.removeView(Ch6);
            Ch6=null;
        }
    }
    public void isExist(String date){
        if(is_mense()==1)in_memse();
        else out_memse();
        String queryStr = "select * FROM Data where _date = '%s'";
        queryStr = String.format(queryStr, date);
        Cursor c = db.rawQuery(queryStr, null);
        c.moveToFirst();
        if (c.getCount()>0) {
            flag2=1;

            Switch s = (Switch) findViewById(R.id.switch3);
            if(Integer.parseInt(c.getString(4))==1)s.setChecked(true);
            else s.setChecked(false);

            Switch e = (Switch) findViewById(R.id.end);
            if(Integer.parseInt(c.getString(5))==1)e.setChecked(true);
            else e.setChecked(false);

            int F =Integer.parseInt(c.getString(1));
            RadioButton f_b= (RadioButton)findViewById(R.id.f1);;
            if(F==1) f_b = (RadioButton)findViewById(R.id.f1);
            else if (F==2) f_b = (RadioButton)findViewById(R.id.f2);
            else if (F==3) f_b = (RadioButton)findViewById(R.id.f3);
            else if (F==4) f_b = (RadioButton)findViewById(R.id.f4);
            else if (F==5) f_b = (RadioButton)findViewById(R.id.f5);
            if(F!=-1)f_b.setChecked(true);
            else{
                RadioGroup flow = (RadioGroup)findViewById(R.id.RGflow);
                flow.clearCheck();
            }


            int C =Integer.parseInt(c.getString(2));
            SeekBar sb=(SeekBar)findViewById(R.id.seekBar);
            if(C!=0&&C!=-1)sb.setProgress(C-1);
            else sb.setProgress(1);

            int Q =Integer.parseInt(c.getString(3));
            RadioButton q_b= (RadioButton)findViewById(R.id.q1);;
            if(Q==1) q_b = (RadioButton)findViewById(R.id.q1);
            else  if(Q==2) q_b = (RadioButton)findViewById(R.id.q2);
            else  if(Q==3) q_b = (RadioButton)findViewById(R.id.q3);
            if(Q!=-1)q_b.setChecked(true);
            else {
                RadioGroup quality = (RadioGroup)findViewById(R.id.RGqaulity);
                quality.clearCheck();
            }

            int CA =Integer.parseInt(c.getString(6));
            RadioButton ca_b= (RadioButton)findViewById(R.id.ca1);;
            if(CA==1) ca_b = (RadioButton)findViewById(R.id.ca1);
            else  if(CA==2) ca_b = (RadioButton)findViewById(R.id.ca2);
            else  if(CA==3) ca_b = (RadioButton)findViewById(R.id.ca3);
            if(CA!=-1)ca_b.setChecked(true);
            else{
                RadioGroup carry = (RadioGroup)findViewById(R.id.RGcarry);
                carry.clearCheck();
            }

            Check_clear();
            Cursor c2=db.rawQuery("SELECT * FROM Data2 Where _date = '"+date+"'",null);
            c2.moveToFirst();
            if(c2.getString(1).equals("1")){
                CheckBox ch1=(CheckBox)findViewById(R.id.checkBox);
                ch1.setChecked(true);
            }
            if(c2.getString(2).equals("1")){
                CheckBox ch2=(CheckBox)findViewById(R.id.checkBox2);
                ch2.setChecked(true);
                produce_Ch2();
                Cursor c3=db.rawQuery("SELECT * FROM Ch2 Where _date = '"+date+"'",null);
                c3.moveToFirst();
                if(c3.getCount()!=0){
                    for(int i=0;i<7;i++){
                        if(c3.getString(i+1).equals("1"))ch2_v[i].setChecked(true);
                    }
                }
            }
            if(c2.getString(3).equals("1")){
                CheckBox ch3=(CheckBox)findViewById(R.id.checkBox3);
                ch3.setChecked(true);
                produce_Ch3();
                Cursor c3=db.rawQuery("SELECT * FROM Ch3 Where _date = '"+date+"'",null);
                c3.moveToFirst();
                if(c3.getCount()!=0){
                    for(int i=0;i<3;i++){
                        if(c3.getString(i+1).equals("1"))ch3_v[i].setChecked(true);
                    }
                }
            }
            if(c2.getString(4).equals("1")){
                CheckBox ch4=(CheckBox)findViewById(R.id.checkBox4);
                ch4.setChecked(true);
                produce_Ch4();
                Cursor c3=db.rawQuery("SELECT * FROM Ch4 Where _date = '"+date+"'",null);
                c3.moveToFirst();
                if(c3.getCount()!=0){
                    for(int i=0;i<3;i++){
                        if(c3.getString(i+1).equals("1"))ch4_v[i].setChecked(true);
                    }
                }
            }
            if(c2.getString(5).equals("1")){
                CheckBox ch5=(CheckBox)findViewById(R.id.checkBox5);
                ch5.setChecked(true);
                produce_Ch5();
                Cursor c3=db.rawQuery("SELECT * FROM Ch5 Where _date = '"+date+"'",null);
                c3.moveToFirst();
                if(c3.getCount()!=0){
                    for(int i=0;i<4;i++){
                        if(c3.getString(i+1).equals("1"))ch5_v[i].setChecked(true);
                    }
                }
            }
            if(c2.getString(6).equals("1")){
                CheckBox ch6=(CheckBox)findViewById(R.id.checkBox6);
                ch6.setChecked(true);
                produce_Ch6();
                Cursor c3=db.rawQuery("SELECT * FROM Ch6 Where _date = '"+date+"'",null);
                c3.moveToFirst();
                if(c3.getCount()!=0){
                    for(int i=0;i<3;i++){
                        if(c3.getString(i+1).equals("1"))ch6_v[i].setChecked(true);
                    }
                }
            }
            if(c2.getString(7).equals("1")){
                CheckBox ch7=(CheckBox)findViewById(R.id.checkBox7);
                ch7.setChecked(true);
            }
            if(c2.getString(8).equals("1")){
                CheckBox ch8=(CheckBox)findViewById(R.id.checkBox8);
                ch8.setChecked(true);
            }
            if(c2.getString(9).equals("1")){
                CheckBox ch9=(CheckBox)findViewById(R.id.checkBox9);
                ch9.setChecked(true);
            }
            if(c2.getString(10).equals("1")){
                CheckBox ch10=(CheckBox)findViewById(R.id.checkBox10);
                ch10.setChecked(true);
            }

        }
        else {
            flag2=0;
            New();
        }
    }
    public void cancel_END(){
        i_M=-1;
        String str=account+","+passWD+","+peroid+","+Y+"/"+M+"/"+D+","+i_M+","+email+",";
        try {
            FileOutputStream out = openFileOutput("passwd.txt",MODE_PRIVATE);
            out.write(str.getBytes());
            out.close();
            Toast.makeText(this,"成功寫入檔案",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"文件创建失败"+e.toString(),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        ContentValues cv = new ContentValues();
        cv.put("isEnd",0);
        String id =m1DisplayDate.getText().toString();
        db.update(DATABASE_TABLE,cv, "_date='"+id+"'",null);
    }
    public void change_firstday(View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month =  calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        String DateTime="???";
        new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String DateTime = String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(dayOfMonth);
                String [] Date = DateTime.split("/");
                int tY=Integer.parseInt(Date[0]);
                int tM=Integer.parseInt(Date[1]);
                int tD=Integer.parseInt(Date[2]);
                int tday= Mon[tM-1] + tD;
                if(i_M!=-1&&i_M+(day2-tday)<=0){
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.ic_launcher_background)
                            .setTitle("Message")
                            .setMessage("經期開始日期不可以在結束日期之後喔(*´▽`*)")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else if((day2-tday)>=peroid){
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.ic_launcher_background)
                            .setTitle("Message")
                            .setMessage("經期開始日期不可以在上一次的經期之前喔(*´▽`*)")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
                else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.ic_launcher_background)
                            .setTitle("Message")
                            .setMessage("請問是否確定將經期開始日期由"+Y+"/"+M+"/"+D+"改為"+DateTime+"?")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try{
                                    /*將原日期is_Start改為0*/
                                    ContentValues cv = new ContentValues();
                                    cv.put("isStart",0);
                                    String id =Y+"/";
                                    if(M<10)id+="0";
                                    id+=M+"/";
                                    if(D<10)id+="0";
                                    id+=D;
                                    db.update(DATABASE_TABLE,cv, "_date='"+id+"'",null);
                                    /*獲取新日期*/
                                    String [] Date = DateTime.split("/");
                                    Y=Integer.parseInt(Date[0]);
                                    M=Integer.parseInt(Date[1]);
                                    D=Integer.parseInt(Date[2]);
                                    /*將新日期is_Start改為1*/
                                    ContentValues cv2 = new ContentValues();
                                    cv2.put("isStart",1);
                                    String id2 =Y+"/";
                                    if(M<10)id2+="0";
                                    id2+=M+"/";
                                    if(D<10)id2+="0";
                                    id2+=D;
                                    Cursor tmp=db.rawQuery("SELECT * From Data WHERE _date ='"+id2+"'",null);
                                    tmp.moveToFirst();
                                    if(tmp.getCount()!=0){
                                        db.update(DATABASE_TABLE,cv2, "_date='"+id2+"'",null);
                                    }
                                    else{
                                        cv2.put("_date",id2);
                                        cv2.put("flow",-1);
                                        cv2.put("color",-1);
                                        cv2.put("carry",-1);
                                        cv2.put("quality",-1);
                                        cv2.put("isEnd",0);
                                        db.insert(DATABASE_TABLE,null,cv2);

                                        ContentValues cv3 = new ContentValues();
                                        cv3.put("_date",id2);
                                        for(int i=1;i<=10;i++)cv3.put("ch"+Integer.toString(i),false);
                                        db.insert("Data2",null,cv3);
                                    }

                                    /*修改i_M 及 period*/
                                    int day3= Mon[M-1] + D;
                                    peroid=peroid+(day3-day2);
                                    if(i_M!=-1)i_M=i_M+(day2-day3);

                                    String str=account+","+passWD+","+peroid+","+id2+","+i_M+","+email+",";

                                        FileOutputStream out = openFileOutput("passwd.txt",MODE_PRIVATE);
                                        out.write(str.getBytes());
                                        out.close();

                                        //更改資料庫裡的歷史月經週期紀錄
                                        ContentValues C = new ContentValues();
                                        C.put("_date",id2);
                                        C.put("p",peroid);
                                        db.insert("Period",null,C);
                                        db.delete("Period","_date = '"+id+"'",null);
                                        day2=day3;

                                        FileInputStream in = openFileInput("passwd.txt");
                                        byte[] data = new byte[128];
                                        in.read(data);
                                        in.close();
                                        String line="User not exist";
                                        line = new String(data);

                                        String[] name = line.split(",");
                                        String [] DD = name[3].split("/");
                                        Y=Integer.parseInt(DD[0]);
                                        M=Integer.parseInt(DD[1]);
                                        D=Integer.parseInt(DD[2]);
                                        peroid = Integer.parseInt(name[2]);
                                        flag=1;
                                        account=name[0];
                                        passWD=name[1];
                                        i_M=Integer.parseInt(name[4]);
                                        email=name[5];
                                    }catch (IOException e) {
                                        e.printStackTrace();
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setIcon(R.drawable.ic_launcher_background)
                                                .setTitle("Message")
                                                .setMessage("ERROR: "+e.toString())
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .show();
                                    }
                                }
                            })
                            .show();
                }

            }
        },year,month,day).show();

    }
    public void  goto_home(View view){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,home.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
    //產生表單元件
    public void produce_Ch2(){
        Ch2=new LinearLayout(this);
        LL.addView(Ch2, LinearLayout.LayoutParams.MATCH_PARENT,290);
        Ch2.setBackgroundColor(Color.parseColor("#F37C57"));
        Ch2.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout TV_out=new LinearLayout(this);
        TV_out.setPadding(19,19,19,19);
        TextView tv1=new TextView(this);
        tv1.setText("身\n痛");
        tv1.setTextColor(Color.parseColor("#FFFFFF"));
        tv1.setTextSize(16);
        TV_out.addView(tv1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Ch2.addView(TV_out, w, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout Lout=new LinearLayout(this);
        Ch2.addView(Lout,800,290);
        Lout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout Lout_1=new LinearLayout(this);
        Lout_1.setPadding(0,0,0,8);
        final LinearLayout Lin_1=new LinearLayout(this);
        Lout_1.addView(Lin_1, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        Lin_1.setOrientation(LinearLayout.HORIZONTAL);
        Lin_1.setPadding(13,13,13,13);
        Lin_1.setGravity(Gravity.CENTER_VERTICAL);
        Lin_1.setBackgroundColor(Color.parseColor("#92FFFFFF"));
        for(int i=0;i<4;i++){
            ch2_v[i]= new CheckBox(this);
            ch2_v[i].setText(str2[i]);
            ch2_v[i].setTextSize(12);
            Lin_1.addView(ch2_v[i]);
        }
        Lout.addView(Lout_1, LinearLayout.LayoutParams.MATCH_PARENT,140);
        final LinearLayout Lin_2=new LinearLayout(this);
        Lin_2.setOrientation(LinearLayout.HORIZONTAL);
        Lin_2.setPadding(13,13,13,13);
        Lin_2.setGravity(Gravity.CENTER_VERTICAL);
        Lin_2.setBackgroundColor(Color.parseColor("#92FFFFFF"));
        for(int i=4;i<7;i++){
            ch2_v[i]= new CheckBox(this);
            ch2_v[i].setText(str2[i]);
            ch2_v[i].setTextSize(12);
            Lin_2.addView(ch2_v[i]);
        }
        Lout.addView(Lin_2,LinearLayout.LayoutParams.MATCH_PARENT,140);
    }
    public void produce_Ch3(){
        Ch3=new LinearLayout(this);
        LL.addView(Ch3, LinearLayout.LayoutParams.MATCH_PARENT,155);
        Ch3.setBackgroundColor(Color.parseColor("#F37C57"));
        Ch3.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout TV_out=new LinearLayout(this);
        TV_out.setPadding(19,0,19,19);
        TextView tv1=new TextView(this);
        tv1.setText("皮\n膚");
        tv1.setTextColor(Color.parseColor("#FFFFFF"));
        tv1.setTextSize(16);
        TV_out.addView(tv1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Ch3.addView(TV_out, w, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout Lout=new LinearLayout(this);
        Ch3.addView(Lout,800,148);
        Lout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout Lout_1=new LinearLayout(this);
        final LinearLayout Lin_1=new LinearLayout(this);
        Lout_1.addView(Lin_1, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        Lin_1.setOrientation(LinearLayout.HORIZONTAL);
        Lin_1.setPadding(13,13,13,13);
        Lin_1.setGravity(Gravity.CENTER_VERTICAL);
        Lin_1.setBackgroundColor(Color.parseColor("#92FFFFFF"));
        for(int i=0;i<3;i++){
            ch3_v[i]= new CheckBox(this);
            ch3_v[i].setText(str3[i]);
            ch3_v[i].setTextSize(12);
            Lin_1.addView(ch3_v[i]);
        }
        Lout.addView(Lout_1, LinearLayout.LayoutParams.MATCH_PARENT,140);
    }
    public void produce_Ch4(){
        Ch4=new LinearLayout(this);
        LL.addView(Ch4, LinearLayout.LayoutParams.MATCH_PARENT,155);
        Ch4.setBackgroundColor(Color.parseColor("#F37C57"));
        Ch4.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout TV_out=new LinearLayout(this);
        TV_out.setPadding(19,0,19,19);
        TextView tv1=new TextView(this);
        tv1.setText("口\n腔");
        tv1.setTextColor(Color.parseColor("#FFFFFF"));
        tv1.setTextSize(16);
        TV_out.addView(tv1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Ch4.addView(TV_out, w, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout Lout=new LinearLayout(this);
        Ch4.addView(Lout,800,148);
        Lout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout Lout_1=new LinearLayout(this);
        final LinearLayout Lin_1=new LinearLayout(this);
        Lout_1.addView(Lin_1, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        Lin_1.setOrientation(LinearLayout.HORIZONTAL);
        Lin_1.setPadding(13,13,13,13);
        Lin_1.setGravity(Gravity.CENTER_VERTICAL);
        Lin_1.setBackgroundColor(Color.parseColor("#92FFFFFF"));
        for(int i=0;i<3;i++){
            ch4_v[i]= new CheckBox(this);
            ch4_v[i].setText(str4[i]);
            ch4_v[i].setTextSize(12);
            Lin_1.addView(ch4_v[i]);
        }
        Lout.addView(Lout_1, LinearLayout.LayoutParams.MATCH_PARENT,140);
    }
    public void produce_Ch5(){
        Ch5=new LinearLayout(this);
        LL.addView(Ch5, LinearLayout.LayoutParams.MATCH_PARENT,155);
        Ch5.setBackgroundColor(Color.parseColor("#F37C57"));
        Ch5.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout TV_out=new LinearLayout(this);
        TV_out.setPadding(19,0,19,19);
        TextView tv1=new TextView(this);
        tv1.setText("出\n血");
        tv1.setTextColor(Color.parseColor("#FFFFFF"));
        tv1.setTextSize(16);
        TV_out.addView(tv1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Ch5.addView(TV_out, w, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout Lout=new LinearLayout(this);
        Ch5.addView(Lout,800,148);
        Lout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout Lout_1=new LinearLayout(this);
        final LinearLayout Lin_1=new LinearLayout(this);
        Lout_1.addView(Lin_1, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        Lin_1.setOrientation(LinearLayout.HORIZONTAL);
        Lin_1.setPadding(13,13,13,13);
        Lin_1.setGravity(Gravity.CENTER_VERTICAL);
        Lin_1.setBackgroundColor(Color.parseColor("#92FFFFFF"));
        for(int i=0;i<4;i++){
            ch5_v[i]= new CheckBox(this);
            ch5_v[i].setText(str5[i]);
            ch5_v[i].setTextSize(12);
            Lin_1.addView(ch5_v[i]);
        }
        Lout.addView(Lout_1, LinearLayout.LayoutParams.MATCH_PARENT,140);
    }
    public void produce_Ch6(){
        Ch6=new LinearLayout(this);
        LL.addView(Ch6, LinearLayout.LayoutParams.MATCH_PARENT,155);
        Ch6.setBackgroundColor(Color.parseColor("#F37C57"));
        Ch6.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout TV_out=new LinearLayout(this);
        TV_out.setPadding(19,0,19,19);
        TextView tv1=new TextView(this);
        tv1.setText("情\n緒");
        tv1.setTextColor(Color.parseColor("#FFFFFF"));
        tv1.setTextSize(16);
        TV_out.addView(tv1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Ch6.addView(TV_out, w, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout Lout=new LinearLayout(this);
        Ch6.addView(Lout,800,148);
        Lout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout Lout_1=new LinearLayout(this);
        final LinearLayout Lin_1=new LinearLayout(this);
        Lout_1.addView(Lin_1, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        Lin_1.setOrientation(LinearLayout.HORIZONTAL);
        Lin_1.setPadding(13,13,13,13);
        Lin_1.setGravity(Gravity.CENTER_VERTICAL);
        Lin_1.setBackgroundColor(Color.parseColor("#92FFFFFF"));
        for(int i=0;i<3;i++){
            ch6_v[i]= new CheckBox(this);
            ch6_v[i].setText(str6[i]);
            ch6_v[i].setTextSize(12);
            Lin_1.addView(ch6_v[i]);
        }
        Lout.addView(Lout_1, LinearLayout.LayoutParams.MATCH_PARENT,140);
    }
    //資料庫更新
    public void sumbit_Ch2(){
        ContentValues cv = new ContentValues();
        cv.put("_date",m1DisplayDate.getText().toString());
        for(int i=0;i<7;i++){
            if(ch2_v[i].isChecked())cv.put("a"+Integer.toString(i+1),true);
            else cv.put("a"+Integer.toString(i+1),false);
        }
        String id =m1DisplayDate.getText().toString();
        db.insert("Ch2",null,cv);
        db.update("Ch2",cv, "_date='"+id+"'",null);
    }
    public void sumbit_Ch3(){
        ContentValues cv = new ContentValues();
        cv.put("_date",m1DisplayDate.getText().toString());
        for(int i=0;i<3;i++){
            if(ch3_v[i].isChecked())cv.put("a"+Integer.toString(i+1),true);
            else cv.put("a"+Integer.toString(i+1),false);
        }
        String id =m1DisplayDate.getText().toString();
        db.insert("Ch3",null,cv);
        db.update("Ch3",cv, "_date='"+id+"'",null);
    }
    public void sumbit_Ch4(){
        ContentValues cv = new ContentValues();
        cv.put("_date",m1DisplayDate.getText().toString());
        for(int i=0;i<3;i++){
            if(ch4_v[i].isChecked())cv.put("a"+Integer.toString(i+1),true);
            else cv.put("a"+Integer.toString(i+1),false);
        }
        String id =m1DisplayDate.getText().toString();
        db.insert("Ch4",null,cv);
        db.update("Ch4",cv, "_date='"+id+"'",null);
    }
    public void sumbit_Ch5(){
        ContentValues cv = new ContentValues();
        cv.put("_date",m1DisplayDate.getText().toString());
        for(int i=0;i<4;i++){
            if(ch5_v[i].isChecked())cv.put("a"+Integer.toString(i+1),true);
            else cv.put("a"+Integer.toString(i+1),false);
        }
        String id =m1DisplayDate.getText().toString();
        db.insert("Ch5",null,cv);
        db.update("Ch5",cv, "_date='"+id+"'",null);
    }
    public void sumbit_Ch6(){
        ContentValues cv = new ContentValues();
        cv.put("_date",m1DisplayDate.getText().toString());
        for(int i=0;i<3;i++){
            if(ch6_v[i].isChecked())cv.put("a"+Integer.toString(i+1),true);
            else cv.put("a"+Integer.toString(i+1),false);
        }
        String id =m1DisplayDate.getText().toString();
        db.insert("Ch6",null,cv);
        db.update("Ch6",cv, "_date='"+id+"'",null);
    }
    public void IsLeapYear(int y){
        int f=0;
        if(y%4==0)f=1;
        if(y%100==0)f=0;
        if(y%400==0)f=1;
        if(f==1)Mon=Mon2;
        else Mon=Mon1;
    }
    public void Info_flow(View view){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("流量分法")
                .setIcon(getResources().getDrawable( R.drawable.leaf ))
                .setMessage("以基本型（20~23公分）衛生棉為例:\n1. 正常：平均為每2～3小時需更換一片衛生棉，一天約換6～9片。\n2. 量多：一天衛生棉要更換10片以上，且經血常弄髒衣褲。\n3. 量少：一天只需換5片以下的衛生棉\n4. 點滴性出血: 經血非常少，甚至只有少少幾滴。\n")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    public void Info_quality(View view){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("質地分法")
                .setIcon(getResources().getDrawable( R.drawable.leaf ))
                .setMessage("1. 正常: 質地如豆漿。\n2. 黏稠: 質地如果凍或軟爛的紅豆湯。\n3. 清稀: 質地清如水。")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}

