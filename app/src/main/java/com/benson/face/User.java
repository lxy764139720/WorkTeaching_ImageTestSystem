package com.benson.face;

/**
 * Created by sumixer01 on 2018/4/15.
 */

public class User {

    public enum Complexion {
        BLACK("black"), WHITE("white"), WHEAT("wheat"), YELLOW("yellow");
        private String complexion;

        Complexion(String complextion) {
            this.complexion = complextion;
        }

        public String getComplexion() {
            return complexion;
        }
    }

    public enum FaceType {
        FANG, CHANG, LING, EDAN, YUAN
    }

    public enum BodyType {
        A("A"), H("H"), O("O"), X("X"), Y("Y");
        String bodyType;

        BodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public String getBodyType() {
            return bodyType;
        }
    }

    public Complexion complexion = null;
    public FaceType facetype;
    public BodyType bodyType;
    public String name;
    public String gender;
    public float height;//身高
    public float shoulder;//肩围
    public float hip;//臀围
    public float waist;//腰围
    public float weight;//体重

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
