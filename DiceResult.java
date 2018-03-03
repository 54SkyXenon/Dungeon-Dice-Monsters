import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DiceResult here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DiceResult extends Actor
{
    public DiceResult(int result)
    {
        setImage(new GreenfootImage("Game Tiles/dice-" + result + ".png"));
        GreenfootImage image = getImage();
        image.scale(150, 150);
        setImage(image);
    }
}
