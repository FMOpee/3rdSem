package sample;

import java.util.Date;

import static sample.Main.tileSize;

public class MATH {
    /** Translate square number into pixel */
    public static int[] translatorXY(int sq){
        int[] xy= new int[2];
        int x,y;
        sq--;                   //converting for indexing. like 1 will be 0, 100 will be 99

        if( (sq / 10) % 2 == 0 ){   //if the square is 1-10,21-30
            x = (sq % 10) * tileSize ;
            y = 600 - ((sq / 10) * tileSize + tileSize);
        }
        else{
            x = 600 - ((sq % 10) * tileSize + tileSize);
            y = 600 - ((sq / 10) * tileSize + tileSize);
        }

        xy[0]=x+10;
        xy[1]=y+10;

        return xy;
    }

    /**random number. not anything fancy. as simple as it gets*/
    public static int randomGenerator(){
        long x = 11;
        long y = 214903917;
        long  time= x * (new Date()).getTime() + y ;
        time /= 83;
        time = x * time + y;
        return  (int) (1 + time % 6);

    }
}
