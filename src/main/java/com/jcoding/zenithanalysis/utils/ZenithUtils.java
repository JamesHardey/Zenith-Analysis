package com.jcoding.zenithanalysis.utils;

import com.jcoding.zenithanalysis.entity.Assignment;
import com.jcoding.zenithanalysis.services.AdminServices;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class ZenithUtils {

    private static final Logger LOGGER = Logger.getLogger(ZenithUtils.class.getPackageName());

    public static boolean uploadFile(String pathName, MultipartFile file){

        Path filePath = Paths.get("upload/"+pathName).toAbsolutePath();

        try{
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);
            Files.write(filePath, file.getBytes());
        }

        catch (IOException e){
            LOGGER.info("Error in uploading resume file");
            return false;
        }
        return true;
    }

    public static void deleteInFolder(String fullPathName) {
        Path path = Paths.get("upload/"+fullPathName).toAbsolutePath();
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            LOGGER.info("Unable to delete file "+fullPathName);
            return;
        }
        LOGGER.info("File successfully deleted");
    }

    public static String formatTime(String time){
        SimpleDateFormat format_24 = new SimpleDateFormat("HH:mm");
        try {
            Date tim = format_24.parse(time);
            time = new SimpleDateFormat("hh:mm aa").format(tim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String reFormatTime(String time){
        SimpleDateFormat format_24 = new SimpleDateFormat("hh:mm aa");
        try {
            Date tim = format_24.parse(time);
            time = new SimpleDateFormat("HH:mm").format(tim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getFileName(String id, String title,ZenithFileType name, MultipartFile file){
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        fileName = title + "_" + id + extension;
        return name.value+"/" + fileName;
    }

    public static String getClassUploadFileName(Assignment assignment, MultipartFile file){
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        fileName = assignment.getTitle() + "_" + assignment.getId() + extension;
        return "assignment/" + fileName;
    }

    public static ZenithFileType getResourceType(String type){
        if(type.equalsIgnoreCase("presentation guide")){
            return ZenithFileType.PRESENTATION;
        }

        else if(type.equalsIgnoreCase("resume")) return ZenithFileType.RESUME;

        else if(type.equalsIgnoreCase("cover letter")) return ZenithFileType.COVER_LETTER;

        else return null;
    }
}
