package config;

/*Commentaire de creation des cellules dans le fichier texte :
- *---* = Mur horizontale fermer;
- *   * = Mur horizontale ouvert (trois espaces requit entre les *);
- | = mur vertical fermer;
- . =DOT(important: mettre le point avec un espace entre le mur ouest fermer ou ouvert et de meme entre le mur est ouvert ou fermer
Exemple: "| . |");
- E = ENERGIZED (Gros point meme proceder que le ".". Exemple="| E |");
- pour notifier rien dans la cellule "|   |" avec les deux mur separe de 3 espaces.
*/
public record Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Cell.Content initialContent) {
    public enum Content { NOTHING, DOT, ENERGIZER}
    // FIXME: all these factories are convenient, but it is not very "economic" to have so many methods!

    public Cell(boolean northWall, boolean eastWall, boolean southWall, boolean westWall, Content initialContent) {
        this.northWall = northWall;
        this.eastWall = eastWall;
        this.southWall = southWall;
        this.westWall = westWall;
        this.initialContent = initialContent;
    }
}
