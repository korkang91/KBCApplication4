package com.kangbc.kbcapplication4.Data;

/**
 * Created by mac on 2017. 6. 26..
 */

public class Info {
    Integer faceCount;
    Size size;

    public Integer getFacecount() {
        return faceCount;
    }

    public Size getSize() {
        return size;
    }

    public class Size {
        Integer width;
        Integer height;

        public Integer getHeight() {
            return height;
        }

        public Integer getWidth() {
            return width;
        }
    }
}
