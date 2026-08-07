package io;

import java.io.File;

public class Demo7 {
    public static void main(String[] args) {
        File file = new File("./aaa");
        File file1 = new File("./aaa/bbb/ccc");
        boolean ret = file.mkdir();
        boolean ret1 = file1.mkdirs();
        System.out.println(file.isDirectory());

    }

}
