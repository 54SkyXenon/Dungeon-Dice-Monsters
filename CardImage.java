import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CardImage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CardImage extends Actor
{
    GreenfootImage thisImage;
    
    public CardImage(String filepath)
    {
        thisImage = new GreenfootImage(filepath);
        thisImage.scale(350, 500);
        setImage(thisImage);
    }
}
