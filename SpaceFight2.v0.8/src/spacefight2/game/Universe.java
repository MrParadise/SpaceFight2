/* Universe.java
 *
 * Contains information about a Universe
  * The thing a player is going to do*
 *Copyright 2017 Johnny Paradise for Paradise Intergalactic Enterprises.
 *
 *This program is licensed under the GPL.  Please see COPYING for more information.
 *
 */

package spacefight2.game;

public class Universe
{
   // Attributes
   private boolean absorbSet     = false;
   private int clusterLocation   = -1;                                               // Where does this Universe exist in the cluster?   
   private int energy            = SpaceFightConfig.STARTING_ENERGY;
   private boolean isDestroyed   = false;
   private int numPlanets        = SpaceFightConfig.DEFAULT_PLANETS_PER_UNIVERSE; 
   private int playerId          = -1;                                        // Keeps track of which player this is.  Starts at 0
   private int timesShielded     = 0;                                         // Everytime you shield
   private boolean shieldsUp     = false;
   
   public Universe()
   {
   
   }

   public void decTimesShielded()
   {
      timesShielded--;
   }
   
   public boolean getAbsorb()
   {
      return absorbSet;
   }

   public boolean getDestroyed()
   {
      return isDestroyed;
   }
   
   public int getEnergy()
   {
      return energy;
   }
   
   public int getLocation()
   {
      return clusterLocation;
   }
   
   public int getPlanets()
   {
      return numPlanets;
   }
      
   public int getPlayer()
   {
      return playerId;
   }
   
   public double getShieldReduction()
   {
      double reduction = SpaceFightConfig.ACTION_SHIELDS_DAMAGE_MODIFIER;
      
      for (int i = 0; i < getTimesShielded(); i++)
         reduction *= SpaceFightConfig.ACTION_SHIELDS_DECAY_MODIFIER;
            
      return reduction;
   }
   
   public boolean getShields()
   {
      return shieldsUp;
   }
      
   public int getTimesShielded()
   {
      return timesShielded;
   }
   
   public boolean hasLocation()
   {
      return ((clusterLocation == -1) ? false : true);
   }
  
   public void incTimesShielded()
   {
      timesShielded++;
   }
   
   public boolean modEnergy(int e)
   {
      // TBI checks for valid values
      
      // Lets see how it changes this first
      int startingEnergy = energy;
      int potentialEnergy = energy + e;
      
      if (potentialEnergy > SpaceFightConfig.MAX_ENERGY)
         energy = SpaceFightConfig.MAX_ENERGY;
      else
         energy = potentialEnergy;
      
//      if (getInfoLevel() & SpaceFightConfig.INFO_LEVEL_DEBUG)
//         System.out.println("Universe::modEnergy -- Energy mod request " + e + " Starting Energy:  " + startingEnergy + " End energy: " + energy);
      
      // We don't care if potentialEnergy is < 0, since we'll check for that later
      return true;
   }
   
   public int modPlanets(int m)
   {
      numPlanets += m;
      
      return numPlanets;
   }
      
   public boolean setAbsorb(boolean a)
   {
      boolean previousAbsorb = absorbSet;
      absorbSet = a;
      return previousAbsorb;
   }
   
   public void setDestroyed()
   {
      // Why no parameter?  Why do we need one.  You can't come back from the dead.
      isDestroyed = true;
   }
   
   public boolean setEnergy(int e)
   {
      // TBI checks for valid values
      energy = e;
      
      return true;
   }
   
   public boolean setLocation(int loc)
   {
      
      if (loc >= 0 && loc < SpaceFight.getNumUniverses())
      {
         clusterLocation = loc;
      
      /*   
         if (SpaceFightConfig.LOG_INFO)
            System.out.println("Universe::setLocation -- Setting Universe loc to " + loc);
        */ 
         return true;
      }
      else
         return false;
   }
   
   public boolean setPlayer(int p)
   {
      if (p >= 0 && p < SpaceFight.getNumUniverses())
      {
         playerId = p;
         return true;
      }
      else
         return false;
   }
   
   public boolean setShields(boolean s)
   {
      boolean previousShields = shieldsUp;
      
      shieldsUp = s;
      
      return s;
   }
  
}
   
