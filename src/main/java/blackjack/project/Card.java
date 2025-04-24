package blackjack.project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Card {
    private String id;
    private JLabel cardImage;

    //Card constructor
    public Card() {
        generateImg();
    }

    //Generate image
    private void generateImg() {
        Deck deck = new Deck();
        Random ran = new Random();

        //Get a random card
        id = deck.getDeck().get(ran.nextInt(deck.getDeck().size()));
        cardImage = new JLabel();
        cardImage.setIcon(Card.resizeImg(id));
    }

    //Resize the card image
    public static ImageIcon resizeImg(String id) {
        BufferedImage img;
        try {
            img = ImageIO.read(Card.class.getResourceAsStream("/" + id + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Scale the image
        Image scaledImg = img.getScaledInstance(225, 300, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public String getId() {
        return id;
    }

    public JLabel getCardImage() {
        return cardImage;
    }
}