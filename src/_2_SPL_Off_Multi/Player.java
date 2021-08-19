/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package _2_SPL_Off_Multi;

import static _2_SPL_Off_Multi.MathMethods.translatorXY;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Fahim Morshed
 * IITDU, BSSE1102
 * Email: bsse1102@iit.du.ac.bd
 * 
 */

public class Player {
    private boolean isInjured;
    private boolean isKilled;
    private boolean haveEaten;
    private boolean isPlaying;
    private final ImageView image;
    private int position;
    
    public Player(String path) throws FileNotFoundException{
        this.position = -1;
        this.image = new ImageView ( new Image ( new FileInputStream ( path )));
        image.setTranslateX(translatorXY(position)[0]+5);
        image.setTranslateY(translatorXY(position)[1]+5);
        image.setFitHeight(50);
        image.setFitWidth(50);
        
        this.isInjured = false;
        this.isKilled = false;
        this.haveEaten = false;
        this.isPlaying = false;
    }

    public Player(String path, int position, int[] state) throws FileNotFoundException{
        this.position = position;
        this.image = new ImageView ( new Image ( new FileInputStream ( path )));
        image.setTranslateX(translatorXY(position)[0]+5);
        image.setTranslateY(translatorXY(position)[1]+5);
        image.setFitHeight(50);
        image.setFitWidth(50);

        this.isInjured = state[1]==1;
        this.isKilled = state[1]==2;
        this.haveEaten = state[2]==1;
        this.isPlaying = state[0]==1;
    }

    public boolean isInjured() {
        return isInjured;
    }
    public boolean isKilled() {
        return isKilled;
    }
    public boolean haveEaten() {
        return haveEaten;
    }
    public ImageView getImage() {
        return image;
    }
    public int getPosition() {
        return position;
    }
    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
    public void setInjured(boolean isInjured) {
        this.isInjured = isInjured;
    }
    public void setKilled(boolean isKilled) {
        this.isKilled = isKilled;
    }
    public void setEaten(boolean haveEaten) {
        this.haveEaten = haveEaten;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    
    
}