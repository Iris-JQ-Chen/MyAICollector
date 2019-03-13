package cn.aysst.www.aicollector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.aysst.www.aicollector.Adapter.ProvideAudioTaskSuperAdapter;
import cn.aysst.www.aicollector.Adapter.ProvidePictureTaskSuperAdapter;
import cn.aysst.www.aicollector.Adapter.ProvideTextTaskSuperAdapter;
import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.ProviderForShow;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;

public class SeeTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SEE_MY_TASK = "see_my_task";

    private List<ProviderForShow> providerForShowList = new ArrayList<>();
    private List<ProvideForTask> provideForTaskList = new ArrayList<>();
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_task);

        Intent intent = getIntent();
        Task myTask = intent.getParcelableExtra(SEE_MY_TASK);
        provideForTaskList = myTask.getProvideList();
        if (provideForTaskList == null){
            provideForTaskList = new ArrayList<>();
        }

        findViewById(R.id.pull_up_onsee).setOnClickListener(this);

        ((TextView)findViewById(R.id.my_task_name)).setText(myTask.getName());
        ((TextView)findViewById(R.id.my_task_info)).setText(myTask.getInfo());
        ((TextView)findViewById(R.id.my_task_publisher)).setText(myTask.getPublishId()+"");
        try{
            switch (myTask.getType()){
                case Task.TYPE_PICTURE:
                    ((TextView)findViewById(R.id.my_task_type)).setText("图片");
                    prePicRecycler();
                    MY_TASK_TYPE = Task.TYPE_PICTURE;
                    break;
                case Task.TYPE_AUDIO:
                    ((TextView)findViewById(R.id.my_task_type)).setText("音频");
                    preAudRecycler();
                    MY_TASK_TYPE = Task.TYPE_AUDIO;
                    break;
                case Task.TYPE_TEXT:
                    ((TextView)findViewById(R.id.my_task_type)).setText("文本");
                    preTextRecycler();
                    MY_TASK_TYPE = Task.TYPE_TEXT;
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ((TextView)findViewById(R.id.my_task_gold)).setText(myTask.getGold()+"");

        initProviderForShowList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pull_up_onsee:
                backToTop();
                break;
            default:
                break;
        }
    }

    /**
     * 给视频任务配置recycler
     */
    private void preTextRecycler()throws NullListInAdapterException {
        recyclerViewText = (RecyclerView)findViewById(R.id.recycler_view_onseetask);
        layoutManagerText = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        provideTextAdapter = new ProvideTextTaskSuperAdapter(providerForShowList);
        recyclerViewText.setLayoutManager(layoutManagerText);
        recyclerViewText.setAdapter(provideTextAdapter);
        Log.d("AICollector","preTextRecycler");
    }

    /**
     * 给图片任务配置recycler
     */
    private void prePicRecycler()throws NullListInAdapterException{
        recyclerViewPic = (RecyclerView)findViewById(R.id.recycler_view_onseetask);
        layoutManagerPic = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        providePicAdapter = new ProvidePictureTaskSuperAdapter(providerForShowList);
        recyclerViewPic.setLayoutManager(layoutManagerPic);
        recyclerViewPic.setAdapter(providePicAdapter);
    }

    /**
     * 给音频任务配置recycler
     */
    private void preAudRecycler()throws NullListInAdapterException{
        recyclerViewAud = (RecyclerView)findViewById(R.id.recycler_view_onseetask);
        layoutManagerAud = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        provideAudAdapter = new ProvideAudioTaskSuperAdapter(providerForShowList);
        recyclerViewAud.setLayoutManager(layoutManagerAud);
        recyclerViewAud.setAdapter(provideAudAdapter);
        Log.d("AICollector","preAudRecycler");
    }

    private void initProviderForShowList(){
        providerForShowList.clear();
        for (ProvideForTask provideForTask : provideForTaskList) {
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
                stringList = providerForShowList.get(position).getProvideUriList();
                stringList.add(provideUriString);
                providerForShowList.get(position).setProvideUriList(stringList);
            }else {
                stringList.add(provideUriString);
                providerForShow.setProviderName(providerName);
                providerForShow.setProvideUriList(stringList);
                providerForShowList.add(providerForShow);
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
        for(int i = 0 ; i < providerForShowList.size() ; i++){
            if (providerName.equals(providerForShowList.get(i).getProviderName())){
                return i;
            }
        }
        return -1;
    }

    private void backToTop(){
        switch (MY_TASK_TYPE){
            case Task.TYPE_PICTURE:
                layoutManagerPic.smoothScrollToPosition(recyclerViewPic,null,0);
                return;
            case Task.TYPE_AUDIO:
                layoutManagerAud.smoothScrollToPosition(recyclerViewAud,null,0);
                return;
            case Task.TYPE_TEXT:
                layoutManagerText.smoothScrollToPosition(recyclerViewText,null,0);
                return;
            default:
                return;
        }
    }
}
