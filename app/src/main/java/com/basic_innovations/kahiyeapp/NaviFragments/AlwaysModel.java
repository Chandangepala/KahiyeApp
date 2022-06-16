package com.basic_innovations.kahiyeapp.NaviFragments;

public class AlwaysModel {
    String alwName;
    String alwTime;
    String alwImg;
    int alwProfImg;

    public AlwaysModel() {
    }

    public AlwaysModel(String alwName, String alwTime, String alwImg, int alwProfImg) {
        this.alwName = alwName;
        this.alwTime = alwTime;
        this.alwImg = alwImg;
        this.alwProfImg = alwProfImg;
    }

    public String getAlwName() {
        return alwName;
    }

    public void setAlwName(String alwName) {
        this.alwName = alwName;
    }

    public String getAlwTime() {
        return alwTime;
    }

    public void setAlwTime(String alwTime) {
        this.alwTime = alwTime;
    }

    public String getAlwImg() {
        return alwImg;
    }

    public void setAlwImg(String alwImg) {
        this.alwImg = alwImg;
    }

    public int getAlwProfImg() {
        return alwProfImg;
    }

    public void setAlwProfImg(int alwProfImg) {
        this.alwProfImg = alwProfImg;
    }
}
