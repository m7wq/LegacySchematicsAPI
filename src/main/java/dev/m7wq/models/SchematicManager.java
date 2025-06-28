package dev.m7wq.models;

import com.sk89q.worldedit.CuboidClipboard;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;

public interface SchematicManager {

    CuboidClipboard load(File file, World bukkitWorld);
    void paste(CuboidClipboard clipboard, Location pasteLocation, World bukkitWorld);
    CuboidClipboard copy(Location min, Location max, World bukkitWorld);
    void save(CuboidClipboard clipboard, File file, World bukkitWorld);
}
