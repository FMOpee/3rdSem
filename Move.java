package sample;

import javafx.scene.control.Button;

import java.util.ArrayList;

import static sample.FileProcessing.parseInteger;
import static sample.MATH.translatorXY;
import static sample.Main.*;

public class Move {
    /** Method to move deer */
    public static void deerMoves(int role){
        Main.deerPos += role;
        int[] xy = translatorXY(Main.deerPos);
        Main.deerX = xy[0] + 5; // bacause the player size is 50. so they need a 5 padding
        Main.deerY = xy[1] + 5;

        deer.setTranslateX(Main.deerX);
        deer.setTranslateY(Main.deerY);
    }

    public static void deerJumps(){
        ArrayList<Button> temporary = new ArrayList<>();

        for(int i=1;i<Main.map.get(Main.deerPos-1).size();i++){
            int pos = Main.map.get(Main.deerPos-1).get(i);
            Button b = new Button(""+pos);
            b.setMinSize(60,60);
            b.setTranslateX(translatorXY(pos)[0]);
            b.setTranslateY(translatorXY(pos)[1]);
            b.setStyle("-fx-background-color:#FFD700; -fx-font-size: 2em; -fx-border-width: 2px; -fx-border-color: #000000");
            temporary.add(b);
        }
        for(int i=0; i < temporary.size(); i++){
            int finalI = i;
            temporary.get(i).setOnAction(e -> {
                Main.deerPos = parseInteger( temporary.get(finalI).getText());
                deer.setTranslateX(translatorXY(Main.deerPos)[0]+5);
                deer.setTranslateY(translatorXY(Main.deerPos)[1]+5);
                for(int j=0; j< temporary.size();j++){
                    Main.buttons.getChildren().remove(temporary.get(j));
                }

                if(Main.manIsKilled){
                    Main.deerTurn=false;                 // turn changes
                    Main.tigerTurn=true;
                }
                else{
                    Main.deerTurn = false;
                    Main.manTurn = true;
                }

                if(Main.deerPos >= 100){
                    Main.buttons.getChildren().removeAll(tigerButton,manButton,deerButton);
                    dan.setText("DEER IS THE WINNER");
                }
            });
            buttons.getChildren().add(temporary.get(i));
        }

    }

    /** Method to move man */
    public static void manMoves(int role){
        manPos += role;
        int[] xy = translatorXY(manPos);
        manX = xy[0] + 5;
        manY = xy[1] + 5;

        man.setTranslateX(manX);
        man.setTranslateY(manY);
    }

    public static void manJumps() {
        ArrayList<Button> temporary = new ArrayList<>();

        for(int i=1;i<map.get(manPos-1).size();i++){
            int pos = map.get(manPos-1).get(i);
            Button b = new Button(""+pos);
            b.setMinSize(60,60);
            b.setTranslateX(translatorXY(pos)[0]);
            b.setTranslateY(translatorXY(pos)[1]);
            b.setStyle("-fx-background-color:#FFD700; -fx-font-size: 2em; -fx-border-width: 2px; -fx-border-color: #000000");
            temporary.add(b);
        }
        for(int i=0; i < temporary.size(); i++){
            int finalI = i;
            temporary.get(i).setOnAction(e -> {
                manPos = parseInteger( temporary.get(finalI).getText());
                man.setTranslateX(translatorXY(manPos)[0]+5);
                man.setTranslateY(translatorXY(manPos)[1]+5);
                for(int j=0; j< temporary.size();j++){
                    buttons.getChildren().remove(temporary.get(j));
                }

                if (manPos == deerPos){        // jodi horin ke kheye fele
                    deerIsKilled=true;
                    deerPos = -2;
                    players.getChildren().remove(deer);
                    buttons.getChildren().remove(deerButton);
                }

                tigerTurn = true;
                manTurn = false;

                if(deerIsKilled && manPos >= 100 ){           // horin mara gele ar manush 100 ghor par hoile manush jitse
                    manTurn = false;
                    dan.setText("MAN IS THE WINNER");
                    buttons.getChildren().removeAll(tigerButton,manButton);
                    players.getChildren().removeAll(tigerButton);
                }

            });
            buttons.getChildren().add(temporary.get(i));
        }

    }

    /** Method to move tiger */
    public static void tigerMoves(int role) {
        tigerPos += role;
        int[] xyNow = translatorXY(tigerPos);
        tigerX = xyNow[0] + 5;
        tigerY = xyNow[1] + 5;

        tiger.setTranslateX(tigerX);
        tiger.setTranslateY(tigerY);
    }

    public static void tigerJumps(){
        ArrayList<Button> temporary = new ArrayList<>();

        for(int i=1;i<map.get(tigerPos-1).size();i++){
            int pos = map.get(tigerPos-1).get(i);
            Button b = new Button(""+pos);
            b.setMinSize(60,60);
            b.setTranslateX(translatorXY(pos)[0]);
            b.setTranslateY(translatorXY(pos)[1]);
            b.setStyle("-fx-background-color:#FFD700; -fx-font-size: 2em; -fx-border-width: 2px; -fx-border-color: #000000");
            temporary.add(b);
        }
        for(int i=0; i < temporary.size(); i++){
            int finalI = i;
            temporary.get(i).setOnAction(e -> {
                tigerPos = parseInteger( temporary.get(finalI).getText());
                tiger.setTranslateX(translatorXY(tigerPos)[0]+5);
                tiger.setTranslateY(translatorXY(tigerPos)[1]+5);
                for(int j=0; j< temporary.size();j++){
                    buttons.getChildren().remove(temporary.get(j));
                }

                if (tigerPos == deerPos){        // jodi horin ke kheye fele
                    deerIsKilled=true;
                    deerPos = -2;
                    players.getChildren().remove(deer);
                    buttons.getChildren().remove(deerButton);
                    manTurn = true;             // tahole ebar manusher dan
                    tigerTurn = false;
                }
                else if(tigerPos == manPos){    // ar jodi manushke kheye fele tahole
                    manIsKilled = true;
                    players.getChildren().remove(man);
                    buttons.getChildren().remove(manButton);
                    manPos = -1;
                    deerTurn = true;            // horin er e dan hobe er por
                    tigerTurn = false;
                }
                else if(deerIsKilled && ! manIsKilled){     // jodi kono khawa khawi na hoy ar horin allready mara giye thake
                    manTurn = true;
                    tigerTurn = false;
                }
                else if(!deerIsKilled){                     // horin jibito thakle dan horin er
                    deerTurn = true;
                    tigerTurn = false;
                }


                if(deerIsKilled && manIsKilled){           // horin, manush 2 jon e mara gele bagh jitse
                    tigerTurn = false;
                    dan.setText("TIGER IS THE WINNER");
                    buttons.getChildren().remove(tigerButton);
                }
            });
            buttons.getChildren().add(temporary.get(i));
        }

    }
}
