import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Write a description of class PlayerInfo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlayerInfo extends Actor
{
    private GameAvatar onTheBoard, actualAvatar;
    private ArrayList<Item> inventory;
    private Item currentlyEquippedWeapon, currentlyEquippedShield;
    private String avatarName;
    private String actualName;
    private boolean initialized;
    private int startSpace;
    private int lifePoints;
    private int xLocation;
    private int yLocation;
    private int ATK;
    private int DEF;
    
    public PlayerInfo(String avatarName, String actualName)
    {
        this.avatarName = avatarName;
        this.actualName = actualName;
        this.lifePoints = 300;
        this.initialized = false;
        this.startSpace = 0;
        this.inventory = new ArrayList<Item>();
        
        inventory.add(new Weapon("Bare Hands", 10, 0));
        inventory.add(new Shield("Defenseless", 0, 0));
        currentlyEquippedWeapon = inventory.get(0);
        currentlyEquippedShield = inventory.get(1);
        ATK = 10;
        DEF = 0;
    }
    
    public String getName()
    {
        return avatarName;
    }
    
    public void setRow(int y)
    {
        yLocation = y;
    }
    
    public void setCol(int x)
    {
        xLocation = x;
    }
    
    public void setName(String avatarName)
    {
        this.avatarName = avatarName;
    }
    
    public void deductLP(int amount)
    {
        if (amount > 0)
            lifePoints -= amount;
    }
    
    public void gainLP(int amount)
    {
        lifePoints += amount;
    }
    
    public void toggleInitialized()
    {
        initialized = true;
    }
    
    public GameAvatar initialize(int x, int y, int startSpace)
    {
        setCol(x);
        setRow(y);
        this.startSpace = startSpace;
        initialized = true;
        onTheBoard = new GameAvatar(actualName, 40, 40);
        actualAvatar = new GameAvatar(actualName, 200, 200);
        return onTheBoard;
    }
    
    public void addToInventory(Item item)
    {
        inventory.add(item);
    }
    
    public void setNewShield(Item theShield)
    {
        currentlyEquippedShield = theShield;
        DEF = ((Shield)currentlyEquippedShield).getDEF();
    }
    
    public void setNewWeapon(Item theWeapon)
    {
        currentlyEquippedWeapon = theWeapon;
        ATK = ((Weapon)currentlyEquippedWeapon).getATK();
    }
    
    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
    
    public GameAvatar getAvatar()
    {
        return actualAvatar;
    }
    
    public String getActualName()
    {
        return actualName;
    }
    
    public String getEquippedWeaponName()
    {
        return currentlyEquippedWeapon.getName();
    }
    
    public String getEquippedShieldName()
    {
        return currentlyEquippedShield.getName();
    }
    
    public int getATK()
    {
        return ATK;
    }    
    
    public int getDEF()
    {
        return DEF;
    }
    
    public int getCol()
    {
        return xLocation;
    }
    
    public int getRow()
    {
        return yLocation;
    }
    
    public int getStartSpace()
    {
        return startSpace;
    }
    
    public int getLP()
    {
        return lifePoints;
    }
    
    public boolean isInitialized()
    {
        return initialized;
    }
}
