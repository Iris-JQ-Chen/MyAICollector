package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SizeF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.Task;
import cn.aysst.www.aicollector.R;
import cn.aysst.www.aicollector.ShowAudioActivity;
import cn.aysst.www.aicollector.ShowPictureActivity;
import cn.aysst.www.aicollector.ShowTextActivity;

/**
 * Created by 蒲公英之流 on 2019-03-19.
 */

public class ProvideOnSeeTaskAdapter extends RecyclerView.Adapter<ProvideOnSeeTaskAdapter.ViewHolder> {
    private Context mContext;
    private List<ProvideForTask> provideForTaskList;

    public ProvideOnSeeTaskAdapter (List<ProvideForTask> provideForTaskList){ this.provideForTaskList = provideForTaskList; }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView mTextViewName;
        TextView mTextViewID;
        TextView mTextViewTime;
        public ViewHolder(View itemView){
            super(itemView);
            mCardView = (CardView) itemView;
            mTextViewName = (TextView)itemView.findViewById(R.id.provider_name_for_test);
            mTextViewID = (TextView)itemView.findViewById(R.id.provide_info_for_test);
            mTextViewTime = (TextView)itemView.findViewById(R.id.provide_time_for_test);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.answer_item,parent,false);
        ProvideOnSeeTaskAdapter.ViewHolder holder = new ProvideOnSeeTaskAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ProvideForTask provideForTask = provideForTaskList.get(position);
        holder.mTextViewName.setText(provideForTask.getProviderName());
        holder.mTextViewID.setText(new StringBuffer(provideForTask.getProvideID()).toString());
        holder.mTextViewTime.setText(provideForTask.getProvideTime());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProvideForTask provideForTask1 = provideForTaskList.get(position);
                if (mContext != null){
                    switch (provideForTask1.getTaskType()){
                        case Task.TYPE_PICTURE:
                            Intent intentPic = new Intent(mContext, ShowPictureActivity.class);
                            intentPic.putExtra(ShowPictureActivity.SHOW_PICTURE_AC,provideForTask1);
                            mContext.startActivity(intentPic);
                            break;
                        case Task.TYPE_AUDIO:
                            Intent intentAud = new Intent(mContext, ShowAudioActivity.class);
                            intentAud.putExtra(ShowAudioActivity.SHOW_AUDIO_AC,provideForTask1);
                            mContext.startActivity(intentAud);
                            break;
                        case Task.TYPE_TEXT:
                            Intent intentText = new Intent(mContext, ShowTextActivity.class);
                            intentText.putExtra(ShowTextActivity.SHOW_TEXT_AC,provideForTask1);
                            mContext.startActivity(intentText);
                            break;

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return provideForTaskList.size();
    }
}
