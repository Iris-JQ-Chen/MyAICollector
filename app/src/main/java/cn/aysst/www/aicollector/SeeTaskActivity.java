package cn.aysst.www.aicollector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.aysst.www.aicollector.Class.Task;

public class SeeTaskActivity extends AppCompatActivity {
    public static final String SEE_MY_TASK = "see_my_task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_task);

        Intent intent = getIntent();
        Task myTask = intent.getParcelableExtra(SEE_MY_TASK);
    }
}
