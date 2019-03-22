package cn.aysst.www.aicollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.aysst.www.aicollector.DialogFragment.EditDialogFragment;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener,EditDialogFragment.NoticeDialogListener {

    private RadioButton radioButtonZ;
    private RadioButton radioButtonW;
    private TextView textViewSetNum;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        getSupportActionBar().setTitle("在线充值");

        radioButtonZ = (RadioButton)findViewById(R.id.zhifubao_on_recharge);
        radioButtonW = (RadioButton)findViewById(R.id.weixin_on_recharge);
        textViewSetNum = (TextView)findViewById(R.id.show_num_on_recharge);

        ((TextView)findViewById(R.id.show_gold_on_recharge)).setText("10086");

        findViewById(R.id.set_num_on_recharge).setOnClickListener(this);
        findViewById(R.id.pay_on_recharge).setOnClickListener(this);
        radioButtonZ.setOnClickListener(this);
        radioButtonW.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_num_on_recharge:
                setGoldNum();
                break;
            case R.id.pay_on_recharge:

                break;
            case R.id.zhifubao_on_recharge:
                if (radioButtonW.isChecked()){
                    radioButtonW.setChecked(false);
                }
                break;
            case R.id.weixin_on_recharge:
                if (radioButtonZ.isChecked()){
                    radioButtonZ.setChecked(false);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setGoldNum(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","自定义充值钱数");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"EditNameDialog");
    }

    @Override
    public void onDialogPositiveClick(EditDialogFragment dialog) {
        if(bundle != null){
            switch (bundle.getString("message")){
                case "自定义充值钱数":
                    textViewSetNum.setText(dialog.myData.toString());
                default:
                    break;
            }
        }
    }

    @Override
    public void onDialogNegativeClick(EditDialogFragment dialog) {

    }
}
