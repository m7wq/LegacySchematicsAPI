package dev.m7wq;


import dev.m7wq.impl.LegacySchematicsManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class SchematicsAPI
{



    @Getter
    LegacySchematicsManager schematicsManager;


    public void load(){

        schematicsManager = new LegacySchematicsManager();
    }


}
