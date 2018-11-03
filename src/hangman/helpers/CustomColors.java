package hangman.helpers;

import java.awt.Color;
/**
 * @author Kyle Astudillo
 * @date: 9/17/2018
 * @description: Helps maintains the colors of buttons by groups
 */
public enum CustomColors {

    IMPORTANT(140, 208, 238), //Light Blue
    BACKGROUND(80, 80, 80), //Dark Gray
    OPERATIONS(105, 220, 206), //Blue Green
    DEFAULT(120, 120, 120), //Light Gray
    RETURN(207, 158, 217), //Pink
    TOGGLE(105, 220, 124), //Light Green
    BOX(120, 120, 120),
    ROWFILENOTFOUND(240, 70, 70),
    ROWREPLACED(122, 200, 238),
    ROWBRANCH(238, 122, 235);


    private  int r;
    private int g;
    private int b;

    CustomColors(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color getColor(CustomColors calColor){
        return new Color(calColor.r, calColor.g, calColor.b);
    }

}
