package io;

import java.io.File;

public class Demo6 {
    public static void main(String[] args) {
        File file = new File("d:/");
        File[] files = file.listFiles();
        if(files == null) {
            System.out.println("空目录");
            return ;
        }
        for(File f : files) {
            System.out.println(f.getName());
        }
    }
}
