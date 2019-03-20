package cn.aysst.www.aicollector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.aysst.www.aicollector.Class.ProvideForTask;

public class ShowAudioActivity extends AppCompatActivity {
    public static final String SHOW_AUDIO_AC = "show_audio_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_audio);

        Intent intent = getIntent();
        ProvideForTask provideForTask = intent.getParcelableExtra(SHOW_AUDIO_AC);

        TextView textView = (TextView)findViewById(R.id.text_view_on_show_aud);

        textView.setText(provideForTask.getAudioUriStr());
    }
}
