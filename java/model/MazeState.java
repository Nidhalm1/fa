package model;

import model.PacMan;
import config.Cell;
import config.MazeConfig;
import geometry.IntCoordinates;
import geometry.RealCoordinates;
import gui.App;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;

import static config.Cell.Content.NOTHING;
import static model.Ghost.*;

public final class MazeState {
    private final MazeConfig config;
    private final int height;
    private final int width;

    private final boolean[][] gridState;

    private final List<Critter> critters;
    private int score;

    private final Map<Critter, RealCoordinates> initialPos;
    private int lives = 3;

    Text screenScore = new Text("Score: " + score); // Creation du Texte pour afficher le score
    Text livesText = new Text("Lives: " + lives);

    public MazeState(MazeConfig config) {
        this.config = config;
        height = config.getHeight();
        width = config.getWidth();
        critters = List.of(PacMan.INSTANCE, Ghost.CLYDE, BLINKY, INKY, PINKY);
        gridState = new boolean[height + 1][width + 1];
        initialPos = Map.of(
                PacMan.INSTANCE, config.getPacManPos().toRealCoordinates(1.0),
                BLINKY, config.getBlinkyPos().toRealCoordinates(1.0),
                INKY, config.getInkyPos().toRealCoordinates(1.0),
                CLYDE, config.getClydePos().toRealCoordinates(1.0),
                PINKY, config.getPinkyPos().toRealCoordinates(1.0));
        resetCritters();
    }

    public List<Critter> getCritters() {
        return critters;
    }

    public double getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLives() {
        return lives;
    }

    public void update(long deltaTns) {
        // FIXME: too many things in this method. Maybe some responsibilities can be
        // delegated to other methods or classes?
        for (var critter : critters) {
            var curPos = critter.getPos();
            var nextPos = critter.nextPos(deltaTns);
            var curNeighbours = curPos.intNeighbours();
            var nextNeighbours = nextPos.intNeighbours();
            if (!curNeighbours.containsAll(nextNeighbours)) { // the critter would overlap new cells. Do we allow it?
                switch (critter.getDirection()) { // ici pour collision sur les murs idée
                    case NORTH -> {
                        for (var n : curNeighbours)
                            if (config.getCell(n).northWall()) {
                                nextPos = curPos.floorY();
                                nextPos = nextPos.floorX();
                                break;
                            }

                    }
                    case EAST -> {
                        for (var n : curNeighbours)
                            if (config.getCell(n).eastWall()) {
                                nextPos = curPos.ceilX();
                                nextPos = nextPos.ceilY();
                                break;
                            }

                    }
                    case SOUTH -> {
                        for (var n : curNeighbours)
                            if (config.getCell(n).southWall()) {
                                nextPos = curPos.ceilY();
                                nextPos = nextPos.ceilX();
                                break;
                            }

                    }
                    case WEST -> {
                        for (var n : curNeighbours)
                            if (config.getCell(n).westWall()) {
                                nextPos = curPos.floorX();
                                nextPos = nextPos.floorY();
                                break;
                            }

                    }
                }

            }

            if (nextPos.x() <= width && nextPos.x() >= 0 && nextPos.y() <= height && nextPos.y() >= 0) { 
                // si la prochian position est dans le plateau alors on ne se teleporte pas de l'autre coté
                critter.setPos(nextPos);// faire un modulo plutot
            } else if (nextPos.x() >= width || nextPos.x() <= 0) { // sinon si on est dehors sur la largeur alors on se
                                                                   // teleporte sur l'autre coté en largeur
                critter.setPos(nextPos.warp(width, width));
            } else { // de meme pour la hauteur
                critter.setPos(nextPos.warp(width, height));
            }

        }
        // L'ajout du score et les vies sont dans la class pacman
        var pacPos = PacMan.INSTANCE.getPos().round();
        PacMan.INSTANCE.pacmanTouche(pacPos, this);
        PacMan.INSTANCE.ajoutScore(gridState, this);
        // test distance avec quel direction doit prendre un fantome

        System.out.println("Direction que doit prendre BLINKY:" + BLINKY.nextDirection(config));
        System.out.println("Position:"+BLINKY.getPos().getx()+" "+BLINKY.getPos().gety());
        System.out.println("Direction Actuelle:" + BLINKY.getDirection());
        System.out.println("Direction Interdite:" + BLINKY.getCantGo());
        // System.out.println("tst:"+CLYDE.getDistanceWithPacman(Direction.NORTH));

        BLINKY.testMoving(config);
        PINKY.testMoving(config);
        CLYDE.testMoving(config);
    }

    

    void addScore(int increment) {// sans private la fonction est package-private accessible seulement dans la
                                  // package model
        score += increment;
        displayScore();
    }

    public Text getScreenScore() {
        return screenScore;
    }

    public int getScore() {
        return score;
    }

    private void displayScore() {
        //System.out.println("Score: " + score);
        screenScore.setText("Score: " + score + " Lives: " + lives);
        // Mettez à jour le texte des vies en même temps que le score
    }

    private void displayLives() {
        //System.out.println("Lives: " + lives);
        screenScore.setText("Score: " + score + " Lives: " + lives); // Mettez à jour uniquement le texte pour afficher
        // le nombre de vies
    }

    public void playerLost() {// Méthode appelée lorsqu'un joueur perd une vie.
        if (lives > 0) {
            lives--;// Décrémenter le nombre de vies.
            displayLives();// Mettre à jour l'affichage des vies.
            if (lives == 0) { // si le jpueur na plus de vies on affiche l'ecran de GO

                App.showGameOver();
            }
            // renisialiser les entites du jeu pour une nv partie
            resetCritters();
        }
    }

    void resetCritter(Critter critter) {
        critter.setDirection(Direction.NONE);
        critter.setPos(initialPos.get(critter));
    }

    void resetCritters() {
        for (var critter : critters)
            resetCritter(critter);
    }

    public MazeConfig getConfig() {
        return config;
    }

    public boolean getGridState(IntCoordinates pos) {
        return gridState[pos.y()][pos.x()];
    }
}
