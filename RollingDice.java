import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class RollingDice here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RollingDice extends Actor
{
    GifImage theDiceThatIsRolling;
    
    public RollingDice()
    {
        theDiceThatIsRolling = new GifImage("dice.gif");
    }
    
    public int showResult()
    {
        int result = Greenfoot.getRandomNumber(6) + 1;
        setImage("Game Tiles/dice-" + result + ".png");
        getImage().scale(150,150);
        return result;
    }
    
    public void changeFrame()
    {
        for (GreenfootImage image : theDiceThatIsRolling.getImages())
        {
            image.scale(150, 150);
        }
        setImage(theDiceThatIsRolling.getCurrentImage());
    }
}
