package cn.aysst.www.aicollector.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.aysst.www.aicollector.Class.ProvideForTask;
import cn.aysst.www.aicollector.CustomException.NullListInAdapterException;
import cn.aysst.www.aicollector.R;

/**
 * Created by 蒲公英之流 on 2019-02-22.
 */

public class ProvidePictureTaskAdapter extends RecyclerView.Adapter<ProvidePictureTaskAdapter.ViewHolder> {

    private Context context;
    private List<ProvideForTask> provideForTaskList;

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView providePictureImageView;
        public ViewHolder(View itemView){
            super(itemView);
            providePictureImageView = (ImageView)(itemView.findViewById(R.id.provide_picture));
        }
    }

    public ProvidePictureTaskAdapter(List<ProvideForTask> provideForTaskList) throws NullListInAdapterException{
        if (provideForTaskList == null){
            throw new NullListInAdapterException("ProvidePictureTaskAdapter构造时被传入空列表");
        }
        this.provideForTaskList = provideForTaskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(this.context == null){
            this.context = parent.getContext();
        }
        View view = LayoutInflater.from(this.context).inflate(R.layout.provide_picturetask_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProvideForTask provideForTask = provideForTaskList.get(position);
        holder.providePictureImageView.setImageBitmap(stringToBitmap(provideForTask.getPictureBitStr()));
    }

    @Override
    public int getItemCount() {
        return provideForTaskList.size();
    }

    /**
     * Base64字符串转换成图片
     *
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
