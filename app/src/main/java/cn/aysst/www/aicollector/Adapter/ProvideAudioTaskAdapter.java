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

public class ProvideAudioTaskAdapter extends RecyclerView.Adapter<ProvideAudioTaskAdapter.ViewHolder> {

    private Context context;
    private List<ProvideForTask> provideForTaskList;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView audUriStrText;
        public ViewHolder (View itemView){
            super(itemView);
            audUriStrText = (TextView)(itemView.findViewById(R.id.provide_audio_uri_str));
        }
    }

    public ProvideAudioTaskAdapter (List<ProvideForTask> provideForTaskList) throws NullListInAdapterException{
        if (provideForTaskList == null){
            throw new NullListInAdapterException("ProvideAudioTaskAdapter构造时被传入空列表");
        }
        this.provideForTaskList = provideForTaskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.context == null){
            this.context = parent.getContext();
        }
        View view = LayoutInflater.from(this.context).inflate(R.layout.provide_audiotask_item,parent,false);
        ProvideAudioTaskAdapter.ViewHolder holder = new ProvideAudioTaskAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProvideForTask provideForTask = provideForTaskList.get(position);
        holder.audUriStrText.setText(provideForTask.getAudioUriStr());
    }

    @Override
    public int getItemCount() {
        return provideForTaskList.size();
    }
}
