package geometry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Direction;

public record RealCoordinates(double x, double y) {

    public static final RealCoordinates ZERO = new RealCoordinates(0, 0);
    public static final RealCoordinates NORTH_UNIT = new RealCoordinates(0, -1);
    public static final RealCoordinates EAST_UNIT = new RealCoordinates(1, 0);
    public static final RealCoordinates SOUTH_UNIT = new RealCoordinates(0, 1);
    public static final RealCoordinates WEST_UNIT = new RealCoordinates(-1, 0);


    public RealCoordinates plus(RealCoordinates other) {
        return new RealCoordinates(x + other.x, y + other.y);
    }

    public RealCoordinates times(double multiplier) {
        return new RealCoordinates(x * multiplier, y * multiplier);
    }

    /**
     *
     * @return the coordinates of all integer squares that a unit square with current coordinates would intersect
      */
    public Set<IntCoordinates> intNeighbours() {
        return new HashSet<>(List.of(
                new IntCoordinates((int) Math.floor(x), (int) Math.floor(y)),
                new IntCoordinates((int) Math.floor(x), (int) Math.ceil(y)),
                new IntCoordinates((int) Math.ceil(x), (int) Math.floor(y)),
                new IntCoordinates((int) Math.ceil(x), (int) Math.ceil(y))
        )
        );
    }

    public double getx(){
        return x;
    }

    public double gety(){
        return y;
    }

    public RealCoordinates setx(double n){
        return new RealCoordinates(n, y);
    }

    public RealCoordinates sety(double n){
        return new RealCoordinates(x, n);
    }

    public IntCoordinates round() {
        return new IntCoordinates((int) Math.round(x), (int) Math.round(y));
    }

    public boolean areFloor(Direction direction){
        switch(direction){
                    case NORTH -> {
                        return (this.y <= Math.floor(this.y)+0.05 && this.y >= Math.floor(this.y)-0.05);
                    }
                    case EAST -> {
                        return (this.x <= Math.ceil(this.x)+0.05 && this.x >= Math.ceil(this.x)-0.05);
                    }
                    case SOUTH -> {
                        return (this.y <= Math.ceil(this.y)+0.05 && this.y >= Math.ceil(this.y)-0.05);
                    }
                    case WEST -> {
                        return (this.x <= Math.floor(this.x)+0.05 && this.x >= Math.floor(this.x)-0.05);
                    }
                    default ->{
                        return true;
                    }
                }
        /* 
        return (this.x >= Math.floor(this.x) && this.x < Math.floor(this.x)+0.01) || 
        (this.x <= Math.floor(this.x)&& this.x < Math.floor(this.x)-0.01) || 
        (this.y >= Math.floor(this.y) && this.y < Math.floor(this.y)+0.01) || 
        (this.y <= Math.floor(this.y)&& this.y < Math.floor(this.y)-0.01);*/
    }

    public RealCoordinates floorX() {
        return new RealCoordinates((int) Math.floor(x), y);
    }

    public RealCoordinates floorY() {
        return new RealCoordinates(x, (int) Math.floor(y));
    }

    public RealCoordinates ceilX() {
        return new RealCoordinates((int) Math.ceil(x), y);
    }

    public RealCoordinates ceilY() {
        return new RealCoordinates(x, (int) Math.ceil(y));
    }

    //La fonction permet de mettre les cordonnées X ou Y à une valeur entière
    //En fonction de la direction rentrée en paramètre
    public RealCoordinates floorOrCeilFromDirection(Direction direction){ 
        switch(direction){
            case NORTH -> {
                return this.floorY();
            }
            case EAST -> {
                return this.ceilX();
            }
            case SOUTH -> {
                return this.ceilY();
            }
            case WEST -> {
                return this.floorX();
            }
            default ->{
                return this;
            }
        }
        
    }

    public RealCoordinates warp(int width, int height) {
        var rx = x;
        var ry = y;
        while (Math.round(rx) <= 0)
            rx += width;
        while (Math.round(ry) <= 0)
            ry += height;
        while (Math.round(rx) >= width)
            rx -= width;
        while (Math.round(rx) >= height)
            ry -= height;
        return new RealCoordinates(rx, ry);
    }
}
