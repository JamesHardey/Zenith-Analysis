package com.jcoding.zenithanalysis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestFile1 {

    public static void main(String[] args) {
        /*String path = //"file:///"+
                System.getProperty("user.dir")+"/upload";
        String location = Paths.get(path).toUri().toString();
        System.out.println(location);*/

        Path path = Paths.get("./upload/as").toAbsolutePath();
        System.out.println(Files.exists(path));
        System.out.println(path);

    }
}
