package com.jcoding.zenithanalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestFile1 {

    public static void main(String[] args) {
        //String path = TestFile1.class.getResource("static").getPath();
        Path path = Paths.get("static").toAbsolutePath();
        if(!Files.exists(path)){
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(
                Files.exists(path));
        System.out.println(path);

    }
}
