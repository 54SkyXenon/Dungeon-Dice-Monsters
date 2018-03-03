import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Item here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Item extends Actor
{
    private String itemName;
    private CardImage thisCard;
    private int ID;
    
    public static final int[] targetHarmfulSpells = {1, 4, 24, 27};
    public static final int[] healingSpells = {2, 9, 11, 13};
    public static final int[] specialScenario = {8, 14, 18, 22};
    
    public Item(String itemName, int ID)
    {
        this.itemName = itemName;
        this.ID = ID;
        thisCard = new CardImage("Cards/" + itemName + ".jpg");
    }
    
    public static boolean partOfHarmful(int ID)
    {
        for (int i = 0; i < targetHarmfulSpells.length; i++)
            if (targetHarmfulSpells[i] == ID)
                return true;
        return false;
    }
    
    public static boolean partOfHealing(int ID)
    {
        for (int i = 0; i < healingSpells.length; i++)
            if (healingSpells[i] == ID)
                return true;
        return false;
    }
    
    public static boolean partOfSpecial(int ID)
    {
        for (int i = 0; i < specialScenario.length; i++)
            if (specialScenario[i] == ID)
                return true;
        return false;
    }
    
    
    public String getName()
    {
        return itemName;
    }
    
    public CardImage getCardImage()
    {
        return thisCard;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public static Item getAppropriateItem(int firstRoll, int secondRoll)
    {
        if (firstRoll == 1 && secondRoll == 1)
            return new Spell("Black Magic - Megira", 200, 1);
            
        else if ((firstRoll == 1 && secondRoll == 2) || (firstRoll == 3 && secondRoll == 6))
            return new Spell("White Magic - Luona", -100, 2);
            
        else if (firstRoll == 1 && secondRoll == 3)
            return new Weapon("Great Sword", 50, 3);
            
        else if ((firstRoll == 1 && secondRoll == 4) || (firstRoll == 3 && secondRoll == 2) || (firstRoll == 4 && secondRoll == 1) || (firstRoll == 5 && secondRoll == 6) || (firstRoll == 6 && secondRoll == 5)) 
            return new Spell("Death Ball", 100, 4);
            
        else if (firstRoll == 1 && secondRoll == 5)
            return new Weapon("Staff", 20, 5);
            
        else if ((firstRoll == 1 && secondRoll == 6) || (firstRoll == 5 && secondRoll == 4))
            return new Spell("Cursed Treasure", 0, 6);
            
        else if (firstRoll == 2 && secondRoll == 1)
            return new Shield("Big Shield", 80, 7);
            
        else if (firstRoll == 2 && secondRoll == 2)
            return new Spell("Monster Scroll - Stripper", 0, 8);
            
        else if ((firstRoll == 2 && secondRoll == 3) || (firstRoll == 5 && secondRoll == 5))
            return new Spell("White Magic - Luona LV 2", -200, 9);
            
        else if (firstRoll == 2 && secondRoll == 4)
            return new Shield("Master Shield", 100, 10);
            
        else if ((firstRoll == 2 && secondRoll == 5) || (firstRoll == 3 && secondRoll == 4))
            return new Spell("Fairy's Gift", -100, 11);
            
        else if (firstRoll == 2 && secondRoll == 6)
            return new Weapon("Long Sword", 40, 12);
            
        else if ((firstRoll == 3 && secondRoll == 1) || (firstRoll == 6 && secondRoll == 3))
            return new Spell("Fairy's Gift LV 2", -200, 13);
            
        else if (firstRoll == 3 && secondRoll == 3)
            return new Spell("Black Magic - Zap", 100, 14);
            
        else if (firstRoll == 3 && secondRoll == 5)
            return new Shield("Iron Shield", 80, 15);
            
        else if (firstRoll == 4 && secondRoll == 2)
            return new Weapon("Battle Axe", 40, 16);
            
        else if (firstRoll == 4 && secondRoll == 3)
            return new Shield("Hero's Shield", 80, 17);
            
        else if (firstRoll == 4 && secondRoll == 4)
            return new Spell("Monster Scroll - Gogyal", 100, 18);
            
        else if (firstRoll == 4 && secondRoll == 5)
            return new Weapon("Spear", 30, 19);
            
        else if (firstRoll == 4 && secondRoll == 6)
            return new Shield("Great Shield", 100, 20);
            
        else if (firstRoll == 5 && secondRoll == 1)
            return new Weapon("Saber", 30, 21);
            
        else if (firstRoll == 5 && secondRoll == 2)
            return new Spell("Black Magic - Freeze", 0, 22);
            
        else if (firstRoll == 5 && secondRoll == 3)
            return new Weapon("Master Sword", 50, 23);
            
        else if (firstRoll == 6 && secondRoll == 1)
            return new Spell("Black Magic - Gidora", 100, 24);
            
        else if (firstRoll == 6 && secondRoll == 2)
            return new Shield("Trash Can Lid", 50, 25);
           
        else if (firstRoll == 6 && secondRoll == 4)
            return new Weapon("Hammer", 20, 26);
            
        else if (firstRoll == 6 && secondRoll == 6)
            return new Spell("Black Magic - Ganda", 200, 27);
            
        else
            return null;
    }
}
