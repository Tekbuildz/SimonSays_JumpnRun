package levelHandling;

public class Cube {

    private final int cubeID;
    private int x;
    private int y;
    private final int SIZE = 40;

    private boolean airLeft;
    private boolean airRight;
    private boolean airAbove;
    private boolean airBelow;

    public Cube(int cubeID, int x, int y) {
        this.cubeID = cubeID;
        this.x = x;
        this.y = y;
    }

    public int getCubeID() {
        return cubeID;
    }

    public boolean isAirLeft() {
        return airLeft;
    }

    public void setAirLeft(boolean airLeft) {
        this.airLeft = airLeft;
    }

    public boolean isAirRight() {
        return airRight;
    }

    public void setAirRight(boolean airRight) {
        this.airRight = airRight;
    }

    public boolean isAirAbove() {
        return airAbove;
    }

    public void setAirAbove(boolean airAbove) {
        this.airAbove = airAbove;
    }

    public boolean isAirBelow() {
        return airBelow;
    }

    public void setAirBelow(boolean airBelow) {
        this.airBelow = airBelow;
    }

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

    public int getSIZE() {
        return SIZE;
    }
}
