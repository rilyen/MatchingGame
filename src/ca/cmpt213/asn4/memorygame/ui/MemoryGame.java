package ca.cmpt213.asn4.memorygame.ui;

import ca.cmpt213.asn4.memorygame.game.GameLogic;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class handles the Memory Game UI
 */
public class MemoryGame extends Application {
    private GridPane gridpane = new GridPane();
    private Label winLabel = new Label();
    private int numMoves = 0;
    private Label movesLabel = new Label("Moves: " + numMoves);
    private GameLogic gameLogic;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Memory Matching Game");
            Button reset = new Button("NEW GAME");
            reset.setOnAction(new ButtonClickHandler());
            reset.setMaxSize(100,50);
            gridpane.add(reset, 3, 6);

            gridpane.add(movesLabel, 1, 6);
            gridpane.setHgap(10);
            gridpane.setVgap(10);
            gridpane.setAlignment(Pos.TOP_CENTER);
            gridpane.setPadding(new Insets(10));

            gameLogic = new GameLogic(gridpane, winLabel, movesLabel);

            Scene scene = new Scene(gridpane, 600, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Error setting up the Memory Game: " + e.getMessage());
        }
    }

    class ButtonClickHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                gameLogic.resetGame();
            } catch (Exception e) {
                System.out.println("Error resetting the game: " +  e.getMessage());
            }
        }
    }
}
