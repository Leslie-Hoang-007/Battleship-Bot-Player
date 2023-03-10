import battleship.BattleShip2;

/**
 * Starting code for COMP10205 - Assignment#6 - Version 2 of BattleShip
 * @author Mehedi, Roopa, and Leslie
 */

public class A6 {
    public static void main(String[] args) {

        // DO NOT add any logic to this code
        // All logic must be added to your Bot implementation
        // see fireShot in the ExampleBot class

//        final int NUMBEROFGAMES = 1;
        final int NUMBEROFGAMES = 10000;


        System.out.println(BattleShip2.getVersion());
        BattleShip2 battleShip = new BattleShip2(NUMBEROFGAMES, new BottyBot());
        int [] gameResults = battleShip.run();

//        HashSet<Ship> shipsDestroyedSet = new HashSet<Ship>();
//        shipsDestroyedSet.insert(new Ship(2));

        // You may add some analysis code to look at all the game scores that are returned in gameResults
        // This can be useful for debugging purposes.

        battleShip.reportResults();
        battleShip.numberOfShipsSunk();

//        System.out.println(shipsDestroyedSet.contains(new Ship(2)));
//        shipsDestroyedSet.remove(new Ship(2));
//        System.out.println("hello");
//        System.out.println(shipsDestroyedSet.contains(new Ship(2)));

    }



}
