package com.jcoding.zenithanalysis.utils;

import com.jcoding.zenithanalysis.entity.Assignment;
import com.jcoding.zenithanalysis.entity.Uploads;

import java.util.Map;

public class ZenithMessages {

    public static Map<String, String> assignmentMessage(Assignment assignment, String baseUrl){
        String url = "https://"+baseUrl+"home";

        String subject = "Assignment From Zenith-Analysis";
        String body = "Module: "+assignment.getModule()+"\n"
                +"Title: "+assignment.getTitle()+"\n"
                +"\n\n"
                +assignment.getInstructions()+"\n"
                +"\n\n"
                +"Uploaded date:   "+assignment.getUploadDate()+"\n"
                +"Submission date: "+assignment.getSubmissionDate()+"\n"
                +"\n"
                +"Click this link below to view full details"+"\n"
                +"\n"
                +url+"\n"
                +"\n\n"
                +"Zenith-Analysis";

        return Map.of(
                "subject", subject,
                "body", body
        );
    }


    public static Map<String, String> classActivityMessage(Uploads upload, String baseUrl){
        String url = "https://"+baseUrl+"home";

        String subject = "Zenith-Analysis Class Activity";
        String body =
                "Module: "+upload.getModule()+"\n"
                +"Title: "+upload.getTitle()+"\n"
                +"\n\n"
                +upload.getMessage()+"\n"
                +"\n\n"
                +"Uploaded date:   "+upload.getUploadDate()+"\n"
                +"\n"
                +"Click this link below to view full details"+"\n"
                +"\n"
                +url+"\n"
                +"\n\n"
                +"Zenith-Analysis";

        return Map.of(
                "subject", subject,
                "body", body
        );
    }

}
