package dev.m7wq.models;

import com.sk89q.worldedit.CuboidClipboard;
import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import java.io.File;

public interface SchematicManager {

    Clipboard load(File file, World bukkitWorld);
    void paste(Clipboard clipboard, Location pasteLocation, World bukkitWorld);
    Clipboard copy(Location min, Location max, World bukkitWorld);
    void save(Clipboard clipboard, File file, World bukkitWorld);
}
