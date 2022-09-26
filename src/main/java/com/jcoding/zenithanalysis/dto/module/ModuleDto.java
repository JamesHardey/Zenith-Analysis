package com.jcoding.zenithanalysis.dto.module;

public class ModuleDto {

    private Long id;
    private String moduleTitle;
    private String moduleDetails;

    public ModuleDto() {
    }

    public ModuleDto(Long id, String moduleTitle, String moduleDetails) {
        this.id = id;
        this.moduleTitle = moduleTitle;
        this.moduleDetails = moduleDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public String getModuleDetails() {
        return moduleDetails;
    }

    public void setModuleDetails(String moduleDetails) {
        this.moduleDetails = moduleDetails;
    }
}
