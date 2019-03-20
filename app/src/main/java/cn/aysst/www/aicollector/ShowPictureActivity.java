package cn.aysst.www.aicollector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cn.aysst.www.aicollector.Class.ProvideForTask;

public class ShowPictureActivity extends AppCompatActivity {
    public static final String SHOW_PICTURE_AC = "show_picture_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_picture);

        Intent intent = getIntent();
        ProvideForTask provideForTask = intent.getParcelableExtra(SHOW_PICTURE_AC);

        ImageView imageView = (ImageView)findViewById(R.id.image_view_on_show_pic);
        TextView textView = (TextView)findViewById(R.id.text_view_on_show_pic);

        textView.setText(provideForTask.getPictureUriStr());
        try {
            Uri uri = Uri.parse(provideForTask.getPictureUriStr());
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            imageView.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
