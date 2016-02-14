/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hockey;

/**
 *
 * @author ägarenn
 */
public class Coordinates {

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private float x;
    private float y;
    
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
