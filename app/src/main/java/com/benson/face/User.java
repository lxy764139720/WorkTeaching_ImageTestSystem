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
    public String number;
    public String height;
}
