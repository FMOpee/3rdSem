package _2_SPL_Off_Multi;

//import static _2_SPL_Single.MathMethodsForSingle.waiting;
import static _2_SPL_Off_Multi.MPGame.*;
import static _2_SPL_Off_Multi.MathMethods.*;
import static _2_SPL_Off_Multi.MoveMethods.deerJumps;
import static _2_SPL_Off_Multi.MoveMethods.manJumps;
import static _2_SPL_Off_Multi.MoveMethods.tigerJumps;
import java.nio.file.Paths;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author Fahim Morshed
 * IITDU, BSSE1102
 * Email: bsse1102@iit.du.ac.bd
 * 
 */

public class Utils {
    public static void deerPressed(){
        if(turn == 0) {
            music();
            random = randomGenerator();         //first random
            //dan.setText(""+random);             //then show random
            animateDice(random);


            Task<Void> sleeperTillDiceRole = new Task<Void>() {
                @Override
                protected Void call() {
                try {
                    Thread.sleep(1025);      //to make it sleep while the dice is still rolling
                } catch (InterruptedException ignored) {}
                return null;
                }
            };
            sleeperTillDiceRole.setOnSucceeded(eventTillDiceRole -> {
                if(players[0].isPlaying()){                          // aage 6 pore thakle dan grohonjoggo hobe
                    Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(210) , e ->{
                        animation(0,players[0].getPosition()+1 , 0.20);              //deer moves
                    }));
                    animationTimeline.setCycleCount(random);
                    animationTimeline.play();


                    Task<Void> sleeper = new Task<Void>() {
                        @Override
                        protected Void call(){
                            try {
                                Thread.sleep((random+1)* 210L);      //to make it sleep while the player is still moving
                            } catch (InterruptedException ignored) {}
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(event -> {
                        if(players[0].getPosition() > 99){
                            if(players[0].haveEaten()){           // already khawa dawa hoye gele ar horin 100 ghor par hoile horin jitse
                                turn = 3;
                                winningMessage.setText("DEER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[1].getImage(),players[2].getImage());
                                isEnded = true;
                            }
                            else if(players[2].haveEaten() && !players[2].isKilled()){     //Ar bagher khawa dawa hoile se jitse
                                turn = 3;
                                winningMessage.setText("TIGER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[1].getImage());
                                isEnded = true;
                            }
                            else if(players[2].isKilled() && !players[1].isKilled()){     //bagh o mara gele manush jitbe
                                turn = 3;
                                winningMessage.setText("HUNTER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[2].getImage());
                                isEnded = true;
                            }
                            else{           // Ar naile khela stealmate
                                turn = 3;
                                winningMessage.setText("DEER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[2].getImage());
                                isEnded =true;
                            }
                        }
                        else if( map.get(players[0].getPosition()).size()>1){  //jump korte parle korbe
                            deerJumps();
                            turn =3;                    // so that people cannot press any player buttons while temporary buttons are available
                            deerButton.setImage(buttonImages[0]);          //deer playable
                        }
                        else{
                            int fruitsIndex = 3;
                            for (int i=0; i<3; i++){    //dekhbe kon folta khay
                                if(players[0].getPosition() == fruitsPosition[i] && fruitsExistence[i]) {
                                    fruitsIndex=i;
                                    break;
                                }
                            }

                            if(fruitsIndex != 3) {      //jodi fol khay
                                if(!players[0].haveEaten()){
                                    players[0].setEaten(true);                              //eaten is true now
                                    sbGroup.getChildren().add(sbComponents.get(3));
                                }
                                fruitsExistence[fruitsIndex] = false;
                                bgGroup.getChildren().remove(fruits[fruitsIndex]);
                                if(players[1].isKilled()){         //manush more giye thakle
                                    turn = 2;
                                    tigerButton.setImage(buttonImages[7]);          //tiger playing
                                }
                                else{               //naile manusher khela
                                    turn = 1;
                                    hunterButton.setImage(buttonImages[4]);          //hunter playing
                                }
                                deerButton.setImage(buttonImages[0]);          //deer playable
                            }
                            else if (players[1].getPosition() == players[0].getPosition()){        // jodi manush ke ahoto kore
                                if(players[1].isInjured() && !players[2].isKilled()){         // manush jodi age thekei ahoto hoy ar bagh jibito hoy
                                    players[1].setKilled(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(6));
                                    sbGroup.getChildren().addAll(sbComponents.get(5),sbComponents.get(9));
                                    players[1].setPosition(-2);
                                    playerGroup.getChildren().remove(players[1].getImage());
                                    hunterButton.setImage(buttonImages[5]);          //hunter dead
                                    turn = 0;                                       //horin mair dan paibe so dan change hobe na
                                }
                                else if(players[1].isInjured() && players[2].isKilled()){       //bagh o mara gele horin jitse
                                    players[1].setKilled(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(6));
                                    sbGroup.getChildren().addAll(sbComponents.get(5),sbComponents.get(9));
                                    players[1].setPosition(-2);
                                    playerGroup.getChildren().remove(players[1].getImage());
                                    hunterButton.setImage(buttonImages[5]);          //hunter dead
                                    turn = 3;
                                    winningMessage.setText("DEER IS THE WINNER");
                                    utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                    playerGroup.getChildren().removeAll(players[1].getImage(),players[2].getImage());
                                    isEnded =true;
                                }
                                else{               // ar naile manush 50% hoiya jaibe
                                    players[1].setInjured(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(7));
                                    sbGroup.getChildren().addAll(sbComponents.get(6));
                                    turn = 0;                                      //horin mair-dain pabe so dan o change hobe na
                                }
                            }
                            else if(players[0].getPosition() ==  players[2].getPosition()){     //jodi baghere ahoto kore
                                if(players[2].isInjured() && !players[1].isKilled()){         // bagh jodi age thekei ahoto hoy ar manush jibito hoy
                                    players[2].setKilled(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(11));
                                    sbGroup.getChildren().addAll(sbComponents.get(10),sbComponents.get(14));
                                    players[2].setPosition(-2);
                                    playerGroup.getChildren().remove(players[2].getImage());
                                    tigerButton.setImage(buttonImages[8]);          //tiger dead
                                    turn = 0;                                       //horin mair-dain pabe so dan change hobe na
                                }
                                else if(players[2].isInjured() && players[1].isKilled()){       //manush o mara gele horin jitse
                                    players[2].setKilled(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(11));
                                    sbGroup.getChildren().addAll(sbComponents.get(10),sbComponents.get(14));
                                    players[2].setPosition(-2);
                                    playerGroup.getChildren().remove(players[2].getImage());
                                    tigerButton.setImage(buttonImages[8]);          //tiger dead
                                    turn = 3;
                                    winningMessage.setText("DEER IS THE WINNER");
                                    utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                    playerGroup.getChildren().removeAll(players[1].getImage(),players[2].getImage());
                                    isEnded =true;
                                }
                                else{               // ar naile bagh 50% hoiya jaibe
                                    players[2].setInjured(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(12));
                                    sbGroup.getChildren().addAll(sbComponents.get(11));
                                    turn = 0;                                      //HORIN MAIR-DAIN PABE. SO turn change hobe na
                                }
                            }
                            else{           // naile khali turn change hobe
                                if(!players[1].isKilled()){     // bagh mara gele horiner khela
                                    turn = 1;
                                    hunterButton.setImage(buttonImages[4]);          //hunter playing
                                }
                                else{       // naile bagher khela
                                    turn = 2;
                                    tigerButton.setImage(buttonImages[7]);          //tiger playing
                                }
                                deerButton.setImage(buttonImages[0]);          //deer playable
                            }
                        }
                    });
                    new Thread(sleeper).start();

                }
                else if( random == 6 ){             //aage 6 pore nai but ebar porse then abar khelbe ar ekhon theke gona shuru hobe
                    players[0].setPlaying(true);
                    sbGroup.getChildren().add(sbComponents.get(2));
                }
                else{                               //ageo 6 pore nai, ekhono na taile dan charai turn change hobe
                    if(!players[1].isKilled()){
                        turn = 1;
                        hunterButton.setImage(buttonImages[4]);          //hunter playing
                    }
                    else{
                        turn = 2;
                        tigerButton.setImage(buttonImages[7]);          //tiger playing
                    }
                    deerButton.setImage(buttonImages[0]);          //deer playable
                }
            });
            new Thread(sleeperTillDiceRole).start();

        }
    }

    public static void manPressed(){
        if(turn == 1) {
            music();
            random = randomGenerator();         //first random
            //dan.setText(""+random);             //then show random
            animateDice(random);

            Task<Void> sleeperTillDiceRole = new Task<Void>() {
                @Override
                protected Void call(){
                    try {
                        Thread.sleep(1025);      //to make it sleep while the player is still moving
                    } catch (InterruptedException ignored) {}
                    return null;
                }
            };
            sleeperTillDiceRole.setOnSucceeded(eventTillDiceRole -> {
                if(players[1].isPlaying()){                          // aage 6 pore thakle dan grohonjoggo hobe
                    Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(210) , e ->{
                        animation(1,players[1].getPosition()+1 , 0.20);              //hunter moves
                    }));
                    animationTimeline.setCycleCount(random);
                    animationTimeline.play();

                    Task<Void> sleeper = new Task<Void>() {
                        @Override
                        protected Void call(){
                            try {
                                Thread.sleep((random+1)* 210L);
                            } catch (InterruptedException ignored) {}
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(event -> {
                        if(players[1].getPosition() > 99){
                            if(players[1].haveEaten()){           // already khawa dawa hoye gele ar manush 100 ghor par hoile manush jitse
                                turn = 3;
                                winningMessage.setText("MAN IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[0].getImage(),players[2].getImage());
                                isEnded =true;
                            }
                            else if(players[0].isKilled() && !players[2].isKilled()){     //manusher khawa na hoile but bagher khawa dawa hoile se jitse
                                turn = 3;
                                winningMessage.setText("TIGER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[0].getImage());
                                isEnded =true;
                            }
                            else if(players[2].isKilled()){     //bagh o mara gele horin jitbe
                                turn = 3;
                                winningMessage.setText("DEER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                playerGroup.getChildren().removeAll(players[1].getImage());
                                isEnded =true;
                            }
                            else{           // Ar naile bagh ar horiner khela
                                turn = 2;
                                players[1].setKilled(true);
                                hunterButton.setImage(buttonImages[5]);          //hunter dead
                                tigerButton.setImage(buttonImages[7]);          //tiger playing
                            }
                        }
                        else if( map.get(players[1].getPosition()).size()>1){  //jump korte parle korbe
                            manJumps();
                            turn =3;                    // so that people cannot press any player buttons while temporary buttons are available
                            hunterButton.setImage(buttonImages[3]);          //hunter playable
                        }
                        else{
                            int fruitsIndex = 3;
                            for (int i=0; i<3; i++){    //dekhbe kon folta khay
                                if(players[1].getPosition() == fruitsPosition[i] && fruitsExistence[i]) {
                                    fruitsIndex=i;
                                    break;
                                }
                            }

                            if(fruitsIndex != 3){      //jodi fol khay
                                if(!players[1].haveEaten()){
                                    sbGroup.getChildren().add(sbComponents.get(8));
                                    players[1].setEaten(true);
                                }
                                fruitsExistence[fruitsIndex] = false;
                                bgGroup.getChildren().remove(fruits[fruitsIndex]);
                                if(!players[2].isKilled()){         //bagh jibito thakle
                                    turn = 2;
                                    tigerButton.setImage(buttonImages[7]);          //tiger playing
                                }
                                else{               //naile horiner khela
                                    turn = 0;
                                    deerButton.setImage(buttonImages[1]);          //deer playing
                                }
                                hunterButton.setImage(buttonImages[3]);          //hunter playable
                            }
                            else if (players[1].getPosition() == players[0].getPosition()){        // jodi horin ke kheye fele
                                players[0].setKilled(true);
                                if(!players[1].haveEaten()){
                                    sbGroup.getChildren().add(sbComponents.get(8));
                                    players[1].setEaten(true);
                                }
                                sbGroup.getChildren().removeAll(sbComponents.get(2));
                                sbGroup.getChildren().addAll(sbComponents.get(0),sbComponents.get(4));
                                players[0].setPosition(-2);
                                playerGroup.getChildren().remove(players[0].getImage());
                                deerButton.setImage(buttonImages[2]);          //deer dead

                                if(!players[2].isKilled()){         //bagh jibito thakle
                                    turn = 1;                               //hunter mair-dain pabe. so turn change hobe na
                                }
                                else{               //naile manush jitse
                                    turn = 3;
                                    winningMessage.setText("MAN IS THE WINNER");
                                    utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                    playerGroup.getChildren().removeAll(players[0].getImage(),players[2].getImage());
                                    isEnded =true;
                                }
                            }
                            else if(players[1].getPosition() ==  players[2].getPosition()){     //jodi baghere ahoto kore
                                if(players[2].isInjured() && !players[0].isKilled()){         // bagh jodi age thekei ahoto hoy ar horin jibito hoy
                                    players[2].setKilled(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(11));
                                    sbGroup.getChildren().addAll(sbComponents.get(10),sbComponents.get(14));
                                    players[2].setPosition(-2);
                                    playerGroup.getChildren().remove(players[2].getImage());
                                    tigerButton.setImage(buttonImages[8]);          //tiger dead
                                    turn = 1;                                       //hunter mair-dain pabe. so turn change hobe na
                                }
                                else if(players[2].isInjured() && players[0].isKilled()){       //horin o mara gele manush jitse
                                    players[2].setKilled(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(11));
                                    sbGroup.getChildren().addAll(sbComponents.get(10),sbComponents.get(14));
                                    players[2].setPosition(-2);
                                    playerGroup.getChildren().remove(players[2].getImage());
                                    tigerButton.setImage(buttonImages[8]);          //tiger dead
                                    turn = 3;
                                    winningMessage.setText("MAN IS THE WINNER");
                                    utilGroup.getChildren().removeAll(deerButton, tigerButton, hunterButton);
                                    playerGroup.getChildren().removeAll(players[2].getImage());
                                    isEnded =true;
                                }
                                else{               // ar naile bagh 50% hoiya jaibe
                                    players[2].setInjured(true);
                                    sbGroup.getChildren().removeAll(sbComponents.get(12));
                                    sbGroup.getChildren().addAll(sbComponents.get(11));
                                    turn = 1;                                       //hunter mair-dain pabe. so turn change hobe na
                                }
                            }
                            else{           // naile khali turn change hobe
                                if(players[2].isKilled()){     // bagh mara gele horiner khela
                                    turn = 0;
                                    deerButton.setImage(buttonImages[1]);          //deer playing
                                }
                                else{       // naile bagher khela
                                    turn = 2;
                                    tigerButton.setImage(buttonImages[7]);          //tiger playing
                                }
                                hunterButton.setImage(buttonImages[3]);          //hunter playable
                            }
                        }
                    });
                    new Thread(sleeper).start();

                }
                else if( random == 6 ){             //aage 6 pore nai but ebar porse then abar khelbe ar ekhon theke gona shuru hobe
                    players[1].setPlaying(true);
                    sbGroup.getChildren().add(sbComponents.get(7));
                }
                else{                               //ageo 6 pore nai, ekhono na taile dan charai turn change hobe
                    if(!players[2].isKilled()){
                        turn = 2;
                        tigerButton.setImage(buttonImages[7]);          //tiger playing
                    }
                    else{
                        turn = 0;
                        deerButton.setImage(buttonImages[1]);          //deer playing
                    }
                    hunterButton.setImage(buttonImages[3]);          //hunter playable
                }
            });
            new Thread(sleeperTillDiceRole).start();
        }
    }

    public static void tigerPressed(){
        if(turn == 2) {
            music();
            random = randomGenerator();         //first random
            //dan.setText(""+random);             //then show random
            animateDice(random);

            Task<Void> sleeperTillDiceRole = new Task<Void>() {
                @Override
                protected Void call(){
                    try {
                        Thread.sleep(1025);      //to make it sleep while the player is still moving
                    } catch (InterruptedException ignored) {}
                    return null;
                }
            };
            sleeperTillDiceRole.setOnSucceeded(eventTillDiceRole -> {
                if(players[2].isPlaying()){                         // aage 6 pore thakle dan grohonjoggo hobe
                    Timeline animationTimeline = new Timeline(new KeyFrame(Duration.millis(210) , e ->{
                        animation(2,players[2].getPosition()+1, 0.20);              //tiger moves
                    }));
                    animationTimeline.setCycleCount(random);
                    animationTimeline.play();

                    Task<Void> sleeper = new Task<Void>() {
                        @Override
                        protected Void call(){
                            try {
                                Thread.sleep((random+1)* 210L);
                            } catch (InterruptedException ignored) {}
                            return null;
                        }
                    };
                    sleeper.setOnSucceeded(event -> {
                        if( players[2].getPosition()>99 ){              //jodi boner baire jay arki taile more jabe
                            players[2].setKilled(true);
                            playerGroup.getChildren().remove(players[2].getImage());
                            sbGroup.getChildren().add(sbComponents.get(14));
                            tigerButton.setImage(buttonImages[8]);          //tiger dead

                            if(players[0].isKilled() && players[1].haveEaten()){    //bagh, horin more gele ar manusher khawa hoile se jitbe
                                turn = 3;
                                winningMessage.setText("HUNTER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, hunterButton, tigerButton);
                                isEnded =true;
                            }
                            else if(players[0].isKilled() && !players[1].haveEaten()){  // manusher khawa na hoile match shesh. keu jite nai
                                turn = 3;
                                winningMessage.setText("HUNTER IS THE WINNER");
                                utilGroup.getChildren().removeAll(deerButton, hunterButton, tigerButton);
                                isEnded =true;
                            }
                            else if(players[1].isKilled()){       //Ar horin beche thakle O manish mara gele se jitbe
                                turn = 3;
                                winningMessage.setText("DEER IS WINNER");
                                utilGroup.getChildren().removeAll(deerButton, hunterButton, tigerButton);
                                isEnded =true;
                            }
                            else{          // Ar nahoile horiner khela
                                turn = 0;
                                deerButton.setImage(buttonImages[1]);          //deer playing
                            }
                        }
                        else if(map.get(players[2].getPosition()).size()>1){    // par na hoile dekhbe jump hoy kina
                            tigerJumps();
                            turn =3;                    // so that people cannot press any player buttons while temporary buttons are available
                            tigerButton.setImage(buttonImages[6]);          //tiger playable
                        }
                        else{
                            if (players[2].getPosition() == players[0].getPosition()){        // jodi horin ke kheye fele
                                players[0].setKilled(true);
                                players[0].setPosition(-2);
                                playerGroup.getChildren().remove(players[0].getImage());
                                sbGroup.getChildren().remove(sbComponents.get(2));
                                sbGroup.getChildren().addAll(sbComponents.get(0),sbComponents.get(4));
                                if(!players[2].haveEaten()){
                                    sbGroup.getChildren().add(sbComponents.get(13));
                                    players[2].setEaten(true);
                                }
                                deerButton.setImage(buttonImages[2]);          //deer dead
                                turn = 2;             // bagh mair-dain pabe. so turn change hobe na
                            }
                            else if(players[2].getPosition() == players[1].getPosition()){    // ar jodi manushke kheye fele tahole
                                players[1].setKilled(true);
                                playerGroup.getChildren().remove(players[1].getImage());

                                if(players[1].isInjured())
                                    sbGroup.getChildren().remove(sbComponents.get(6));
                                else
                                    sbGroup.getChildren().remove(sbComponents.get(7));

                                sbGroup.getChildren().addAll(sbComponents.get(5),sbComponents.get(9));
                                if(!players[2].haveEaten()){
                                    sbGroup.getChildren().add(sbComponents.get(13));
                                    players[2].setEaten(true);
                                }
                                hunterButton.setImage(buttonImages[5]);          //hunter dead
                                players[1].setPosition(-1);
                                turn = 2;             // bagh mair-dain pabe. so turn change hobe na
                            }
                            else if(players[0].isKilled() && ! players[1].isKilled()){     // jodi kono khawa khawi na hoy ar horin allready mara giye thake
                                turn = 1;
                                hunterButton.setImage(buttonImages[4]);          //hunter playing
                                tigerButton.setImage(buttonImages[6]);          //tiger playable
                            }
                            else if(!players[0].isKilled()){                     // horin jibito thakle dan horin er
                                turn = 0;
                                deerButton.setImage(buttonImages[1]);          //deer playing
                                tigerButton.setImage(buttonImages[6]);          //tiger playable
                            }

                            if(players[0].isKilled() && players[1].isKilled() && players[2].haveEaten()){           // horin, manush 2 jon e mara gele bagh jitse
                                turn = 3;
                                winningMessage.setText("TIGER IS THE WINNER");
                                utilGroup.getChildren().removeAll(tigerButton, hunterButton, deerButton);
                                isEnded =true;
                            }
                        }
                    });
                    new Thread(sleeper).start();

                }
                else if( random == 6 ){             //aage 6 pore nai but ebar porse then abar khelbe ar ekhon theke gona shuru hobe
                    players[2].setPlaying(true);
                    sbGroup.getChildren().add(sbComponents.get(12));
                }
                else{                               //ageo 6 pore nai, ekhono na taile dan charai
                    tigerButton.setImage(buttonImages[6]);          //tiger playable                 // turn change hobe
                    if(players[0].isKilled()){
                        turn = 1;
                        hunterButton.setImage(buttonImages[4]);          //hunter playing
                    }
                    else{
                        turn = 0;
                        deerButton.setImage(buttonImages[1]);          //deer playing
                    }
                }
            });
            new Thread(sleeperTillDiceRole).start();
        }
    }

    public static void music(){
        Media track = new Media(Paths.get("src\\_3_photo\\dices\\rolling\\dice_sound.mp3").toUri().toString());
        MediaPlayer diceSound = new MediaPlayer(track);
        diceSound.play();
    }
}
