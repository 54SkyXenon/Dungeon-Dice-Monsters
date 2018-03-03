import greenfoot.*;
import java.util.List;

public class GameStartWorld extends World
{
    NumberOfPlayers two, three, four;

    public GameStartWorld()
    {    
        super(625, 351, 1); 

        two = new NumberOfPlayers("2players");
        three = new NumberOfPlayers("3players");
        four = new NumberOfPlayers("4players");

        addObject(new DungeonDiceLogo(), 312, 87);

        addObject(two, 315, 166);
        addObject(three, 315, 216);
        addObject(four, 315, 266);
    }

    public void act()
    {
        if (Greenfoot.mouseClicked(two))
        {
            List objects = getObjects(null);
            removeObjects(objects);
            Greenfoot.setWorld(new CharacterSelectionScreen(2));
        }

        else if (Greenfoot.mouseClicked(three))
        {
            List objects = getObjects(null);
            removeObjects(objects);
            Greenfoot.setWorld(new CharacterSelectionScreen(3));
        }

        else if (Greenfoot.mouseClicked(four))
        {
            List objects = getObjects(null);
            removeObjects(objects);
            Greenfoot.setWorld(new CharacterSelectionScreen(4));
        }
    }
}
