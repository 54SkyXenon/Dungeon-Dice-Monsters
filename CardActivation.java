import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CardActivation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CardActivation extends Actor
{
    GifImage theActivatingSpell;
    
    public CardActivation(String spellName)
    {
        theActivatingSpell = new GifImage("Card Animations/" + spellName + ".gif");
    }
    
    public void changeFrame()
    {
        for (GreenfootImage image : theActivatingSpell.getImages())
        {
            image.scale(350, 500);
        }
        setImage(theActivatingSpell.getCurrentImage());
    }   
}
