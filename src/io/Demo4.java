package io;

import java.io.File;
import java.io.IOException;

public class Demo4 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:/test.txt");
        System.out.println(file.createNewFile());
        System.out.println(file.delete());
        System.out.println(file.createNewFile());
        file.deleteOnExit();
        System.out.println(file.exists());
    }
}
