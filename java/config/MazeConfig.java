package config;

import geometry.IntCoordinates;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import static config.Cell.Content.DOT;
import static config.Cell.*;
import static config.Cell.Content.NOTHING;
import static config.Cell.Content.ENERGIZER;

public class MazeConfig {
    public MazeConfig(Cell[][] grid, IntCoordinates pacManPos, IntCoordinates blinkyPos, IntCoordinates pinkyPos,
            IntCoordinates inkyPos, IntCoordinates clydePos) {
        this.grid = new Cell[grid.length][grid[0].length];
        for (int i = 0; i < getHeight(); i++) {
            if (getWidth() >= 0)
                System.arraycopy(grid[i], 0, this.grid[i], 0, getWidth());
        }
        this.pacManPos = pacManPos;
        this.blinkyPos = blinkyPos;
        this.inkyPos = inkyPos;
        this.pinkyPos = pinkyPos;
        this.clydePos = clydePos;
    }

    private final Cell[][] grid;
    private final IntCoordinates pacManPos, blinkyPos, pinkyPos, inkyPos, clydePos;

    public IntCoordinates getPacManPos() {
        return pacManPos;
    }

    public IntCoordinates getBlinkyPos() {
        return blinkyPos;
    }

    public IntCoordinates getPinkyPos() {
        return pinkyPos;
    }

    public IntCoordinates getInkyPos() {
        return inkyPos;
    }

    public IntCoordinates getClydePos() {
        return clydePos;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public int getLenght() {
        return this.grid.length;
    }

    public Cell getCell(IntCoordinates pos) {
        return grid[Math.floorMod(pos.y(), getHeight())][Math.floorMod(pos.x(), getWidth())];
    }

    public static String lireContenuFichierTexte(String cheminFichier) throws IOException {// fonction de lecture.
        StringBuilder contenu = new StringBuilder();
        try (BufferedReader bufferLecture = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            while ((ligne = bufferLecture.readLine()) != null) {
                // Remplace les tabulations par des espaces (4 espaces dans cet exemple)
                String ligneModifiee = ligne.replaceAll("\t", "    ");
                ligneModifiee = ligne.replaceAll("\\s+$", "");
                contenu.append(ligneModifiee).append("\n");
            }
        }
        return contenu.toString();
    }
    // simple example with a square shape
    // TODO: mazes should be loaded from a text file

    public static MazeConfig makeExample1() {
        // Creation d'une grille
        Cell[][] maze = new Cell[15][18];
        for (int a = 0; a < 15; a++) {
            for (int b = 0; b < 18; b++) {
                maze[a][b] = new Cell(false, false, false, false, NOTHING);
            }
        }
        // Pour passer la map.txt mettre le chemin absolue du fichier dans ressources
        String cheminFichier = MazeConfig.class.getClassLoader().getResource("map.txt").getPath();//
        // String cheminFichier = "/Users/nervalgbaguidi/Desktop/Test.txt";
        try {
            String contenuDuFichier = lireContenuFichierTexte(cheminFichier);
            int saut = 5;
            int i = 1;
            int j = 0;
            int line = 1;
            int parcourt = 1;

            // Modification de toutes les cellule du en fonction de leurs caractéristiques.

            for (int x = 0; x < contenuDuFichier.length(); x += saut) {

                if ((contenuDuFichier.charAt(x) == '*' && contenuDuFichier.charAt(x + 1) == '-'
                        && contenuDuFichier.charAt(x + 2) == '-' && contenuDuFichier.charAt(x + 3) == '-'
                        && contenuDuFichier.charAt(x + 4) == '*') && line == 1) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(true, info.eastWall(), info.southWall(), info.westWall(),
                            NOTHING);
                    j++;
                    saut = 5;

                }
                if ((contenuDuFichier.charAt(x) == '*' && contenuDuFichier.charAt(x + 1) == '-'
                        && contenuDuFichier.charAt(x + 2) == '-' && contenuDuFichier.charAt(x + 3) == '-'
                        && contenuDuFichier.charAt(x + 4) == '*') && line == 3) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(info.northWall(), info.eastWall(), true, info.westWall(),
                            info.initialContent());
                    j++;
                    saut = 5;
                }

                if ((contenuDuFichier.charAt(x) == '*' && contenuDuFichier.charAt(x + 1) == ' '
                        && contenuDuFichier.charAt(x + 2) == ' ' && contenuDuFichier.charAt(x + 3) == ' '
                        && contenuDuFichier.charAt(x + 4) == '*') && line == 1) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(false, info.eastWall(), info.southWall(), info.westWall(),
                            NOTHING);
                    j++;
                    saut = 5;
                }
                if ((contenuDuFichier.charAt(x) == '*' && contenuDuFichier.charAt(x + 1) == ' '
                        && contenuDuFichier.charAt(x + 2) == ' ' && contenuDuFichier.charAt(x + 3) == ' '
                        && contenuDuFichier.charAt(x + 4) == '*') && line == 3) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(info.northWall(), info.eastWall(), false, info.westWall(),
                            info.initialContent());
                    j++;
                    saut = 5;
                }

                if (contenuDuFichier.charAt(x) == '|' && parcourt == 1 && line == 2) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(info.northWall(), info.eastWall(), info.southWall(), true,
                            NOTHING);
                    saut = 1;
                    parcourt += 1;
                }

                if (contenuDuFichier.charAt(x) == '|' && parcourt == 5 && line == 2) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(info.northWall(), true, info.southWall(), info.westWall(),
                            info.initialContent());
                    saut = 1;
                    parcourt = 1;
                    j++;
                }

                if (contenuDuFichier.charAt(x) == ' ' && line == 2) {
                    if (parcourt == 5) {
                        saut = 1;
                        parcourt = 1;
                        j++;
                    } else {
                        saut = 1;
                        parcourt += 1;
                    }
                }

                if (contenuDuFichier.charAt(x) == '.' && parcourt == 3 && line == 2) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(info.northWall(), info.eastWall(), info.southWall(), info.westWall(),
                            DOT);
                    saut = 1;
                    parcourt += 1;
                }
                if (contenuDuFichier.charAt(x) == 'E' && parcourt == 3 && line == 2) {
                    Cell info = maze[i][j];
                    maze[i][j] = new Cell(info.northWall(), info.eastWall(), info.southWall(), info.westWall(),
                            ENERGIZER);
                    saut = 1;
                    parcourt += 1;
                }

                if (contenuDuFichier.charAt(x) == '\n') {
                    if (line == 3) {
                        i++;
                        j = 0;
                        line = 1;
                        saut = 1;
                    } else {
                        line += 1;
                        j = 0;
                        saut = 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new MazeConfig(maze, new IntCoordinates(4, 1), new IntCoordinates(0, 1), new IntCoordinates(0, 13),
                new IntCoordinates(17, 1), new IntCoordinates(17, 13));
    }
}
