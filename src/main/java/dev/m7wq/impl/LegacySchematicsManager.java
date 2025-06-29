package dev.m7wq.impl;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

import com.sk89q.worldedit.session.ClipboardHolder;

import com.google.common.io.Closer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import dev.m7wq.models.SchematicManager;
import org.bukkit.Location;


public class LegacySchematicsManager implements SchematicManager {


    // Load a schematic of a file
    @Override
    public Clipboard load(File file, org.bukkit.World bukkitWorld) {

        // try to get the format of a file and check if it is valid (.schematic)
        ClipboardFormat format = ClipboardFormat.findByFile(file);
        if (format == null)
            throw new RuntimeException("The file should end with .schematic FOOL!");

        // Reading the file as a clipboard
        Closer closer = Closer.create();

        try {


            FileInputStream fis = closer.register(new FileInputStream(file));
            BufferedInputStream bis = closer.register(new BufferedInputStream(fis));
            ClipboardReader reader = format.getReader(bis);


            return reader.read(BukkitUtil.getLocalWorld(bukkitWorld).getWorldData());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load schematic: " + e.getMessage(), e);
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {}
        }
    }

    // paste a clipboard as a physically schematic on minecraft :)
    public void paste(Clipboard clipboard, Location pasteLocation, org.bukkit.World bukkitWorld) {
        // Defining the worldEditWorld for the worldData and the editSession
        WorldEdit worldEdit = WorldEdit.getInstance();
        com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(bukkitWorld);
        EditSession editSession = worldEdit.getEditSessionFactory().getEditSession(weWorld, -1);

        // Transfer the pasteLocation into a vector
        // So its ready for the preparing
        Vector to = BukkitUtil.toVector(pasteLocation);

        // Making the clipboard holder that can create a paste and make it into an operation
        ClipboardHolder holder = new ClipboardHolder(clipboard, weWorld.getWorldData());

        Operation operation = holder.createPaste(editSession, weWorld.getWorldData())
                .to(to)
                .ignoreAirBlocks(false)
                .build();

        try {
            // Complete the paste operation
            Operations.completeLegacy(operation);
        } catch (WorldEditException e) {
            throw new RuntimeException("Failed to paste schematic: " + e.getMessage(), e);
        }
    }

    public Clipboard copy(Location min, Location max, org.bukkit.World bukkitWorld) {

        // Defining the region by the positions and the worldedit world
        com.sk89q.worldedit.world.World weWorld = BukkitUtil.getLocalWorld(bukkitWorld);
        Vector minVec = BukkitUtil.toVector(min);
        Vector maxVec = BukkitUtil.toVector(max);
        Region region = new CuboidRegion(weWorld, minVec, maxVec);


        // Create the clipboard and the edit session as a preparing for the creating the operation
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);

        // Creating the operation (ForwardExtentCopy)
        ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());

        try {


            // Completing the copy operation
            Operations.completeLegacy(copy);
        } catch (WorldEditException e) {

            throw new RuntimeException("Failed to copy region: " + e.getMessage(), e);
        }
        // setting the origin and return the clipboard
        clipboard.setOrigin(minVec);
        return clipboard;
    }

    public void save(Clipboard clipboard, File file, org.bukkit.World bukkitWorld) {


        if (!file.getName().endsWith(".schematic"))
            throw new IllegalStateException("Theres no .schematic on the last of the file name... Get Lost man");


        ClipboardFormat format = ClipboardFormat.SCHEMATIC;


        // Writing the schematic data into the file
        Closer closer = Closer.create();
        try {


            // Checkers if the file is found
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs())
                throw new IOException("Couldnt create directories for file");


            FileOutputStream fos = closer.register(new FileOutputStream(file));

            BufferedOutputStream bos = closer.register(new BufferedOutputStream(fos));
            ClipboardWriter writer = closer.register(format.getWriter(bos));

            writer.write(clipboard, BukkitUtil.getLocalWorld(bukkitWorld).getWorldData());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save schematic: " + e.getMessage(), e);
        } finally {
            try {
                closer.close();
            } catch (IOException ignored) {}
        }
    }
}
