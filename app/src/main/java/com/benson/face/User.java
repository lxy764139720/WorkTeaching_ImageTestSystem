package com.benson.face;

/**
 * Created by sumixer01 on 2018/4/15.
 */

public class User {

    public enum Complexion {
        BLACK, WHITE, WHEAT, YELLOW
    }

    public enum FaceType {
        FANG, CHANG, LING, EDAN, YUAN
    }

    public enum BodyType {
        A, H, O, X, Y
    }

    public Complexion complexion = null;
    public FaceType facetype;
    public BodyType bodyType;
    public String name;
    public String gender;
    public int height;//身高
    public int shoulder;//肩围
    public int hip;//臀围
    public int waist;//腰围
    public int weight;//体重

    public void bodyType() {
        float a = shoulder;
        float b = hip;
        float c = waist;

        if (a > b) {
            bodyType = BodyType.Y;
        } else if (a < b) {
            bodyType = BodyType.A;
        } else {  //a=b
            float whRatio = c / b;  //腰臀比
            if (whRatio < 0.85) {
                bodyType = BodyType.X;
            } else if (whRatio < 1 && whRatio >= 0.85) {
                bodyType = BodyType.H;
            } else {
                bodyType = BodyType.O;
            }
        }
    }
}
