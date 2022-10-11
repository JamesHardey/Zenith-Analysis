package com.jcoding.zenithanalysis.utils;

public enum ZenithFileType {

    ASSIGNMENT("assignment"),
    RESUME("resume"),
    COVER_LETTER("cover_letter"),
    UPLOAD("activities"),
    PRESENTATION("presentation_guide");

    String value;

    ZenithFileType(String value){
        this.value = value;
    }


}
