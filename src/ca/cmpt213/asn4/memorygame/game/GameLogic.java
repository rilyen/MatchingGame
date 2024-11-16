package ca.cmpt213.asn4.memorygame.game;

import ca.cmpt213.asn4.memorygame.ui.Card;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * This class handles the game logic of the Memory Game
 */
public class GameLogic {
    private static final int NUMROWS = 4;
    private static final int NUMCOLS = 4;
    private Card firstCard, secondCard;
    private int[][] cards = new int[NUMROWS][NUMCOLS];
    private GridPane gridpane;
    private int numPairsToFind = NUMROWS + NUMCOLS;
    private Map<Card, ImageView> cardImageMap = new HashMap<>();
    private Label winLabel;
    private boolean canSelect = true;  // flag to control card selection
    private int numMoves = 0;
    private Label movesLabel;
    private Image backImage = new Image("file:img/back.png");

    public GameLogic(GridPane gridpane, Label winLabel, Label movesLabel) {
        this.gridpane = gridpane;
        this.winLabel = winLabel;
        this.movesLabel = movesLabel;
        setupGrid();
    }

    public void setupGrid() {
        // shuffle the cards
        try {
            Integer[] cardNumbers = new Integer[NUMROWS*NUMCOLS];
            for (int i=0; i<(NUMROWS+NUMCOLS); i++) {
                cardNumbers[i*2] = i+1;
                cardNumbers[i*2+1] = i+1;
            }
            List<Integer> numberList = Arrays.asList(cardNumbers);
            Collections.shuffle(numberList);
            cardNumbers = numberList.toArray(new Integer[0]);
            // populate the 2d array with the cards
            for (int i=0; i<NUMROWS; i++) {
                for (int j=0; j<NUMCOLS; j++) {
                    cards[i][j] = cardNumbers[i*4 + j];
                    ImageView imgView = new ImageView(backImage);
                    imgView.setFitHeight(100);
                    imgView.setFitWidth(100);
                    gridpane.add(imgView, i, j);
                    imgView.addEventHandler(MouseEvent.MOUSE_CLICKED, new ImageClickHandler());
                    //System.out.print(cards[i][j] + " ");  // testing
                }
                //System.out.println();  // testing
            }
        } catch (Exception e) {
            System.out.println("Error setting up the grid: " + e.getMessage());
        }
    }

    public void resetGame() {
        try {
            firstCard = null;
            secondCard = null;
            canSelect = true;
            gridpane.getChildren().removeIf(node->node instanceof ImageView);
            gridpane.getChildren().remove(winLabel);
            for (int i=0; i<NUMROWS; i++) {
                for (int j=0; j<NUMCOLS; j++) {
                    cards[i][j] = 0;
                }
            }
            numPairsToFind = NUMROWS + NUMCOLS;  // Reset the number of pairs to find
            numMoves = 0;  // Reset the number of moves so far
            movesLabel.setText("Moves: " + numMoves);  // Update the moves label
            setupGrid();
        } catch (Exception e) {
            System.out.println("Error resetting the grid: " + e.getMessage());
        }
    }

    class ImageClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent mouseEvent) {
            if (!canSelect) return;  // Prevent selection if currently waiting

            ImageView source = (ImageView)mouseEvent.getSource();
            Integer colIndex = GridPane.getColumnIndex(source);
            Integer rowIndex = GridPane.getRowIndex(source);
            int row = rowIndex.intValue();
            int col = colIndex.intValue();
            int cardNumber = cards[row][col];
            Card card = new Card(cardNumber, row, col);
            checkCard(card, source);
        }
    }
    public void checkCard(Card card, ImageView source) {
        if (firstCard == null) { // first card selected
            firstCard = card;
            source.setImage(firstCard.getCardImage());
            cardImageMap.put(firstCard, source);
        } else {  // second card selected, check if matches with first card
            numMoves++;
            movesLabel.setText("Moves: " + numMoves);
            secondCard = card;
            source.setImage(secondCard.getCardImage());
            cardImageMap.put(secondCard, source);
            canSelect = false;  // disable further selection

            if (firstCard.isMatching(secondCard)) {  // matches, keep the image view
                numPairsToFind--;
                firstCard = null;
                secondCard = null;
                canSelect = true;
                if (numPairsToFind == 0) {  // found all the pairs, end of game
                    winLabel.setText("YOU WIN!");
                    gridpane.add(winLabel, 1, 5);
                }
            } else {  // doesn't match, flip it back after a pause
                int millisecondsSleep = 2000;
                Task<Void> sleep = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(millisecondsSleep);
                        return null;
                    }
                };
                sleep.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        if (cardImageMap.get(firstCard) != null) {
                            cardImageMap.get(firstCard).setImage(backImage);
                        }
                        if (cardImageMap.get(secondCard) != null) {
                            cardImageMap.get(secondCard).setImage(backImage);
                        }
                        firstCard = null;
                        secondCard = null;
                        canSelect = true;
                    }
                });
                new Thread(sleep).start();
            }
        }
    }
}
