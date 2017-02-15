/* PlayerCommand
 *
 * The thing a player is going to do
 */

package spacefight2.game;

public class PlayerCommand 
{

   // Class properties
   private String    action      = new String("");             // No default action, prevents destroyed players from accidentally running a command
   private int       energyUsage = 0;                          // For actions that can spend an optional amount to shoot
   private int       playerId    = -1;                
   private int       target      = -1;
   
   // Constructor w/command
   public PlayerCommand()
   {
      
   
   }
   
   public String getAction()
   {
      return action;
   }
   
   public int getEnergyUsage()
   {
      return energyUsage;
   }
   
   public int getTarget()
   {
      return target;
   }
   
   public int getPlayer()
   {
      return playerId;
   }
   
   public boolean setAction(String a)
   {
      // TBI check that command is valid and use default if not
      action = a;  
      
      return true;      // TBI return based on validity of command 
   }
   
   public boolean setEnergyUsage(int e)
   {
      // No way to validate if we have the avail energy, game will have to handle that.
      energyUsage = e;
      
      return true;
   }
   
   public boolean setPlayer(int p)
   {
      playerId = p;
      return true;
   }
   
   public int setTarget(int t)
   {
      int previousTarget = target;
      
      target = t;
      
      return previousTarget;  
   }  
}