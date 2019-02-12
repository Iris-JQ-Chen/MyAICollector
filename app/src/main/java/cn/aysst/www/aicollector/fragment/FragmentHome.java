package cn.aysst.www.aicollector.fragment;

import cn.aysst.www.aicollector.R;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.Adapter.TaskAdapter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
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
		adapter = new TaskAdapter(taskList);
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
			Random random = new Random();
			taskList.add(new Task("任务"+i,"任务"+i+"的信息",random.nextInt(3),Task.OTHER_TASK));
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
}
