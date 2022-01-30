package _2_SPL_Off_Multi;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import static _2_SPL_Off_Multi.MPGame.*;


/**
 *
 * @author Fahim Morshed
 * IITDU, BSSE1102
 * Email: bsse1102@iit.du.ac.bd
 * 
 */

public class MathMethods {
    public static ImageView[] rollingDice = new  ImageView[5];
    public static ImageView[] rolledDice = new ImageView[6];
    public static int lastRolled = 6;

    public static double[] translatorXY(int number){
        double[] xy = new double[2];
        if((number / 10) % 2 == 0) xy[0] = (number % 10) * 60;
        else xy[0] = 600 - (number % 10 + 1) * 60;
        xy[1] = 600 - (number / 10 + 1) * 60;

        return xy;
    }
    
    /*random number. not anything fancy. as simple as it gets*/
    public static int randomGenerator(){
        long  time = (new Date()).getTime();
        int random = (int) (time % 100);
        if(random<10) return 1;
        else if(random<25) return 2;
        else if(random<40) return 3;
        else if(random<55) return 4;
        else if(random<75) return 5;
        else  return 6;
    }
    
    public static String timeGetter(){
        seconds++;
        int min = seconds/60;
        int fd = min/10;
        int sd = min%10;
        int sec = seconds%60;
        int td = sec/10;
        int _4d = sec %10;
        return fd+sd+" : "+td+_4d;
    }

    public static void animation(int player, int to, double duration){
        int xc = (int) (translatorXY(to)[0]+5 - players[player].getImage().getTranslateX());
        int yc = (int) (translatorXY(to)[1]+5 - players[player].getImage().getTranslateY());
        players[player].setPosition(to);
        if(xc>0){
            Timeline xt = new Timeline(new KeyFrame(Duration.millis((int) (duration*1000/xc)) , e ->
                players[player].getImage().setTranslateX(players[player].getImage().getTranslateX()+1)
            ));
            xt.setCycleCount(xc);
            xt.play();
        }
        else if (xc<0){
            Timeline xt = new Timeline(new KeyFrame(Duration.millis((int) (duration*1000/Math.abs(xc))) , e ->
                players[player].getImage().setTranslateX(players[player].getImage().getTranslateX()-1)
            ));
            xt.setCycleCount(Math.abs(xc));
            xt.play();
        }


        if(yc>0) {
            Timeline yt = new Timeline(new KeyFrame(Duration.millis((int) (duration * 1000 / yc)), e ->
                players[player].getImage().setTranslateY(players[player].getImage().getTranslateY() + 1)
            ));
            yt.setCycleCount(yc);
            yt.play();
        }
        else if(yc<0) {
            Timeline yt = new Timeline(new KeyFrame(Duration.millis((int) (duration * 1000 /Math.abs(yc))), e ->
                players[player].getImage().setTranslateY(players[player].getImage().getTranslateY() - 1)
            ));
            yt.setCycleCount(Math.abs(yc));
            yt.play();
        }
        posLabel[player].setText((to+1)+"");
    }

    public static void importDiceImage() throws FileNotFoundException {
        for(int i=0; i<5;i++){
            rollingDice[i] = new ImageView(new Image(new FileInputStream("src\\_3_photo\\dices\\rolling\\dice"+(i+1)+".png")));
            rollingDice[i].setFitHeight(80);
            rollingDice[i].setFitWidth(80);
            rollingDice[i].setTranslateX(730);
            rollingDice[i].setTranslateY(565);
        }
        for(int i=0; i<6; i++){
            rolledDice[i] = new ImageView(new Image(new FileInputStream("src\\_3_photo\\dices\\rolled\\"+(i+1)+".png")));
            rolledDice[i].setFitHeight(70);
            rolledDice[i].setFitWidth(70);
            rolledDice[i].setTranslateX(735);
            rolledDice[i].setTranslateY(570);
        }
    }

    static int tracking = 0;

    public static void animateDice(int random){
        tracking =0;
        sbGroup.getChildren().remove(rolledDice[lastRolled-1]);
        sbGroup.getChildren().add(rollingDice[tracking%5]);
        tracking++;
        Timeline diceAnimationTimeline = new Timeline(new KeyFrame(Duration.millis(66.6667) , e ->{
            sbGroup.getChildren().remove(rollingDice[(tracking-1)%5]);
            sbGroup.getChildren().add(rollingDice[tracking%5]);
            tracking++;
        }));
        diceAnimationTimeline.setCycleCount(15);
        diceAnimationTimeline.play();

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() {
            try {
                Thread.sleep(1020);      //to make it sleep while the player is still moving
            } catch (InterruptedException ignored) {}
            return null;
            }
        };
        sleeper.setOnSucceeded(event -> {
            sbGroup.getChildren().remove(rollingDice[(tracking-1)%5]);
            sbGroup.getChildren().add(rolledDice[random-1]);
            lastRolled = random;
        });
        new Thread(sleeper).start();
    }
}
