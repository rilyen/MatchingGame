package ca.cmpt213.asn4.memorygame.ui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class manages the cards that appear in the Memory Game UI
 */
public class Card extends Button {
    private int cardNumber;
    private int cardRow;
    private int cardCol;
    private Image cardImage;
    private ImageView cardImageView;

    public Card(int cardNumber, int cardRow, int cardCol) {
        this.cardNumber = cardNumber;
        this.cardRow = cardRow;
        this.cardCol = cardCol;
        cardImage = new Image("file:img/" + cardNumber + ".png");
        cardImageView = new ImageView(cardImage);
        cardImageView.setFitWidth(100);
        cardImageView.setFitHeight(100);
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getCardRow() {
        return cardRow;
    }

    public int getCardCol() {
        return cardCol;
    }

    public Image getCardImage() {
        return cardImage;
    }

    public ImageView getCardImageView() {
        return cardImageView;
    }

    /**
     * Returns true if cards have the same card number, but has a distinct (cardRow,cardCol)
     * @param otherCard
     * @return
     */
    public boolean isMatching(Card otherCard) {
        if (cardNumber == otherCard.getCardNumber()) {
            if (!((cardRow==otherCard.getCardRow())&&(cardCol==otherCard.getCardCol()))) {
                return true;
            }
        }
        return false;
    }
}
