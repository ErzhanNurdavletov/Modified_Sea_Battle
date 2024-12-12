import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class Main{

    static Random random = new Random();
    static Scanner sc = new Scanner(System.in);

    static String[][] gameField = new String[8][8];
    static String[][] outputField = new String[8][8];

    static String ship = " ⚓ ";
    static String water = " \uD83C\uDF0A ";
    static String miss = " ❌ ";
    static String hit = " \uD83D\uDCA5 ";
    static String sunk = " \uD83D\uDC80 ";

    static ArrayList<String> players = new ArrayList<>();
    static ArrayList<Integer> scores = new ArrayList<>();

    static ArrayList<Integer> coordinatesForCheckingSunk = new ArrayList<>();
    static ArrayList<Boolean> waysForCheckingSunk = new ArrayList<>();

    public static void main(String[] args) {

        while(true) {
            System.out.print("Enter your name: ");
            players.add(sc.nextLine());
            prepareGameField();
            placeShips();
            int numberAttempts = startGameCountAttemptsNumber();
            scores.addLast(numberAttempts);
            clearSavedCoordinatesCheckingSunk();
            showGameField();
            showUsersScores();

            System.out.print("Do you want to play again? <no> or <yes> : ");
            String choice = sc.nextLine();
            if(choice.equals("no") || choice.equals(" no")) {
                break;
            }
        }
    }
    static int startGameCountAttemptsNumber() {
        String[] letters = {"", "A", "B", "C", "D", "E", "F", "G"};
        int userAttemptsNumber = 0;
        while(checkGameFinished()) {
            clearConsole();
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
                System.out.println("Was already hit, try again");
            }
            else{
                System.out.println("Miss");
                gameField[inputX][inputY] = miss;
                outputField[inputX][inputY] = miss;
                userAttemptsNumber++;
            }
            changeSunkShipSymbol();
        }
        return userAttemptsNumber;
    }
    static void showUsersScores() {
        Collections.sort(scores);
        System.out.println("Game finished. Results: ");
        for(int i = 0; i < players.size(); i++) {
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
            if(horizontalWay) {
                Xcoordinate = random.nextInt(7) + 1;
                Ycoordinate = random.nextInt(8 - shipSize) + 1;
            }
            else{
                Xcoordinate = random.nextInt(8 - shipSize) + 1;
                Ycoordinate = random.nextInt(7) + 1;
            }
            if(canPlaceShip(Xcoordinate, Ycoordinate, horizontalWay, shipSize)) {
                if(horizontalWay) {
                    for(int i = 0; i < shipSize; i++) {
                        gameField[Xcoordinate][Ycoordinate + i] = ship;
                    }
                }
                else{
                    for(int i = 0; i < shipSize; i++) {
                        gameField[Xcoordinate + i][Ycoordinate] = ship;
                    }
                }
                coordinatesForCheckingSunk.add(Xcoordinate);
                coordinatesForCheckingSunk.add(Ycoordinate);
                waysForCheckingSunk.add(horizontalWay);
                break;
            }
        }
    }
    static boolean canPlaceShip(int x, int y, boolean horizontalWay, int shipSize) {
            for(int dx = -1; dx <= 1; dx++) {
                for(int dy = -1; dy <= 1; dy++) {
                    if(x + dx < 1 || x + dx > 7 || y + dy < 1 || y + dy > 7) {
                        continue;
                    }
                    if(gameField[x + dx][y + dy].equals(ship)) {
                        return false;
                    }
                }
            }
            if(horizontalWay) {
                for(int dx = -1; dx <= 1; dx++) {
                    for(int dy = -1; dy <= 1; dy++) {
                        if(x + dx < 1 || x + dx > 7 || y + shipSize - 1 + dy < 1 || y + shipSize - 1 + dy > 7) {
                            continue;
                        }
                        if(gameField[x + dx][y + shipSize - 1 + dy].equals(ship)) {
                            return false;
                        }
                    }
                }
            }
            else {
                for(int dx = -1; dx <= 1; dx++) {
                    for(int dy = -1; dy <= 1; dy++) {
                        if(x + shipSize - 1 + dx < 1 || x + shipSize - 1 + dx > 7 || y + dy < 1 || y + dy > 7) {
                            continue;
                        }
                        if(gameField[x + shipSize - 1 + dx][y + dy].equals(ship)) {
                            return false;
                        }
                    }
                }
            }
        return true;
    }
    static void changeSunkShipSymbol() {
        if(waysForCheckingSunk.getFirst()) {
            boolean flag = true;
            for(int i = 0; i < 3; i++) {
                if(!outputField[coordinatesForCheckingSunk.get(0)][coordinatesForCheckingSunk.get(1) + i].equals(hit)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                for(int i = 0; i < 3; i++) {
                    gameField[coordinatesForCheckingSunk.get(0)][coordinatesForCheckingSunk.get(1) + i] = sunk;
                    outputField[coordinatesForCheckingSunk.get(0)][coordinatesForCheckingSunk.get(1) + i] = sunk;
                }
            }
        }
        else {
            boolean flag = true;
            for(int i = 0; i < 3; i++) {
                if(!outputField[coordinatesForCheckingSunk.get(0) + i][coordinatesForCheckingSunk.get(1)].equals(hit)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                for(int i = 0; i < 3; i++) {
                    gameField[coordinatesForCheckingSunk.get(0) + i][coordinatesForCheckingSunk.get(1)] = sunk;
                    outputField[coordinatesForCheckingSunk.get(0) + i][coordinatesForCheckingSunk.get(1)] = sunk;
                }
            }
        }

        if(waysForCheckingSunk.get(1)) {
            boolean flag = true;
            for (int i = 0; i < 2; i++) {
                if(!outputField[coordinatesForCheckingSunk.get(2)][coordinatesForCheckingSunk.get(3) + i].equals(hit)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                for (int i = 0; i < 2; i++) {
                    gameField[coordinatesForCheckingSunk.get(2)][coordinatesForCheckingSunk.get(3) + i] = sunk;
                    outputField[coordinatesForCheckingSunk.get(2)][coordinatesForCheckingSunk.get(3) + i] = sunk;
                }
            }
        }
        else {
            boolean flag = true;
            for (int i = 0; i < 2; i++) {
                if(!outputField[coordinatesForCheckingSunk.get(2) + i][coordinatesForCheckingSunk.get(3)].equals(hit)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                for (int i = 0; i < 2; i++) {
                    gameField[coordinatesForCheckingSunk.get(2) + i][coordinatesForCheckingSunk.get(3)] = sunk;
                    outputField[coordinatesForCheckingSunk.get(2) + i][coordinatesForCheckingSunk.get(3)] = sunk;
                }
            }
        }

        if(waysForCheckingSunk.get(2)) {
            boolean flag = true;
            for (int i = 0; i < 2; i++) {
                if(!outputField[coordinatesForCheckingSunk.get(4)][coordinatesForCheckingSunk.get(5) + i].equals(hit)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                for (int i = 0; i < 2; i++) {
                    gameField[coordinatesForCheckingSunk.get(4)][coordinatesForCheckingSunk.get(5) + i] = sunk;
                    outputField[coordinatesForCheckingSunk.get(4)][coordinatesForCheckingSunk.get(5) + i] = sunk;
                }
            }
        }
        else {
            boolean flag = true;
            for (int i = 0; i < 2; i++) {
                if(!outputField[coordinatesForCheckingSunk.get(4) + i][coordinatesForCheckingSunk.get(5)].equals(hit)) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                for (int i = 0; i < 2; i++) {
                    gameField[coordinatesForCheckingSunk.get(4) + i][coordinatesForCheckingSunk.get(5)] = sunk;
                    outputField[coordinatesForCheckingSunk.get(4) + i][coordinatesForCheckingSunk.get(5)] = sunk;
                }
            }
        }
        for (int i = 6; i < coordinatesForCheckingSunk.size(); i += 2) {
            if(outputField[coordinatesForCheckingSunk.get(i)][coordinatesForCheckingSunk.get(i + 1)].equals(hit)) {
                gameField[coordinatesForCheckingSunk.get(i)][coordinatesForCheckingSunk.get(i + 1)] = sunk;
                outputField[coordinatesForCheckingSunk.get(i)][coordinatesForCheckingSunk.get(i + 1)] = sunk;
            }
        }
    }
    static void clearSavedCoordinatesCheckingSunk() {
        coordinatesForCheckingSunk.clear();
        waysForCheckingSunk.clear();
    }
    static void prepareGameField() {
        for(int i = 1; i < 8; i++) {
            for(int j = 1; j < 8; j++) {
                gameField[i][j] = water;
                outputField[i][j] = water;
            }
        }
        String[] letters = {"   ", " A ", " B ", " C ", " D ", " E ", " F ", " G "};
        for(int i = 0; i < 8; i++) {
            gameField[i][0] = letters[i];
            outputField[i][0] = letters[i];
        }
        for(int i = 1; i < 8; i++) {
            gameField[0][i] = "  " + i + " ";
            outputField[0][i] = "  " + i + " ";
        }
    }
    static void showGameField() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                System.out.print(outputField[i][j]);
            }
            System.out.println();
        }
    }
    static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}