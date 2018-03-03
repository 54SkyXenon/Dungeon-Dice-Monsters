import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;

/**
 * Write a description of class CharacterSelectionLabel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CharacterSelectionLabel extends Actor
{
    private String text;
    private greenfoot.Color textColor, bgColor;
    private int size;
    
    public CharacterSelectionLabel(String text, int size, greenfoot.Color textColor, greenfoot.Color bgColor) 
    {
        this.text = text;
        this.size = size;
        this.textColor = textColor;
        this.bgColor = bgColor;
        setImage(new GreenfootImage(text, size, textColor, bgColor));
    }
    
    public void changeCharacter(String name) 
    {
        String nonCharPart = text.substring(0, 13);
        text = nonCharPart + name;
        setImage(new GreenfootImage(text, size, textColor, bgColor));
    }    
    
    public void changePlayerNumber(int playerNumber)
    {
        String nonPlayerPart = text.split(":")[1];
        text = "P" + playerNumber + " Selected: " + nonPlayerPart;
        setImage(new GreenfootImage(text, size, textColor, bgColor));
    }
}
