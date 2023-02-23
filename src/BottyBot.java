
import battleship.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Battleship shooter
 *
 * Shoots at a parity and enters a search mode to sink ship when a hit is made
 *
 * @author Leslie Hoang
 */
public class BottyBot implements BattleShipBot {

    /** battleship object to be called for functions relating to the game**/
    private BattleShip2 battleShip;
    /** random number for shots **/
    private Random random;
    /** Size of game board**/
    private int gameSize;
    /** parity - shots fired starts at every other, then increases as the smallest ships increases**/
    private int parity = 2;
    /** 2d array to store data about the shots made**/
    public int[][] map;
    /** list for all destroyed battleships **/
    private ArrayList<Integer> stored;
    /** count for the number of shot to sink a battleship **/
    private int count = 0;
    /** Int representing a MISSed shot**/
    private final int MISS = 1;
    /** Int representing a HIT **/
    private final int HIT = 2;
    /** Int represent a shot to be emilimated from play **/
    private final int ELIM = 3;
    /** color for green text(debugging) **/
    private static final String ANSI_GREEN = "\u001B[32m";
    /** color for normal text(debugging)**/
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructor keeps a copy of the BattleShip instance
     * Create instances of any Data Structures and initialize any variables here
     * @param b previously created battleship instance - should be a new game
     */

    @Override
    public void initialize(BattleShip2 b) {
        battleShip = b;
        gameSize = b.BOARD_SIZE;
        map = new int[b.BOARD_SIZE][b.BOARD_SIZE];// create board
        // Need to use a Seed if you want the same results to occur from run to run
        // This is needed if you are trying to improve the performance of your code

        random = new Random(0xAAAAAAAA);   // Needed for random shooter - not required for more systematic approaches
        stored = new ArrayList<Integer>(); // resets list of destroyed ships
        parity = 2; // resets the parity
    }




    /**
     * Create a random shot and calls the battleship shoot method
     * Put all logic here (or in other methods called from here)
     * The BattleShip API will call your code until all ships are sunk
     */
//**********************************************FIRE SHOT + Valid Shot*****************************************************************

    //************************Valid Shot Method*************************************
    /**
     * Generates a random shot based on the available shots that has not been fired
     * @return A valid shot to be fired
     */
    private int[] validShot(){
        boolean valid = false;
        int x = 0;
        int y = 0;

        while (!valid){// keeps looping until valid shot is available
            x = random.nextInt(gameSize);
            y = random.nextInt(gameSize);
            if (map[x][y] == 0 &&(x+y) % parity ==0 ){
                valid = true;
            }
        }

        int [] shot = {x, y};// assigns x and y to a variable
        return shot;// return x + y
    }


    //************************FireShot*************************************

    /**
     * Fires shot!
     * Shoots shot by calling battleShip.shoot: Returns a boolean
     *
     * If shot is a False validShot() method will be called
     *  Else searchMode() method will be called
     */
    @Override
    public void fireShot() {
        int [] shots = validShot(); // calls method for a valid shot
        boolean hit = battleShip.shoot(new Point(shots[0],shots[1]));// takes the shot




        //**************Print Board For de bugging After Each shot*****************

//        for (int i = 0; i < battleShip.BOARD_SIZE; i++) {
//            for (int j = 0; j < battleShip.BOARD_SIZE; j++) {
//                if (map[i][j] == 2){
//                    System.out.print(ANSI_GREEN +map[i][j] + " " +ANSI_RESET);
//                } else{
//                    System.out.print(map[i][j] + " ");
//                }
//
//            }
//            System.out.println();
//
//        }
//        System.out.println("NEXT");


        //**************Print Board For de bugging After Each shot*****************



        if(hit == true){// shot hit enter 2 into map
            count ++;
            map[shots[0]][shots[1]] = HIT;
            searchMode(shots[0],shots[1]);// Enter into search mode once a shot is hit


            setParity(); // increments parity

            count = 0;
        }
        else{// shot miss enter 1 into map
            map[shots[0]][shots[1]] = MISS;
        }
    }

// **********************************************Parity Increment*****************************************************************

