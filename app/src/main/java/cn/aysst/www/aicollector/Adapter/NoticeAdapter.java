package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.aysst.www.aicollector.Class.Notice;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.R;

/**
 * Created by 蒲公英之流 on 2019-02-05.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
    private Context context;
    private List<Notice> noticeList;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView noticeTitle;
        TextView noticeContent;
        public ViewHolder(View view){
            super(view);
            noticeTitle = (TextView)view.findViewById(R.id.notice_title);
            noticeContent = (TextView)view.findViewById(R.id.notice_content);
        }
    }

    public NoticeAdapter (List<Notice> noticeList)throws NullListInAdapterException{
        if (noticeList == null){
            throw new NullListInAdapterException("NoticeAdapter构造时被传入空列表");
        }
        this.noticeList = noticeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.notice_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice notice = noticeList.get(position);
        holder.noticeTitle.setText(notice.getTitle());
        holder.noticeContent.setText(notice.getContent());
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }
}
