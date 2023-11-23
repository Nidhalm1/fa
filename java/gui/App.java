package gui;

import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.awt.*;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import config.MazeConfig;
import model.MazeState;
import javafx.scene.text.Text;

public class App extends Application {
    private static Stage primaryStage; // Variable de classe statique pour la fenêtre principale.

    @Override
    public void start(Stage stage) {
        primaryStage = stage; // Initialisation de la référence du Stage principal.

        var root = new Pane();

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        root.setPrefSize(720,720);

        var gameScene = new Scene(root);

        var pacmanController = new PacmanController();
        gameScene.setOnKeyPressed(pacmanController::keyPressedHandler);
        gameScene.setOnKeyReleased(pacmanController::keyReleasedHandler);
        var maze = new MazeState(MazeConfig.makeExample1());

        Text screenScore = maze.getScreenScore();
        screenScore.setFill(Color.WHITE);
        screenScore.setX(50);
        screenScore.setY(50);

        var gameView = new GameView(maze, root, screenSize.getHeight() / 22);

        // Créer un nouveau bouton
        Button startButton = new Button("Start");
        startButton.setLayoutX(100); // Position du bouton
        startButton.setLayoutY(100);

        // Définir l'action du bouton lorsqu'il est pressé
        startButton.setOnAction(e -> {
            // Commencer le jeu ici
            gameView.animate();
            root.getChildren().remove(startButton); // Supprimez le bouton une fois que le jeu a commencé

        });

        // Ajouter le bouton à la scène
        root.getChildren().addAll(startButton, screenScore);

        primaryStage.setScene(gameScene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // lance l'app javaFx
    }

    public static void showGameOver() { // Méthode statique pour afficher la fenêtre "Game Over".
        Platform.runLater(() -> {
            // Création d'une nouvelle scène pour "Game Over".
            final Stage gameOverStage = new Stage();
            gameOverStage.initModality(Modality.APPLICATION_MODAL); // bloque les interactions avec les uatre fenetres
            gameOverStage.initOwner(primaryStage);// definit le fenetre principale comme proprietaire de la nouvelle
                                                  // fenetre
            gameOverStage.initStyle(StageStyle.TRANSPARENT); // aucune décoration de fenetre pour la transparence

            // Effet de flou sur la fenêtre principale
            primaryStage.getScene().getRoot().setEffect(new BoxBlur(5, 5, 3));
            // crée un paneau pour le contenu de la fenetre de GOVER
            BorderPane gameOverPane = new BorderPane();
            gameOverPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-background-radius: 10;");
            // definit un fond semi-transaprent et arrondit les coins
            // on ajoute le texte pour le G OVER
            Text gameOverText = new Text("Game Over");
            gameOverText.setFont(Font.font("Verdana", 50)); // utilise la police verdana de taille 50
            gameOverText.setFill(Color.RED); // texte en rouge
            // Ajout d'un bouton pour fermer l'application.
            Button closeButton = new Button("Close");
            closeButton.setFont(Font.font("Verdana", 20));// Utilise la police Verdana de taille 20
            closeButton.setOnAction(event -> {
                // Retirer l'effet de flou et arrêter l'application.
                primaryStage.getScene().getRoot().setEffect(null);
                Platform.exit(); // Terminez l'application JavaFX
            });
            // Organise le texte et le bouton en verticale avec un espacement de 10.
            VBox contentBox = new VBox(10, gameOverText, closeButton);
            contentBox.setAlignment(Pos.CENTER);// centre les element dans la boite
            contentBox.setPadding(new Insets(50));// ajoute un rembourage autour des elements
            // on place la boite au centre
            gameOverPane.setCenter(contentBox);
            // Crée une nouvelle scène pour la fenêtre "Game Over" avec un fond transparent.

            Scene gameOverScene = new Scene(gameOverPane);
            gameOverScene.setFill(Color.TRANSPARENT);
            // Prépare une transition en fondu pour l'affichage de la fenêtre "Game Over".

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), gameOverPane);
            fadeTransition.setFromValue(0); // commence transparent
            fadeTransition.setToValue(1);// fini opaque
            fadeTransition.setOnFinished(event -> gameOverStage.show());// affiche la fenetre aprés la transition
            // Définit la scène pour le stage "Game Over" et lance la transition en fondu.

            gameOverStage.setScene(gameOverScene);
            fadeTransition.play();
        });
    }
}
