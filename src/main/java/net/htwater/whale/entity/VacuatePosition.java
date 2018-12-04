package net.htwater.whale.entity;

import java.util.Objects;

/**
 * @author  shitianlong
 * @description template class for vatucate tools
 * @since 2018-09-10
 */
public class VacuatePosition {
    private int x;
    private int y;
    private int buffer;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBuffer() {
        return buffer;
    }

    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }

    public VacuatePosition(int x, int y, int buffer) {
        this.x = x;
        this.y = y;
        this.buffer = buffer;
    }

    public VacuatePosition() {
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof VacuatePosition){
            return (Math.abs(this.x-((VacuatePosition) o).getX())<buffer&&
                    Math.abs(this.y-((VacuatePosition) o).getY())<buffer);
        }
       return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(x + "" + y);
    }
}
