package cn.aysst.www.aicollector.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蒲公英之流 on 2019-02-02.
 */

public class Task implements Parcelable {

    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_AUDIO = 2;

    public static final int MY_TASK = 3;
    public static final int OTHER_TASK = 4;

    private String name;
    private String info;
    private int type;
    private int belong;
    private int publishId;
    private double gold;
    private String time;
    private int reviewNum;
    private int providerNum;
    private List<ProvideForTask> provideList;

    public Task(String name,String info,int type,int belong,int reviewNum,int providerNum){
        this.name = name;
        this.info = info;
        this.type = type;
        this.belong = belong;
        this.reviewNum = reviewNum;
        this.providerNum = providerNum;
        provideList = new ArrayList<>();
    }

    public Task(){
        provideList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getBelong() {
        return belong;
    }
    public void setBelong(int belong) {
        this.belong = belong;
    }

    public int getPublishId() {
        return publishId;
    }
    public void setPublishId(int publishId) {
        this.publishId = publishId;
    }

    public double getGold() {
        return gold;
    }
    public void setGold(double gold) {
        this.gold = gold;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public int getReviewNum() {
        return reviewNum;
    }
    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public int getProviderNum() {
        return providerNum;
    }
    public void setProviderNum(int providerNum) {
        this.providerNum = providerNum;
    }

    public List<ProvideForTask> getProvideList() {
        return provideList;
    }
    public void setProvideList(List<ProvideForTask> provideList) {
        this.provideList = provideList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(TYPE_PICTURE);
//        dest.writeInt(TYPE_VIDEO);
//        dest.writeInt(TYPE_AUDIO);
//        dest.writeInt(MY_TASK);
//        dest.writeInt(OTHER_TASK);
        dest.writeString(name);
        dest.writeString(info);
        dest.writeInt(type);
        dest.writeInt(belong);
        dest.writeInt(publishId);
        dest.writeDouble(gold);
        dest.writeString(time);
        dest.writeInt(reviewNum);
        dest.writeInt(providerNum);
        dest.writeList(provideList);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>(){
        @Override
        public Task createFromParcel(Parcel source) {
            Task task = new Task();
            task.setName(source.readString());
            task.setInfo(source.readString());
            task.setType(source.readInt());
            task.setBelong(source.readInt());
            task.setPublishId(source.readInt());
            task.setGold(source.readDouble());
            task.setTime(source.readString());
            task.setReviewNum(source.readInt());
            task.setProviderNum(source.readInt());
            task.setProvideList(source.readArrayList(ProvideForTask.class.getClassLoader()));
            return task;
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
