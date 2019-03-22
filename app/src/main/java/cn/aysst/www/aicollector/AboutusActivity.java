package cn.aysst.www.aicollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        getSupportActionBar().setTitle("智集APP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
