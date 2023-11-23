package model;

import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public enum Ghost implements Critter {

    // TODO: implement a different AI for each ghost, according to the description in Wikipedia's page
    BLINKY, INKY, PINKY, CLYDE;
    // l'ajoute de l'attribut isBlue pour pouvoir changer la couleur des fontome en belu ;
    private boolean isBlue = false ;
    private RealCoordinates pos;
    private Direction direction = Direction.NONE;
    //Un fantome ne peut pas retourner en arrière 
    //On a donc besoin de connaitre la direction ou il ne peut pas aller
    private Direction cantGo = Direction.NONE;
    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public void setPos(RealCoordinates newPos) {
        pos = newPos;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
        switch (direction) {// On change aussi la variable de cantGo 
            case NORTH ->{
                this.cantGo = Direction.SOUTH;
            }
            case SOUTH ->{
                this.cantGo = Direction.NORTH;
            }
            case EAST ->{
                this.cantGo = Direction.WEST;
            }
            case WEST ->{
                this.cantGo = Direction.EAST;
            }
                
        }
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public Direction getCantGo(){
        return cantGo;
    }

    @Override
    public double getSpeed() {
        return 2;
    }
    public void transformerEnBleu(){ 
        this.isBlue = true;
    }
    public void transformerEnNormal(){
        this.isBlue = false ;
    }
    public boolean isBlue(){
        return isBlue ;
    }



    //Regarde la distance entre le fantome et pacman à la prochaine position
    //Il faut rentrer en argument la direction que va prendre le fantome pour savoir ca prochaine position
    public double defaultGetDistanceWithPacman(Direction direction,double paramX,double paramY){
            RealCoordinates fantomePos = this.getPos();
            switch (direction) {
                case NORTH ->{
                    fantomePos = fantomePos.sety(fantomePos.gety()-1);
                }
                case SOUTH ->{
                    fantomePos = fantomePos.sety(fantomePos.gety()+1);
                }
                case WEST ->{
                    fantomePos = fantomePos.setx(fantomePos.getx()-1);
                }
                case EAST ->{
                    fantomePos = fantomePos.setx(fantomePos.getx()+1);
                }
                default ->{
                    fantomePos = fantomePos.setx(fantomePos.getx());
                }    
            }
            RealCoordinates pacPos = PacMan.INSTANCE.getPos();
            
            
            return Math.sqrt(Math.pow(fantomePos.getx()-(pacPos.getx()+paramY),2)+Math.pow(fantomePos.gety()-(pacPos.gety()+paramY),2));
    }

    public double pinkyGetDistanceWithPacman(Direction direction){
        switch (PacMan.INSTANCE.getDirection()) {
            case NORTH ->{
                return defaultGetDistanceWithPacman(direction, -4, -4);
            }
            case SOUTH ->{
                return defaultGetDistanceWithPacman(direction, 0, 4);
            }
            case EAST ->{
                return defaultGetDistanceWithPacman(direction, 4, 0);
            }
            case WEST ->{
                return defaultGetDistanceWithPacman(direction, -4, 0);
            }
            default ->{
                return defaultGetDistanceWithPacman(direction, 0, 0);
            }
        }
    }

    
    
    public double getDistanceWithPacman(Direction direction){
            if(this==PINKY)
                return pinkyGetDistanceWithPacman(direction);
            else return defaultGetDistanceWithPacman(direction, 0, 0);
    }

    
    
     
    public Direction defaultNextDirection(MazeConfig config){
        Direction[] tabDirection = Direction.values();
        IntCoordinates roundpos = this.getPos().round();
        boolean[] directionName = 
        {config.getCell(roundpos).northWall(),config.getCell(roundpos).northWall(),config.getCell(roundpos).eastWall(),
            config.getCell(roundpos).southWall(),config.getCell(roundpos).westWall()};
        double minDistance = 9999999;
        Direction directionFinal = this.getDirection();
        for(int i= 1;i<tabDirection.length;i++){//On parcour toutes les direc possible en omettant NONE
            if(tabDirection[i]!=this.getCantGo()){//On verifie que la direction n'est pas interdite
                if(!directionName[i]){
                    //System.out.println(getDistanceWithPacman(tabDirection[i]));
                    if(getDistanceWithPacman(tabDirection[i])<minDistance){
                        minDistance = getDistanceWithPacman(tabDirection[i]);
                        directionFinal = tabDirection[i];
                    }
                }
            }
        }
        //System.out.println(minDistance);
        return directionFinal;
    }

    public Direction clydeNextDirection(MazeConfig config){
        if(this.getDistanceWithPacman(Direction.NONE)<=4){//Normalement c'est 8 mais vu la taille de la map 4 est plus logique
            List<Direction> RanDirection = new ArrayList<>(Arrays.asList(Direction.values()));
            RanDirection.remove(this.getCantGo());
            if(RanDirection.contains(Direction.NONE)){
                RanDirection.remove(Direction.NONE);
            }
            Random rd = new Random();
            int r = rd.nextInt(4);
            //System.out.println("Choix aléatoire");
            return RanDirection.get(r);
        }
        else return defaultNextDirection(config);
    }

    public Direction nextDirection(MazeConfig config){
        if(this==CLYDE)
           return clydeNextDirection(config);
        else
            return defaultNextDirection(config);
    }

    public void testMoving(MazeConfig config){
        if(this.getPos().areFloor(this.getDirection())){
            System.out.println("CHANGEMENT");
            this.setPos(this.getPos().floorOrCeilFromDirection(this.getDirection()));
            this.setDirection(this.nextDirection(config));
            
        }
 
    }




    
    
    
    

}