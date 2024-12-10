import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Main{

    static Random random = new Random();
    static Scanner sc = new Scanner(System.in);

    static String[][] gameField = new String[8][8];
    static String[][] outputField = new String[8][8];

    static String ship = " ⚓ ";
    static String water = " \uD83C\uDF0A ";
    static String miss = " ❌ ";
    static String hit = " \uD83D\uDCA5 ";

    static ArrayList<String> players = new ArrayList<>();
    static ArrayList<Integer> scores = new ArrayList<>();

    public static void main(String[] args) {

        while(true) {
            System.out.print("Enter your name: ");
            players.add(sc.nextLine());
            prepareGameField();
            placeShips();
            int numberAttempts = startGameCalculateNumberAttempts();
            scores.addLast(numberAttempts);
            showGameField();
            showUsersScores();

            System.out.print("Do you want to play again? <no> or <yes> : ");
            String choice = sc.nextLine();
            if(choice.equals("no") || choice.equals(" no")) {
                break;
            }
        }
    }
    static int startGameCalculateNumberAttempts() {
        String[] letters = {"", "A", "B", "C", "D", "E", "F", "G"};
        int userAttemptsNumber = 0;
        while(checkGameFinished()) {
            cleanConsole();
            showGameField();
            System.out.print("Enter coordinates like this -> <B 2>: ");
            String[] inputCoordinates = sc.nextLine().split(" ");
            int inputX = 0;
            int inputY = Integer.parseInt(inputCoordinates[1]);
            boolean incorrectInputY = true;
            for(int i = 0; i < letters.length; i++) {
                if(inputCoordinates[0].equals(letters[i])) {
                    inputX = i;
                    incorrectInputY = false;
                    break;
                }
            }
            if(inputCoordinates.length != 2 || Integer.parseInt(inputCoordinates[1]) < 1 || Integer.parseInt(inputCoordinates[1]) > 7
            || incorrectInputY) {
                System.out.println("Incorrect input, try again");
                continue;
            }

            if(gameField[inputX][inputY].equals(ship)) {
                gameField[inputX][inputY] = hit;
                outputField[inputX][inputY] = hit;
                System.out.println("Hit!");
                userAttemptsNumber++;
            }
            else if(gameField[inputX][inputY].equals(hit) || gameField[inputX][inputY].equals(miss)) {
                System.out.println("Was already hit");
            }
            else {
                System.out.println("Miss");
                gameField[inputX][inputY] = miss;
                outputField[inputX][inputY] = miss;
                userAttemptsNumber++;
            }
        }
        return userAttemptsNumber;
    }
    static void showUsersScores() {
        System.out.println("Game finished. Results: ");
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i) + "'s score is: " + scores.get(i));
        }
    }
    static boolean checkGameFinished() {
        for(int i = 1; i < 8; i++) {
            for(int j = 1; j < 8; j++) {
                if(gameField[i][j].equals(ship)) {
                    return true;
                }
            }
        }
        return false;
    }
    static void placeShips() {
        placeShip(3);
        placeShip(2);
        placeShip(2);
        for (int i = 0; i < 4; i++) {
            placeShip(1);
        }
    }
    static void placeShip(int shipSize) {
        boolean horizontalWay;
        int Xcoordinate;
        int Ycoordinate;
        while(true) {
            horizontalWay = random.nextBoolean();
            if (horizontalWay) {
                Xcoordinate = random.nextInt(7) + 1;
                Ycoordinate = random.nextInt(8 - shipSize) + 1;
            } else {
                Xcoordinate = random.nextInt(8 - shipSize) + 1;
                Ycoordinate = random.nextInt(7) + 1;
            }
            if (canPlaceShip(Xcoordinate, Ycoordinate, horizontalWay, shipSize)) {
                if (horizontalWay) {
                    for (int i = 0; i < shipSize; i++) {
                        gameField[Xcoordinate][Ycoordinate + i] = ship;
                        outputField[Xcoordinate][Ycoordinate + i] = ship;
                    }
                }
                else {
                    for (int i = 0; i < shipSize; i++) {
                        gameField[Xcoordinate + i][Ycoordinate] = ship;
                        outputField[Xcoordinate + i][Ycoordinate] = ship;
                    }
                }
                break;
            }
        }
    }
    static boolean canPlaceShip(int x, int y, boolean horizontalWay, int shipSize) {
            for(int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if(x + dx < 1 || x + dx > 7 || y + dy < 1 || y + dy > 7) {
                        continue;
                    }
                    if(gameField[x + dx][y + dy].equals(ship)) {
                        return false;
                    }
                }
            }
            if(horizontalWay) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (x + dx < 1 || x + dx > 7 || y + shipSize - 1 + dy < 1 || y + shipSize - 1 + dy > 7) {
                            continue;
                        }
                        if (gameField[x + dx][y + shipSize - 1 + dy].equals(ship)) {
                            return false;
                        }
                    }
                }
            }
            else {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (x + shipSize - 1 + dx < 1 || x + shipSize - 1 + dx > 7 || y + dy < 1 || y + dy > 7) {
                            continue;
                        }
                        if (gameField[x + shipSize - 1 + dx][y + dy].equals(ship)) {
                            return false;
                        }
                    }
                }
            }
        return true;
    }
    static void prepareGameField() {
        for(int i = 1; i < 8; i++) {
            for(int j = 1; j < 8; j++) {
                gameField[i][j] = water;
                outputField[i][j] = water;
            }
        }
        String[] letters = {"   ", " A ", " B ", " C ", " D ", " E ", " F ", " G "};
        for (int i = 0; i < 8; i++) {
            gameField[i][0] = letters[i];
            outputField[i][0] = letters[i];
        }
        for (int i = 1; i < 8; i++) {
            gameField[0][i] = "  " + i + " ";
            outputField[0][i] = "  " + i + " ";
        }
    }
    static void showGameField() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                System.out.print(gameField[i][j]);
            }
            System.out.println();
        }
    }
    static void cleanConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}