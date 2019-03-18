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

import java.util.List;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.ProviderForShow;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.R;

/**
 * Created by 蒲公英之流 on 2019-02-25.
 */

public class ProvideAudioTaskSuperAdapter extends RecyclerView.Adapter<ProvideAudioTaskSuperAdapter.ViewHolder>{
    private Context mContext;
    private List<ProviderForShow> providerForShowList;
    public ProvideAudioTaskSuperAdapter (List<ProviderForShow> providerForShowList){ this.providerForShowList = providerForShowList; }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView mTextView;
        RecyclerView mRecyclerView;
        public ViewHolder(View itemView){
            super(itemView);
            mCardView = (CardView)itemView;
            mTextView = (TextView)(itemView.findViewById(R.id.provider_name_for_audio));
            mRecyclerView = (RecyclerView)(itemView.findViewById(R.id.recycler_view_sub_ondotask_for_audio));
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(itemView.getContext(),3);
            layoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext != null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provide_audiotask_item_super,parent,false);
        ProvideAudioTaskSuperAdapter.ViewHolder holder = new ProvideAudioTaskSuperAdapter.ViewHolder(view);

        holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(parent.getContext(),DividerItemDecoration.VERTICAL));
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProviderForShow providerForShow = providerForShowList.get(position);
        holder.mTextView.setText(providerForShow.getProviderName());
        if (holder.mRecyclerView.getAdapter() == null){
            holder.mRecyclerView.setAdapter(new ProvideAudioTaskSubAdapter(providerForShow.getProvideUriList()));
        }
    }

    @Override
    public int getItemCount() {
        return providerForShowList.size();
    }


    public class ProvideAudioTaskSubAdapter extends RecyclerView.Adapter<ProvideAudioTaskSuperAdapter.ProvideAudioTaskSubAdapter.ViewHolder> {

        private Context mContext;
        private List<String> uriStringList;

        public ProvideAudioTaskSubAdapter(List<String> uriStringList){ this.uriStringList = uriStringList; }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mImageView;
            TextView mTextView;
            public ViewHolder(View itemView){
                super(itemView);
                mImageView = (ImageView)(itemView.findViewById(R.id.provide_audio_show));
                mTextView = (TextView)(itemView.findViewById(R.id.provide_audio_uri_str));
            }
        }

        @Override
        public ProvideAudioTaskSuperAdapter.ProvideAudioTaskSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext == null){
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provide_audiotask_item_sub,parent,false);
            ProvideAudioTaskSuperAdapter.ProvideAudioTaskSubAdapter.ViewHolder holder = new ProvideAudioTaskSuperAdapter.ProvideAudioTaskSubAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ProvideAudioTaskSuperAdapter.ProvideAudioTaskSubAdapter.ViewHolder holder, int position) {
            String uriString = uriStringList.get(position);
            holder.mTextView.setText(uriString);
        }

        @Override
        public int getItemCount() {
            return uriStringList.size();
        }
    }

}