    /**
     * stores ships sunk in ArrayList and increments parity as needed
     */
    private void setParity(){
        stored.add(count);
//        System.out.println(stored);
        Collections.sort(stored);
        if (stored.get(0)== parity && stored.get(0) != 4){
//                System.out.println("SHIP with: " + count + "HIT sank");
            parity++;
            stored.remove(0);
//                System.out.println("Parity now " + parity);
        }
        if (parity >3 ){
            parity = 4;
        }
        count=0;
    }



// **********************************************Search Mode*****************************************************************
    /**
     * A hit was made, now in searchMode() we will sink ship by shooting adjacent spots
     * @param x x-coordinates of valid shot(hit)
     * @param y y-coordinates of valid shot(hit)
     */
    private void searchMode(int x, int y){
        int curNumShips = battleShip.numberOfShipsSunk(); // current number of ships
        while(curNumShips == battleShip.numberOfShipsSunk()){ // while same number of ship exists
            optionRowPos(x,y);// shoot RIGHT(row+)
            if(curNumShips != battleShip.numberOfShipsSunk()){ //ship still not sunk?
                if(x>1 && x<14){
                    map[(x-1)][y] = HIT;
                }
                return;
            }

            optionRowNeg(x,y);// shoot LEFT(row-)
            if(curNumShips != battleShip.numberOfShipsSunk()){ //ship still not sunk?
                if(x>1 && x<13){
                    map[(x+1)][y] = HIT;
                }
                return;
            }
            optionColPos(x,y);// shoot UP(col+)
            if(curNumShips != battleShip.numberOfShipsSunk()){ //ship still not sunk?
                if(y>1 && y<14){
                    map[x][(y-1)] = HIT;
                }
                return;
            }
            optionColNeg(x,y);// shoot DOWN(col-)
            if(curNumShips != battleShip.numberOfShipsSunk()){ //ship still not sunk?
                if(y>1 && y<14){
                    map[x][(y+1)] = HIT;
                }
                return;
            }

        }
    }
// **********************************************callTheShooter*****************************************************************

    /**
     * Keeps shooting up until ship is sunk or a MISSed shot is made
     * @param x x-coordinate of last HIT
     * @param y y-coordinate of last HIT
     */
    private void optionColPos (int x, int y){
        boolean a = true;
        while (a){// loops
            y++;// increments y
            if(y<0 ||y>14){// in boundaries? No then exit
                return;
            }
            if(battleShip.shoot(new Point(x,y))){// hits, add 2 to map, keeps in loop
                map[x][y] = HIT;
                elimColSurrounding(x,y);
                count++;

            }else{
                map[x][y] = MISS;// miss, add 1 to map, exits loop
                a=false;
            }

        }
    }

    /**
     * Keeps shooting down until ship is sunk or a MISSed shot is made
     * @param x x-coordinate of last HIT
     * @param y y-coordinate of last HIT
     */
    private void optionColNeg (int x, int y){
        boolean a = true;
        while (a){
            y--;// increments
            if(y<0 ||y>14){// in boundaries? No then exit
                return;
            }
            if(battleShip.shoot(new Point(x,y))){// hits, add 2 to map, keeps in loop
                map[x][y] = HIT;
                elimColSurrounding(x,y);
                count++;
            }else{
                map[x][y] = MISS;// miss, add 1 to map, exits loop
                a=false;
            }

        }
    }

    /**
     * Keeps shooting RIGHT until ship is sunk or a MISSed shot is made
     * @param x x-coordinate of last HIT
     * @param y y-coordinate of last HIT
     */
    private void optionRowPos (int x, int y){
        boolean a = true;
        while (a){
            x++;// increments
            if(x<0 ||x>14){// in boundaries? No then exit
                return;
            }
            if(battleShip.shoot(new Point(x,y))){// hits, add 2 to map, keeps in loop
                map[x][y] = HIT;
                elimRowSurrounding(x,y);
                count++;
            }else{
                map[x][y] = MISS;// miss, add 1 to map, exits loop
                a=false;
            }

        }
    }

    /**
     * Keeps shooting LEFT until ship is sunk or a MISSed shot is made
     * @param x x-coordinate of last HIT
     * @param y y-coordinate of last HIT
     */
    private void optionRowNeg (int x, int y){
        boolean a = true;
        while (a){
            x--;// increments
            if(x<0 ||x>14){// in boundaries? No then exit
                return;
            }
            if(battleShip.shoot(new Point(x,y))){// hits, add 2 to map, keeps in loop
                map[x][y] = HIT;
                elimRowSurrounding(x,y);
                count++;
            }else{
                map[x][y] = MISS;// miss, add 1 to map, exits loop
                a=false;
            }

        }
    }
// **********************************************Eliminate Surrounding*****************************************************************

    /**
     * Eliminates blocks perpendicular to vertical ship because rule of this game of battle states ships may not touch perpendicularly to each other
     * @param x x-coordinates of HIT
     * @param y y-coordinates of HIT
     */
    private void elimRowSurrounding(int x, int y){
        if((y-1)>0 && (y+1)< gameSize){
            map[x][y-1] = ELIM;
            map[x][y+1] = ELIM;
        }

    }

    /**
     * Eliminates blocks perpendicular to horizontal ship because rule of this game of battle states ships may not touch perpendicularly to each other
     * @param x x-coordinates of HIT
     * @param y y-coordinates of HIT
     */
    private void elimColSurrounding(int x, int y){
        if((x-1)>0 && (x+1)< gameSize){
            map[x-1][y] = ELIM;
            map[x+1][y] = ELIM;
        }
    }
    /**
     * Authorship of the solution - must return names of all students that contributed to
     * the solution
     * @return names of the authors of the solution
     */

    @Override
    public String getAuthors() {
//        for (int i = 0; i < battleShip.BOARD_SIZE; i++) { //**************Print Board For de bugging*****************
//            for (int j = 0; j < battleShip.BOARD_SIZE; j++) {
//
//                System.out.print(map[i][j] + " ");
//
//            }
//            System.out.println();
//        }
        return "Captain Battlestar Leslie";

    }
}
