package model;

import model.PacMan;
import config.MazeConfig;

import java.util.List;

import config.Cell;
import geometry.IntCoordinates;
import geometry.RealCoordinates;

/**
 * Implements Pac-Man character using singleton pattern. FIXME: check whether
 * singleton is really a good idea.
 */
public final class PacMan implements Critter {
    private Direction direction = Direction.NONE;
    private RealCoordinates pos;
    private boolean energized;

    private PacMan() {
    }

    public static final PacMan INSTANCE = new PacMan();

    @Override
    public RealCoordinates getPos() {
        return pos;
    }

    @Override
    public double getSpeed() {
        return isEnergized() ? 6 : 4;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void setPos(RealCoordinates pos) {
        this.pos = pos;
    }

    /**
     *
     * @return whether Pac-Man just ate an energizer
     */
    public boolean isEnergized() {
        // TODO handle timeout!
        return energized;
    }

    public void setEnergized(boolean energized) {
        this.energized = energized;
    }

    void ajoutScore(boolean[][] gridState, MazeState maze) {
        var pacPos = PacMan.INSTANCE.getPos().round();
        Cell currentCell = maze.getConfig().getCell(pacPos);
        // Vérifier si la cellule contient un point ou un energizer
        if (!gridState[pacPos.y()][pacPos.x()]) {
            if (maze.getConfig().getCell(pacPos).initialContent() == Cell.Content.DOT) { // on ajoute un au score quand
                                                                                         // quand il y a une piece dans
                                                                                         // la position actuelle de
                                                                                         // pacman
                gridState[pacPos.y()][pacPos.x()] = true;
                maze.addScore(1);
            } else if (currentCell.initialContent() == Cell.Content.ENERGIZER) {
                gridState[pacPos.y()][pacPos.x()] = true;// Marquer l'energizer comme consommé
                maze.addScore(5);// Ajouter plus de points au score pour un ENERGIZER
                setEnergized(true);// Mettre Pac-Man en état énergisé
            }
        }
    }

    void pacmanTouche(IntCoordinates pacPos, MazeState maze) {
        for (var critter : maze.getCritters()) {
            if (critter instanceof Ghost && critter.getPos().round().equals(pacPos)) {
                if (PacMan.INSTANCE.isEnergized()) {
                    maze.addScore(10);
                    maze.resetCritter(critter);
                } else {
                    maze.playerLost();
                    return;
                }
            }
        }
    }
}
