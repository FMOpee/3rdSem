package _1_Main_Package;

import _2_SPL_Off_Multi.MPGame;
//import _2_SPL_Single.SPGame;

import java.io.*;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 *
 * @author Fahim Morshed
 * IITDU, BSSE1102
 * Email: bsse1102@iit.du.ac.bd
 * 
 */

public class Launcher extends Application{
    private static Scene startScene, firstMenu, multiModeMenu;
    public static MPGame mPGame;
    //public static SPGame sPGame;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Background image for menu
        ImageView menuBG = new ImageView (new Image (new FileInputStream ("src\\_3_photo\\board\\MenuBG.png")));
        menuBG.setOpacity(.60);

        //creating first scene. this will show up in the start of the game.
        Button playButton = new Button("PLAY");
        playButton.setMinSize(150,50);
        playButton.setTranslateX(132);//
        playButton.setTranslateY(75);
        playButton.setOnAction(event -> primaryStage.setScene(firstMenu));
        
        Button quitButton = new Button("QUITE");
        quitButton.setMinSize(150, 50);
        quitButton.setTranslateX(382);
        quitButton.setTranslateY(75);
        quitButton.setOnAction(event -> primaryStage.close());
        
        Group startGroup = new Group();
        startGroup.getChildren().addAll(menuBG,playButton, quitButton);
        startScene = new Scene(startGroup, 664, 500);

        //creating the First Menu. this will appear after we click play in the firstScene.
        //Background image for menu
        ImageView firstMenuBG = new ImageView (new Image (new FileInputStream ("src\\_3_photo\\board\\MenuBG.png")));
        firstMenuBG.setOpacity(.60);

        Button loadButton = new Button("CONTINUE");
        loadButton.setMinSize(150,50);
        loadButton.setTranslateX(57);
        loadButton.setTranslateY(75);
        loadButton.setOnAction(e -> {
            try {
                gameLoader(primaryStage);
            } catch (FileNotFoundException ignored) {}
        });

        Button spButton = new Button("EXIT");
        spButton.setMinSize(150, 50);
        spButton.setTranslateX(457);
        spButton.setTranslateY(75);
        spButton.setOnAction(event -> primaryStage.setScene(startScene));
        
        Button mpButton = new Button("NEW GAME");
        mpButton.setMinSize(150,50);
        mpButton.setTranslateX(257);
        mpButton.setTranslateY(75);
        mpButton.setOnAction(event -> primaryStage.setScene(multiModeMenu));
        
        Group firstGroup = new Group();
        firstGroup.getChildren().addAll(firstMenuBG,loadButton, spButton, mpButton);
        firstMenu = new Scene(firstGroup, 664, 500);

        //this scene will show up if we press the multi player button.
        ImageView multiModeMenuBG = new ImageView (new Image (new FileInputStream ("src\\_3_photo\\board\\MenuBG.png")));
        multiModeMenuBG.setOpacity(.60);

        Rectangle rec = new Rectangle(550,25);
        rec.setTranslateX(57);
        rec.setTranslateY(15);
        rec.setFill(Color.WHITE);

        Label currentSelectionLabel = new Label("How Strong Do You Want Your River Currents to Be?");
        currentSelectionLabel.setTranslateX(61);
        currentSelectionLabel.setTranslateY(15);
        currentSelectionLabel.setStyle("-fx-font: bold 16pt \"Arial\";");

        Button level1 = new Button("NORMAL");
        level1.setMinSize(150,50);
        level1.setTranslateX(57);
        level1.setTranslateY(75);
        level1.setOnAction(e -> {
            try {
                mPGame = new MPGame(primaryStage,1);
            } catch (IOException ignored) {}
        });

        Button level2 = new Button("STRONG");
        level2.setMinSize(150, 50);
        level2.setTranslateX(257);
        level2.setTranslateY(75);
        level2.setOnAction(event -> {
            try {
                mPGame = new MPGame(primaryStage,2);
            } catch (IOException ignored) {}
        });

        Button level3 = new Button("VERY STRONG");
        level3.setMinSize(150,50);
        level3.setTranslateX(457);
        level3.setTranslateY(75);
        level3.setOnAction(event -> {
            try {
                mPGame = new MPGame(primaryStage,3);
            } catch (IOException ignored) {}
        });

        Group multiGroup = new Group();
        multiGroup.getChildren().addAll(multiModeMenuBG,rec,currentSelectionLabel,level1, level2, level3);
        multiModeMenu = new Scene(multiGroup, 664, 500);

        //setting the stage
        primaryStage.setTitle("");
        primaryStage.setScene(startScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();

            if(primaryStage.getScene().equals(firstMenu))
                primaryStage.setScene(startScene);
            else if(primaryStage.getScene().equals(multiModeMenu))
                primaryStage.setScene(firstMenu);
            else
                primaryStage.close();
        });
    }

    /** To load previously saved game*/
    private static void gameLoader(Stage stage) throws FileNotFoundException {
        File resume = new File("src\\_4_Files\\resume.txt");
        Scanner sc = new Scanner(resume);
        int mapState = sc.nextInt();
        int rc = sc.nextInt();
        int turn = sc.nextInt();
        int[] positions = new int[3];
        for (int i=0;i<3;i++) positions[i]=sc.nextInt();
        int[] fruitsState = new int[3];
        for (int i=0; i<3; i++) fruitsState[i] = sc.nextInt();
        int[][] playerState = new int[3][3];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                playerState[i][j] = sc.nextInt();
            }
        }

        mPGame = new MPGame(stage,mapState,rc,turn,positions,fruitsState,playerState);
    }
    
    public static void main(String[] args) {
        launch(args);
    }   
}
