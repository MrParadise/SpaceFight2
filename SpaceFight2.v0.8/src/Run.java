/* Main
 *
 *
 *Copyright 2017 Johnny Paradise for Paradise Intergalactic Enterprises.
 *
 *This program is licensed under the GPL.  Please see COPYING for more information.
 *
 * Runs Space Fight 2 really by johnny paradise but we're gonna say Simon B presents because Simon B is pretty awesome and swell and did actually name it so it's kinda an omage and a nod to the artistic side that I totally could not have done--it would have been something stupid and no one would like it because all I do is run on and on and on for no reason.  If you quietly tell me turtle I'll give you 30 points next semester.
 * pew pew pew
 */

import spacefight2.game.*;
import spacefight2.players.*;
import java.util.Scanner;


public class Run 
{
   public static void main(String args[])
   {
      // Local vars
      String commandStr    = new String();
      Integer commandInt   = new Integer(0);
      Scanner input        = new Scanner(System.in);
      int numPlayers       = 2;                          // Change this to the number of players!
      int ties             = 0;
      int winnerId;
      int[] winners        = new int[numPlayers];

      // Create the game
      SpaceFight game = new SpaceFight();     // Create a game w/30 Universes
      
         
      // Add in players
      //
      for (int i = 0; i < numPlayers; i++)
         winners[i] = 0;
         
      game.addPlayer("Player0");
      game.addPlayer("Player1");  
     
      // Set how many games we're going to play
      System.out.println("Please select simulation parameters.  Hit enter for defaults.");
      
      System.out.println("Enter number of matches to play (1 default): ");
      commandStr = input.nextLine();
      commandInt = (commandStr.length() == 0) ? 1 : Integer.parseInt(commandStr);
      game.setNumMatches(commandInt);
      
      // Set our log level
      System.out.println("Enter info level (0 == Min, 1 == Match, 2 == Battle (default), 3 == Debug): ");
      commandStr = input.nextLine();
      commandInt = (commandStr.length() == 0) ? 2 : Integer.parseInt(commandStr);
      game.setInfoLevel(commandInt);
      
      
      // Lastly, set our run level
      System.out.println("Enter run level (c == continuous (default), m == stop after every match, i == iterate each round): ");
      commandStr = input.nextLine();
      commandStr = (commandStr.length() == 0) ? "c" : commandStr;
      game.setRunLevel(commandStr);
      
      
      // And run
      commandStr = "";
      for (int match = 0; match < game.getNumMatches(); match++)
      {
         // Fire up our game
         
         if (game.getRunLevel().equals("m"))
         {
            System.out.print("Enter any key to proceed>");
            commandStr = input.nextLine();   
         }
         
         // Start
         game.start();
         
         while (!game.getGameOver())
         {
            if (game.getRunLevel().equals("i"))
            {
               
               System.out.print("Enter any key to proceed>");
               commandStr = input.nextLine();   
            }
         
            // Tick -- Play another round
            game.tick();
         
         }
         
         // Game ended, log our result
         winnerId = game.getWinner();
         if (winnerId == -1)
            ties++;
         else
            winners[winnerId]++;
         
      }
      
      // Now let's output our results
      System.out.println("SpaceFight over.  After " + game.getNumMatches() + " Matches, Results:  ");
      for (int j = 0; j < numPlayers; j++)
         System.out.println("Wins, Player " + Integer.toString(j) + ": " + Integer.toString(winners[j])); 
      System.out.println("Ties: " + Integer.toString(ties));
   }
} 
 