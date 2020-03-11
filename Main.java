package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main extends Application {
    /** Variable section */

    public static int random;                           //chokkay ja uthbe seta rakhte
    public static final int width=10;                   //number of  square
    public static final int height=10;
    public static final int tileSize=60;                //size of a square in pixel

    public static ArrayList<ArrayList<Integer>> map;    //this will contain all the connections

    public static ImageView deer;                       //players
    public static ImageView man;
    public static ImageView tiger;

    public static Group players = new Group();          //contains 3 player
    public static Group buttons = new Group();

    public static Label dan;                        //dan koto porse seta

    public static Boolean deerTurn = true;          //whether or not its players turn
    public static Boolean manTurn  = false;
    public static Boolean tigerTurn  = false;

    public static Boolean deer6 = false;            // 6 porse kina bujhte
    public static Boolean man6 = false;
    public static Boolean tiger6 = false;

    public static Boolean deerIsKilled = false;     //beche ache kina bujhte
    public static Boolean manIsKilled = false;
    public static Boolean tigerIsKilled = false;


    public static int deerX = -55;                  //position of deer in pixel
    public static int deerY = 545;
    public static int deerPos = 0;                  //Square number of deer
    public static Button deerButton = new Button();

    public static int manX = -55;                   //position of man in pixel
    public static int manY = 545;
    public static int manPos = 0;                   //Square number of man
    public static Button manButton = new Button();

    public static int tigerX = -55;                 //position of tiger in pixel
    public static int tigerY = 545;
    public static int tigerPos = 0;                 //Square number of tiger
    public static Button tigerButton = new Button();


    /**creates and displays the window*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        FileProcessing.mapMaker();
        Scene scene = new Scene(createScene());
        primaryStage.setTitle("Survival of the smartest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /** creating the method to create a board **/
    public Parent createScene() throws FileNotFoundException {
        Pane window = new Pane();

        window.setPrefSize(width*tileSize+20,(height)*tileSize+80);   //setting size of the window
        window.setStyle("-fx-background-color: #A9A9A9");

        //setting the bg
        FileInputStream input = new FileInputStream("photo/board.png");
        Image img = new Image(input);
        ImageView board = new ImageView();
        board.setImage(img);
        board.setFitHeight(604);
        board.setFitWidth(604);
        board.setTranslateX(8);
        board.setTranslateY(8);
        window.getChildren().add(board);

        //creating 3 player
        window.getChildren().add(createPlayer());

        //creating buttons
        window.getChildren().add(createButton());

        return window;
    }


    /**creating players*/
    public Group createPlayer() throws FileNotFoundException {

        FileInputStream inputDr = new FileInputStream("photo/deer.png");
        Image dr = new Image(inputDr);
        deer = new ImageView();
        deer.setImage(dr);
        deer.setFitWidth(50);
        deer.setFitHeight(50);
        deer.setTranslateX(deerX);
        deer.setTranslateY(deerY);

        FileInputStream inputMn = new FileInputStream("photo/hunter.png");
        Image mn = new Image(inputMn);
        man = new ImageView();
        man.setImage(mn);
        man.setFitHeight(50);
        man.setFitWidth(50);
        man.setTranslateX(manX);
        man.setTranslateY(manY);

        FileInputStream inputTg = new FileInputStream("photo/tiger.png");
        Image tg = new Image(inputTg);
        tiger = new ImageView();
        tiger.setImage(tg);
        tiger.setFitWidth(50);
        tiger.setFitHeight(50);
        tiger.setTranslateX(tigerX);
        tiger.setTranslateY(tigerY);


        players.getChildren().addAll(deer,tiger,man); //adding players
        return players;
    }

    /** creating Buttons*/
    public Group createButton(){

        dan = new Label("");
        dan.setTranslateX(295);             //setting the place for random number
        dan.setTranslateY(650);

        deerButton.setText("DEER");    //button for deer
        deerButton.setTranslateX(46);
        deerButton.setTranslateY(620);
        deerButton.setOnAction(e -> {                   //what will happen if deer was pressed
            if(deerTurn){
                random = MATH.randomGenerator();         //first random
                dan.setText(""+random);             //then show random

                if(deer6){                          // aage 6 pore thakle dan grohonjoggo hobe
                    Move.deerMoves(random);              //deer moves

                    if(deerPos<100 && map.get(deerPos-1).size()>1){
                        Move.deerJumps();
                        deerTurn = false;
                        tigerTurn = false;
                        manTurn = false;
                    }
                    else{
                        if(manIsKilled){
                            deerTurn=false;                 // turn changes
                            tigerTurn=true;
                        }
                        else{
                            deerTurn = false;
                            manTurn = true;
                        }

                        if(deerPos >= 100){
                            buttons.getChildren().removeAll(tigerButton,manButton,deerButton);
                            dan.setText("DEER IS THE WINNER");
                        }
                    }

                }
                else if( random == 6 ){             //aage 6 pore nai but ebar porse then abar khelbe ar ekhon theke gona shuru hobe
                    deer6 = true;
                }
                else{                               //ageo 6 pore nai, ekhono na taile dan charai
                    deerTurn=false;                 // turn change hobe
                    manTurn=true;
                }
            }
        });

        manButton.setText("MAN");       //button for man
        manButton.setTranslateX(287);
        manButton.setTranslateY(620);
        manButton.setOnAction(e -> {
            if(manTurn) {
                random = MATH.randomGenerator();         //first random
                dan.setText(""+random);             //then show random

                if(man6){                          // aage 6 pore thakle dan grohonjoggo hobe
                    Move.manMoves(random);              //deer moves

                    if(manPos <100 && map.get(manPos-1).size()>1){
                        Move.manJumps();
                        deerTurn = false;
                        manTurn = false;
                        tigerTurn = false;
                    }
                    else{
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
                    }
                }
                else if( random == 6 ){             //aage 6 pore nai but ebar porse then abar khelbe ar ekhon theke gona shuru hobe
                    man6 = true;
                }
                else{                               //ageo 6 pore nai, ekhono na taile dan charai
                    manTurn=false;                 // turn change hobe
                    tigerTurn=true;
                }
            }
        });

        tigerButton.setText("TIGER");   //button for tiger
        tigerButton.setTranslateX(527);
        tigerButton.setTranslateY(620);
        tigerButton.setOnAction(e -> {
            if(tigerTurn) {
                random = MATH.randomGenerator();         //first random
                dan.setText(""+random);             //then show random

                if(tiger6){                         // aage 6 pore thakle dan grohonjoggo hobe
                    Move.tigerMoves(random);             //deer moves

                    if(tigerPos<=100 && map.get(tigerPos-1).size()>1){
                        Move.tigerJumps();
                        deerTurn = false;
                        manTurn= false;
                        tigerTurn = false;
                    }

                    else{
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
                    }



                }
                else if( random == 6 ){             //aage 6 pore nai but ebar porse then abar khelbe ar ekhon theke gona shuru hobe
                    tiger6 = true;
                }
                else{                               //ageo 6 pore nai, ekhono na taile dan charai
                    tigerTurn=false;                 // turn change hobe
                    if(deerIsKilled)
                        manTurn = true;
                    else
                        deerTurn=true;
                }
            }
        });

        buttons.getChildren().addAll(deerButton,manButton,tigerButton,dan); //adding these to window

        return buttons;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
