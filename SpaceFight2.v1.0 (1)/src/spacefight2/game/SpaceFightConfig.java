/*  UniverseConfig
 *
 * Holds all the config info for SpaceFight2.  Change settings here to affect game play!
 *
 */
 
package spacefight2.game;

public class SpaceFightConfig {

   // First, setup output properties here
   public static final int INFO_LEVEL_MIN             = 0b0001;          // This will show only battle messages
   public static final int INFO_LEVEL_MATCH           = 0b0010;          // Not implemented.  Score and all info not part of the game.
   public static final int INFO_LEVEL_BATTLE          = 0b0100;          // Setup, configuration, and other control info.
   public static final int INFO_LEVEL_DEBUG           = 0b1000;
   
   // Lots of properties...
   public static final String ANONYMOUS_COWARD        = new String("Anonymous Coward");// Used when playerName isn't set
   public static final int DEFAULT_PLANETS_PER_UNIVERSE       = 3;                             // Planets == lives.  3 is default
   public static final int DEFAULT_NUM_UNIVERSES              = 12;                             // How many worlds can exist.  Note universeId 0 <= x < this max   
   public static final int STARTING_ENERGY            = 25;                            // How many you start w/and after planet death.  25 is default
   public static final int ENERGY_PER_ROUND           = 10;                            // How much energy is given per round.  Changes strategy a lot!
   public static final int MAX_ROUNDS                 = 1000;                          // Used to bail out continuous mode
   public static final int MAX_ENERGY                 = 100;                           // Max energy, is inclusive
   
   // Now all of our commands and their costs
   public static final String[] ACTIONS_LIST    = {"shoot", "shields", "absorb"};
   public static final int ACTION_ABSORB_COST   = 2;          // 50% chance to instead absorb all damage   
   public static final int ACTION_SHIELDS_COST  = 5;          // Guaranteed to deal half damage, 25% chance to deal none.
   public static final int ACTION_SHOOT_COST    = 1;          // Nominal for now
   
   // Command modifierS
   public static final double ACTION_ABSORB_CHANCE_ADD_ENERGY  = 0.50;
   public static final double ACTION_SHIELDS_DAMAGE_MODIFIER   = 1.25;              // Is just a starting value.  Every round the chance drops by half 
   public static final double ACTION_SHIELDS_DECAY_MODIFIER    = 0.5;               // Everytime shields are used, usefulness is modified by this   
   public static final int ACTION_SHOOT_DAMAGE_MULTIPLIER      = 3;
   
   public static final int ACTION_SHOOT_SELF_DAMAGE_MODIFIER   = 4;
      
   // All our exit conditions, players will not need these and these should not be changed
   public static final int ERROR_CLASSNOTFOUND_EXCEPTION             = 1;
   public static final int ERROR_INSTANTIATION_EXCEPTION             = 2;
   public static final int ERROR_ILLEGALACCESSEXCEPTION_EXCEPTION    = 3;
   public static final int ERROR_NOSUCHMETHODEXCEPTION_EXCEPTION     = 4;
   public static final int ERROR_INVOCATIONTARGETEXCEPTION_EXCEPTION = 5;
   
   public static final String RUNLEVEL_CONTINUOUS                    = "c";
   public static final String RUNLEVEL_MATCH                         = "m";
   public static final String RUNLEVEL_ITERATE                       = "i";        
   
   public SpaceFightConfig()
   {
   
   
   
   }
   
}