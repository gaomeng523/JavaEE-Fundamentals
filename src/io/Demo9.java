package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Demo9 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:/test.txt");
        file.createNewFile();
        System.out.println(file.exists());
//        InputStream inputStream = new FileInputStream("d:/test.txt");
//        while(true) {
//            // 创建流对象的过程中，相当于“打开文件”
//            // 打开完毕就可以读操作
//            // 使用read方法进行读操作
//            // 下列代码虽然可行，但是实践中一般不这么写。每个字节都需要触发一次read
//            // read 是读硬盘的，速度比着读内存很慢
//            int n = inputStream.read();
//            if(n == -1){
//                // 读取到文件的末尾了，读取完毕
//                break;
//            }
//            // 打印字节的时候习惯使用十六禁止打印
//            System.out.printf("%x\n",n);
//        }
//        InputStream inputStream = new FileInputStream("d:/test.txt");
//        while(true) {
//            // 由于不知道文件的大小，可能很大，超过了1024
//            // 即使是使用了read的第二种版本，也需要搭配循环
//            byte[] bytes = new byte[1024];
//            int n = inputStream.read(bytes);
//            if(n == -1){
//                // 读取到文件的末尾了，读取完毕
//                break;
//            }
//            // 打印字节的时候习惯使用十六禁止打印
//            for (int i = 0; i < n; i++) {
//                System.out.printf("%x\n",bytes[i]);
//            }
//        }
//        inputStream.close();

        // 使用try with resource
        try(InputStream inputStream = new FileInputStream("d:/test.txt");) {
            while(true) {
                // 由于不知道文件的大小，可能很大，超过了1024
                // 即使是使用了read的第二种版本，也需要搭配循环
                byte[] bytes = new byte[1024];
                int n = inputStream.read(bytes);
                if(n == -1){
                    // 读取到文件的末尾了，读取完毕
                    break;
                }
                // 打印字节的时候习惯使用十六禁止打印
                for (int i = 0; i < n; i++) {
                    System.out.printf("%x\n",bytes[i]);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
