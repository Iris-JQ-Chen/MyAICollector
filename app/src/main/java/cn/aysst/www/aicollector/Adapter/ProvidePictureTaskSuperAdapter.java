package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.Class.ProviderForShow;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.R;

/**
 * Created by 蒲公英之流 on 2019-02-22.
 */

public class ProvidePictureTaskSuperAdapter extends RecyclerView.Adapter<ProvidePictureTaskSuperAdapter.ViewHolder> {
    private Context mContext;
    private List<ProviderForShow> providerForShowList;
    public ProvidePictureTaskSuperAdapter (List<ProviderForShow> providerForShowList) { this.providerForShowList = providerForShowList; }

    class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        TextView mTextView;
        RecyclerView mRecyclerView;
        public ViewHolder(View itemView){
            super(itemView);
            mCardView = (CardView)itemView;
            mTextView = (TextView)(itemView.findViewById(R.id.provider_name_for_pic));
            mRecyclerView = (RecyclerView)(itemView.findViewById(R.id.recycler_view_sub_ondotask_for_pic));
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provide_picturetask_item_super,parent,false);
        ProvidePictureTaskSuperAdapter.ViewHolder holder = new ProvidePictureTaskSuperAdapter.ViewHolder(view);

        holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(parent.getContext(),DividerItemDecoration.VERTICAL));
        holder.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProviderForShow providerForShow = providerForShowList.get(position);
        holder.mTextView.setText(providerForShow.getProviderName());
        if (holder.mRecyclerView.getAdapter() == null){
            holder.mRecyclerView.setAdapter(new ProvidePictureTaskSubAdapter(providerForShow.getProvideUriList()));
        }
    }

    @Override
    public int getItemCount() {
        return providerForShowList.size();
    }


    public class ProvidePictureTaskSubAdapter extends RecyclerView.Adapter<ProvidePictureTaskSuperAdapter.ProvidePictureTaskSubAdapter.ViewHolder> {

        private Context mContext;
        private List<String> uriPicStrList;

        public ProvidePictureTaskSubAdapter (List<String> uriPicStrList){ this.uriPicStrList = uriPicStrList; }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView mImageView;
            TextView mTextView;
            public ViewHolder(View itemView){
                super(itemView);
                mImageView = (ImageView)(itemView.findViewById(R.id.provide_picture));
                mTextView = (TextView)(itemView.findViewById(R.id.provide_picture_name));
            }
        }

        @Override
        public ProvidePictureTaskSuperAdapter.ProvidePictureTaskSubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext == null){
                this.mContext = parent.getContext();
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provide_picturetask_item_sub,parent,false);
            ProvidePictureTaskSuperAdapter.ProvidePictureTaskSubAdapter.ViewHolder holder = new ProvidePictureTaskSuperAdapter.ProvidePictureTaskSubAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ProvidePictureTaskSuperAdapter.ProvidePictureTaskSubAdapter.ViewHolder holder, int position) {
            String uriPicString = uriPicStrList.get(position);
            Uri uri = Uri.parse(uriPicString);

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.mContext.getContentResolver().openInputStream(uri));
                holder.mImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.mTextView.setText(uriPicString);
            holder.mTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        }

        @Override
        public int getItemCount() {
            return uriPicStrList.size();
        }
    }

}





















