package cn.aysst.www.aicollector.fragment;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.R;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.Adapter.TaskAdapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class FragmentHome extends Fragment{
	private List<Task> taskList = new ArrayList<>();
	private TaskAdapter adapter;
	private GridLayoutManager layoutManager;
	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private FloatingActionButton floatingActionButton;

	public static FragmentHome newInstance() {
		FragmentHome fragment = new FragmentHome();                ;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initOtherTasks();
		View view = inflater.inflate(R.layout.activity_fragment_home,null);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_onhome);
		layoutManager = new GridLayoutManager(getActivity(),1);
		try {
			adapter = new TaskAdapter(taskList);
		}catch (Exception e){
			e.printStackTrace();
		}
		swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_onhome);
		floatingActionButton = (FloatingActionButton)view.findViewById(R.id.pull_up_onhome);

		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(layoutManager);

		swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshTask();
			}
		});

		floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				backToTop();
			}
		});

		return view;
	}

	private void initOtherTasks(){
		taskList.clear();
		for(int i = 0;i<50;i++){
			int type = new Random().nextInt(3);
			Task task = new Task("任务"+i,"任务"+i+"的信息",type,Task.OTHER_TASK,new Random().nextInt(50),new Random().nextInt(600));
			task.setPublishId(new Random().nextInt());
			task.setGold(new Random().nextDouble());
			task.setTime(getTime());
			switch (type){
				case Task.TYPE_PICTURE:
				    task.setProvideList(new ArrayList<ProvideForTask>());
//					task.setProvideList(getProvidePicList());
					break;
				case Task.TYPE_VIDEO:
					task.setProvideList(getProvideVidList());
					break;
				case Task.TYPE_AUDIO:
					task.setProvideList(getProvideAudList());
					break;
				default:
					break;
			}
			taskList.add(task);
		}
	}

	private void refreshTask(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//进行刷新任务消息
						Toast.makeText(getActivity(),"下拉刷新",Toast.LENGTH_SHORT).show();
						swipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}).start();
	}

	private void backToTop(){
		LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
		manager.smoothScrollToPosition(recyclerView,null,0);
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

	private List<ProvideForTask> getProvidePicList(){
		List<ProvideForTask> provideForTaskList = new ArrayList<>();
		for(int i = 0;i<4;i++){
			ProvideForTask provideForTask = new ProvideForTask();
			provideForTask.setPictureBitStr(bitmapToString(BitmapFactory.decodeResource(getResources(),R.drawable.apple)));
			provideForTaskList.add(provideForTask);
		}
		return provideForTaskList;
	}

	private List<ProvideForTask> getProvideVidList(){
		List<ProvideForTask> provideForTaskList = new ArrayList<>();
		for(int i = 0;i<3;i++){
			ProvideForTask provideForTask = new ProvideForTask();
			provideForTask.setVideoUriStr("videoUriStringTEST");
			provideForTaskList.add(provideForTask);
		}
		return provideForTaskList;
	}

	private List<ProvideForTask> getProvideAudList(){
		List<ProvideForTask> provideForTaskList = new ArrayList<>();
		for(int i = 0;i<3;i++){
			ProvideForTask provideForTask = new ProvideForTask();
			provideForTask.setAudioUriStr("audioUriStringTEST");
			provideForTaskList.add(provideForTask);
		}
		return provideForTaskList;
	}

	/**
	 * 图片转换成base64字符串
	 *
	 * @param bitmap
	 * @return
	 */
	public static String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imgBytes = baos.toByteArray();// 转为byte数组
		return Base64.encodeToString(imgBytes, Base64.DEFAULT);
	}

}
