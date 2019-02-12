package cn.aysst.www.aicollector.Class;

/**
 * Created by 蒲公英之流 on 2019-02-05.
 */

public class Notice {

    private String title;
    private String content;

    public Notice(String title,String content){
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }
}
