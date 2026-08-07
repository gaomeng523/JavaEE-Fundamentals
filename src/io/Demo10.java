package io;

import java.io.*;

public class Demo10 {
    public static void main(String[] args) {
        try (OutputStream outputStream = new FileOutputStream("d:/test.txt",true)) {
            outputStream.write(65);
            outputStream.write(66);
            outputStream.write(67);
            outputStream.write(68);
            outputStream.write(69);
            outputStream.write(70);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
