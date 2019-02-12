package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.aysst.www.aicollector.R;
import cn.aysst.www.aicollector.Class.Task;

/**
 * Created by 蒲公英之流 on 2019-02-03.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView taskNameText;
        Button taskTypeButton;
        TextView taskInfoText;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            taskNameText = (TextView)view.findViewById(R.id.task_name);
            taskTypeButton = (Button)view.findViewById(R.id.task_type);
            taskInfoText = (TextView)view.findViewById(R.id.task_info);
        }
    }

    public TaskAdapter(List<Task> taskList){
        this.taskList = taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.task_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskNameText.setText(task.getName());
        holder.taskNameText.getPaint().setFakeBoldText(true);
        holder.taskInfoText.setText(task.getInfo());
        switch (task.getType()){
            case Task.TYPE_PICTURE:
                holder.taskTypeButton.setText("图片");
                break;
            case Task.TYPE_AUDIO:
                holder.taskTypeButton.setText("音频");
                break;
            case Task.TYPE_VIDEO:
                holder.taskTypeButton.setText("视频");
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
