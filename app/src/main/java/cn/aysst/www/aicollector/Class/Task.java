package cn.aysst.www.aicollector.Class;

/**
 * Created by 蒲公英之流 on 2019-02-02.
 */

public class Task {

    public static final int TYPE_PICTURE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;

    public static final int MY_TASK = 3;
    public static final int OTHER_TASK = 4;

    private String name;
    private String info;
    private int type;
    private int belong;
    private int reviewNum;
    private int providerNum;

    public Task(String name,String info,int type,int belong,int reviewNum,int providerNum){
        this.name = name;
        this.info = info;
        this.type = type;
        this.belong = belong;
        this.reviewNum = reviewNum;
        this.providerNum = providerNum;
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
}