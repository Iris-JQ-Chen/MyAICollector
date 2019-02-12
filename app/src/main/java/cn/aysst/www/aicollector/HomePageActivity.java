package cn.aysst.www.aicollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView userNameText;
    private TextView userMailText;
    private TextView userGoldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        circleImageView = (CircleImageView)findViewById(R.id.portrait_onhomepage);
        userNameText = (TextView)findViewById(R.id.username_onhomepage);
        userMailText = (TextView)findViewById(R.id.usermail_onhomepage);
        userGoldText = (TextView)findViewById(R.id.usergold_onhomepage);

        circleImageView.setImageResource(R.drawable.apple);
        userNameText.setText("AYSST");
        userMailText.setText("aysst@mail.com");
        userGoldText.setText("360 - 金币");
    }
}
