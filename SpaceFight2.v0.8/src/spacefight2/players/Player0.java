/* Player1.java
 *
 * AI for SpaceFight2.  
 * He just wants to pew pew pew.
 *Copyright 2017 Johnny Paradise for Paradise Intergalactic Enterprises.
 *
 *This program is licensed under the GPL.  Please see COPYING for more information.
 *
 *
 */

package spacefight2.players;
import java.lang.Math;
import spacefight2.game.*;
 


public class Player0 {

   private int currentTarget           = -1;
   private String playerName           = new String("Aggressive Jerk");
   private boolean someoneShotMe       = false;

   public Player0()
   {
   
   }
   
   // getName()
   //
   // returns the playername
   public String getName()
   {
      return playerName;
   }
   
   // playTurn
   // This method must return a command -- this is where your universe does it's thing.
   // Passes in a Universe object reflecting your current uni state
   public PlayerCommand playTurn(Universe myUniverse)
   {
      // Setup our command and other local vars...all we're going to do is attack.  It's just pew pew pew all day long here.
      PlayerCommand command   = new PlayerCommand();
      int potentialTarget     = -1;
      
      // Shoot.  We almost always shoot.
      if (someoneShotMe && myUniverse.getTimesShielded() < 2)
      {
         // Drop our flag, raise our shields, return our command
         someoneShotMe = false;   
         command.setAction("shields");
         return command;
      }
     else  // We're going to shoot.  We like shooting.
      {
         command.setAction("shoot");
         
         // Now lets set our target.  See if we have a target first
         if (currentTarget >= 0) // We must have a valid target, so lets pew pew pew at full power
            command.setEnergyUsage(myUniverse.getEnergy() - SpaceFightConfig.ACTION_SHOOT_COST);
         else
         {
            // We're going to do test fires until we've found a target
            command.setEnergyUsage(1);    
         
            // Here's how we find a random target
            while (currentTarget == -1)
            {
               potentialTarget = (int)(Math.random() * getNumUniverses());    // Will generate between 0 and 1 below max
      
               // Make sure we don't shoot ourselves--yes it is allowed
               if (potentialTarget == myUniverse.getLocation())
                  continue;
               else
                  currentTarget = potentialTarget;
            }   
         }
         
         // Great, we've got a target.  Lets shoot them.         
         command.setTarget(currentTarget);

         // Now return our command    
         return command;
      }
   }
   
   public void processTurn(PlayerResponse p)
   {
         // See if we got shot and didn't have shields up.
         if (p.getAttackerLoc() >= 0 && p.getPlayerCommand().getAction().equals("shields") == false)
         {
            someoneShotMe = true;
            
            // If we don't have a valid target, well we do now
            if (currentTarget == -1)
               currentTarget = p.getAttackerLoc();
         }
         // else we'll see 
         else if (p.getSuccess() == false && p.getPlayerCommand().getAction().equals("shoot"))     
            currentTarget = -1;     // reset our target.  Maybe we should save that missed shot data?
         // else we pew pew pew until they ded ded ded
   }
}
