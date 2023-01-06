import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int gameBoardLength = 4; //this will be the size of the game board
        char water = '-'; //this will be the character symbol for the ship
        char ship = 's'; //this will be the character symbol for s
        char hit = 'X'; //this will be the character symbol for a hit
        char miss = 'O'; //this will be the character symbol for a miss
        int shipNumber = 3; //this will be the number of ships

        char[][] gameBoard = createGameBoard(gameBoardLength, water, ship, shipNumber); //this is a 2d character array that will be our game board

        printGameBoard(gameBoard, water, ship);  //this will run and print the game board
    }

    private static void printGameBoard(char[][] gameBoard, char water, char ship) {
        int gameBoardLength = gameBoard.length;
        System.out.print("  ");
        for (int i = 0; i < gameBoardLength; i++) {  //this for loop will label the column with number
            System.out.print((i + 1) + " ");
        }
        System.out.println();

        for (int row = 0; row < gameBoardLength; row++) {  //iterate through board rows
            System.out.print((row + 1) + " ");  //label row with numbers
                     for (int column = 0; column < gameBoardLength; column++) {  //iterate through columns
                    char position = gameBoard[row][column];
                    if (position == ship) {
                        System.out.print(water + " ");
                    }
                    else {
                        System.out.print(position + " ");
                    }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static char[][] createGameBoard(int gameBoardLength, char water, char ship, int shipNumber) {
        char[][] gameBoard = new char[gameBoardLength][gameBoardLength]; //this will make the board the size of the declared gameBoardLength
        for (char[] row : gameBoard) {
            Arrays.fill(row, water );  //use built in method to fill each row with water
        }
        return placeShips(gameBoard, shipNumber, water, ship); //this function will add ships to the board
    }

    private static char[][] placeShips(char[][] gameBoard, int shipNumber, char water, char ship) {
        int numOfPlacedShips = 0;  //this declares the number of placed ships on the board is currently 0
        int gameBoardLength = gameBoard.length;;
        while (numOfPlacedShips < shipNumber) {  //continue to run this while the number of ships placed is less than the ship number
            int[] location = generateShipCoordinates(gameBoardLength);
            char possiblePosition = gameBoard[location[0]][location[1]];

            if (possiblePosition == water){  //if the area is water, the ship can be placed there (this prevents ships from being placed on top of each other)
                gameBoard[location[0]][location[1]] = ship;
                numOfPlacedShips++;
            }
        }
        return gameBoard;
    }

    private static int[] generateShipCoordinates(int gameBoardLength) {
        int[] coordinates = new int[2];  //this int array will take the 2 (x & y) coordinates
        for (int i=0; i< coordinates.length; i++) {
            coordinates[i] = new Random().nextInt(gameBoardLength);  //will fill with random int
        }
        return coordinates;
    }
}
