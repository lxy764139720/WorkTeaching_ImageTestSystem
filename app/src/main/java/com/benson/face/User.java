package com.benson.face;

/**
 * Created by sumixer01 on 2018/4/15.
 */

public class User {

    public enum Complexion {
        BLACK, WHITE, WHEAT, YELLOW
    }

    public enum FaceType {
        FANG, CHANG, LING, EDAN, YUAN,
    }

    public Complexion complexion;
    public FaceType facetype;
    public String name;
    public String gender;
    public int height;//身高
    public int shoulder;//肩围
    public int hip;//臀围
    public int waist;//腰围
    public int weight;//体重
}
