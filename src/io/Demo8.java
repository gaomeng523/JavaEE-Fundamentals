package io;

import java.io.File;

public class Demo8 {
    public static void main(String[] args) {
        File source = new File("d:/test.txt");
        File destination = new File("d:/test2.txt");
        source.renameTo(destination);
    }
}
