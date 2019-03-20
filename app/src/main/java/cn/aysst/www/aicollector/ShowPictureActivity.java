package cn.aysst.www.aicollector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import cn.aysst.www.aicollector.Class.ProvideForTask;

public class ShowPictureActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SHOW_PICTURE_AC = "show_picture_activity";

    private Uri pictureUri = null;
    private Bitmap pictureBit = null;
    private InputStream pictureInputStream = null;
    private OutputStream pictureOutputStream = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        Intent intent = getIntent();
        ProvideForTask provideForTask = intent.getParcelableExtra(SHOW_PICTURE_AC);
        pictureUri = Uri.parse(provideForTask.getPictureUriStr());
        try {
//            pictureBit = BitmapFactory.decodeStream(getContentResolver().openInputStream(pictureUri));
            pictureBit = MediaStore.Images.Media.getBitmap(getContentResolver(),pictureUri);
            pictureInputStream = getContentResolver().openInputStream(pictureUri);
            pictureOutputStream = getContentResolver().openOutputStream(pictureUri);
        }catch (Exception e){
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.info_on_show_pic)).setText(provideForTask.getPictureUriStr());
        ((ImageView)findViewById(R.id.view_on_show_pic)).setImageBitmap(pictureBit);
        ((Button)findViewById(R.id.delete_on_show_pic)).setOnClickListener(this);
        ((Button)findViewById(R.id.download_on_show_pic)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_on_show_pic:
                deletePic();break;
            case R.id.download_on_show_pic:
                dowloadPic();break;
        }
    }

    private void deletePic(){

    }

    private void dowloadPic(){
        int numread;
        String path = getExternalCacheDir().getPath()+"/image";

        try {
            File file = new File(path);
            if (!file.exists()){ file.mkdirs(); }
            FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+getTime()+".jpg");
            byte b[] = new byte[1024];
            do {
                numread = pictureInputStream.read(b);
                if (numread == -1){ break; }
                fileOutputStream.write(b,0,numread);
            }while (true);

            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

//        File outputFile = new File(getExternalCacheDir().getPath()+getTime()+".jpg");
//        try {
//            if (outputFile.exists()){
//                outputFile.delete();
//            }
//            outputFile.createNewFile();
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
//            pictureBit.compress(Bitmap.CompressFormat.JPEG,80,bufferedOutputStream);
//            bufferedOutputStream.flush();
//            bufferedOutputStream.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
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
