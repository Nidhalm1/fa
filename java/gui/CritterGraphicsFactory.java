package gui;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Critter;
import model.Ghost;
import model.PacMan;


public final class CritterGraphicsFactory {
    private final double scale;

    public CritterGraphicsFactory(double scale) {
        this.scale = scale;
    }

    public GraphicsUpdater makeGraphics(Critter critter) {
        var size = 0.7;
        var url = (critter instanceof PacMan) ? "pacman.png" :
                switch ((Ghost) critter) {
                    case BLINKY -> ((Ghost) critter).isBlue()? "ghost_blue_image.gif":"ghost_blinky.png"; // savoir si le fantome est bleu si oui generer une image bleu , sinon une image du fantome normal
                    case CLYDE -> ((Ghost) critter).isBlue()?"ghost_blue_image.gif":"ghost_clyde.png";
                    case INKY -> ((Ghost) critter) .isBlue()?"ghost_blue_image.gif":"ghost_inky.png";
                    case PINKY -> ((Ghost) critter).isBlue()? "ghost_blue_image.gif": "ghost_pinky.png";
                };
        var image = new ImageView(new Image(url, scale * size, scale * size, true, true));
        return new GraphicsUpdater() {
            @Override
            public void update() {
                image.setTranslateX((critter.getPos().x() + (1 - size) / 2) * scale);
                image.setTranslateY((critter.getPos().y() + (1 - size) / 2) * scale);
                // Debug.out("sprite updated");
            }

            @Override
            public Node getNode() {
                return image;
            }
        };
    }
}
