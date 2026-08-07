package io;

import java.io.*;
import java.util.*;

public class Demo17 {
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
        scanDirWithContent(rootDir, token, result);
        System.out.println("共找到了符合条件的文件 " + result.size() + " 个，它们分别");
        for (File file : result) {
            System.out.println(file.getCanonicalPath());
        }
    }

    private static void scanDirWithContent(File rootDir, String token, List<File> result) throws IOException {
        File[] files = rootDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirWithContent(file, token, result);
            } else {
                if (isContentContains(file, token)) {
                    result.add(file.getAbsoluteFile());
                }
            }
        }
    }

    // 我们全部按照utf‑8的字符文件来处理
    private static boolean isContentContains(File file, String token) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(is, "UTF-8")) {
                while (scanner.hasNextLine()) {
                    sb.append(scanner.nextLine());
                    sb.append("\r\n");
                }
            }
        }
        return sb.indexOf(token) != -1;
    }
}
