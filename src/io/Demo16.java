package io;

import java.io.*;
import java.util.*;

public class Demo16 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入要复制的文件（绝对路径 OR 相对路径）：");
        String sourcePath = scanner.next();
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            System.out.println("文件不存在，请确认路径是否正确");
            return;
        }

        if (!sourceFile.isFile()) {
            System.out.println("文件不是普通文件，请确认路径是否正确");
            return;
        }

        System.out.print("请输入要复制到的目标路径（绝对路径 OR 相对路径）：");
        String destPath = scanner.next();
        File destFile = new File(destPath);

        if (destFile.exists()) {
            if (destFile.isDirectory()) {
                System.out.println("目标路径已经存在，并且是一个目录，请确认路径是否正确");
                return;
            }
            if (destFile.isFile()) {
                System.out.println("目标路径已经存在，是否要进行覆盖? y/n");
                String ans = scanner.next();
                if (!ans.toLowerCase().equals("y")) {
                    System.out.println("停止复制");
                    return;
                }
            }
        }

        try (InputStream is = new FileInputStream(sourceFile)) {
            try (OutputStream os = new FileOutputStream(destFile)) {
                byte[] buf = new byte[1024];
                int len;
                while (true) {
                    len = is.read(buf);
                    if (len == -1) {
                        break;
                    }
                    os.write(buf, 0, len);
                }
                os.flush();
            }
        }
        System.out.println("复制已完成");
    }
}