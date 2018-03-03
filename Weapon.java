import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Weapon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Weapon extends Item
{
    private int ATK;
    
    public Weapon(String itemName, int ATK, int ID)
    {
        super(itemName, ID);
        this.ATK = ATK;
    }
    
    public int getATK()
    {
        return ATK;
    }
}
