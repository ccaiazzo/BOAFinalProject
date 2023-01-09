import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int gameBoardLength = 4; //this will be the size of the game board
        char water = '-'; //this will be the character symbol for the ship
        char ship = 's'; //this will be the character symbol for s
        char hit = 'X'; //this will be the character symbol for a hit
        char miss = 'O'; //this will be the character symbol for a miss
        int shipNumber = 3; //this will be the number of ships

        //for console color
         final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
         final String ANSI_RESET = "\u001B[0m";



        char[][] gameBoard = createGameBoard(gameBoardLength, water, ship, shipNumber); //this is a 2d character array that will be our game board

        printGameBoard(gameBoard, water, ship);  //this will run and print the game board
        int undetectedShipNumber = shipNumber;//the initial value of undetected ships will be however many ships are in the game at the start
        while(undetectedShipNumber > 0) { //this will run as long as there are still undetected ships remaining on the board
            int[] guessCoordinates = getUserCoordinates(gameBoardLength);
            char locationViewUpdate = evaluateGuessAndGetTheTarget(guessCoordinates, gameBoard, ship, water, hit, miss); //this will determine whether the user target is a hit or miss.
            if (locationViewUpdate == hit) {
                undetectedShipNumber--;
            }
            gameBoard = updateGameBoard(gameBoard, guessCoordinates, locationViewUpdate);
            printGameBoard(gameBoard, water, ship);
        }
        System.out.print(ANSI_CYAN_BACKGROUND + "You Win!" + ANSI_RESET);
    }
    private static char[][] updateGameBoard(char[][] gameBoard, int[] guessCoordinates, char locationViewUpdate) {
        int row = guessCoordinates[0];
        int col = guessCoordinates[1];
        gameBoard[row][col] = locationViewUpdate;
        return gameBoard;
    }


    private static int[] getUserCoordinates(int gameBoardLength) { //this method will take the user's row and column target guess
        int row;
        int col;
        do {
            System.out.print("Row: ");
            row = new Scanner(System.in).nextInt();
        } while(row < 1 || row > gameBoardLength + 1); //will ask user for input again if the given value is outside the game board's length
        do {
            System.out.print("Column: ");
            col = new Scanner(System.in).nextInt();
        } while(col < 1 || col > gameBoardLength + 1); //same logic as above
        return new int[]{row - 1, col - 1};//we subtract 1 from the user's input to ensure we are checking the correct index on the board
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

    private static char evaluateGuessAndGetTheTarget(int[] guessCoordinates, char[][] gameBoard, char ship, char water, char hit, char miss){ //this method will evaluate whether user hit or missed a ship
        String message;
        int row = guessCoordinates[0];
        int col = guessCoordinates[1];

        char target = gameBoard[row][col]; //this will check what target the guessed coordinates are hitting
        if(target == ship) { //nested if else statement will check the target and see whether it hit water or a ship and will update the game board.
            message = "Hit!";
            target = hit; //if target hits a ship it will return a hit char
        } else if (target == water) {
            message = "Miss!";
            target = miss; //if target lands in water it will return a water char
        } else {
            message = "Already hit."; //The only two options is to hit a ship or hit water,so anything else means coordinated were already hit
        }
        System.out.println(message); //will print the message to let user know whether they hit or miss.
        return target; //will return the target value that the coordinates hit
    }
}

