package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.ProviderForShow;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.R;

/**
 * Created by 蒲公英之流 on 2019-02-25.
 */

public class ProvideTextTaskSuperAdapter extends RecyclerView.Adapter<ProvideTextTaskSuperAdapter.ViewHolder>{
    private Context mContext;
    private List<ProviderForShow> providerForShowList;
    public ProvideTextTaskSuperAdapter(List<ProviderForShow> providerForShowList){ this.providerForShowList = providerForShowList; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView mTextView;
        RecyclerView mRecyclerView;
        public ViewHolder(View itemView){
            super(itemView);
            mCardView = (CardView)itemView;
            mTextView = (TextView)(itemView.findViewById(R.id.provider_name_for_text));
            mRecyclerView = (RecyclerView)(itemView.findViewById(R.id.recycler_view_sub_ondotask_for_text));
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(itemView.getContext(),3);
            layoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(layoutManager);
        }

        public RecyclerView getmRecyclerView() {
            return mRecyclerView;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provide_texttask_item_super,parent,false);
        ProvideTextTaskSuperAdapter.ViewHolder holder = new ProvideTextTaskSuperAdapter.ViewHolder(view);

        holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(parent.getContext(),DividerItemDecoration.VERTICAL));
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProviderForShow providerForShow = providerForShowList.get(position);
        holder.mTextView.setText(providerForShow.getProviderName());
        if (holder.mRecyclerView.getAdapter() == null) {
            holder.mRecyclerView.setAdapter(new ProvideTextTaskSubAdapter(providerForShow.getProvideUriList()));
        }
    }

    @Override
    public int getItemCount() {
        return providerForShowList.size();
    }

    public class ProvideTextTaskSubAdapter extends RecyclerView.Adapter<ProvideTextTaskSuperAdapter.ProvideTextTaskSubAdapter.ViewHolder>{

        private Context mContext;
        private List<String> uriTextStrList;

        public ProvideTextTaskSubAdapter(List<String> uriTextStrList){ this.uriTextStrList = uriTextStrList; }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mImageView;
            TextView mTextView;
            public ViewHolder(View itemView){
                super(itemView);
                mImageView = (ImageView)(itemView.findViewById(R.id.provide_text_show));
                mTextView = (TextView)(itemView.findViewById(R.id.provide_text_uri_str));
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provide_texttask_item_sub,parent, false);
            final ProvideTextTaskSuperAdapter.ProvideTextTaskSubAdapter.ViewHolder holder = new ProvideTextTaskSuperAdapter.ProvideTextTaskSubAdapter.ViewHolder(view);

            holder.mImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(mContext,holder.mTextView.getText()+"长按事件触发",Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"点击事件触发",Toast.LENGTH_SHORT).show();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String uriTextString = uriTextStrList.get(position);
            holder.mTextView.setText(uriTextString);
        }

        @Override
        public int getItemCount() {
            return uriTextStrList.size();
        }
    }

}




















