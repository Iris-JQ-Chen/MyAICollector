package cn.aysst.www.aicollector;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import cn.aysst.www.aicollector.Adapter.ProvideAudioTaskSuperAdapter;
import cn.aysst.www.aicollector.Adapter.ProvidePictureTaskSuperAdapter;
import cn.aysst.www.aicollector.Adapter.ProvideTextTaskSuperAdapter;
import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.ProviderForShow;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.DialogFragment.InputTextDialogFragment;

public class DoTaskActivity extends AppCompatActivity implements View.OnClickListener, InputTextDialogFragment.InputTextDialogListener {
    public static final String DO_OTHER_TASK = "do_other_task";
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private static final int RECORD_AUDIO = 3;
    private static final int CHOOSE_AUDIO = 4;
    private static final int INPUT_TEXT = 5;
    private static final int CHOOSE_TEXT = 6;

    private static final int REQUEST_WE_STORAGE_PER = 100;
    private static final int REQUEST_RE_STORAGE_PER = 101;

    private FloatingActionButton floatingActionButton;

    private Uri imageUri;

    private List<ProviderForShow> providerShowList = new ArrayList<>();
    private List<ProvideForTask> provideList = new ArrayList<>();
    private RecyclerView recyclerViewPic;
    private RecyclerView recyclerViewText;
    private RecyclerView recyclerViewAud;
    private LinearLayoutManager layoutManagerPic;
    private LinearLayoutManager layoutManagerText;
    private LinearLayoutManager layoutManagerAud;
    private ProvidePictureTaskSuperAdapter providePicAdapter;
    private ProvideTextTaskSuperAdapter provideTextAdapter;
    private ProvideAudioTaskSuperAdapter provideAudAdapter;

    private int MY_TASK_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("AIColletor","onCreate");
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
                case Task.TYPE_TEXT:
                    ((TextView)findViewById(R.id.other_task_type)).setText("文本");
                    preTextRecycler();
                    MY_TASK_TYPE = Task.TYPE_TEXT;
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

