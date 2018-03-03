import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;
import javax.swing.JOptionPane;
/**
 * Write a description of class CharacterSelectionScreen here.
 * 
 * @author Brandon Liang
 * @version 5/11/17
 */
public class CharacterSelectionScreen extends World
{
    private int numPlayers, currentPlayerToBeAssigned;
    private ArrayList<GameAvatar> mySelectableChars;
    private ArrayList<PlayerInfo> players;
    private CharacterSelectionLabel helpfulLabel;
    private GameAvatar selectedChar;
    private GreenfootSound ost;

    public CharacterSelectionScreen(int numPlayers)
    {    
        super(800, 380, 1); 
        this.numPlayers = numPlayers;
        currentPlayerToBeAssigned = 1;

        players = new ArrayList<PlayerInfo>();

        Prompt characterPickPrompt = new Prompt();
        addObject(characterPickPrompt, (int)(getWidth()/1.5), getHeight()/4);

        mySelectableChars = new ArrayList<GameAvatar>();
        mySelectableChars.add(new GameAvatar("unknown", 290, 290));
        mySelectableChars.add(new GameAvatar("suzaku", 290, 290));
        mySelectableChars.add(new GameAvatar("kirito", 290, 290));
        mySelectableChars.add(new GameAvatar("levi", 290, 290));
        mySelectableChars.add(new GameAvatar("saitama", 290, 290));
        mySelectableChars.add(new GameAvatar("akame", 290, 290));
        mySelectableChars.add(new GameAvatar("shinoa", 290, 290));
        mySelectableChars.add(new GameAvatar("touka", 290, 290));
        mySelectableChars.add(new GameAvatar("android 18", 290, 290));
        selectedChar = mySelectableChars.get(0);
        addObject(selectedChar, 160, 160);
        addObject(new GameAvatar("suzaku", 90, 90), 434, 214); //on the first row
        addObject(new GameAvatar("kirito", 90, 90), 537, 214);
        addObject(new GameAvatar("levi", 90, 90), 640, 214);
        addObject(new GameAvatar("saitama", 90, 90), 742, 214);
        addObject(new GameAvatar("akame", 90, 90), 434, 314); //on the second row
        addObject(new GameAvatar("shinoa", 90, 90), 537, 314);
        addObject(new GameAvatar("touka", 90, 90), 640, 314);
        addObject(new GameAvatar("android 18", 90, 90), 742, 314);

        helpfulLabel = new CharacterSelectionLabel("P" + currentPlayerToBeAssigned + " Selected: " + mySelectableChars.get(0).getName(), 30, greenfoot.Color.BLACK, greenfoot.Color.WHITE);
        addObject(helpfulLabel, 136, 332);
        
        ost = new GreenfootSound("Char Select.mp3");
        ost.playLoop();
    }

    public void act()
    {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null)
        {
            Actor object = mouse.getActor();
            if (object instanceof GameAvatar)
            {
                String name = ((GameAvatar)object).getName();
                for (int c = 0; c < mySelectableChars.size(); c++)
                {
                    if (mySelectableChars.get(c).getName().equals(name))
                    {
                        selectedChar.setImage("Game Avatars/" + mySelectableChars.get(c).getName() + ".png");
                        selectedChar.getImage().scale(290,290);
                        helpfulLabel.changeCharacter(name);
                    }
                }
                
                if (Greenfoot.mouseClicked(object))
                {
                    String editedName = JOptionPane.showInputDialog("Edit name for this player: ");
                    
                    while (editedName == null || editedName.equals(""))
                        editedName = JOptionPane.showInputDialog("Edit name for this player: ");
                        
                    players.add(new PlayerInfo(editedName, name));
                    helpfulLabel.changePlayerNumber(++currentPlayerToBeAssigned);
                    if (currentPlayerToBeAssigned > numPlayers)
                    {
                        ost.pause();
                        String[] options = {"yes please", "no"};
                        Object choice = JOptionPane.showInputDialog(null, "You decide", "Music? ", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        while (choice == null)
                            choice = JOptionPane.showInputDialog(null, "You decide", "Music? ", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        Greenfoot.setWorld(new GameBoardWorld(players, ((String)choice).equals("yes please")));
                    }
                }
            }
        }
    }
} 

