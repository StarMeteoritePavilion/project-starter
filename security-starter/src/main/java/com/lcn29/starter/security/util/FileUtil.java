package com.lcn29.starter.security.util;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2020-01-31 13:39
 */
public class FileUtil {

    /**
     * create folder
     * @param foldPath
     * @return
     */
    public static boolean createFolder(String foldPath) {

        if (foldPath == null || foldPath.isBlank()) {
            return false;
        }
        if (fileOrPathExist(foldPath)) {
            return true;
        }
        String[] splitPath = isWindow() ? foldPath.split("\\\\") : foldPath.split("/");

        File folder;
        StringBuilder pathBuilder = new StringBuilder();
        for (String path : splitPath) {
            pathBuilder.append(path).append(File.separator);
            folder = new File(pathBuilder.toString());
            if (!folder.exists() && !folder.mkdir()) {
                return false;
            }
        }
        return true;
    }

    /**
     * create file
     * @param filePath
     * @return
     */
    public static boolean createFile(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank() || filePath.endsWith(File.separator)) {
            return false;
        }
        if (fileOrPathExist(filePath)) {
            return true;
        }
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        System.out.println(fileName);
        // the fullPath = foldPath + file.separator + fileName
        String foldPath = filePath.substring(0, filePath.length() - File.separator.length() - fileName.length());

        // the isBlank() is used to handler base path in linux, such as /test.txt, the foldPath is blank
        if (!foldPath.isBlank() && !createFolder(foldPath)) {
            return false;
        }
        File file = new File(filePath);
        return file.createNewFile();
    }

    /**
     * determine if the file or path exists
     * @param path
     * @return
     */
    public static boolean fileOrPathExist(String path) {
        return new File(path).exists();
    }

    /**
     * the os is window
     * @return
     */
    private static boolean isWindow() {
        return System.getProperty("os.name").toLowerCase().contains("window");
    }

}
