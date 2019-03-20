package cn.aysst.www.aicollector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import cn.aysst.www.aicollector.Class.ProvideForTask;

public class ShowTextActivity extends AppCompatActivity {
    public static final String SHOW_TEXT_AC = "show_text_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        Intent intent = getIntent();
        ProvideForTask provideForTask = intent.getParcelableExtra(SHOW_TEXT_AC);

        TextView textView = (TextView)findViewById(R.id.text_view_on_show_text);

        textView.setText(provideForTask.getTextUriStr());
    }
}
