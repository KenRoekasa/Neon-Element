package client;

import entities.Player;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;

public class InputHandler {


    // this needs to be made much more efficient !!!!
    // possibilities are:
    // threading, faster angle calculation, both
    public static void mouseAngleCalc(Player player, Stage primaryStage, MouseEvent event) {
        double opposite = primaryStage.getWidth()/2 - event.getX();

        double adjacent = primaryStage.getHeight()/2 - event.getY();

        double angle = Math.atan(Math.abs(opposite)/Math.abs(adjacent));
        angle = Math.toDegrees(angle);

        if(adjacent < 0 && opposite < 0) {
            angle = 180 - angle;
        } else if (adjacent < 0) {
            angle = angle + 180;
        } else if (opposite > 0) {
            angle = 360 - angle;
        }

        if(angle - 45 >= 0 ) {
            angle -= 45;
        } else {
            angle += 315;
        }

        player.setPlayerAngle(new Rotate(angle));
    }

    public static void handleInput(Player player, ArrayList<String> input, Rectangle board) {
        boolean left = input.contains("LEFT") || input.contains("A");
        boolean right = input.contains("RIGHT") || input.contains("D");
        boolean up = input.contains("UP") || input.contains("W");
        boolean down = input.contains("DOWN") || input.contains("S");


        if (left && up || left & down || right && up || right & down) {
            if (left & up) {
                player.moveLeftCartesian();
            }
            if (left && down) {
                player.moveDownCartestian(board.getHeight());
            }

            if (right && up) {
                player.moveUpCartesian();
            }

            if (right && down){
                player.moveRightCartesian(board.getWidth());
            }
        } else {
            moveIsometric(player, board, left, right, up, down);
        }

    }

    private static void moveIsometric(Player player, Rectangle board, boolean left, boolean right, boolean up, boolean down) {
        if (left) {
            player.moveLeft(board.getWidth());
        }

        if (up) {
            player.moveUp();
        }

        if (right) {
            player.moveRight(board.getWidth(), board.getHeight());
        }
        if (down) {
            player.moveDown(board.getWidth(), board.getHeight());
        }
    }

}
