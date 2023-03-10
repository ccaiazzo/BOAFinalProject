import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


public class Main {

    //colors
    static final String YELLOW_BACKGROUND = "\u001B[43m";
    static final String ANSI_RESET = "\u001B[0m";
    static final String GREEN_BACKGROUND = "\u001B[42m";
    static final String PURPLE_BACKGROUND = "\u001B[45m";

    final static String RED_TEXT = "\u001B[31m";


    public static void main(String[] args) {

        //instructions
        System.out.print("Hello! This game is a recreation of the battle ship game." +
                "\nThe computer will generate ships and randomly place them onto the board. " +
                "\nYou will be prompted to enter the row and column of which spot you wish to strike. " +
                "\nIf you hit the ship, the spot you entered with show an 'X'. Otherwise, it'll show an 'O' for a miss." +
                "\nOnce you hit all the hidden ships, you win!");
        System.out.println();
        System.out.println();


        int gameBoardLength = 4; //this will be the size of the game board
        char water = '-'; //this will be the character symbol for the water
        char ship = 's'; //this will be the character symbol for ship
        char hit = 'X'; //this will be the character symbol for a hit
        char miss = 'O'; //this will be the character symbol for a miss
        int shipNumber = 3; //this will be the number of ships
        int turnCount = 1;

        //for console color
        final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
        final String ANSI_RESET = "\u001B[0m";


        char[][] gameBoard = createGameBoard(gameBoardLength, water, ship, shipNumber); //this is a 2d character array that will be our game board

        printGameBoard(gameBoard, water, ship);  //this will run and print the game board
        int undetectedShipNumber = shipNumber;//the initial value of undetected ships will be however many ships are in the game at the start
        while (undetectedShipNumber > 0) { //this will run as long as there are still undetected ships remaining on the board
            System.out.printf("Turn %s\n", turnCount);
            int[] guessCoordinates = getUserCoordinates(gameBoardLength);
            char locationViewUpdate = evaluateGuessAndGetTheTarget(guessCoordinates, gameBoard, ship, water, hit, miss, shipNumber); //this will determine whether the user target is a hit or miss.
            if (locationViewUpdate == hit) {
                undetectedShipNumber--;
                if(undetectedShipNumber == 1) {
                    System.out.printf("%s ship remaining.\n", undetectedShipNumber);
                } else {
                    System.out.printf("%s ships remaining.\n", undetectedShipNumber);
                }
            } else {
                if(undetectedShipNumber == 1) {
                    System.out.printf("%s ship remaining.\n", undetectedShipNumber);
                } else {
                    System.out.printf("%s ships remaining.\n", undetectedShipNumber);
                }

            }
            turnCount++;

            //quit
            System.out.println("DO YOU WANT TO CONTINUE PLAYING? 'any key' for yes / 'quit' or 'q' to quit");
            Scanner scan = new Scanner(System.in);
            String toQuit = scan.next().toUpperCase();
            if (toQuit.equalsIgnoreCase("QUIT")||toQuit.equalsIgnoreCase("q")) {
                System.out.println("Thanks for playing! See you next time!");
                System.exit(0);
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
        int row = 0;
        int col = 0;
        boolean flag;
        do {
            try {
                Scanner s = new Scanner(System.in);
                System.out.print("Row: ");
                row = s.nextInt();
                flag = false;
            } catch (Exception e) { //will ask use to input again if value given is NOT an integer
                System.out.println("Enter only an integer value from 1-4.");
                flag = true;
            }
        } while (row < 1 || row > gameBoardLength); //will ask user for input again if the given value is outside the game board's length
        do {
            try {
                Scanner s = new Scanner(System.in);
                System.out.print("Column: ");
                col = s.nextInt();
                flag = false;
            } catch (Exception e) { //will ask use to input again if value given is NOT an integer
                System.out.println("Enter only an integer value from 1-4.");
                flag = true;
            }
        } while (col < 1 || col > gameBoardLength); //same logic as above
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
                } else {
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
            Arrays.fill(row, water);  //use built in method to fill each row with water
        }
        return placeShips(gameBoard, shipNumber, water, ship); //this function will add ships to the board
    }

    private static char[][] placeShips(char[][] gameBoard, int shipNumber, char water, char ship) {
        int numOfPlacedShips = 0;  //this declares the number of placed ships on the board is currently 0
        int gameBoardLength = gameBoard.length;
        ;
        while (numOfPlacedShips < shipNumber) {  //continue to run this while the number of ships placed is less than the ship number
            int[] location = generateShipCoordinates(gameBoardLength);
            char possiblePosition = gameBoard[location[0]][location[1]];

            if (possiblePosition == water) {  //if the area is water, the ship can be placed there (this prevents ships from being placed on top of each other)
                gameBoard[location[0]][location[1]] = ship;
                numOfPlacedShips++;
            }
        }
        return gameBoard;
    }

    private static int[] generateShipCoordinates(int gameBoardLength) {
        int[] coordinates = new int[2];  //this int array will take the 2 (x & y) coordinates
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = new Random().nextInt(gameBoardLength);  //will fill with random int
        }
        return coordinates;
    }

    private static char evaluateGuessAndGetTheTarget(int[] guessCoordinates, char[][] gameBoard, char ship, char water, char hit, char miss, int shipNumber) { //this method will evaluate whether user hit or missed a ship
        String message;
        int row = guessCoordinates[0];
        int col = guessCoordinates[1];


        char target = gameBoard[row][col]; //this will check what target the guessed coordinates are hitting
        if (target == ship) { //nested if else statement will check the target and see whether it hit water or a ship and will update the game board.
            playSound("Sounds\\\\explosion.wav");
            message = GREEN_BACKGROUND + "Hit!" + ANSI_RESET;
            target = hit; //if target hits a ship it will return a hit char
            System.out.println();
        } else if (target == water) {
            playSound("Sounds\\\\splash.wav");
            message = YELLOW_BACKGROUND + "Miss!" + ANSI_RESET;
            target = miss; //if target lands in water it will return a water char
        } else {
            message = PURPLE_BACKGROUND + "Already hit." + ANSI_RESET; //The only two options is to hit a ship or hit water,so anything else means coordinated were already hit
        }
        System.out.println(message); //will print the message to let user know whether they hit or miss.
        return target; //will return the target value that the coordinates hit
    }

    private static void playSound(String filepath) {
        try {
            File soundPath = new File(filepath);

            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Can't find file.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

