package blackjack.project;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<String> hand;
    private static List<String> allCards;

    //Player constructor
    public Player() {
        hand = new ArrayList<>();
        allCards = new ArrayList<>();
    }

    //Method to draw a card
    public Card draw(int facedown) {
        Card card = null;
        boolean flag = true;
        while (flag) {
            card = new Card();

            //Draw a card facedown
            if (facedown == 1) {
                card.getCardImage().setIcon(Card.resizeImg("card_back"));
            }

            //Draw a card not present on board
            if (!allCards.contains(card.getId())) {
                hand.add(card.getId());
                allCards.add(card.getId());
                flag = false;
            }
        }
        return card;
    }

    public List<String> getHand() {
        return hand;
    }
}
