import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class GameAvatar extends Actor
{
    private String characterName;
    
    public GameAvatar(String animeCharacterName, int x, int y)
    {
        this.characterName = animeCharacterName;
        setImage(new GreenfootImage("Game Avatars/" + animeCharacterName + ".png"));
        
        GreenfootImage image = getImage();
        image.scale(x, y);
        setImage(image);
    }
    
    public String getName()
    {
        return characterName;
    }
}
