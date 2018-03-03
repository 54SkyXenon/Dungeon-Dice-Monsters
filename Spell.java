import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Spell here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Spell extends Item
{
    private int magicATK;
    
    public Spell(String itemName, int magicATK, int ID)
    {
        super(itemName, ID);
        this.magicATK = magicATK;
    }
    
    public int getMagicATK()
    {
        return magicATK;
    }
}