        initProviderForShowList();
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
                    provideListAdd(imageUri.toString());
                    providerShowListAdd(imageUri.toString());
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode ==  RESULT_OK){
                    List<Uri> result = Matisse.obtainResult(data);
                    List<String> stringList = new ArrayList<>();

                    for (Uri uri : result) {
                        stringList.add(uri.toString());
                    }

                    providerListAdd(stringList);
                    providerShowListAdd(stringList);
                }
                break;
            case CHOOSE_TEXT:
                if (resultCode == RESULT_OK){
                    List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);

                    for(int i = 0;i < list.size();i++){
                        Uri uri = Uri.fromFile(new File(list.get(i)));
                        list.set(i,uri.toString());
                    }

                    providerListAdd(list);
                    providerShowListAdd(list);
                }
                break;
            case RECORD_AUDIO:
                if (resultCode == RESULT_OK){
                    Uri audioUri = data.getData();


                    provideListAdd(audioUri.toString());
                    providerShowListAdd(audioUri.toString());
                }
                break;
            case CHOOSE_AUDIO:
                if (resultCode == RESULT_OK){
                    List<String> list= data.getStringArrayListExtra(Constant.RESULT_INFO);

                    for(int i = 0;i < list.size();i++){
                        Uri audioFileUir = Uri.fromFile(new File(list.get(i)));
                        Uri audioContentUri = getContentUriFromFileUri(audioFileUir);
                        if (audioContentUri == null){
                            list.set(i,audioFileUir.toString());
                        }else {
                            list.set(i,audioContentUri.toString());
                        }
                    }

                    providerListAdd(list);
                    providerShowListAdd(list);
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
                    Toast.makeText(DoTaskActivity.this,"“写”内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DoTaskActivity.this,"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_RE_STORAGE_PER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(DoTaskActivity.this,"“读”内存权限已开放",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DoTaskActivity.this,"没有给予内存权限",Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

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
            case Task.TYPE_TEXT:
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
                    case R.id.input_text:
                        openInputText();
                        return true;
                    case R.id.text_file:
                        openTextFile();
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
    private void preTextRecycler()throws NullListInAdapterException{
        recyclerViewText = (RecyclerView)findViewById(R.id.recycler_view_ondotask);
        layoutManagerText = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        provideTextAdapter = new ProvideTextTaskSuperAdapter(providerShowList);
        recyclerViewText.setLayoutManager(layoutManagerText);
        recyclerViewText.setAdapter(provideTextAdapter);
        Log.d("AICollector","preTextRecycler");
    }

    /**
     * 给图片任务配置recycler
     */
    private void prePicRecycler()throws NullListInAdapterException{
        recyclerViewPic = (RecyclerView)findViewById(R.id.recycler_view_ondotask);
        layoutManagerPic = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        providePicAdapter = new ProvidePictureTaskSuperAdapter(providerShowList);
        recyclerViewPic.setLayoutManager(layoutManagerPic);
        recyclerViewPic.setAdapter(providePicAdapter);
    }

    /**
     * 给音频任务配置recycler
     */
    private void preAudRecycler()throws NullListInAdapterException{
        recyclerViewAud = (RecyclerView)findViewById(R.id.recycler_view_ondotask);
        layoutManagerAud = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        provideAudAdapter = new ProvideAudioTaskSuperAdapter(providerShowList);
        recyclerViewAud.setLayoutManager(layoutManagerAud);
        recyclerViewAud.setAdapter(provideAudAdapter);
        Log.d("AICollector","preAudRecycler");
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
        if (ContextCompat.checkSelfPermission(DoTaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DoTaskActivity.this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WE_STORAGE_PER);
        }else if (ContextCompat.checkSelfPermission(DoTaskActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DoTaskActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_RE_STORAGE_PER);
        } else {
            Matisse.from(DoTaskActivity.this)
                    .choose(MimeType.allOf())//图片类型
                    .countable(true)//true:选中后显示数字;false:选中后显示对号
                    .maxSelectable(9)//可选的最大数
                    .capture(false)//选择照片时，是否显示拍照
                    .captureStrategy(new CaptureStrategy(true, "cn.aysst.www.aicollector.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                    .imageEngine(new GlideEngine())//图片加载引擎
                    .forResult(CHOOSE_PHOTO);//
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
//            Intent intent = new Intent("android.intent.action.GET_CONTENT");
//            intent.setType("audio/*");
//            startActivityForResult(intent,CHOOSE_AUDIO);
            new LFilePicker()
                    .withActivity(DoTaskActivity.this)
                    .withRequestCode(CHOOSE_AUDIO)
                    .withIconStyle(Constant.ICON_STYLE_YELLOW)
                    .withMutilyMode(true)
                    .withTitle("选择文件")
                    .withBackgroundColor("#FFFFFF")
                    .withTitleColor("#000000")
                    .withFileFilter(new String[]{".wav",".aif",".au",".mp3",".ram",".wma",".mmf",".amr",".aac",".flac"})
                    .withBackIcon(Constant.BACKICON_STYLEONE)
                    .start();
        }
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 输入文本
     */
    private void openInputText(){
        showInputTextDialog();
    }

    /**
     * 相应PopupMenu.OnMenuItemClickListener点击事件OnMenuItemClick
     * 打开视频
     */
    private void openTextFile(){
        if(ContextCompat.checkSelfPermission(DoTaskActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DoTaskActivity.this,new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WE_STORAGE_PER);
        }else {
            new LFilePicker()
                    .withActivity(DoTaskActivity.this)
                    .withRequestCode(CHOOSE_TEXT)
                    .withIconStyle(Constant.ICON_STYLE_YELLOW)
                    .withMutilyMode(true)
                    .withTitle("选择文件")
                    .withBackgroundColor("#FFFFFF")
                    .withTitleColor("#000000")
                    .withFileFilter(new String[]{".txt",".doc",".docx"})
                    .withBackIcon(Constant.BACKICON_STYLEONE)
                    .start();
        }
    }

    /**
     * 从file;///的Uri获得content://的Uri
     * @param fileUri
     * @return
     */
    private Uri getContentUriFromFileUri(Uri fileUri){
        File imageFile = new File(fileUri.getPath());
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/audio/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }

    }

    private void initProviderForShowList(){
        providerShowList.clear();
        for (ProvideForTask provideForTask : provideList) {
            ProviderForShow providerForShow = new ProviderForShow();
            String providerName = provideForTask.getProviderName();
            String provideUriString = null;
            List<String> stringList = new ArrayList<>();
//            stringList.clear();
            switch (MY_TASK_TYPE){
                case Task.TYPE_PICTURE:
                    provideUriString = provideForTask.getPictureUriStr();
                    break;
                case Task.TYPE_AUDIO:
                    provideUriString = provideForTask.getAudioUriStr();
                    break;
                case Task.TYPE_TEXT:
                    provideUriString = provideForTask.getTextUriStr();
                    break;
                default:
                    provideUriString = "出错！";
                    break;
            }
            if(showListContain(providerName)){
                int position = getPosition(providerName);
                stringList = providerShowList.get(position).getProvideUriList();
                stringList.add(provideUriString);
                providerShowList.get(position).setProvideUriList(stringList);
            }else {
                stringList.add(provideUriString);
                providerForShow.setProviderName(providerName);
                providerForShow.setProvideUriList(stringList);
                providerShowList.add(providerForShow);
            }
        }
    }

    private void provideListAdd(String string){
        ProvideForTask provideForTask = new ProvideForTask();
        switch (MY_TASK_TYPE){
            case Task.TYPE_PICTURE:
                provideForTask.setPictureUriStr(string);
                break;
            case Task.TYPE_AUDIO:
                provideForTask.setAudioUriStr(string);
                break;
            case Task.TYPE_TEXT:
                provideForTask.setTextUriStr(string);
                break;
            default:
                Toast.makeText(DoTaskActivity.this,"未知异常",Toast.LENGTH_SHORT).show();
                break;
        }
        provideList.add(provideForTask);
    }

    private void providerListAdd(List<String> stringList){
        for (String s : stringList) {
            provideListAdd(s);
        }
    }

    private void providerShowListAdd(String string){
        String providerName = new Random().nextInt(10)+"";
        if (showListContain(providerName)){
            int position = getPosition(providerName);
            List<String> stringList = providerShowList.get(position).getProvideUriList();
            stringList.add(string);
            providerShowList.get(position).setProvideUriList(stringList);
            try{
                switch (MY_TASK_TYPE){
                    case Task.TYPE_PICTURE:
                        prePicRecycler();break;
                    case Task.TYPE_AUDIO:
                        preAudRecycler();break;
                    case Task.TYPE_TEXT:
                        preTextRecycler();break;
                    default:
                        Toast.makeText(DoTaskActivity.this,"未知异常",Toast.LENGTH_SHORT);break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            ProviderForShow providerForShow = new ProviderForShow();
            providerForShow.setProviderName(providerName);
            List<String> stringList = new ArrayList<>();
            stringList.add(string);
            providerForShow.setProvideUriList(stringList);
            providerShowList.add(providerForShow);
            try{
                switch (MY_TASK_TYPE){
                    case Task.TYPE_PICTURE:
                        prePicRecycler();break;
                    case Task.TYPE_AUDIO:
                        preAudRecycler();break;
                    case Task.TYPE_TEXT:
                        preTextRecycler();break;
                    default:
                        Toast.makeText(DoTaskActivity.this,"未知异常",Toast.LENGTH_SHORT);break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void providerShowListAdd(List<String> list){
        String providerName = new Random().nextInt(10)+"";
        if (showListContain(providerName)){
            int position = getPosition(providerName);
            List<String> stringList = providerShowList.get(position).getProvideUriList();
            for (String s :list) {
                stringList.add(s);
            }
            providerShowList.get(position).setProvideUriList(stringList);

            try{
                switch (MY_TASK_TYPE){
                    case Task.TYPE_PICTURE:
                        prePicRecycler();break;
                    case Task.TYPE_AUDIO:
                        preAudRecycler();break;
                    case Task.TYPE_TEXT:
                        preTextRecycler();break;
                    default:
                        Toast.makeText(DoTaskActivity.this,"未知异常",Toast.LENGTH_SHORT);break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            ProviderForShow providerForShow = new ProviderForShow();
            providerForShow.setProviderName(providerName);
            providerForShow.setProvideUriList(list);
            providerShowList.add(providerForShow);

            try{
                switch (MY_TASK_TYPE){
                    case Task.TYPE_PICTURE:
                        prePicRecycler();break;
                    case Task.TYPE_AUDIO:
                        preAudRecycler();break;
                    case Task.TYPE_TEXT:
                        preTextRecycler();break;
                    default:
                        Toast.makeText(DoTaskActivity.this,"未知异常",Toast.LENGTH_SHORT);break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param providerName
     * @return
     */
    private boolean showListContain(String providerName){
        if (getPosition(providerName) != -1){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param providerName
     * @return
     */
    private int getPosition(String providerName){
        for(int i = 0 ; i < providerShowList.size() ; i++){
            if (providerName.equals(providerShowList.get(i).getProviderName())){
                return i;
            }
        }
        return -1;
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

    private void showInputTextDialog(){
        InputTextDialogFragment dialogFragment = new InputTextDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message","请输入文本");
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getSupportFragmentManager(),"InputTextDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(InputTextDialogFragment dialogFragment) {
        String providerName = new Random().nextInt(10)+"";
        if (showListContain(providerName)){
            int position = getPosition(providerName);
            List<String> stringList = providerShowList.get(position).getProvideUriList();
            stringList.add(dialogFragment.getmData());
            providerShowList.get(position).setProvideUriList(stringList);

//            int firstItemPostion = layoutManagerText.findFirstVisibleItemPosition();
//            int lastItemPostion = layoutManagerText.findLastVisibleItemPosition();
//            ProvideTextTaskSuperAdapter.ViewHolder holder = (ProvideTextTaskSuperAdapter.ViewHolder)getHolder(recyclerViewText,position);
//            if (holder != null){
//                holder.getmRecyclerView().getAdapter().notifyDataSetChanged();
//            }

            try {
                preTextRecycler();
            } catch (NullListInAdapterException e) {
                e.printStackTrace();
            }
        }else {
            ProviderForShow providerForShow = new ProviderForShow();
            providerForShow.setProviderName(providerName);
            List<String> stringList = new ArrayList<>();
            stringList.add(dialogFragment.getmData());
            providerForShow.setProvideUriList(stringList);
            providerShowList.add(providerForShow);
//            provideTextAdapter.notifyDataSetChanged();
            try {
                preTextRecycler();
            } catch (NullListInAdapterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDialogNegativeClick(InputTextDialogFragment dialogFragment) {

    }

    // 获取照片旋转的度数,
//                        int degree = getBitmapDegree(originalImagePath);
//                        String file = "";
//                        if (degree != 0) {
//                            bitmap = rotateBitmapByDegree(bitmap, degree);
//                            File filea = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "camera");
//                            file = filea.getAbsolutePath() + "/" + bitmap.toString().hashCode() + ".jpg";
//                        }

    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Log.d("tag", "rotate bitmap exception:" + e);
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("tag", "degree=" + degree);
        return degree;
    }

}
