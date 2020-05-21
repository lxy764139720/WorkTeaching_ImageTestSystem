package com.benson.face;

import java.io.EOFException;

/**
 * Created by Administrator on 2016-11-06.
 */

public class FaceppDetect1 {
    public interface CallBack {
        void success(String result);

        void error(EOFException exception);
    }

    public static void detect(final byte[] bm, final CallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                //创建请求


                byte[] arrays = bm;
                FacePlus1 face = new FacePlus1();

                String str;
                str = null;
                try {
                    str = face.main(arrays);
                    // Log.e("TAG",str);
                } catch (Exception e) {

                }


                if (callBack != null) {
                    callBack.success(str);

                }

            }
        }).start();
    }
}
