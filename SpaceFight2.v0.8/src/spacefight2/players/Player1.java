/* Player2.java
 *
 * AI for SpaceFight2.  
 * She just wants to keep shields up and then blam.
 *
 *
 *Copyright 2017 Johnny Paradise for Paradise Intergalactic Enterprises.
 *
 *This program is licensed under the GPL.  Please see COPYING for more information.
 *
 */

package spacefight2.players;

import spacefight2.game.*;
import java.lang.Math;

public class Player1 {

   private String playerName           = new String("Passive Peoples");
   private int target                  = -1;

   public Player1()
   {

   }
   
   public String getPlayerName()
   {
      return playerName;
   }
   
   public PlayerCommand playTurn(Universe u)
   {
      // This player is going to put up shields and absorb a lot
      PlayerCommand command   = new PlayerCommand();
      int potentialTarget     = -1;
 
      // Let's first rely on the shields     
      if (u.getTimesShielded() < 1)
      {
         command.setAction("shields");
         return command;
      }
      
      // So we've been putting up our shields too much.  Let's try an absorb unless someone's shooting at us
      if (target == -1 && u.getEnergy() < SpaceFightConfig.MAX_ENERGY)
      {
         command.setAction("absorb");
      }
      else
      {
         // We know we'll be shooting
         command.setAction("shoot");  

         // Do we have a target?  We're going to shoot minimal energy each time, unless we can spare half
         if (target >= 0)
         {
            if (u.getEnergy() > (SpaceFightConfig.MAX_ENERGY / 2))
               command.setEnergyUsage(SpaceFightConfig.MAX_ENERGY / 2);
            else     
               command.setEnergyUsage(SpaceFightConfig.ENERGY_PER_ROUND - SpaceFightConfig.ACTION_SHOOT_COST); 
         }
         else // We haven't found a valid target yet and will just shoot for minimal amounts
         {
            command.setEnergyUsage(1);
            
            // Find a target that isn't us
            while (target == -1)
            {
               potentialTarget = (int)(Math.random() * SpaceFightConfig.NUM_UNIVERSES);               
               if (potentialTarget == u.getLocation())   // We don't want to shoot ourselves
                  continue;
               else
                  target = potentialTarget;
            }
         }
         
         // Set our target
         command.setTarget(target);
      }
   
      // Now return our command
      return command;

   }
   
   public void processTurn(PlayerResponse p)
   {
      // Lets see if we need to erase our target -- but only if we shoot
      if (p.getSuccess() == false && p.getPlayerCommand().getAction().equals("shoot"))
         target = -1;
         
      // See if someone shot us, switch to them if so
      if (p.getAttackerLoc() >= 0)
         target = p.getAttackerLoc();
      
   }
}