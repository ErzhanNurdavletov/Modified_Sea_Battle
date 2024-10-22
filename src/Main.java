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
                    System.out.println(key + " " + usersScore.get(key));
                }
            }
            else if (userChoice == yes) {
                fillGameFieldWater(computerField, gameField);
            }
        }
    }

    static void placeShips (String[][] field) {
        int wayChoice = random.nextInt(2);
        int vertical = 0;

        int x = 0;
        int y = 0;

        if (wayChoice == vertical) {
            x = random.nextInt(7);
            y = random.nextInt(5);
        }
        else {
            x = random.nextInt(5);
            y = random.nextInt(7);
        }


        for (int i = 0; i < 3; i++) {
            field[x][y + i] = " ⚓ ";

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
                System.out.println("hit!");
                computerField[x][y] = " x ";
                gameField[x][y] = " x ";
            }
            else if (computerField[x][y].equals( " ~ ") || computerField[x][y].equals(" m ")) {
                System.out.println("missed...");
                computerField[x][y] = " m ";
                gameField[x][y] = " m ";
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
                 gameField[i][j] = " ~ ";
                 computerField[i][j] = " ~ ";
             }
         }
     }









}