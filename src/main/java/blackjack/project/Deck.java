package blackjack.project;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final String[] cards = {"A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
    private final String[] suits = {"H", "S", "C", "D"};
    private final List<String> deck = new ArrayList<>();

    //Deck constructor
    public Deck() {
        generateDeck();
    }

    //Generate a deck of 52 unique cards
    void generateDeck() {
        for (String card : cards) {
            for (String suit : suits) {
                deck.add(card + suit);
            }
        }
    }

    public List<String> getDeck() {
        return deck;
    }
}
