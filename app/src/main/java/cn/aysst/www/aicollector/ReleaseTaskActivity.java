package cn.aysst.www.aicollector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import wheelview.WheelView;

public class ReleaseTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ImageView backImageView;
    private TextView editGoldText;
    private TextView showGoldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_releasetask);

        toolbar = (Toolbar)findViewById(R.id.toolbar_onrelasetask);
        backImageView = (ImageView)findViewById(R.id.back_to_release);
        editGoldText = (TextView)findViewById(R.id.main_show_dialog_btn);
        showGoldText = (TextView)findViewById(R.id.show_gold);

        setSupportActionBar(toolbar);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editGoldText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_show_dialog_btn:
                View outerView = LayoutInflater.from(this).inflate(R.layout.dialog_content_view, null);
                final WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                wv.setItems(getNumbers(),0);//init selected position is 0 初始选中位置为0
                wv.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int selectedIndex, String item) {
                        Log.d(TAG, "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                        showGoldText.setText(item);
                    }
                });

                new AlertDialog.Builder(this)
                        .setTitle("WheelView in Dialog")
                        .setView(outerView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ReleaseTaskActivity.this,
                                        "selectedIndex: "+ wv.getSelectedPosition() +"  selectedItem: "+ wv.getSelectedItem(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
        }
    }

    private ArrayList getNumbers() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            list.add(i + "");
        }
        return  list;
    }
}
