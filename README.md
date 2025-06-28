## First of all please put a star 🥺 it took me hours of searching and trying to make this masterpiece

# IMPORTANT: Use the jar file on lib as dependency and plugin;
Tutorial To use it as a dependency:
---

## ✅ Step 1: Project Structure

```
my-plugin/
├── libs/
│   └── worldedit-bukkit-6.1.jar
├── pom.xml or build.gradle
```

Commit the `.jar` to the `libs/` folder in your repo.

---

## 🔷 Maven Setup

```xml
<repositories>
  <repository>
    <id>local-libs</id>
    <url>file://${project.basedir}/libs</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.sk89q.worldedit</groupId>
    <artifactId>worldedit-bukkit</artifactId>
    <version>6.1</version>
  </dependency>
</dependencies>
```

---

## 🟡 Gradle Setup

```groovy
repositories {
    flatDir { dirs 'libs' }
}

dependencies {
    implementation name: 'worldedit-bukkit-6.1'
}
```



# LegacySchematicsAPI
Simple Schematics-API for legacy minecraft versions powered by WorldEdit 6.1 | Beginners-Friendly!

[![](https://jitpack.io/v/m7wq/LegacySchematicsAPI.svg)](https://jitpack.io/#m7wq/LegacySchematicsAPI)

# Maven

Add to pom.xml
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
Step 2. Add the dependency
```xml
	<dependency>
	    <groupId>com.github.m7wq</groupId>
	    <artifactId>LegacySchematicsAPI</artifactId>
	    <version>1.0-PATCHED</version>
	</dependency>
```

# Gradle

Add it in your root settings.gradle at the end of repositories:
```gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```gradle
	dependencies {
	        implementation 'com.github.m7wq:LegacySchematicsAPI:1.0-PATCHED'
	}
```

# Examples
```java
public class MyPlugin extends JavaPlugin {




    @Override
    public void onEnable(){

        // Initialize a variable for the SchematicsAPI
        SchematicsAPI schematicsAPI = new SchematicsAPI();

        // Step 2. Load the schematics api
        schematicsAPI.load();
        
        // Schematic Manager and your world initialization
        LegacySchematicsManager schematicsManager = schematicsAPI.getSchematicsManager();
        World world = getServer().getWorld("myworld");

        // Loading a schematic from a file
        File file = new File(getDataFolder().getAbsolutePath()+"/schematics/blackhouse.schematic");
        Clipboard blackHouseClipboard = schematicsAPI.getSchematicsManager().load(file, world);
       
        // Copying a schematic already valid
        Location pos1 = (Location) getConfig().get("pos1"),
                pos2 = (Location) getConfig().get("pos2");
        Clipboard mySchematic = schematicsManager.copy(pos1,pos2,world);
        
        // Save your copied schematic or loaded one into a file
        File dir = new File(getDataFolder().getAbsolutePath()+"/scematics");
        schematicsManager.save(blackHouseClipboard,dir,world); // loaded schematic
        schematicsManager.save(mySchematic,dir,world); // copied schematic
        
        // Paste your schematics to transform into physical minecraft buildings
        Location placeLocation = new Location(world,0,0,0);
        schematicsManager.paste(blackHouseClipboard,placeLocation,world); // loaded schematic
        schematicsManager.paste(mySchematic,placeLocation,world); // copied schematic


    }
}
```
