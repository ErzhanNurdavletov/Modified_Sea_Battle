import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Random random = new Random();


    public static void main(String[] args) {

        String[][] gameField = new String[7][7];
        String[][] computerField = new String[7][7];
        HashMap<String, Integer> usersScore = new HashMap<>();

        fillGameFieldWater(computerField, gameField);

        boolean userPlaying = true;

        while (userPlaying) {
            placeShips(computerField);
            System.out.print("Enter your username: ");
            String userName = sc.nextLine();
            System.out.println("Enter coordinates (example: 0 6)");



            int numberUsersAttempts = startGame(computerField, gameField);
            usersScore.put(userName, numberUsersAttempts);

            System.out.println("Do you want to play again?");
            System.out.print("1 - yes, 0 - no: ");
            int userChoice = sc.nextInt();
            int no = 0;
            int yes = 1;

            if (userChoice == no) {
                userPlaying = false;
                for (String key : usersScore.keySet()) {
                    System.out.println("User: " + key + ", " + key + "'s number attempts: " + usersScore.get(key));
                }
            }
            else if (userChoice == yes) {
                fillGameFieldWater(computerField, gameField);
            }
            else {
                System.out.println("Incorrect input, try again");
                return;
            }
        }
    }

    static void placeShips (String[][] field) {
        placeShip(field, 3);
        placeShip(field, 2);
        placeShip(field, 2);

        for (int i = 0; i < 4; i++) {
            placeShip(field, 1);
        }
    }
    static void placeShip(String[][] field, int length) {
        boolean placed = false;

        while (!placed) {
            int vertical = random.nextInt(2);  // 0 - горизонтально, 1 - вертикально
            int x = vertical == 1 ? random.nextInt(7 - length + 1) : random.nextInt(7);
            int y = vertical == 0 ? random.nextInt(7 - length + 1) : random.nextInt(7);

            if (canPlaceShip(field, x, y, length, vertical == 1)) {
                for (int i = 0; i < length; i++) {
                    if (vertical == 1) {
                        field[x + i][y] = " ⚓ ";
                    } else {
                        field[x][y + i] = " ⚓ ";
                    }
                }
                placed = true;
            }
        }
    }
    static boolean canPlaceShip(String[][] field, int x, int y, int length, boolean vertical) {
        int startX = Math.max(0, x - 1);
        int endX = Math.min(6, vertical ? x + length : x + 1);
        int startY = Math.max(0, y - 1);
        int endY = Math.min(6, vertical ? y + 1 : y + length);
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (!field[i][j].equals(" \uD83C\uDF0A ")) {
                    return false;
                }
            }
        }
        return true;
    }
    static void checkSunkShip(String[][] computerField, String[][] gameField, int x, int y) {
        if (computerField[x][y].equals(" X ")) {
            boolean isSunk = true;

            for (int i = 0; i < 7; i++) {
                if (computerField[x][i].equals(" ⚓ ")) {
                    isSunk = false;
                    break;
                }
            }
            for (int i = 0; i < 7; i++) {
                if (computerField[i][y].equals(" ⚓ ")) {
                    isSunk = false;
                    break;
                }
            }
            if (isSunk) {
                for (int i = 0; i < 7; i++) {
                    if (computerField[x][i].equals(" X ")) {
                        computerField[x][i] = " S ";
                        gameField[x][i] = " S ";
                    }
                    if (computerField[i][y].equals(" X ")) {
                        computerField[i][y] = " S ";
                        gameField[i][y] = " S ";
                    }
                }
            }
        }
    }
    static int startGame (String[][] computerField, String[][] gameField) {
        boolean gameStatus = true;

        int numberAttempts = 0;

        while (gameStatus) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            numberAttempts++;


            if (x > 7 || x < 0 || y > 7 || y < 0) {
                System.out.println("input incorrect, try again");
                continue;
            }

            else if (computerField[x][y].equals( " ⚓ ")) {
                computerField[x][y] = " X ";
                gameField[x][y] = " X ";
                System.out.println("hit!");
                checkSunkShip(computerField, gameField, x, y);
            }
            else if (computerField[x][y].equals( " \uD83C\uDF0A ") || computerField[x][y].equals(" M ")) {
                computerField[x][y] = " M ";
                gameField[x][y] = " M ";
                System.out.println("miss...");
            }


            displayGameField(gameField);
            gameStatus = checkGameStatus(computerField);

        }
        System.out.println("Game finished. You won! number of your attempts: " + numberAttempts);
        return numberAttempts;

     }

     static void displayGameField (String[][] gameFiled) {
         for (int i = 0; i < gameFiled.length; i++) {
             for (int j = 0; j < gameFiled[i].length; j++) {
                 System.out.print(gameFiled[i][j]);
             }
             System.out.println();
         }
     }

     static boolean checkGameStatus (String[][] computerField) {
        for (int i = 0; i < computerField.length; i++) {
            for (int j = 0; j < computerField[i].length; j++) {
                if (computerField[i][j].equals(" ⚓ ")) {
                    return true;
                }
            }
         }
        return false;
     }

     static void fillGameFieldWater (String[][] computerField, String[][] gameField) {
         for (int i = 0; i < 7; i++) {
             for (int j = 0; j < 7; j++) {
                 gameField[i][j] = " \uD83C\uDF0A ";
                 computerField[i][j] = " \uD83C\uDF0A ";
             }
         }
     }









}