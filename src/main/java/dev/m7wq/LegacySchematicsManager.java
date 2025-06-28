package dev.m7wq;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.*;

public class LegacySchematicsManager {

    // From file to a clipboard of a schematic
    public Clipboard load(File file, World world) {
        ClipboardFormat format = ClipboardFormat.findByFile(file);






        try {

            FileInputStream inputStream = new FileInputStream(file);;
            ClipboardReader reader = format.getReader(inputStream);

            com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(world);
            WorldData worldData = weWorld.getWorldData();

            Clipboard clipboard = reader.read(worldData);

            return clipboard;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Paste the schematic on paste location
    public void paste(Clipboard clipboard, Location pasteLocation, World bukkitWorld){


        com.sk89q.worldedit.world.World world = BukkitUtil.getLocalWorld(bukkitWorld);

        WorldData worldData = world.getWorldData();

        try{

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world,-1);

            Vector vector = BukkitUtil.toVector(pasteLocation);

            Operation operation = new ClipboardHolder(clipboard,worldData)
                    .createPaste(editSession,worldData)
                    .to(vector)
                    .ignoreAirBlocks(false)
                    .build();

            Operations.complete(operation);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // Paste the schematic on paste location with permission to set if schematic placing ignores air blocks
    public void paste(Clipboard clipboard, Location pasteLocation, World bukkitWorld, boolean ignoreAirBlocks){


        com.sk89q.worldedit.world.World world = BukkitUtil.getLocalWorld(bukkitWorld);

        WorldData worldData = world.getWorldData();

        try{

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world,-1);

            Vector vector = BukkitUtil.toVector(pasteLocation);

            Operation operation = new ClipboardHolder(clipboard,worldData)
                    .createPaste(editSession,worldData)
                    .to(vector)
                    .ignoreAirBlocks(ignoreAirBlocks)
                    .build();

            Operations.complete(operation);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // Copy a schematic from a minecraft physical building
    public Clipboard copy(Location min, Location max, World world){
        try {

            Vector minV = BukkitUtil.toVector(min);
            Vector maxV = BukkitUtil.toVector(max);

            CuboidRegion region = new CuboidRegion(minV,maxV);
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

            com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(world);

            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(weWorld,region,clipboard,region.getMinimumPoint());


            Operations.complete(forwardExtentCopy);

            return clipboard;
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

    }

    // save a clipboard of a schematic into a file
    public void save(Clipboard clipboard, File file, World world){

        try {

            com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(world);

            OutputStream outputStream = new FileOutputStream(file);

            ClipboardWriter writer = ClipboardFormat.SCHEMATIC.getWriter(outputStream);

            writer.write(clipboard, weWorld.getWorldData());

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }




}
