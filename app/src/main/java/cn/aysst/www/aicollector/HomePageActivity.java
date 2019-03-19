package cn.aysst.www.aicollector;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private TextView myReleaseText;
    private TextView myAnswerText;
    private TextView showMailView;
    private TextView showPhoneView;
    private TextView showSexView;
    private BottomDialog bottomDialog;

    private static final int REQUEST_WE_STORAGE_PER = 0;
    private static final int REQUEST_RE_STORAGE_PER = 1;
    private static final int REQUEST_CAMERA_PER = 2;
    private static final int CHANGE_PORTRAIT = 100;

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
        myReleaseText = (TextView)findViewById(R.id.my_release_onhomepage);
        myAnswerText = (TextView)findViewById(R.id.my_answer_onhomepage);
        showMailView = (TextView)findViewById(R.id.show_mail_onhomepage);
        showPhoneView = (TextView)findViewById(R.id.show_phone_onhomepage);
        showSexView = (TextView)findViewById(R.id.show_sex_onhomepage);

        circleImageView.setImageResource(R.drawable.apple);
        userNameText.setText("AYSST");
        userMailText.setText("aysst@mail.com");
        userGoldText.setText("360 - 金币");

        userNameText.setOnClickListener(this);
        userMailText.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        findViewById(R.id.my_mail_layout).setOnClickListener(this);
        findViewById(R.id.my_phone_layout).setOnClickListener(this);
        findViewById(R.id.my_sex_layout).setOnClickListener(this);
        findViewById(R.id.my_release_layout).setOnClickListener(this);
        findViewById(R.id.my_answer_layout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.username_onhomepage:
                showEditNameDialog();
                break;
            case R.id.usermail_onhomepage:
                showEditMailDialog();
                break;
            case R.id.my_mail_layout:
                showEditMailDialog();
                break;
            case R.id.my_phone_layout:
                showEditPhoneDialog();
                break;
            case R.id.my_sex_layout:;
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
            case R.id.my_release_layout:
                break;
            case R.id.my_answer_layout:
                break;
            case R.id.portrait_onhomepage:
                if (openGallary()){
                    break;
                }else {
                    return;
                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHANGE_PORTRAIT:
                if (resultCode == RESULT_OK){
                    List<Uri> result = Matisse.obtainResult(data);
                    Uri portraitUri = result.get(0);
                    Bitmap portraitBit = null;
                    if (portraitUri != null){
                        try {
                            portraitBit = getBitmapFromUri(HomePageActivity.this,portraitUri);
                            circleImageView.setImageBitmap(portraitBit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_WE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(HomePageActivity.this,"“写”内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(HomePageActivity.this,"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_RE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(HomePageActivity.this,"“读”内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(HomePageActivity.this,"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CAMERA_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(HomePageActivity.this,"照相机权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(HomePageActivity.this,"没有给予照相机权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void showEditNameDialog(){
        EditDialogFragment dialogFragment = new EditDialogFragment();
        bundle = new Bundle();
        bundle.putString("message","我的昵称");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"EditNameDialog");
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
                    userMailText.setText(dialog.myData);
                    break;
                case "我的手机":
                    showPhoneView.setText(dialog.myData);
                    break;
                case "我的昵称":
                    userNameText.setText(dialog.myData);
                default:
                    break;
            }
        }
    }

    @Override
    public void onDialogNegativeClick(EditDialogFragment dialog) {

    }

    private boolean openGallary(){
        if (ContextCompat.checkSelfPermission(HomePageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomePageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WE_STORAGE_PER);
            return false;
        }else if (ContextCompat.checkSelfPermission(HomePageActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomePageActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_RE_STORAGE_PER);
            return false;
        }else if (ContextCompat.checkSelfPermission(HomePageActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HomePageActivity.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PER);
            return false;
        }else {
            Matisse.from(HomePageActivity.this)
                    .choose(MimeType.allOf())//图片类型
                    .countable(true)//true:选中后显示数字;false:选中后显示对号
                    .maxSelectable(1)//可选的最大数
                    .capture(true)//选择照片时，是否显示拍照
                    .captureStrategy(new CaptureStrategy(true, "cn.aysst.www.aicollector.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                    .imageEngine(new GlideEngine())//图片加载引擎
                    .forResult(CHANGE_PORTRAIT);//
            return true;
        }
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFromUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
