/* SpaceFight class 
 *
 * Runs the game
 */
 
package spacefight2.game;

// SF2 imports
import spacefight2.players.*;
import spacefight2.game.*;

// Java imports
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.lang.Math;
import java.lang.System;
 
public class SpaceFight {
 
   // Class properties
   private boolean            hasWinner         = false;
   private int                infoLevel         = SpaceFightConfig.INFO_LEVEL_DEBUG;
   private int                match             = 0;
   private int                numMatches        = 1;
   private int                numPlayers        = 0;                              
   private int                numUniverses      = SpaceFightConfig.DEFAULT_NUM_UNIVERSES;
   private List<Object>       players           = new ArrayList<Object>();          // Holds the Class data for each player's implementation -- uses reflection to include
   private PlayerCommand[]    playerCommands;
   private List<String>       playerNames       = new ArrayList<String>();
   private PlayerResponse[]   playerResponses;                                     
   private int                roundCounter      = 0;
   private String             runLevel          = new String("i");      
   private boolean            tieGame           = false;                            // Condition reached when max_rounds is reached
   private Universe[]         universes;                                    // Holds player state data
   private int                winnerPlayerId    = -1;

   public SpaceFight()
   {
      System.out.println("Creating Simon Burdick Presents:  SpaceFight2:  Space Harder");
   
   
   }
    
