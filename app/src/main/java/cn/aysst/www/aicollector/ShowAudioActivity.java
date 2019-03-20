package cn.aysst.www.aicollector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.Task;

public class ShowAudioActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SHOW_AUDIO_AC = "show_audio_activity";

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Uri audioUri = null;
    private InputStream audioInputStream = null;
    private OutputStream audioOutputStream = null;
    private String audioPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_audio);

        Intent intent = getIntent();
        ProvideForTask provideForTask = intent.getParcelableExtra(SHOW_AUDIO_AC);
        audioUri = Uri.parse(provideForTask.getAudioUriStr());
        try {
            audioInputStream = getContentResolver().openInputStream(audioUri);
            audioOutputStream = getContentResolver().openOutputStream(audioUri);
        }catch (Exception e){
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.info_on_show_aud)).setText(provideForTask.getAudioUriStr());
        ((Button)findViewById(R.id.play_on_show_aud)).setOnClickListener(this);
        ((Button)findViewById(R.id.pause_on_show_aud)).setOnClickListener(this);
        ((Button)findViewById(R.id.stop_on_show_aud)).setOnClickListener(this);
        ((Button)findViewById(R.id.delete_on_show_aud)).setOnClickListener(this);
        ((Button)findViewById(R.id.download_on_show_aud)).setOnClickListener(this);

        initMediaPlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_on_show_aud:
                playAudio();break;
            case R.id.pause_on_show_aud:
                pauseAudio();break;
            case R.id.stop_on_show_aud:
                stopAudio();break;
            case R.id.delete_on_show_aud:
                deleteAudio();break;
            case R.id.download_on_show_aud:
                downloadAudio();break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void initMediaPlayer(){
        try {
            if (audioPath != null){
                mediaPlayer.setDataSource(audioPath);
            }else {
                mediaPlayer.setDataSource(ShowAudioActivity.this,audioUri);
            }
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void playAudio(){
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    private void pauseAudio(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    private void stopAudio(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.reset();
            initMediaPlayer();
        }
    }

    private void deleteAudio(){
        //数据库删除
        finish();
    }

    private void downloadAudio(){
        int numread;
        String path = getExternalCacheDir().getPath()+"/audio";
        try {
            File file  = new File(path);
            if (!file.exists()){ file.mkdirs(); }
            FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+getTime()+".mp3");
            byte b[] = new byte[1024];
            do {
                numread = audioInputStream.read(b);
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
