/* PlayerResponse
 *
 *
 *Copyright 2017 Johnny Paradise for Paradise Intergalactic Enterprises.
 *
 *This program is licensed under the GPL.  Please see COPYING for more information.
 *
 * The opposite of giving a turn -- this is the object that stores what a command has done
 */
 
package spacefight2.game;

import spacefight2.game.*;

public class PlayerResponse {

   private int attackerId              = -1;
   private int attackerLocation        = -1;       // Can't have anonymous hurting now can we
   private boolean commandSuccess;                 // If the command was successful
   private int energyModifier          = 0;
   private int playerId                = -1;
   private int target                  = -1;
   private boolean wasDamaged          = false;    // Aww, did someone hurt you?  
   
   private PlayerCommand command; 
   
   public PlayerResponse()
   {
   
   }
   
   public PlayerCommand getPlayerCommand()
   {
      return command;
   }
   
   public int getAttacker()
   {
      return attackerId;
   }

   public int getAttackerLoc()
   {
      return attackerLocation;
   }
   
   public boolean getDamaged()
   {
      return wasDamaged;
   }
   
   public int getEnergy()
   { 
      return energyModifier;
   }
   
   public int getPlayer()
   {
      return playerId;
   }
   
   public boolean getSuccess()
   {
      return commandSuccess;
   }
 
   public int getTarget()
   {
      return target;
   }
   
   public int modEnergy(int e)
   {
      energyModifier += e;
      
      return energyModifier;
   }
   
   public boolean setAttacker(int a)
   {
      if (a >= -1)
      {
         attackerId = a;
         return true;
      }
      else
         return false;
   }

   public boolean setAttackerLoc(int l)
   {
      if (l >= -1)
      {
         attackerLocation = l;
         return true;
      }
      else
         return false;
   }
   
   public boolean setDamaged(boolean d)
   {
      wasDamaged = d;

      return true;
   }
     
   public boolean setEnergy(int e)
   {
      energyModifier = e;
      
      return true;
   }
   
   public boolean setPlayer(int p)
   {
      playerId = p;
      return true;
   }
   
   public boolean setPlayerCommand(PlayerCommand c)
   {
      command = c;
        
      return true;  
   }
    
   public boolean setSuccess(boolean s)
   {
      boolean previousVal = commandSuccess;
      commandSuccess = s;
      
      return previousVal;
   }
   
   public boolean setTarget(int t)
   {
      if (t >= 0 && t < SpaceFightConfig.NUM_UNIVERSES)
      {
         target = t;
         return true;
      }
      else
         return false;
   }
         
}       