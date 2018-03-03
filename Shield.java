import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Shield here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shield extends Item
{
    private int DEF;
    
    public Shield(String itemName, int DEF, int ID)
    {
        super(itemName, ID);
        this.DEF = DEF;
    }
    
    public int getDEF()
    {
        return DEF;
    }
}
