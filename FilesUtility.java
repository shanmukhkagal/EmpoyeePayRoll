package com.payroll;

import java.io.File;

public class FilesUtility {

    public static boolean deleteFiles(File files) {
        File[] fileList = files.listFiles();
        if( fileList != null) {
            for (File file : fileList) {
                deleteFiles(file);
            }
        }
        return files.delete();
    }
}
