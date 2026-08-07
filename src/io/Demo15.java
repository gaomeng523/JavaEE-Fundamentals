package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Demo15 {

    public static void scanDir(File rootDir, String token, List<File> result){
        File[] files = rootDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                scanDir(file, token, result);
            } else {
                if (file.getName().contains(token)) {
                    result.add(file.getAbsoluteFile());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要扫描的根目录（绝对路径 OR 相对路径）：");
        String rootDirPath = scanner.next();

        File rootDir = new File(rootDirPath);
        if (!rootDir.isDirectory()) {
            System.out.println("您输入的根目录不存在或者不是目录，退出");
            return;
        }

        System.out.print("请输入要找出的文件名中的字符：");
        String token = scanner.next();

        List<File> result = new ArrayList<>();
        // 因为文件系统是树形结构，所以我们使用深度优先遍历（递归）完成遍历
        scanDir(rootDir, token, result);

        System.out.println("共找到了符合条件的文件 " + result.size() + " 个，它们分别");
        for (File file : result) {
            System.out.println(file.getCanonicalPath() + "请问您是否要删除该文件");
            String in = scanner.next();
            if (in.toLowerCase().equals("y")) {
                file.delete();
            }
        }
    }
}
