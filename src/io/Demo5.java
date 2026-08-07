package io;

import java.io.File;

public class Demo5 {
    public static void main(String[] args) {
        File file = new File("d:/");
        String[] fileNames = file.list();
        if(fileNames == null){
            System.out.println("空目录");
            return;
        }
        for(String fileName : fileNames){
            System.out.println(fileName);
        }
    }
}
