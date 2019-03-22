package cn.aysst.www.aicollector;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cn.aysst.www.aicollector.Class.ProvideForTask;

public class ShowTextActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SHOW_TEXT_AC = "show_text_activity";

    private Uri textUri = null;
    private String textString = null;
    private InputStream textInputStream = null;
    private OutputStream textOuputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        Intent intent = getIntent();
        ProvideForTask provideForTask = intent.getParcelableExtra(SHOW_TEXT_AC);
        textUri = Uri.parse(provideForTask.getTextUriStr());
        try {
            textInputStream = getContentResolver().openInputStream(textUri);
            textString = getStringFromInputStream(textInputStream);
            textOuputStream = getContentResolver().openOutputStream(textUri);
        }catch (Exception e){
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.info_on_show_text)).setText(provideForTask.getTextUriStr());
        ((TextView)findViewById(R.id.view_on_show_text)).setText(textString);
        ((Button)findViewById(R.id.delete_on_show_text)).setOnClickListener(this);
        ((Button)findViewById(R.id.download_on_show_text)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_on_show_text:
                deleteText();break;
            case R.id.download_on_show_text:
                downloadText();break;
        }
    }

    public static String getStringFromInputStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private void deleteText(){
        //数据库删除
        finish();
    }

    private void downloadText(){
        int numread;
        String path = getExternalCacheDir().getPath()+"/text";
        try {
            File file = new File(path);
            if (!file.exists()){ file.mkdirs(); }
            FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+getTime()+".txt");
            byte b[] = new byte[1024];
            do {
                numread = textInputStream.read(b);
                if (numread == -1){ break; }
                fileOutputStream.write(b,0,numread);
            }while (true);

            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return ""+year+month+day+"_"+hour+minute+second;
    }

}
