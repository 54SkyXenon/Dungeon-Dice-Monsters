import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
/**
 * Controls text prompts in the game.
 * 
 * @author Brandon Liang
 * @version 3/13/17
 */
public class Prompt extends Actor
{
    GifImage pickCharacter;
    
    public Prompt()
    {
        pickCharacter = new GifImage("character prompt.gif");
    }
    
    public void act() 
    {
        for (GreenfootImage image : pickCharacter.getImages())
        {
            image.scale(300, 120);
        }
        setImage(pickCharacter.getCurrentImage());
    }    
}
