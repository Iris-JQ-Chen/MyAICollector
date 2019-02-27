package cn.aysst.www.aicollector;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.aysst.www.aicollector.Adapter.ProvideAudioTaskAdapter;
import cn.aysst.www.aicollector.Adapter.ProvidePictureTaskAdapter;
import cn.aysst.www.aicollector.Adapter.ProvideVideoTaskAdapter;
import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;

public class DoTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DO_OTHER_TASK = "do_other_task";
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int TAKE_VIDEO = 3;
    public static final int CHOOSE_VIDEO = 4;
    public static final int RECORD_AUDIO = 5;
    public static final int CHOOSE_AUDIO = 6;

    public static final int REQUEST_WE_STORAGE_PER = 100;

    private FloatingActionButton floatingActionButton;

    private Uri imageUri;

    private List<ProvideForTask> provideList = new ArrayList<>();
    private RecyclerView recyclerViewPic;
    private RecyclerView recyclerViewVid;
    private RecyclerView recyclerViewAud;
    private GridLayoutManager layoutManagerPic;
    private GridLayoutManager layoutManagerVid;
    private GridLayoutManager layoutManagerAud;
    private ProvidePictureTaskAdapter providePicAdapter;
    private ProvideVideoTaskAdapter provideVideoAdapter;
    private ProvideAudioTaskAdapter provideAudAdapter;

    private int MY_TASK_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_task);

        Intent intent = getIntent();
        Task otherTask = intent.getParcelableExtra(DO_OTHER_TASK);
        provideList = otherTask.getProvideList();
        if (provideList == null){
            provideList = new ArrayList<ProvideForTask>();
        }

        floatingActionButton = findViewById(R.id.provide_for_task);
        floatingActionButton.setOnClickListener(this);

        ((TextView)findViewById(R.id.other_task_name)).setText(otherTask.getName());
        ((TextView)findViewById(R.id.other_task_info)).setText(otherTask.getInfo()+"\n"+otherTask.getTime());
        ((TextView)findViewById(R.id.other_task_publisher)).setText(otherTask.getPublishId()+"");
        try {
            switch (otherTask.getType()){
                case Task.TYPE_PICTURE:
                    ((TextView)findViewById(R.id.other_task_type)).setText("图片");
                    prePicRecycler();
                    MY_TASK_TYPE = Task.TYPE_PICTURE;
                    break;
                case Task.TYPE_VIDEO:
                    ((TextView)findViewById(R.id.other_task_type)).setText("视频");
                    preVidRecycler();
                    MY_TASK_TYPE = Task.TYPE_VIDEO;
                    break;
                case Task.TYPE_AUDIO:
                    ((TextView)findViewById(R.id.other_task_type)).setText("音频");
                    preAudRecycler();
                    MY_TASK_TYPE = Task.TYPE_AUDIO;
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.other_task_gold)).setText(otherTask.getGold()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.provide_for_task:
                showPopup(v);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ProvideForTask providerPicTask = new ProvideForTask();
                        providerPicTask.setPictureBitStr(getStrFormBit(bitmap));
//                        providerPicTask.setPictureBitmap(bitmap);
                        provideList.add(providerPicTask);
                        providePicAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode ==  RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case TAKE_VIDEO:
                if (resultCode == RESULT_OK){
                    Uri videoUri = data.getData();
                    ProvideForTask provideForTask = new ProvideForTask();
                    provideForTask.setVideoUriStr(videoUri.toString());
                    provideList.add(provideForTask);
                    provideVideoAdapter.notifyDataSetChanged();
                }
                break;
            case CHOOSE_VIDEO:
                if (resultCode == RESULT_OK){
                    Uri videoUri = data.getData();
                    ProvideForTask provideForTask = new ProvideForTask();
                    provideForTask.setVideoUriStr(videoUri.toString());
                    provideList.add(provideForTask);
                    provideVideoAdapter.notifyDataSetChanged();
                }
                break;
            case RECORD_AUDIO:
                if (resultCode == RESULT_OK){
                    Uri audioUri = data.getData();
                    ProvideForTask provideForTask = new ProvideForTask();
                    provideForTask.setAudioUriStr(audioUri.toString());
                    provideList.add(provideForTask);
                    provideAudAdapter.notifyDataSetChanged();
                }
                break;
            case CHOOSE_AUDIO:
                if (resultCode == RESULT_OK){
                    Uri audioUri = data.getData();
                    ProvideForTask provideForTask = new ProvideForTask();
                    provideForTask.setAudioUriStr(audioUri.toString());
                    provideList.add(provideForTask);
                    provideAudAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_WE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(DoTaskActivity.this,"内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DoTaskActivity.this,"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu (Menu menu){
//        switch (MY_TASK_TYPE){
//            case Task.TYPE_PICTURE:
//                menu.getItem(2).setVisible(false);
//                menu.getItem(2).setEnabled(false);
//                menu.getItem(3).setVisible(false);
//                menu.getItem(3).setEnabled(false);
//                menu.getItem(4).setVisible(false);
//                menu.getItem(4).setEnabled(false);
//                menu.getItem(5).setVisible(false);
//                menu.getItem(5).setEnabled(false);
//                return super.onPrepareOptionsMenu(menu);
//            case Task.TYPE_VIDEO:
//                menu.getItem(2).setVisible(false);
//                menu.getItem(2).setEnabled(false);
//                menu.getItem(3).setVisible(false);
//                menu.getItem(3).setEnabled(false);
//                menu.getItem(0).setVisible(false);
//                menu.getItem(0).setEnabled(false);
//                menu.getItem(1).setVisible(false);
//                menu.getItem(1).setEnabled(false);
//                return super.onPrepareOptionsMenu(menu);
//            case Task.TYPE_AUDIO:
//                menu.getItem(0).setVisible(false);
//                menu.getItem(0).setEnabled(false);
//                menu.getItem(1).setVisible(false);
//                menu.getItem(1).setEnabled(false);
//                menu.getItem(4).setVisible(false);
//                menu.getItem(4).setEnabled(false);
//                menu.getItem(5).setVisible(false);
//                menu.getItem(5).setEnabled(false);
//                return super.onPrepareOptionsMenu(menu);
//            default:
//                return super.onPrepareOptionsMenu(menu);
//        }
//    }

    /**
     * 相应View.OnClickListener点击事件OnClick
     * @param view
     */
    private void showPopup(View view){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.fab_provider_on_dotaskactivity);
        Menu menu = popupMenu.getMenu();
        switch (MY_TASK_TYPE){
            case Task.TYPE_PICTURE:
                Log.d("AICollector",menu.size()+"");
                menu.getItem(2).setVisible(false);
                menu.getItem(2).setEnabled(false);
                menu.getItem(3).setVisible(false);
                menu.getItem(3).setEnabled(false);
                menu.getItem(4).setVisible(false);
                menu.getItem(4).setEnabled(false);
                menu.getItem(5).setVisible(false);
                menu.getItem(5).setEnabled(false);
                break;
            case Task.TYPE_VIDEO:
                Log.d("AICollector",menu.size()+"");
                menu.getItem(2).setVisible(false);
                menu.getItem(2).setEnabled(false);
                menu.getItem(3).setVisible(false);
                menu.getItem(3).setEnabled(false);
                menu.getItem(0).setVisible(false);
                menu.getItem(0).setEnabled(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(1).setEnabled(false);
                break;
            case Task.TYPE_AUDIO:
                Log.d("AICollector",menu.size()+"");
                menu.getItem(0).setVisible(false);
                menu.getItem(0).setEnabled(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(1).setEnabled(false);
                menu.getItem(4).setVisible(false);
                menu.getItem(4).setEnabled(false);
                menu.getItem(5).setVisible(false);
                menu.getItem(5).setEnabled(false);
                break;
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.camera:
                        openCamera();
                        return true;
                    case R.id.picture_file:
                        openPictureFile();
                        return true;
                    case R.id.audio_recoder:
                        openAudioRecoder();
                        return true;
                    case R.id.audio_file:
                        openAudioFile();
                        return true;
                    case R.id.video_recoder:
                        openVideoRecoder();
                        return true;
                    case R.id.video_file:
                        openVideoFile();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    /**
     * 给视频任务配置recycler
     */
    private void preVidRecycler()throws NullListInAdapterException{
        recyclerViewVid = (RecyclerView)findViewById(R.id.recycler_view_ondotask);
        layoutManagerVid = new GridLayoutManager(this,1);
        provideVideoAdapter = new ProvideVideoTaskAdapter(provideList);
        recyclerViewVid.setLayoutManager(layoutManagerVid);
        recyclerViewVid.setAdapter(provideVideoAdapter);
    }

    /**
     * 给图片任务配置recycler
     */
    private void prePicRecycler()throws NullListInAdapterException{
        recyclerViewPic = (RecyclerView)findViewById(R.id.recycler_view_ondotask);
        layoutManagerPic = new GridLayoutManager(this,3);
        providePicAdapter = new ProvidePictureTaskAdapter(provideList);
        recyclerViewPic.setLayoutManager(layoutManagerPic);
        recyclerViewPic.setAdapter(providePicAdapter);
    }

    /**
     * 给音频任务配置recycler
     */
    private void preAudRecycler()throws NullListInAdapterException{
        recyclerViewAud = (RecyclerView)findViewById(R.id.recycler_view_ondotask);
        layoutManagerAud = new GridLayoutManager(this,1);
        provideAudAdapter = new ProvideAudioTaskAdapter(provideList);
        recyclerViewAud.setLayoutManager(layoutManagerAud);
        recyclerViewAud.setAdapter(provideAudAdapter);
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开照相机
     */
    private void openCamera(){
        String outputImageName = getTime()+".jpg";
        File outputImage = new File(getExternalCacheDir(),outputImageName);
        try {
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(DoTaskActivity.this,"cn.aysst.www.aicollector.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开图片
     */
    private void openPictureFile(){
        if(ContextCompat.checkSelfPermission(DoTaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DoTaskActivity.this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WE_STORAGE_PER);
        }else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent,CHOOSE_PHOTO);
        }
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开录音机
     */
    private void openAudioRecoder(){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent,RECORD_AUDIO);
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开音频
     */
    private void openAudioFile(){
        if(ContextCompat.checkSelfPermission(DoTaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DoTaskActivity.this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WE_STORAGE_PER);
        }else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("audio/*");
            startActivityForResult(intent,CHOOSE_AUDIO);
        }
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开录像机
     */
    private void openVideoRecoder(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,TAKE_VIDEO);
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开视频
     */
    private void openVideoFile(){
        if(ContextCompat.checkSelfPermission(DoTaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DoTaskActivity.this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WE_STORAGE_PER);
        }else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("video/*");
            startActivityForResult(intent,CHOOSE_VIDEO);
            Log.d("AICollector","openVideoFile");
        }
    }

    /**
     * 图片转换成base64字符串
     *
     * @param bitmap
     * @return
     */
    private String getStrFormBit(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent intent){
        String imagePath = null;
        Uri uri = intent.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }

        if(imagePath != null){
            ProvideForTask provideForTask = new ProvideForTask();
            provideForTask.setPictureBitStr(getStrFormBit(BitmapFactory.decodeFile(imagePath)));
            provideList.add(provideForTask);
            providePicAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(DoTaskActivity.this,"无法得到图片文件",Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImageBeforeKitKat(Intent intent){
        Uri uri = intent.getData();
        String imagePath = getImagePath(uri,null);

        if(imagePath != null){
            ProvideForTask provideForTask = new ProvideForTask();
            provideForTask.setPictureBitStr(getStrFormBit(BitmapFactory.decodeFile(imagePath)));
            provideList.add(provideForTask);
            providePicAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(DoTaskActivity.this,"无法得到图片文件",Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return ""+year+month+day+"_"+hour+minute+second;
    }
}
