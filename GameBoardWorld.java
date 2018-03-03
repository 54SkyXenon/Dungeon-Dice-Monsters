import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;
import javax.swing.*;
/**
 * Write a description of class GameBoardWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameBoardWorld extends World
{
    private Terrain[][] gameBoard;

    private ArrayList<PlayerInfo> thePlayers;
    private ArrayList<GameAvatar> playerAvatarsOnBoard;
    private GreenfootSound ost;
    private boolean mouseClickedRollDice;
    private int playerInitialized;
    private int playerTurn;

    public GameBoardWorld(ArrayList<PlayerInfo> thePlayers, boolean musicToggled)
    {    
        super(1280, 720, 1); 
        if(musicToggled)
        {
            ost = new GreenfootSound("Game Music.mp3");
            ost.playLoop();
        }
        mouseClickedRollDice = false;
        gameBoard = new Terrain[14][18];
        playerInitialized = 1;
        playerTurn = 1;
        this.thePlayers = thePlayers;
        playerAvatarsOnBoard = new ArrayList<GameAvatar>(thePlayers.size()); 
        initBoard();
        addBoard();
        addObject(new DungeonDiceLogo(), 865, 48);
    }

    public void act()
    {
        if (!allInitialized())
        {
            Greenfoot.delay(100);
            setCharacterLocations();
        }
        else
        {
            while (!someoneWon())
                playGame();
            victoryWindow();
            Greenfoot.stop();
        }
    }

    public void victoryWindow()
    {
        String oneWhoWon = "";
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (thePlayers.get(i).getLP() > 0)
                oneWhoWon = thePlayers.get(i).getName();
        }
        JOptionPane.showMessageDialog(null, oneWhoWon + " is the winnner!");
    }

    public void playGame()
    {
        takeTurn();
    }

    public void takeTurn()
    {
        if (thePlayers.get(playerTurn-1).getLP() > 0)
        {
            GameAvatar currentPlayer = new GameAvatar(thePlayers.get(playerTurn-1).getActualName(), 290, 290);
            addObject(currentPlayer, 930, 515);
            
            CharacterSelectionLabel turnIndicator = new CharacterSelectionLabel("It's " + thePlayers.get(playerTurn-1).getName() + "'s Turn...", 30, greenfoot.Color.BLACK, greenfoot.Color.WHITE);
            addObject(turnIndicator, 930, 350);
            int numToMove = diceRoll("amount of moves", thePlayers.get(playerTurn - 1), false);
            
            while (numToMove > 0)
            {
                String[] possibleMoves = getPossibleMoves();
                Object moveChosen = JOptionPane.showInputDialog(null, "Make your move...", "Available moves left: " + numToMove, JOptionPane.QUESTION_MESSAGE, null, possibleMoves, possibleMoves[0]);
                removeObject(playerAvatarsOnBoard.get(playerTurn - 1));
                String courseOfAction = (String)moveChosen;
                if (courseOfAction.equals("up"))
                {
                    thePlayers.get(playerTurn - 1).setRow(thePlayers.get(playerTurn - 1).getRow()-1);
                    addObject(playerAvatarsOnBoard.get(playerTurn - 1), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getX(), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getY());
                }
                else if (courseOfAction.equals("down"))
                {
                    thePlayers.get(playerTurn - 1).setRow(thePlayers.get(playerTurn - 1).getRow()+1);
                    addObject(playerAvatarsOnBoard.get(playerTurn - 1), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getX(), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getY());
                }
                else if (courseOfAction.equals("left"))
                {
                    thePlayers.get(playerTurn - 1).setCol(thePlayers.get(playerTurn - 1).getCol()-1);
                    addObject(playerAvatarsOnBoard.get(playerTurn - 1), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getX(), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getY());
                }
                else if (courseOfAction.equals("right"))
                {
                    thePlayers.get(playerTurn - 1).setCol(thePlayers.get(playerTurn - 1).getCol()+1);
                    addObject(playerAvatarsOnBoard.get(playerTurn - 1), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getX(), gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getY());
                }
                Greenfoot.delay(50);

                if (gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getGroundType().equals("treasure chest"))
                {
                    thePlayers.get(playerTurn - 1).addToInventory(getFromTreasureChest());
                    int x = gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getX();
                    int y = gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()].getY();
                    removeObject(gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()]);
                    gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()] = new Terrain("stone");
                    addObject(gameBoard[thePlayers.get(playerTurn - 1).getRow()][thePlayers.get(playerTurn - 1).getCol()], x, y);
                }

                if (onTheSameSpace())
                {
                    String[] options = {"yes", "no"};
                    Object choice = JOptionPane.showInputDialog(null, "Would you like to fight?", "On a tile with an opponent...", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    if (((String)choice).equals("yes"))
                    {
                        int attackerPrecedence = diceRoll("who attacks", thePlayers.get(playerTurn - 1), false);
                        int defenderPrecedence = diceRoll("who attacks", thePlayers.get(defenderOnSquare()), false);

                        while (attackerPrecedence == defenderPrecedence)
                        {
                            attackerPrecedence = diceRoll("who attacks", thePlayers.get(playerTurn - 1), false);
                            defenderPrecedence = diceRoll("who attacks", thePlayers.get(defenderOnSquare()), false);
                        }

                        if (attackerPrecedence < defenderPrecedence) //defender attacks attacker
                        {
                            int damageMultiplier = diceRoll("the damage the attacker takes", thePlayers.get(defenderOnSquare()), false);
                            JOptionPane.showMessageDialog(null, thePlayers.get(playerTurn - 1).getName() + " took " + (damageMultiplier * thePlayers.get(defenderOnSquare()).getATK()) + " damage!");
                            thePlayers.get(playerTurn - 1).deductLP( (damageMultiplier * thePlayers.get(defenderOnSquare()).getATK()) - thePlayers.get(playerTurn - 1).getDEF());
                            JOptionPane.showMessageDialog(null, thePlayers.get(playerTurn - 1).getName() + " is at " + thePlayers.get(playerTurn - 1).getLP() + " life points!");
                        }
                        else
                        {
                            int damageMultiplier = diceRoll("the damage the defender takes", thePlayers.get(playerTurn - 1), false);
                            JOptionPane.showMessageDialog(null, thePlayers.get(defenderOnSquare()).getName() + " took " + (damageMultiplier * thePlayers.get(playerTurn - 1).getATK()) + " damage!");
                            thePlayers.get(defenderOnSquare()).deductLP( (damageMultiplier * thePlayers.get(playerTurn - 1).getATK()) - thePlayers.get(defenderOnSquare()).getDEF());
                            JOptionPane.showMessageDialog(null, thePlayers.get(defenderOnSquare()).getName() + " is at " + thePlayers.get(defenderOnSquare()).getLP() + " life points!");
                        }
                    }
                }
                numToMove--;
            }

            String[] otherOptions = {"Activate a spell...",  "View inventory...", "Equip an item...", "End your turn..."};
            Object moveChosen = JOptionPane.showInputDialog(null, "Other courses of \naction", "Anything else?", JOptionPane.QUESTION_MESSAGE, null, otherOptions, otherOptions[0]);
            boolean spellActivated = false;
            while (!((String)moveChosen).equals("End your turn..."))
            {
                if (((String)moveChosen).equals("View inventory..."))
                    printInventoryContents();

                else if (((String)moveChosen).equals("Equip an item..."))
                    equipContents();

                else if (!spellActivated)
                {
                    spellActivated = true;
                    activateSpell();
                }
                moveChosen = JOptionPane.showInputDialog(null, "Other courses of action", "Anything else?", JOptionPane.QUESTION_MESSAGE, null, otherOptions, otherOptions[0]);
            }
            Greenfoot.delay(5);
            removeObject(currentPlayer);
            removeObject(turnIndicator);
        }
        
        if (playerTurn == thePlayers.size())
            playerTurn = 1;
        else
            playerTurn++;
    }

    public void activateSpell()
    {
        ArrayList<String> theContents = new ArrayList<String>();
        ArrayList<Item> playerInventory = thePlayers.get(playerTurn - 1).getInventory();
        for (int i = 0; i < playerInventory.size(); i++)
        {
            String itemDesc = "";
            if (playerInventory.get(i) instanceof Spell)
                itemDesc += playerInventory.get(i).getName();
            if (!itemDesc.equals(""))
                theContents.add(itemDesc);
        }
        theContents.add("Go Back");

        String[] contentsAsArray = new String[theContents.size()];

        for (int i = 0; i < theContents.size(); i++)
            contentsAsArray[i] = theContents.get(i);

        Object viewChosen = JOptionPane.showInputDialog(null, "Choose only one to use:", "Activatable spells", JOptionPane.QUESTION_MESSAGE, null, contentsAsArray, contentsAsArray[0]);
        if (!viewChosen.equals("Go Back"))
        {
            for (int i = 0; i < playerInventory.size(); i++)
            {
                if (playerInventory.get(i).getName().equals(viewChosen))
                {
                    applyEffect((Spell)playerInventory.get(i));
                    playerInventory.remove(i);
                }
            }
        }
    }

    public void applyEffect(Spell theSpell)
    {
        CardActivation spellCard = new CardActivation(theSpell.getName()); 
        addObject(spellCard, 200, 300);
        Greenfoot.playSound("Spell Activate.wav");
        for (int i = 0; i < 15; i++)
        {
            spellCard.changeFrame();
            Greenfoot.delay(5);
        }

        removeObject(spellCard);

        if (Item.partOfHarmful(theSpell.getID()))
        {
            if (theSpell.getID() == 1 || theSpell.getID() == 27)
            {
                for (int i = 0; i < 18; i++)
                {
                    for (int j = 0; j < thePlayers.size(); j++)
                    {
                        if (j != playerTurn - 1 && thePlayers.get(j).getRow() == thePlayers.get(playerTurn-1).getRow() && thePlayers.get(j).getCol() == i)
                            thePlayers.get(j).deductLP(200);
                    }
                }
                for (int i = 0; i < 14; i++)
                {
                    for (int j = 0; j < thePlayers.size(); j++)
                    {
                        if (j != playerTurn - 1 && thePlayers.get(j).getCol() == thePlayers.get(playerTurn-1).getCol() && thePlayers.get(j).getRow() == i)
                            thePlayers.get(j).deductLP(200);
                    }
                }
            }
            else if (theSpell.getID() == 4 || theSpell.getID() == 24)
            {
                for (int i = 0; i < 18; i++)
                {
                    for (int j = 0; j < thePlayers.size(); j++)
                    {
                        if (j != playerTurn - 1 && thePlayers.get(j).getRow() == thePlayers.get(playerTurn-1).getRow() && thePlayers.get(j).getCol() == i)
                            thePlayers.get(j).deductLP(100);
                    }
                }
                for (int i = 0; i < 14; i++)
                {
                    for (int j = 0; j < thePlayers.size(); j++)
                    {
                        if (j != playerTurn - 1 && thePlayers.get(j).getCol() == thePlayers.get(playerTurn-1).getCol() && thePlayers.get(j).getRow() == i)
                            thePlayers.get(j).deductLP(100);
                    }
                }
            }
        }

        else if (Item.partOfHealing(theSpell.getID()))
        {
            if (theSpell.getID() == 11 || theSpell.getID() == 2)
            {
                thePlayers.get(playerTurn-1).gainLP(100);
            }
            else if (theSpell.getID() == 9 || theSpell.getID() == 13)
            {
                thePlayers.get(playerTurn-1).gainLP(200);
            }
        }
        
        else 
            System.out.println("LOL SORRY I DIDNT HAVE TIME TO PROGRAM THESE CARDS :D!!!!!!!");

        Greenfoot.delay(5);

    }

    public void equipContents()
    {
        ArrayList<String> theContents = new ArrayList<String>();
        ArrayList<Item> playerInventory = thePlayers.get(playerTurn - 1).getInventory();

        for (int i = 0; i < playerInventory.size(); i++)
        {
            String itemDesc = "";
            if (playerInventory.get(i) instanceof Weapon)
                itemDesc += playerInventory.get(i).getName() + " (Weapon) ";
            else if (playerInventory.get(i) instanceof Shield)
                itemDesc += playerInventory.get(i).getName() + " (Shield) ";
            if (playerInventory.get(i).getName().equals(thePlayers.get(playerTurn-1).getEquippedWeaponName()) || playerInventory.get(i).getName().equals(thePlayers.get(playerTurn-1).getEquippedShieldName()))
                itemDesc += " - Equipped";
            if (!itemDesc.equals(""))
                theContents.add(itemDesc);
        }
        theContents.add("Go Back");
        String[] contentsAsArray = new String[theContents.size()];

        for (int i = 0; i < theContents.size(); i++)
            contentsAsArray[i] = theContents.get(i);

        Object viewChosen = JOptionPane.showInputDialog(null, "Choose which to equip:", "Your equippable inventory", JOptionPane.QUESTION_MESSAGE, null, contentsAsArray, contentsAsArray[0]);
        while (!((String)viewChosen).equals("Go Back"))
        {
            String theItemToDisplay = ((String)viewChosen).substring(0, ((String)viewChosen).indexOf("(") - 1 );

            r: for (int i = 0; i < playerInventory.size(); i++)
            {
                if (playerInventory.get(i).getName().equals(theItemToDisplay))
                {
                    if (playerInventory.get(i) instanceof Shield)
                    {
                        thePlayers.get(playerTurn - 1).setNewShield(playerInventory.get(i));
                        break r;
                    }
                    else if (playerInventory.get(i) instanceof Weapon)
                    {
                        thePlayers.get(playerTurn - 1).setNewWeapon(playerInventory.get(i));
                        break r;
                    }
                }
            }

            CardImage viewCard = new CardImage("Cards/" + theItemToDisplay + ".jpg");
            addObject(viewCard, 200, 300);
            Greenfoot.delay(50);
            JOptionPane.showMessageDialog(null, "Item equipped!");
            removeObject(viewCard);
            Greenfoot.delay(50);
            viewChosen = JOptionPane.showInputDialog(null, "Choose which to equip:", "Your equippable inventory", JOptionPane.QUESTION_MESSAGE, null, contentsAsArray, contentsAsArray[0]);
        }
    }

    public void printInventoryContents()
    {
        ArrayList<String> theContents = new ArrayList<String>();
        ArrayList<Item> playerInventory = thePlayers.get(playerTurn - 1).getInventory();

        for (int i = 0; i < playerInventory.size(); i++)
        {
            String itemDesc = "";
            if (playerInventory.get(i) instanceof Weapon)
                itemDesc += playerInventory.get(i).getName() + " (Weapon) ";
            else if (playerInventory.get(i) instanceof Shield)
                itemDesc += playerInventory.get(i).getName() + " (Shield) ";
            else
                itemDesc += playerInventory.get(i).getName() + " (Spell) ";

            if (playerInventory.get(i).getName().equals(thePlayers.get(playerTurn-1).getEquippedWeaponName()) || playerInventory.get(i).getName().equals(thePlayers.get(playerTurn-1).getEquippedShieldName()))
                itemDesc += " - Equipped";

            theContents.add(itemDesc);
        }
        theContents.add("Go Back");

        String[] contentsAsArray = new String[theContents.size()];

        for (int i = 0; i < theContents.size(); i++)
            contentsAsArray[i] = theContents.get(i);

        Object viewChosen = JOptionPane.showInputDialog(null, "View any?", "Your inventory", JOptionPane.QUESTION_MESSAGE, null, contentsAsArray, contentsAsArray[0]);
        while (!((String)viewChosen).equals("Go Back"))
        {
            String theItemToDisplay = ((String)viewChosen);
            CardImage viewCard = new CardImage("Cards/" + theItemToDisplay.substring(0, theItemToDisplay.indexOf("(") - 1 ) + ".jpg");

            addObject(viewCard, 200, 300);
            Greenfoot.delay(50);
            JOptionPane.showMessageDialog(null, "Click OK when you are done viewing");
            removeObject(viewCard);
            Greenfoot.delay(50);
            viewChosen = JOptionPane.showInputDialog(null, "View any?", "Your inventory", JOptionPane.QUESTION_MESSAGE, null, contentsAsArray, contentsAsArray[0]);
        }
    }

    public int defenderOnSquare()
    {
        int x = thePlayers.get(playerTurn - 1).getRow();
        int y = thePlayers.get(playerTurn - 1).getCol();
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (i != playerTurn - 1 && thePlayers.get(i).getRow() == x && thePlayers.get(i).getCol() == y)
                return i;
        }
        return -1;
    }

    public boolean onTheSameSpace()
    {
        int x = thePlayers.get(playerTurn - 1).getRow();
        int y = thePlayers.get(playerTurn - 1).getCol();
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (i != playerTurn - 1 && thePlayers.get(i).getRow() == x && thePlayers.get(i).getCol() == y)
                return true;
        }
        return false;
    }

    public Item getFromTreasureChest()
    {
        int firstRoll = diceRoll("the first dice roll to get your item", thePlayers.get(playerTurn - 1), false);
        int secondRoll = diceRoll("the second dice roll to get your item", thePlayers.get(playerTurn - 1), false);

        Item myItem = Item.getAppropriateItem(firstRoll, secondRoll);

        CardImage cardShow = myItem.getCardImage();
        addObject(cardShow, 200, 300);
        Greenfoot.delay(50);
        JOptionPane.showMessageDialog(null, "You rolled a " + firstRoll + " and a " + secondRoll + " to get " + myItem.getName());
        removeObject(cardShow);
        Greenfoot.delay(50);
        return Item.getAppropriateItem(firstRoll, secondRoll);
    }

    public String[] getPossibleMoves()
    {
        ArrayList<String> moves = new ArrayList<String>();

        int upLocation = thePlayers.get(playerTurn - 1).getRow() - 1;
        int downLocation = thePlayers.get(playerTurn - 1).getRow() + 1;
        int leftLocation = thePlayers.get(playerTurn - 1).getCol() - 1;
        int rightLocation = thePlayers.get(playerTurn - 1).getCol() + 1;
        int curX = thePlayers.get(playerTurn - 1).getCol();
        int curY = thePlayers.get(playerTurn - 1).getRow();

        if (upLocation < 14 && upLocation >= 0 && !gameBoard[upLocation][curX].getGroundType().equals("none"))
        {moves.add("up");}

        if (downLocation < 14 && downLocation >= 0 && !gameBoard[downLocation][curX].getGroundType().equals("none"))
        {moves.add("down");}

        if (leftLocation < 18 && leftLocation >= 0 && !gameBoard[curY][leftLocation].getGroundType().equals("none"))
        {moves.add("left");}

        if (rightLocation < 18 && rightLocation >= 0 && !gameBoard[curY][rightLocation].getGroundType().equals("none"))
        {moves.add("right");}

        String[] arrayOfMoves = new String[moves.size()];
        for (int i = 0; i < moves.size(); i++)
            arrayOfMoves[i] = moves.get(i);

        return arrayOfMoves;
    }

    public int diceRoll(String determineWhat, PlayerInfo who, boolean rethrow)
    {
        if (!rethrow)
            JOptionPane.showMessageDialog(null, who.getName() + ": Press OK to roll dice to determine " + determineWhat);
        else
            JOptionPane.showMessageDialog(null, who.getName() + ": Taken! Press OK to roll dice to determine " + determineWhat + " again");
        playDiceAnimation();
        int result = Greenfoot.getRandomNumber(6) + 1;
        DiceResult throwResult = new DiceResult(result);
        addObject(throwResult, 866, 190);
        Greenfoot.delay(100);
        removeObject(throwResult);
        return result;
    }

    public void playDiceAnimation()
    {
        RollingDice myRollingDice = new RollingDice();
        addObject(myRollingDice, 866, 190);
        for (int frame = 0; frame < 16; frame++)
        {
            myRollingDice.changeFrame();
            Greenfoot.delay(5);
        }
        removeObject(myRollingDice);
    }

    public void setCharacterLocations()
    {
        if (findMostRecentlyNotInitialized() == -1)
            return;
        r: for (int i = 0; i < thePlayers.size(); i++)
        {
            if (!thePlayers.get(i).isInitialized())
            {
                int result = diceRoll("starting position", thePlayers.get(i), false);
                while (startSpaceTaken(result))
                    result = diceRoll("starting position", thePlayers.get(i), true);
                switch (result)
                {
                    case 1:
                    {
                        playerAvatarsOnBoard.add(thePlayers.get(i).initialize(0, 0, 1));
                        addObject(playerAvatarsOnBoard.get(i), 40, 40);
                        break;
                    }
                    case 2:
                    {
                        playerAvatarsOnBoard.add(thePlayers.get(i).initialize(5, 0, 2));
                        addObject(playerAvatarsOnBoard.get(i), 240, 40);
                        break;
                    }
                    case 3:
                    {
                        playerAvatarsOnBoard.add(thePlayers.get(i).initialize(13, 0, 3));
                        addObject(playerAvatarsOnBoard.get(i), 560, 40);
                        break;
                    }
                    case 4:
                    {
                        playerAvatarsOnBoard.add(thePlayers.get(i).initialize(3, 13, 4));
                        addObject(playerAvatarsOnBoard.get(i), 160, 560);
                        break;
                    }
                    case 5:
                    {
                        playerAvatarsOnBoard.add(thePlayers.get(i).initialize(12, 13, 5));
                        addObject(playerAvatarsOnBoard.get(i), 520, 560);
                        break;
                    }
                    case 6:
                    {
                        playerAvatarsOnBoard.add(thePlayers.get(i).initialize(17, 13, 6));
                        addObject(playerAvatarsOnBoard.get(i), 720, 560);
                        break;
                    }
                } 
                playerInitialized++;
                break r;
            }
        }

        playerTurn = 1;
    }

    public int findMostRecentlyNotInitialized()
    {
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (!thePlayers.get(i).isInitialized())
                return i;
        }
        return -1;
    }

    public boolean startSpaceTaken(int spaceNumber)
    {   
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (thePlayers.get(i).getStartSpace() == spaceNumber)
                return true;
        }
        return false;
    }

    public boolean allInitialized()
    {
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (!thePlayers.get(i).isInitialized())
                return false;
        }
        return true;
    }

    public boolean someoneWon()
    {
        int lossCount = 0;
        for (int i = 0; i < thePlayers.size(); i++)
        {
            if (thePlayers.get(i).getLP() <= 0)
                lossCount++;
        }
        return lossCount == thePlayers.size() - 1;
    }

    public void initBoard()
    {
        for (int j = 0; j < 14; j++)
            for (int i = 0; i < 18; i++)
                gameBoard[j][i] = new Terrain("stone");

        gameBoard[0][2] = new Terrain("none");
        gameBoard[0][3] = new Terrain("none");
        gameBoard[0][4] = new Terrain("none");
        gameBoard[0][7] = new Terrain("none");
        gameBoard[0][8] = new Terrain("none");
        gameBoard[0][9] = new Terrain("none");
        gameBoard[0][10] = new Terrain("none");
        gameBoard[0][11] = new Terrain("none");
        gameBoard[0][12] = new Terrain("none");
        gameBoard[0][15] = new Terrain("none");
        gameBoard[0][16] = new Terrain("none");
        gameBoard[0][17] = new Terrain("none");
        gameBoard[1][1] = new Terrain("none");
        gameBoard[1][2] = new Terrain("none");
        gameBoard[1][3] = new Terrain("none");
        gameBoard[1][4] = new Terrain("none");
        gameBoard[1][6] = new Terrain("none");
        gameBoard[1][7] = new Terrain("none");
        gameBoard[1][8] = new Terrain("none");
        gameBoard[1][9] = new Terrain("none");
        gameBoard[1][10] = new Terrain("none");
        gameBoard[1][11] = new Terrain("none");
        gameBoard[1][12] = new Terrain("none");
        gameBoard[1][13] = new Terrain("none");
        gameBoard[1][15] = new Terrain("none");
        gameBoard[1][16] = new Terrain("none");
        gameBoard[1][17] = new Terrain("none");
        gameBoard[2][8] = new Terrain("none");
        gameBoard[2][9] = new Terrain("none");
        gameBoard[3][8] = new Terrain("none");
        gameBoard[3][9] = new Terrain("none");
        gameBoard[6][8] = new Terrain("none");
        gameBoard[6][9] = new Terrain("none");
        gameBoard[7][8] = new Terrain("none");
        gameBoard[7][9] = new Terrain("none");
        gameBoard[10][8] = new Terrain("none");
        gameBoard[10][9] = new Terrain("none");
        gameBoard[11][8] = new Terrain("none");
        gameBoard[11][9] = new Terrain("none");
        gameBoard[12][0] = new Terrain("none");
        gameBoard[12][1] = new Terrain("none");
        gameBoard[12][2] = new Terrain("none");
        gameBoard[12][4] = new Terrain("none");
        gameBoard[12][5] = new Terrain("none");
        gameBoard[12][6] = new Terrain("none");
        gameBoard[12][7] = new Terrain("none");
        gameBoard[12][8] = new Terrain("none");
        gameBoard[12][9] = new Terrain("none");
        gameBoard[12][10] = new Terrain("none");
        gameBoard[12][11] = new Terrain("none");
        gameBoard[12][13] = new Terrain("none");
        gameBoard[12][14] = new Terrain("none");
        gameBoard[12][15] = new Terrain("none");
        gameBoard[12][16] = new Terrain("none");
        gameBoard[13][0] = new Terrain("none");
        gameBoard[13][1] = new Terrain("none");
        gameBoard[13][2] = new Terrain("none");
        gameBoard[13][5] = new Terrain("none");
        gameBoard[13][6] = new Terrain("none");
        gameBoard[13][7] = new Terrain("none");
        gameBoard[13][8] = new Terrain("none");
        gameBoard[13][9] = new Terrain("none");
        gameBoard[13][10] = new Terrain("none");
        gameBoard[13][13] = new Terrain("none");
        gameBoard[13][14] = new Terrain("none");
        gameBoard[13][15] = new Terrain("none");
        gameBoard[0][1] = new Terrain("treasure chest");
        gameBoard[0][6] = new Terrain("treasure chest");
        gameBoard[0][14] = new Terrain("treasure chest");
        gameBoard[2][10] = new Terrain("treasure chest");
        gameBoard[2][17] = new Terrain("treasure chest");
        gameBoard[3][3] = new Terrain("treasure chest");
        gameBoard[3][6] = new Terrain("treasure chest");
        gameBoard[3][13] = new Terrain("treasure chest");
        gameBoard[3][3] = new Terrain("treasure chest");
        gameBoard[3][6] = new Terrain("treasure chest");
        gameBoard[3][13] = new Terrain("treasure chest");
        gameBoard[4][0] = new Terrain("treasure chest");
        gameBoard[5][16] = new Terrain("treasure chest");
        gameBoard[6][3] = new Terrain("treasure chest");
        gameBoard[6][7] = new Terrain("treasure chest");
        gameBoard[6][10] = new Terrain("treasure chest");
        gameBoard[6][13] = new Terrain("treasure chest");
        gameBoard[8][1] = new Terrain("treasure chest");
        gameBoard[9][14] = new Terrain("treasure chest");
        gameBoard[9][17] = new Terrain("treasure chest");
        gameBoard[10][4] = new Terrain("treasure chest");
        gameBoard[10][11] = new Terrain("treasure chest");
        gameBoard[11][0] = new Terrain("treasure chest");
        gameBoard[11][7] = new Terrain("treasure chest");
        gameBoard[13][4] = new Terrain("treasure chest");
        gameBoard[13][11] = new Terrain("treasure chest");
        gameBoard[13][16] = new Terrain("treasure chest");
        gameBoard[0][0] = new Terrain("dice-1");
        gameBoard[0][5] = new Terrain("dice-2");
        gameBoard[0][13] = new Terrain("dice-3");
        gameBoard[13][3] = new Terrain("dice-4");
        gameBoard[13][12] = new Terrain("dice-5");
        gameBoard[13][17] = new Terrain("dice-6");
    }

    public void addBoard()
    {
        for (int j = 0; j < 14; j++)
            for (int i = 0; i < 18; i++)
                addObject(gameBoard[j][i], 40 * (i+1), 40 * (j+1));
    }
}
