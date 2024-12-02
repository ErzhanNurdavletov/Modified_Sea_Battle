import java.util.Scanner;
import java.util.Random;

public class Main{

    static Random random = new Random();
    static Scanner sc = new Scanner(System.in);

    static String[][] gameField = new String[7][7];
    static String[][] outputField = new String[7][7];

    static String ship = " ⚓ ";
    static String water = " \uD83C\uDF0A ";
    static String miss = " ❌ ";
    static String hit = " \uD83D\uDCA5 ";

    public static void main(String[] args) {
        fillGameFieldWater();
        placeShips();
        takeInputCoordinates();
        showGameField();
    }
    static void takeInputCoordinates() {
        while(checkGameFinished()) {
            showGameField();
            System.out.print("Enter coordinates like this -> <0 6>: ");
            int inputX = sc.nextInt();
            int inputY = sc.nextInt();

            if(gameField[inputX][inputY].equals(ship)) {
                gameField[inputX][inputY] = hit;
                outputField[inputX][inputY] = hit;
                System.out.println("Hit!");
            }
            else if(gameField[inputX][inputY].equals(hit) || gameField[inputX][inputY].equals(miss)) {
                System.out.println("Was already hit");
            }
            else {
                System.out.println(" Miss...");
                gameField[inputX][inputY] = miss;
                outputField[inputX][inputY] = miss;
            }
        }
    }
    static boolean checkGameFinished() {
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
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
                Xcoordinate = random.nextInt(7);
                Ycoordinate = random.nextInt(8 - shipSize);
            } else {
                Xcoordinate = random.nextInt(8 - shipSize);
                Ycoordinate = random.nextInt(7);
            }
            if (CanPlaceShip(Xcoordinate, Ycoordinate, horizontalWay, shipSize)) {
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
    static boolean CanPlaceShip(int Xcoordinate, int Ycoordinate, boolean horizontalWay, int shipSize) {
        if(horizontalWay) {
            for (int i = 0; i < shipSize; i++) {
                if(gameField[Xcoordinate][Ycoordinate + i].equals(ship)) {
                    return false;
                }
            }
        }
        else {
            for (int i = 0; i < shipSize; i++) {
                if(gameField[Xcoordinate + i][Ycoordinate].equals(ship)) {
                    return false;
                }
            }
        }
        return true;
    }
    static void fillGameFieldWater() {
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                gameField[i][j] = water;
                outputField[i][j] = water;
            }
        }
    }
    static void showGameField() {
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                System.out.print(gameField[i][j]);
            }
            System.out.println();
        }
    }

}