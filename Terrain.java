import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Terrain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Terrain extends Actor
{
    private GreenfootImage theTerrain;
    private String terrainType;

    public Terrain(String terrainType)
    {
        this.terrainType = terrainType;
        theTerrain = new GreenfootImage("Game Tiles/" + terrainType + ".png");
        theTerrain.scale(40, 40);
        if (terrainType.equals("none"))
            theTerrain.setTransparency(0);
        setImage(theTerrain);
    }

    public String getGroundType()
    {
        return terrainType;
    }
}
