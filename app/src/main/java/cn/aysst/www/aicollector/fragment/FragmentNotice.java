package cn.aysst.www.aicollector.fragment;
import cn.aysst.www.aicollector.Adapter.NoticeAdapter;
import cn.aysst.www.aicollector.Class.Notice;
import cn.aysst.www.aicollector.R;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FragmentNotice extends Fragment{
	private List<Notice> noticeList = new ArrayList<>();
	private RecyclerView recyclerView;
	private NoticeAdapter noticeAdapter;
	private GridLayoutManager layoutManager;
	private SwipeRefreshLayout swipeRefreshLayout;
	private FloatingActionButton floatingActionButton;

	public static FragmentNotice newInstance() {
		FragmentNotice fragment = new FragmentNotice();                ;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initNotices();
		View view = inflater.inflate(R.layout.activity_fragment_notice, null);
		recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_onnotice);
		try{
			noticeAdapter = new NoticeAdapter(noticeList);
		}catch (Exception e){
			e.printStackTrace();
		}
		layoutManager = new GridLayoutManager(getActivity(),1);
		swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh_onnotice);
		floatingActionButton = (FloatingActionButton)view.findViewById(R.id.pull_up_onnotice);

		recyclerView.setAdapter(noticeAdapter);
		recyclerView.setLayoutManager(layoutManager);

		swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshNotice();
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

	private void initNotices(){
		noticeList.clear();
		for(int i = 0;i < 50;i++){
			noticeList.add(new Notice("通知"+i,getRandomLengthContent("通知内容"+i)));
		}
	}

	private String getRandomLengthContent(String content){
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		int length = random.nextInt(20);
		for(int i = 0;i < length;i++){
			builder.append(content);
		}
		return builder.toString();
	}

	private void refreshNotice(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//刷新语法
						Toast.makeText(getContext(),"下拉刷新",Toast.LENGTH_SHORT).show();
						swipeRefreshLayout.setRefreshing(false);
					}
				});
			}
		}).start();
	}

	private void backToTop(){
		LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
		layoutManager.smoothScrollToPosition(recyclerView,null,0);
	}
}
