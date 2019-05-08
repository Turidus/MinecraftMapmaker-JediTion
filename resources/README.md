# Minecraft-MapMaker

This program takes an image and generates multiple files that aid in the generation of Minecraft maps displaying
this image. Contrary to other programs, this does not generate the maps itself.

This program can work with the stepped method, using the full 153 map colors available to
ingame generated maps.

## Generated Files

   A txt file displaying the type and amount of blocks needed

   A txt file displaying the type and position of these blocks

   One or more schematic files for World Edit(see "About large images" below) and compatible plugins to directly import the blocks into Minecraft

   A image showing an approximation of the end result
   
## Usage

Download the Jar from https://github.com/Turidus/MinecraftMapmaker-JediTion/releases/latest

Execute the Jar. At first use it will generate a folder names resources containing some files including this readme.

A GUI will open where you can generate the files you want.

### GUI

####Menu Bar

 - File: Here you can close the window
 - Edit: Here you can save the current configuration or load the default values
 - About: Here you can find out more and check for new updates.


####Main Window
The GUI has three main columns.

In the first column you can choose the image to process and a folder to save the files to. You can also optionally
choose a project name under which the files will be saved and modify the colors and the blocks to be used in the generation.

In the second column you can choose what will be generated.

In the third column you can modify the constrains under which the map will be generated.
The field minY should be on the level where you plan to construct the map. It has to be smaller than 251 and at least 4 smaller than maxY.
The field maxY should be as high as you can to prevent miscolored blocks. It has to be bigger than 4, smaller than 256 and at least 4 bigger than minY.
The field maxS hast to bigger than 0. It limits the size of the generated maxS. If you set it too low you have to import lots of schematics, if you set it too high,
importing the schematics can choke or even outright crash your server (see About large images).

####Colors and Blocks
In this window you can select the colors that are used by clicking the check mark and also select, where available,
which block to use for this color.

## About the cobblestone line
When you built/import the construct, you will notice that
there is an **additional line** at the *north* end, made out of cobblestone. This is necessary to prevent
the first line of the image to be miscolored.  
One easy way to deal with this additional line is to place it just out of range of the map, which prevents it from being rendered.
Another way is to replace the line of cobblestones with something that blends with the environment.

## About large images
Large images, 128 x 128 pixels and larger, can heavily impact your server when you import schematics. You should consider using Fast
Asynchron World Edit or similar to prevent your server from freezing up. You can also split up the schematic into smaller chunks
by providing a -maxS value smaller than the image size.

## About very large images
Very large images, 250 x 250 pixels and larger, not only have all the problems large images have,
they also run into the world height limit. Especially if you have large areas with one single color or you have a really large image
(~450 x 450 pixels and larger) a perfect representation of the image would need Y coordinates higher than 256. To prevent the result 
of exceeding this height limit (or any chosen **-maxY**) this program will force any blocks exceeding the maximum allowed Y coordinate
to be below it, but this also introduces miscolored pixels into the final image. These become very noticeable in images with large areas
with a single color, while busy images can deal better with this.

Besides setting the **-maxY** bigger than your image size, the best way to handle this is to cut your image into ImageSizeX x 256 or
even ImageSizeX x 128 pixel areas. These images can then be processed and placed individually.

An additional problem is performance. Very large images can take a while to process, up to multiple minutes depending on your machine.

The maximal tested size that can be processed is 1000x1000 pixels, larger images can run into the maximum heap size
of the JVM (Java virtual machine) which will crash the program.

## About (multiple) schematics
The schematics expand from the block you are standing on towards **East** and **South**. If you want to place a schematic between (0,0)
and (128,128) you have to stand on (0,0). The way you are looking has no impact on it. The upper left pixel of the image will always
spawn on the (x,z) block you are standing, which will be the north-west most block in the construct.
Multiple schematics are named with their relative placement towards each other. PartZ0X1 has to be placed on the east of partZ0X0, 
partZ1X0 has to be placed on the south of Z0X0.

## About the BaseColorIDs.txt
Here are the blocks for all color values saved. You can add to the used blocks by changing the BlockID value to the BlockID of your choice.
The top block is the default block.