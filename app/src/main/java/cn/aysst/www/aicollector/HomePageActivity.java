package cn.aysst.www.aicollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.aysst.www.aicollector.DialogFragment.EditDialogFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import wheelview.BottomDialog;
import wheelview.WheelView;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener,EditDialogFragment.NoticeDialogListener {
    private CircleImageView circleImageView;
    private TextView userNameText;
    private TextView userMailText;
    private TextView userGoldText;
    private TextView myMailText;
    private TextView myPhoneText;
    private TextView mySexText;
    private TextView myCollectText;
    private TextView myTagText;
    private TextView showMailView;
    private TextView showPhoneView;
    private TextView showSexView;
    private BottomDialog bottomDialog;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        circleImageView = (CircleImageView)findViewById(R.id.portrait_onhomepage);
        userNameText = (TextView)findViewById(R.id.username_onhomepage);
        userMailText = (TextView)findViewById(R.id.usermail_onhomepage);
        userGoldText = (TextView)findViewById(R.id.usergold_onhomepage);
        myMailText = (TextView)findViewById(R.id.my_mail_onhomepage);
        myPhoneText = (TextView)findViewById(R.id.my_phone_onhomepage);
        mySexText = (TextView)findViewById(R.id.my_sex_onhomepage);
        myCollectText = (TextView)findViewById(R.id.my_collect_onhomepage);
        myTagText = (TextView)findViewById(R.id.my_tag_onhomepage);
        showMailView = (TextView)findViewById(R.id.show_mail_onhomepage);
        showPhoneView = (TextView)findViewById(R.id.show_phone_onhomepage);
        showSexView = (TextView)findViewById(R.id.show_sex_onhomepage);

        circleImageView.setImageResource(R.drawable.apple);
        userNameText.setText("AYSST");
        userMailText.setText("aysst@mail.com");
        userGoldText.setText("360 - 金币");

        myMailText.setOnClickListener(this);
        myPhoneText.setOnClickListener(this);
        mySexText.setOnClickListener(this);
        myCollectText.setOnClickListener(this);
        myTagText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_mail_onhomepage:
                showEditMailDialog();
                break;
            case R.id.my_phone_onhomepage:
                showEditPhoneDialog();
                break;
            case R.id.my_sex_onhomepage:;
                View outerView1 = LayoutInflater.from(this).inflate(R.layout.dialog_select_date_time, null);
                final WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wv1);
                List<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                wv1.setItems(list,0);

                TextView tv_ok = (TextView) outerView1.findViewById(R.id.tv_ok);
                TextView tv_cancel = (TextView) outerView1.findViewById(R.id.tv_cancel);
                //点击确定
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        bottomDialog.dismiss();
                        String mSelect = wv1.getSelectedItem();
                        showSexView.setText(mSelect);
                    }
                });
                //点击取消
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        bottomDialog.dismiss();
                    }
                });
                //防止弹出两个窗口
                if (bottomDialog !=null && bottomDialog.isShowing()) {  return; }

                bottomDialog = new BottomDialog(this, R.style.ActionSheetDialogStyle);//将布局设置给Dialog
                bottomDialog.setContentView(outerView1);
                bottomDialog.show();//显示对话框
                break;
            case R.id.my_collect_onhomepage:
                break;
            case R.id.my_tag_onhomepage:
                break;
            default:
                break;
        }
    }

    private void showEditMailDialog(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","我的邮箱");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"EditMailDialog");
    }

    private void showEditPhoneDialog(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","我的手机");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"EditPhoneDialog");
    }

    @Override
    public void onDialogPositiveClick(EditDialogFragment dialog) {
        if(bundle != null){
            switch (bundle.getString("message")){
                case "我的邮箱":
                    showMailView.setText(dialog.myData);
                    break;
                case "我的手机":
                    showPhoneView.setText(dialog.myData);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDialogNegativeClick(EditDialogFragment dialog) {

    }
}
