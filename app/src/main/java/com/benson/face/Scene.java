package com.benson.face;

public class Scene {
    //描述
    private String des;
    //图片资源 ID
    private int imgId;

    public Scene(String des, int imgId) {
        this.des = des;
        this.imgId = imgId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
