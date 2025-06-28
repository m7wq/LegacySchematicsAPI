package dev.m7wq.impl;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.DataException;
import dev.m7wq.models.SchematicManager;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

public class LegacySchematicsManager implements SchematicManager {
    @Override
    public CuboidClipboard load(File file, World bukkitWorld) {

        try {

            FileInputStream fis = new FileInputStream(file);

            CuboidClipboard clipboard = CuboidClipboard.loadSchematic(file);

            fis.close();
            return clipboard;
        }catch (IOException | DataException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void paste(CuboidClipboard clipboard, Location pasteLocation, World bukkitWorld) {

        try {
            com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(bukkitWorld);
            EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld,-1);
            Vector vector = BukkitUtil.toVector(pasteLocation);
            clipboard.paste(session,vector,false);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public CuboidClipboard copy(Location min, Location max, World bukkitWorld) {

        try{

            com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(bukkitWorld);

            Vector minV = BukkitUtil.toVector(min),
                    maxV = BukkitUtil.toVector(max);

            CuboidRegion region = new CuboidRegion(minV,maxV);

            Vector size = new Vector(
                    region.getMaximumPoint().getBlockX()-region.getMinimumPoint().getBlockX()+1,
                    region.getMaximumPoint().getBlockY()-region.getMinimumPoint().getBlockY()+1,
                    region.getMaximumPoint().getBlockZ()-region.getMinimumPoint().getBlockZ()+1
            );

            CuboidClipboard clipboard = new CuboidClipboard(size, region.getMinimumPoint());

            EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld,-1);

            clipboard.copy(session);









        }catch (Exception e){
            e.printStackTrace();
        }



        return null;
    }

    @Override
    public void save(CuboidClipboard clipboard, File file, org.bukkit.World bukkitWorld) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            clipboard.saveSchematic(file);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save schematic", e);
        }
    }

}