   // addPlayer
   // 
   // Takes a string w/the name of the class file.  Will include it in.
   public void addPlayer(String playerName) 
   {
      //boolean foundName = true;  // We'll set otherwise
      
            
      // Make sure we don't have too many players already
      if (getNumPlayers() >= numUniverses)
      {
         if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
            System.out.println("SpaceFight::addPlayer -- Too many players");
        
      }
      else
      {
         if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG) 
            System.out.println("SpaceFight::addPlayer Including Player " + playerName + "...");
         
         // Do this the once
         playerNames.add(playerName);
         setNumPlayers(getNumPlayers() + 1);
      }
   
   }
   
   // checkPlanetDeath
   //
   // See if any universe is below 0 energy and destroy a planet if so
   private void checkPlanetDeath()
   {
      for (Universe u: universes)
      {
         if (u.getEnergy() < 0 && !u.getDestroyed())
         {
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
               System.out.println("Player " + playerNames.get(u.getPlayer()) + " has lost a planet!");
            
            u.modPlanets(-1);
            
            // Now reset their energy
            u.setEnergy(SpaceFightConfig.STARTING_ENERGY);
         }
      }   
   }
  
   // checkPlayerDeath
   // 
   // See if anyone has 0 planets left, if so, take them out of the game
   private void checkPlayerDeath()
   {
      for (Universe u: universes)
      {
         if (u.getPlanets() == 0 && !u.getDestroyed())
         {
            // Welp they'ze dead
            u.setDestroyed();
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
               System.out.println("Player " + playerNames.get(u.getPlayer()) + " has been destroyed!");
         }
      }
   }
   
   private void collectCommands()
   {
      for (int i = 0; i < getNumPlayers(); i++)
      {
         // Lets see if they're destroyed before collecting a command
         if (universes[i].getDestroyed())
         {
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
               System.out.println("SpaceFight::tick -- Skipping turn for player " + playerNames.get(i));
            
            continue;
         }
         else
         {
            // Lets see what happens...
            try 
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
                  System.out.println("SpaceFight::collectCommands -- Soliciting turn for player " + playerNames.get(i));
               
               Class playerClass = Class.forName("spacefight2.players." + playerNames.get(i));
               Method m = playerClass.getDeclaredMethod("playTurn", Universe.class);
              
               // Lastly, lets call the method.
               Object returnObject  = m.invoke(players.get(i), new Object[]{universes[i]});
               playerCommands[i]    = (PlayerCommand)returnObject;
               
               // Set our playerId
               playerCommands[i].setPlayer(i);
               
            }
            catch (ClassNotFoundException e) 
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
                  System.out.println(e.getMessage());
               
               System.exit(SpaceFightConfig.ERROR_CLASSNOTFOUND_EXCEPTION);
            }
            catch (NoSuchMethodException e)
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
                  System.out.println(e.getMessage());
            
               System.exit(SpaceFightConfig.ERROR_NOSUCHMETHODEXCEPTION_EXCEPTION);
            }
            catch (IllegalAccessException e)
            {
               System.exit(SpaceFightConfig.ERROR_ILLEGALACCESSEXCEPTION_EXCEPTION);
            }
            catch (InvocationTargetException e)
            {
               System.exit(SpaceFightConfig.ERROR_INVOCATIONTARGETEXCEPTION_EXCEPTION);
            } 
         }
      }    
   }
   
     
   // getActivePlayers
   //
   // Gets all players not currently destroyed
   public int getActivePlayers()
   {
      int stillAlive = 0;
      
      // See how many have been destroyed
      for (Universe u: universes)
         if (!u.getDestroyed())
            stillAlive++;
      
      return stillAlive;           
   }

    
   // getGameOver()
   //
   // Returns based on hasWinner
   public boolean getGameOver()
   {
      return (hasWinner || tieGame);
   }

   public int getInfoLevel()
   {
      return infoLevel;
   }
   
   public int getMatch()
   {
      return match;
   }
      
   public int getNumMatches()
   {
      return numMatches;
   }
   
   // getNumPlayers()
   //
   // Returns the number of players currently in the game
   public int getNumPlayers()
   {
      return numPlayers;
   }
   
   // getNumUniverses()
   // 
   // 
   public int getNumUniverses()
   {
      return SpaceFightConfig.DEFAULT_NUM_UNIVERSES;;
   }
   
   // getPlayerId(int loc)
   // 
   //
   public int getPlayerId(int loc)
   {
      for (Universe u: universes)
         if (u.getLocation() == loc)
            return u.getPlayer();
         
      // We didn't find anything, return a negative roger
      return -1;
   }
   
   // getRandomUniverse
   // 
   // 1:  Default
   // 2:  Returns a random int between 0 and MAX_UNIVERSES.  Pass a boolean to indicate if needs to be empty (no players)
   private int getRandomUniverse()
   {
      return getRandomUniverse(false);
   }
   
   private int getRandomUniverse(boolean mustBeEmpty)
   {
      int r = -1;
      
      // Loop until we have a valid unoccupied, if necessary
      while (r < 0)
      {
         r = (int)(Math.random() * numUniverses); // Will generate between 0 and (numUniverses - 1);
      
         // See if it's occupied and reset if we care
         if (mustBeEmpty && hasPlayer(r))
            r = -1;
      }
            
      // Now give it back
      return r;
         
   }

   // getRoundCounter
   //
   // Returns num of rounds played
   public int getRounds()
   {
      return roundCounter;
   }
   
   public String getRunLevel()
   {
      return runLevel;
   }


   // getWinner
   //
   // Returns playerId of winner, -1 if no winner, and 
   public int getWinner()
   {
      return winnerPlayerId;
   }
   
   public String getPlayerName(int pId)
   {
      return playerNames.get(pId);
   }

   // giveEnergy
   //
   // Gives energy to all universes
   private void giveEnergy(int e)
   {
      for (Universe u: universes)
         u.modEnergy(e);
   }
   
   // hasPlayer
   //
   // Determines if a location is occupied by a player or just an empty spot :(
   public boolean hasPlayer(int loc)
   {
      
      // Lets loop through our Universe's and see now shall we...
      for (Universe u: universes)
         if (u.hasLocation() && u.getLocation() == loc)
            return true;            // May as well bail
   
      // Hey we found the nothing
      return false;
   
   }
   
   private void incRound()
   {
      roundCounter++;
   }
   
   private void incMatch()
   {
      match++;
   }
   
      
   // prepResponses
   //
   // Gets responses ready by adding the commands to them
   private void prepResponses()
   {
      for (int i = 0; i < getNumPlayers(); i++)
      {
         playerResponses[i].setPlayerCommand(playerCommands[i]);
         playerResponses[i].setPlayer(i);
      }
   }

   // processBlanks()
   //
   // Checks for a blank playerCommand
   private void processBlanks()
   {
      // Loop through our commands and look for blanks or commands not recognized
      for (PlayerCommand c: playerCommands)
      {   
         // Check if they're destroyed or the command is valid
         if (universes[c.getPlayer()].getDestroyed() || Arrays.asList(SpaceFightConfig.ACTIONS_LIST).contains(c.getAction()))
            continue;   // We can continue to the next
         else
         {
            if (c.getAction().length() == 0)
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                  System.out.println("Player " + playerNames.get(c.getPlayer()) + " is going to just lay there like a dead fish.  No command given!");
            }
            else
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                  System.out.println("Player " + playerNames.get(c.getPlayer()) + " is trying a new tactic:  " + c.getAction() + ".  Bold strategy Cotton!");
            }
         }
      }     
   }
      
   // processShields
   //
   // Puts shields up for all players that raised 'em
   private void processShields()
   {
      for (PlayerCommand c: playerCommands)
      {
         if (c.getAction().equals("shields"))
         {
            // Set the shields, adjust their energy.  
            universes[c.getPlayer()].setShields(true);
            universes[c.getPlayer()].incTimesShielded();
            
            // Now fill out the response data
            playerResponses[c.getPlayer()].setSuccess(true);
            playerResponses[c.getPlayer()].modEnergy(SpaceFightConfig.ACTION_SHIELDS_COST * -1);
            playerResponses[c.getPlayer()].setTarget(universes[c.getPlayer()].getLocation());
            
            // Log the action
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
               System.out.println("Player " + playerNames.get(c.getPlayer()) + " has put shields with reduction factor: " + Double.toString(universes[c.getPlayer()].getShieldReduction()));
                
         }
         else
         {
            if (universes[c.getPlayer()].getTimesShielded() > 0)
               universes[c.getPlayer()].decTimesShielded();
         }
      }
   }
   
   // processAbsorbs
   //
   // Sets up a player that chooses to try and absorb an attack
   private void processAbsorbs()
   {
      for (PlayerCommand c: playerCommands)
      {
         if (c.getAction().equals("absorb"))
         {
            // Configure the universe first
            //universes[c.getPlayer()].modEnergy(SpaceFightConfig.ACTION_ABSORB_COST * -1);
            universes[c.getPlayer()].setAbsorb(true);
            
            // Response data -- true, the cost, and setup the target
            playerResponses[c.getPlayer()].setSuccess(true);
            playerResponses[c.getPlayer()].modEnergy(SpaceFightConfig.ACTION_ABSORB_COST * -1);
            playerResponses[c.getPlayer()].setTarget(universes[c.getPlayer()].getLocation());
            
            // Log it
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
               System.out.println("Player " + playerNames.get(c.getPlayer()) + " is trying an absorb!");
         }
      }
   }
   

   // processShoots
   //
   // Main attack function.  
   private void processShoots()
   {
      int damageDealt         = 0;
      double damageModifier   = SpaceFightConfig.ACTION_SHOOT_DAMAGE_MULTIPLIER;
      double damageReduction  = 0;
      boolean absorbEnergy    = false;
      int playerId            = -1;
      int shootEnergy         = 0;        // How much energy into the shot
      int targetPlayerId      = -1;
      
      for (PlayerCommand c: playerCommands)
      {
         playerId = c.getPlayer();
         
         if (c.getAction().equals("shoot"))
         {
            // Copy the target over
            targetPlayerId = getPlayerId(c.getTarget());
            playerResponses[playerId].setTarget(targetPlayerId);
         
            // Check and possibly adjust the energy usage
            if (universes[playerId].getEnergy() < (SpaceFightConfig.ACTION_SHOOT_COST + c.getEnergyUsage()))
               shootEnergy = universes[playerId].getEnergy() - SpaceFightConfig.ACTION_SHOOT_COST;
               
            else if(SpaceFightConfig.ACTION_SHOOT_COST + c.getEnergyUsage() < 0)
            {
               playerResponses[c.getPlayer()].modEnergy(SpaceFightConfig.ACTION_SHOOT_COST * -1000);
               System.out.println(playerNames.get(playerId) + " tried to play with negative energy and created a black hole. oops...");
            }
            else
               shootEnergy = c.getEnergyUsage();
         
            // Output our shot
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
               System.out.println("Player " + playerNames.get(playerId) + " attempting to shoot sector " + Integer.toString(c.getTarget()) + " for " + Integer.toString(shootEnergy) + " energy.");
               
            // Log the cost for the shooter
            playerResponses[playerId].modEnergy((SpaceFightConfig.ACTION_SHOOT_COST + shootEnergy) * -1);
         
            // First lets see if we have a valid and preferrably living target
            if (hasPlayer(c.getTarget()) && !universes[getPlayerId(c.getTarget())].getDestroyed())
            {
               targetPlayerId = getPlayerId(c.getTarget());
               
               // Well we hit something, log that first
               playerResponses[playerId].setSuccess(true);
               playerResponses[targetPlayerId].setAttacker(c.getPlayer());
               playerResponses[targetPlayerId].setAttackerLoc(universes[playerId].getLocation());
            
               // Well we hit someone, lets see if it ourselves first.  Because that's just dumb and we'll deal quadruple damage if so
               if (targetPlayerId == playerId)
               {
                  damageModifier = SpaceFightConfig.ACTION_SHOOT_SELF_DAMAGE_MODIFIER;
                  
                  if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                     System.out.println("Player " + playerNames.get(playerId) + " has shot themself!");
               }                  
               else
               {
                  // Lets see if the target has shields up
                  if (universes[targetPlayerId].getShields())
                  {
                     damageReduction = universes[targetPlayerId].getShieldReduction();
                     
                     if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                        System.out.println("Shields causing damage reduction " + Double.toString(damageReduction));
                  }
                  else if (universes[targetPlayerId].getAbsorb())
                  {
                     if (Math.random() < SpaceFightConfig.ACTION_ABSORB_CHANCE_ADD_ENERGY)
                     {
                        absorbEnergy = true;
                     }
                  }
               }
               
               // So we've setup our damage modifier, now we need to deal the damage to the target
               //damageDealt = (int)((shootEnergy * damageModifier) * damageReduction);
               damageDealt = (int)((1 - damageReduction) * (shootEnergy * damageModifier));
               
               
               // Now add that to the response for the target -- unless we need to flip damage
               if (absorbEnergy)
               {
                  if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                     System.out.println("Absorbing " + Integer.toString(shootEnergy) + " energy for Player " + playerNames.get(c.getPlayer()));
                  
                  
                  playerResponses[targetPlayerId].modEnergy(shootEnergy);      // We actually get to add it to the universe
                  playerResponses[targetPlayerId].setDamaged(false);
               
               }
               else
               {
                  if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                     System.out.println("Player " + playerNames.get(playerId) + " has shot Player " + playerNames.get(targetPlayerId) + " for " + Integer.toString(damageDealt) + " damage!");
                  
                  playerResponses[targetPlayerId].modEnergy(damageDealt * -1);
                  playerResponses[targetPlayerId].setDamaged(true);
               }
               
               // Log any remaining data
               
            }
            else  // We done missed
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
                  System.out.println("Player " + playerNames.get(playerId) + " missed!");
                  
               playerResponses[playerId].setSuccess(false);
            }                     
         }  
      }
   }
   
   // resetPlayer
   // Blow the player class out and recreate it
   //
   private void resetPlayers() 
   {
      // Reset our players array
      players                 = new ArrayList<Object>();      
      String playerName  = new String("");
      
      for (int i = 0; i < getNumPlayers(); i++)
      {
         playerName = playerNames.get(i);
      
         if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG) 
            System.out.println("SpaceFight::addPlayer Trying to Reset Player " + playerName + "...");
         
         // So lets see if we can do this
         try 
         {
            Class playerClass       = Class.forName("spacefight2.players." + playerName);
            Object player           = playerClass.newInstance();
            
            // Add it to our players List and update our numPlayers
            players.add(player);
               
         } 
         catch (ClassNotFoundException e) 
         {
            System.exit(SpaceFightConfig.ERROR_CLASSNOTFOUND_EXCEPTION);
         } 
         catch (InstantiationException e) 
         {
            System.exit(SpaceFightConfig.ERROR_INSTANTIATION_EXCEPTION);
         } 
         catch (IllegalAccessException e) 
         {
            System.exit(SpaceFightConfig.ERROR_ILLEGALACCESSEXCEPTION_EXCEPTION);
         }
      }
   }   
   
   // resolveResponses
   //
   // Will iterate through all responses and update universes accordingly
   private void resolveResponses()
   {
      for (PlayerResponse r: playerResponses)
      {
         // Update our energy
         if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
            System.out.println("SpaceFight::resolveResponses -- Resolving player " + Integer.toString(r.getPlayer()) + " response, modifying energy by " + Integer.toString(r.getEnergy()));
         
         universes[r.getPlayer()].modEnergy(r.getEnergy());           
      } 
   }
   
   
   // sendResponses
   //
   // Sends the responses back to players
   private void sendResponses()
   {
      // Now return those results to our players
      for (int i = 0; i < getNumPlayers(); i++)
      {
         // First see if they're destroyed
         if (universes[i].getDestroyed())
         {
            if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
               System.out.println("SpaceFight::sendResponses -- Skipping returning results for player " + playerNames.get(i));
            
            continue;
         }
         else
         {
            // Lets see what happens...
            try 
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
                  System.out.println("SpaceFight::tick -- Returning results for player " + playerNames.get(i));
               
               Class playerClass = Class.forName("spacefight2.players." + playerNames.get(i));
               Method m = playerClass.getDeclaredMethod("processTurn", PlayerResponse.class);
              
               // Lastly, lets call the method
               m.invoke(players.get(i), new Object[]{playerResponses[i]});
            }
            catch (ClassNotFoundException e) 
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
                  System.out.println(e.getMessage());
               
               System.exit(SpaceFightConfig.ERROR_CLASSNOTFOUND_EXCEPTION);
            }
            catch (NoSuchMethodException e)
            {
               if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
                  System.out.println(e.getMessage());
               
               System.exit(SpaceFightConfig.ERROR_NOSUCHMETHODEXCEPTION_EXCEPTION);
            }
            catch (IllegalAccessException e)
            {
               System.exit(SpaceFightConfig.ERROR_ILLEGALACCESSEXCEPTION_EXCEPTION);
            }
            catch (InvocationTargetException e)
            {
               System.exit(SpaceFightConfig.ERROR_INVOCATIONTARGETEXCEPTION_EXCEPTION);
            } 
         }
      } 
   }
      
    
   // setGameOver()
   //
   // Determines if we should tick again
   public boolean setGameOver()
   {      
      if (getActivePlayers() == 1)
      {
         // Winner winner chicken dinner
         hasWinner = true;
         setWinner();
         
         
         return true;
      }
      // See if our conditions for a tie exist
      else if (getRounds() >= SpaceFightConfig.MAX_ROUNDS || getActivePlayers() == 0)
      {
         tieGame = true;
         return true;
      }
      else  // We must keep on keepin on
         return false;
            
   }
   
   public boolean setInfoLevel(int i)
   {
      if (i == 0)
      {
         infoLevel = SpaceFightConfig.INFO_LEVEL_MIN;
         return true;
      }
      else if (i == 1)
      {
         infoLevel = SpaceFightConfig.INFO_LEVEL_MATCH + SpaceFightConfig.INFO_LEVEL_MIN;
         return true;
      }
      else if (i == 2)
      {
         infoLevel = SpaceFightConfig.INFO_LEVEL_BATTLE + SpaceFightConfig.INFO_LEVEL_MATCH + SpaceFightConfig.INFO_LEVEL_MIN;
         return true;
      }
      else if (i == 3)
      {
         infoLevel = SpaceFightConfig.INFO_LEVEL_DEBUG + SpaceFightConfig.INFO_LEVEL_BATTLE + SpaceFightConfig.INFO_LEVEL_MATCH + SpaceFightConfig.INFO_LEVEL_MIN;
         return true;
      }
      else
         return false;
   }
   
   public boolean setNumMatches(int n)
   {
      if (n > 0)
      {
         numMatches = n;
         return true;
      }
      else
         return false;
   }   
    
   // setNumPlayers
   //
   // Sets the number of players.  No validations
   public boolean setNumPlayers(int n)
   {
      if (n > 0)
      {
         numPlayers = n;
         return true;
      }
      else
         return false;   
   }
   
   // setRunLevel
   // 
   // Turns continuous play on or off.  Returns prior value
   public boolean setRunLevel(String r)
   {
      if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
         System.out.println("SpaceFight::setContinuous -- Setting continuous to " + r);
   
      if (r.equals("c") || r.equals("C") || r.equals("m") || r.equals("M") || r.equals("i") || r.equals("I") )
      {
         runLevel = r;
         return true;
      }
      else
      {
         runLevel = "i";
         return false;
      }
      
   }
   
   // setWinner
   //
   // Must only be one active player, sets that player to the winner
   private boolean setWinner()
   {      
      int winnersFound = 0;
      
      // See how many have been destroyed
      for (Universe u: universes)
         if (!u.getDestroyed())
         {
            winnerPlayerId = u.getPlayer();
            winnersFound++;
         }
      
      if (winnersFound == 1)
         return true;
      else
         return false;
   }
   
   // start
   // 
   // Sets up the Cluster and players therein
   public void start()
   {
      // Update match num to start.  Clear out our winner
      incMatch();
      winnerPlayerId    = -1;
      hasWinner         = false;
      tieGame           = false;
      zeroRoundCounter();
       
      if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_MATCH) == SpaceFightConfig.INFO_LEVEL_MATCH)
         System.out.println("Starting Match " + Integer.toString(getMatch()));
   
      // Reset our players first      
      resetPlayers();
      
      // Create or reset our references and request the mem
      universes         = new Universe[getNumPlayers()];         // The Universe[] at the beginning is optional
      playerCommands    = new PlayerCommand[getNumPlayers()];
      playerResponses   = new PlayerResponse[getNumPlayers()];
      
      // Now lets create new instances for those
      for (int i = 0; i < getNumPlayers(); i++)
         universes[i] = new Universe();
      
      // Now lets init our universes
      int cnt = 0;
      int nextUniverse = -1;
      for (Universe u: universes)
      {
         // First we've got to find a spot in our cluster that's not occupied
         nextUniverse = getRandomUniverse(true);
         u.setLocation(nextUniverse);
         
         if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG) == SpaceFightConfig.INFO_LEVEL_DEBUG)
            System.out.println("SpaceFight::start -- Creating Universe " + cnt + " at location " + Integer.toString(nextUniverse));
               
         // Now set the index we'll need later
         u.setPlayer(cnt);
      
         
         cnt++;      // Increment
      }
   
      // Done         
      return;
   }
    
    // tick
    //
    // Runs one round of the game
   public boolean tick()
   {
      boolean gameEnded = false;
      
      // Increment our round before anything else
      incRound();
      
      if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_BATTLE) == SpaceFightConfig.INFO_LEVEL_BATTLE)
         System.out.println("-- STARTING ROUND " + getRounds() + " --");
      
      turnReset();
   
      // Now lets award everyone energy
      giveEnergy(SpaceFightConfig.ENERGY_PER_ROUND);
      
      // Collect all our commands
      collectCommands();
      
      // Now that we've got our commands, lets stick those in our responses
      prepResponses();
      
      // Now process them all
      // We'll process commands in this order:  
      // shields -> absorb -> shoot
      
      processShields();
      
      processAbsorbs();
      
      processShoots();
      
      processBlanks();
      
      // Resolve damage, check for planet death, see if any dead players
      resolveResponses();
      checkPlanetDeath();
      checkPlayerDeath();      
      
      // Now send those responses
      sendResponses();
      
      // Last, lets check game over conditions
      gameEnded = setGameOver();    
      
      if (gameEnded)
      {
         if ((getInfoLevel() & SpaceFightConfig.INFO_LEVEL_MATCH) == SpaceFightConfig.INFO_LEVEL_MATCH)
         {
            if (wasTie())
               System.out.println("-- GAME OVER,  Rounds:  " + Integer.toString(getRounds()) + ", Tie.");
            else
               System.out.println("-- GAME OVER,  Rounds:  " + Integer.toString(getRounds()) + ", Winnner: Player " + playerNames.get(getWinner()));            
         }
      }  
      
      return true;
   }
   

   
   // turnReset
   //
   // Resets commands, responses, and clears universes
   private void turnReset()
   {
      // First let's wipe our commands and responses
      for (int i = 0; i < getNumPlayers(); i++)
      {
         // Setup the new command.  This just blanks and provides a base -- we'll most likely get wiped by collecting
         playerCommands[i]    = null;
         playerCommands[i]    = new PlayerCommand();
         playerCommands[i].setPlayer(i);
         
         // Reset our responses
         playerResponses[i]   = null;
         playerResponses[i]   = new PlayerResponse();
         playerResponses[i].setPlayer(i);
         
         // Reset each universe
         universes[i].setShields(false);
         universes[i].setAbsorb(false);
      }
   
   }
   
   public boolean wasTie()
   {
      if (getGameOver() == true && tieGame == true)
         return true;
      else
         return false;
   }
    
   private void zeroRoundCounter() 
   {
      roundCounter = 0;
   }
}