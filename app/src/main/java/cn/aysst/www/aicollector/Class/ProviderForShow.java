package cn.aysst.www.aicollector.Class;

import java.util.List;

/**
 * Created by 蒲公英之流 on 2019-03-04.
 */

public class ProviderForShow {

    private String providerName;
    private List<String> provideUriList;

    public ProviderForShow(){}
    public ProviderForShow(String providerName, List<String> provideUriList){
        this.providerName = providerName;
        this.provideUriList = provideUriList;
    }

    public String getProviderName() {
        return providerName;
    }
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public List<String> getProvideUriList() {
        return provideUriList;
    }
    public void setProvideUriList(List<String> provideUriList) {
        this.provideUriList = provideUriList;
    }

    private int getLength(){
        return provideUriList.size();
    }
}
