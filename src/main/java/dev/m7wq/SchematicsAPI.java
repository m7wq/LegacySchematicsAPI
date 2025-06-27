package dev.m7wq;


import lombok.Getter;

public class SchematicsAPI
{



    @Getter
    LegacySchematicsManager schematicsManager;


    public void load(){
        schematicsManager = new LegacySchematicsManager();
    }


}
