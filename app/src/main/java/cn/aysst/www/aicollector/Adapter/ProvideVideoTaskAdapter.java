package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.R;

/**
 * Created by 蒲公英之流 on 2019-02-25.
 */

public class ProvideVideoTaskAdapter extends RecyclerView.Adapter<ProvideVideoTaskAdapter.ViewHolder> {

    private Context context;
    private List<ProvideForTask> provideForTaskList;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView vidUriStrText;
        public ViewHolder(View itemView){
            super(itemView);
            vidUriStrText = (TextView)(itemView.findViewById(R.id.provide_video_uri_str));
        }
    }

    public ProvideVideoTaskAdapter(List<ProvideForTask> provideForTaskList) throws NullListInAdapterException {
        if (provideForTaskList == null){
            throw new NullListInAdapterException("ProvideVideoTaskAdapter构造时被传入空列表");
        }
        this.provideForTaskList = provideForTaskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            this.context = parent.getContext();
        }
        View view = LayoutInflater.from(this.context).inflate(R.layout.provide_videotask_item,parent,false);
        ProvideVideoTaskAdapter.ViewHolder holder = new ProvideVideoTaskAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProvideForTask provideForTask = provideForTaskList.get(position);
        holder.vidUriStrText.setText(provideForTask.getVideoUriStr());
    }

    @Override
    public int getItemCount() {
        return provideForTaskList.size();
    }
}
