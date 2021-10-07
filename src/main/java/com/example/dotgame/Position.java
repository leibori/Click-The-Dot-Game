package com.example.dotgame;

public class Position {
    private int posX;
    private int posY;

    public Position(int posX, int posy) {
        this.posX = posX;
        this.posY = posy;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    @Override
    public String toString() {
        return "You click at{" +
                "X: " + posX +
                ", Y: " + posY +
                '}';
    }
}
