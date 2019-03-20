package cn.aysst.www.aicollector.fragment;
import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.R;
import cn.aysst.www.aicollector.ReleaseTaskActivity;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.Adapter.TaskAdapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class FragmentRelease extends Fragment{
	private List<Task> taskList = new ArrayList<>();
	private TaskAdapter adapter;
	private GridLayoutManager layoutManager;
	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private FloatingActionButton floatingActionButton;

	public static FragmentRelease newInstance() {
		FragmentRelease fragment = new FragmentRelease();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initMyTask();
		View view = inflater.inflate(R.layout.activity_fragment_release, null);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_onrelease);
		try {
			adapter = new TaskAdapter(taskList);
		}catch (Exception e){
			e.printStackTrace();
		}
		layoutManager = new GridLayoutManager(getActivity(),1);
		swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_onrelease);
		floatingActionButton = (FloatingActionButton)view.findViewById(R.id.release_write_onrelease);

		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
		recyclerView.setItemAnimator(new DefaultItemAnimator());

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
				ReleaseTask();
			}
		});

		return view;
	}

	private void initMyTask(){
		taskList.clear();
		for(int i = 0;i<50;i++){
			int type = new Random().nextInt(3);
			Task task = new Task("我的任务"+i,"我的任务"+i+"的信息",type,Task.MY_TASK,new Random().nextInt(50),new Random().nextInt(600));
			task.setPublishId(new Random().nextInt());
			task.setGold(new Random().nextDouble());
			task.setTime(getTime());
			switch (type){
				case Task.TYPE_PICTURE:
					task.setProvideList(getProvidePicList());break;
				case Task.TYPE_TEXT:
					task.setProvideList(getProvideTextList());break;
				case Task.TYPE_AUDIO:
					task.setProvideList(getProvideAudList());break;
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
//						下拉刷新
						Toast.makeText(getActivity(),"下拉刷新我发布的任务",Toast.LENGTH_SHORT).show();
						swipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}).start();
	}

	private void ReleaseTask(){
		Intent intent = new Intent(getActivity(), ReleaseTaskActivity.class);
		startActivity(intent);
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
		for(int i = 0;i<14;i++){
			ProvideForTask provideForTask = new ProvideForTask();
			provideForTask.setPictureUriStr(getUriFromDrawableRes(getActivity(),R.drawable.apple).toString());
			provideForTask.setProviderName(new Random().nextInt(5)+"");
			provideForTask.setTaskType(Task.TYPE_PICTURE);
			provideForTaskList.add(provideForTask);
		}
		return provideForTaskList;
	}

	private List<ProvideForTask> getProvideTextList(){
		List<ProvideForTask> provideForTaskList = new ArrayList<>();
		for(int i = 0;i<14;i++){
			ProvideForTask provideForTask = new ProvideForTask();
//			provideForTask.setTextUriStr("textUriStringTEST"+i);
			provideForTask.setTextUriStr(getAssetsTextUri());
			provideForTask.setProviderName(new Random().nextInt(5)+"");
			provideForTask.setTaskType(Task.TYPE_TEXT);
			provideForTaskList.add(provideForTask);
		}
		return provideForTaskList;
	}

	private List<ProvideForTask> getProvideAudList(){
		List<ProvideForTask> provideForTaskList = new ArrayList<>();
		for(int i = 0;i<14;i++){
			ProvideForTask provideForTask = new ProvideForTask();
//			provideForTask.setAudioUriStr("audioUriStringTEST"+i);
			provideForTask.setAudioUriStr(getAssetsAudUri());
			provideForTask.setProviderName(new Random().nextInt(5)+"");
			provideForTask.setTaskType(Task.TYPE_AUDIO);
			provideForTaskList.add(provideForTask);
		}
		return provideForTaskList;
	}

	/**
	 * 得到资源文件中图片的Uri
	 * @param context 上下文对象
	 * @param id 资源id
	 * @return Uri
	 */
	private Uri getUriFromDrawableRes(Context context, int id) {
		Resources resources = context.getResources();
		String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				+ resources.getResourcePackageName(id) + "/"
				+ resources.getResourceTypeName(id) + "/"
				+ resources.getResourceEntryName(id);
		return Uri.parse(path);
	}

	private String getAssetsTextUri(){
		int i = new Random().nextInt(4);
		switch (i){
			case 0:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.t1).toString();
			case 1:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.t2).toString();
			case 2:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.t3).toString();
			case 3:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.txt44).toString();
			default:
				return null;
		}
	}

	private String getAssetsAudUri(){
		int i = new Random().nextInt(5);
		switch (i){
			case 0:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.a1).toString();
			case 1:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.a2).toString();
			case 2:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.a3).toString();
			case 3:
				return Uri.parse("android.resource://"+getContext().getPackageName()+"/"+ R.raw.mp344).toString();
			default:
				return null;
		}
	}
}
