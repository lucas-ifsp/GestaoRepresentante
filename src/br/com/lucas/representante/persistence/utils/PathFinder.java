package br.com.lucas.representante.persistence.utils;

import java.io.File;

public class PathFinder {

    public static String find() {
        File file = new File(System.getProperty("java.class.path"));
        String dirPath = file.getAbsoluteFile().getParentFile().toString();
        String[] pathParts = dirPath.split(":");
        String jarPath = pathParts[0]+"/";

        return jarPath;
    }
}
