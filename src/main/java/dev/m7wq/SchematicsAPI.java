package dev.m7wq;


import dev.m7wq.impl.LegacySchematicsManager;
import lombok.Getter;

public class SchematicsAPI
{



    @Getter
    LegacySchematicsManager schematicsManager;


    public void load(){
        schematicsManager = new LegacySchematicsManager();
    }


}
