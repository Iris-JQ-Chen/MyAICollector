package cn.aysst.www.aicollector.Class;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 蒲公英之流 on 2019-02-22.
 */

public class ProvideForTask implements Parcelable {

    private int taskType;
    private String taskName;
    private String pictureBitStr;
    private String videoUriStr;
    private String audioUriStr;
    private String providerName;

    public ProvideForTask(){}
    public ProvideForTask(int taskType,String taskName,String pictureBitStr,String videoUriStr,String audioUriStr,String providerName){
        this.taskType = taskType;
        this.taskName = taskName;
        this.pictureBitStr = pictureBitStr;
        this.videoUriStr = videoUriStr;
        this.audioUriStr = audioUriStr;
        this.providerName = providerName;
    }

    public int getTaskType() {
        return taskType;
    }
    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getPictureBitStr() {
        return pictureBitStr;
    }

    public void setPictureBitStr(String pictureBitStr) {
        this.pictureBitStr = pictureBitStr;
    }

    public String getAudioUriStr() {
        return audioUriStr;
    }
    public void setAudioUriStr(String audioUriStr) {
        this.audioUriStr = audioUriStr;
    }

    public String getVideoUriStr() {
        return videoUriStr;
    }
    public void setVideoUriStr(String videoUriStr) {
        this.videoUriStr = videoUriStr;
    }

    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskType);
        dest.writeString(taskName);
//        pictureBitmap.writeToParcel(dest,flags);
        dest.writeString(pictureBitStr);
        dest.writeString(videoUriStr);
        dest.writeString(audioUriStr);
        dest.writeString(providerName);
    }

    public static final Parcelable.Creator<ProvideForTask> CREATOR = new Creator<ProvideForTask>() {
        @Override
        public ProvideForTask createFromParcel(Parcel source) {
            ProvideForTask provideForTask = new ProvideForTask();
            provideForTask.setTaskType(source.readInt());
            provideForTask.setTaskName(source.readString());
//            provideForTask.setPictureBitmap(Bitmap.CREATOR.createFromParcel(source));
            provideForTask.setPictureBitStr(source.readString());
            provideForTask.setVideoUriStr(source.readString());
            provideForTask.setAudioUriStr(source.readString());
            provideForTask.setProviderName(source.readString());
            return provideForTask;
        }

        @Override
        public ProvideForTask[] newArray(int size) {
            return new ProvideForTask[size];
        }
    };
}
