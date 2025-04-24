package blackjack.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Blackjack {

    private final JFrame frame;
    private Player guest;
    private Player dealer;

    //Main method
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Blackjack window = new Blackjack();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
        });
    }

    //Create a frame
    public Blackjack() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1500, 750);
        frame.setMinimumSize(new Dimension(1500, 750));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("BLACKJACK");
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        initialize();
    }

    private void initialize() {

        //Create panel_0
        JPanel panel_0 = new JPanel();
        frame.getContentPane().add(panel_0, BorderLayout.SOUTH);
        panel_0.setLayout(new GridLayout(1, 0, 0, 0));

        //Create the hit button
        JButton btn = new JButton("HIT");
        btn.setHorizontalAlignment(SwingConstants.RIGHT);
        btn.setBackground(new Color(3, 116, 29));
        btn.setFont(new Font("Unispace", Font.BOLD, 25));
        btn.setPreferredSize(new Dimension(40, 60));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        panel_0.add(btn);

        //Create the stay button
        JButton btn_1 = new JButton("STAY\r\n");
        btn_1.setHorizontalAlignment(SwingConstants.LEFT);
        btn_1.setBackground(new Color(3, 116, 29));
        btn_1.setFont(new Font("Unispace", Font.BOLD, 25));
        btn_1.setPreferredSize(new Dimension(40, 60));
        btn_1.setFocusPainted(false);
        btn_1.setBorderPainted(false);
        panel_0.add(btn_1);

        //Create panel_1
        JPanel panel_1 = new JPanel();
        frame.getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new GridLayout(0, 1, 0, 0));
        panel_1.setBackground(new Color(3, 116, 29));

        //Create panel_2
        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(3, 116, 29));
        panel_1.add(panel_2);
        panel_2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        //Create panel_3
        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(3, 116, 29));
        panel_1.add(panel_3);
        panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        //Create panel_4
        JPanel panel_4 = new JPanel();
        frame.getContentPane().add(panel_4, BorderLayout.EAST);
        panel_4.setLayout(new GridLayout(0, 1, 0, 0));
        panel_4.setBackground(new Color(3, 116, 29));

        //Create a jlabel representing the dealer hand
        JLabel dealer_count = new JLabel();
        dealer_count.setHorizontalAlignment(SwingConstants.CENTER);
        dealer_count.setFont(new Font("Unispace", Font.BOLD, 30));
        panel_4.add(dealer_count);
        dealer_count.setBackground(new Color(3, 116, 29));
        dealer_count.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, new Color(128, 0, 0)));
        dealer_count.setPreferredSize(new Dimension(230, 250));

        //Create a jlabel representing the guest hand
        JLabel guest_count = new JLabel();
        guest_count.setHorizontalAlignment(SwingConstants.CENTER);
        guest_count.setFont(new Font("Unispace", Font.BOLD, 30));
        panel_4.add(guest_count);
        guest_count.setBackground(new Color(3, 116, 29));
        guest_count.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, new Color(255, 255, 255)));
        guest_count.setPreferredSize(new Dimension(230, 250));

        //Create the two players
        guest = new Player();
        dealer = new Player();

        //Draw the initial hand of 2 cards for the guest
        while (guest.getHand().size() < 2) {
            panel_3.add(guest.draw(0).getCardImage());
        }
        guest_count.setText("<html>Guest:<br/>" + points(guest.getHand()) + "</html>");

        //Draw the initial hand of 2 cards for the dealer
        Card card;
        int first_pnt = 0;
        while (dealer.getHand().size() < 2) {

            //First card is facedown
            if (dealer.getHand().isEmpty()) {
                card = dealer.draw(1);

                //The points of the first card are hidden
                first_pnt = points(dealer.getHand());
            } else {
                card = dealer.draw(0);
            }
            panel_2.add(card.getCardImage());
        }

        //Show only the points of the second card
        int sec_pnt = points(dealer.getHand()) - first_pnt;

        //In case you draw two aces
        if (sec_pnt == 1) {
            sec_pnt = 11;
        }
        dealer_count.setText("<html>Dealer:<br/>" + sec_pnt + "</html>");

        //Action listener of the hit button
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Draw a card
                panel_3.add(guest.draw(0).getCardImage());

                //Update points
                int score = points(guest.getHand());
                guest_count.setText("<html>Guest:<br/>" + score + "</html>");

                //If score > 21 the guest lose
                if (score > 21) {
                    dealer_count.setText("<html>Dealer:<br/>" + points(dealer.getHand()) + "</html>");
                    winLoseTie("lose", panel_2, btn, btn_1);
                }

                //Limit of 5 cards
                if (guest.getHand().size() >= 5) {
                    btn.setEnabled(false);
                    stay(panel_2, btn, btn_1, dealer_count);
                }
                frame.repaint();
                frame.revalidate();
            }
        });

        //Action listener of the stay button
        btn_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stay(panel_2, btn, btn_1, dealer_count);
            }
        });
    }

    //Method implementing the stay button
    private void stay(JPanel panel, JButton btn, JButton btn_1, JLabel dealer_count) {

        //The dealer will draw up to 5 cards or will stop drawing card if his score is higher than the guest
        while (dealer.getHand().size() < 5 && points(guest.getHand()) > points(dealer.getHand())) {
            panel.add(dealer.draw(0).getCardImage());
        }
        dealer_count.setText("<html>Dealer:<br/>" + points(dealer.getHand()) + "</html>");

        //Lose, win and tie condition
        if (points(dealer.getHand()) > 21 || points(guest.getHand()) > points(dealer.getHand())) {
            winLoseTie("win", panel, btn, btn_1);
        } else if (points(guest.getHand()) == points(dealer.getHand())) {
            winLoseTie("tie", panel, btn, btn_1);
        } else {
            winLoseTie("lose", panel, btn, btn_1);
        }
    }

    //Calculate the points of a hand
    private int points(List<String> inHand) {
        int res = 0;
        boolean aceFlag = true;
        for (int i = 0; i < inHand.size(); i++) {
            String value = inHand.get(i).substring(0, inHand.get(i).length() - 1);
            switch (value) {
                case "K", "Q", "J", "10":
                    res += 10;
                    break;
                case "9":
                    res += 9;
                    break;
                case "8":
                    res += 8;
                    break;
                case "7":
                    res += 7;
                    break;
                case "6":
                    res += 6;
                    break;
                case "5":
                    res += 5;
                    break;
                case "4":
                    res += 4;
                    break;
                case "3":
                    res += 3;
                    break;
                case "2":
                    res += 2;
                    break;

                    //Special case for the ACE
                case "A":
                    if (aceFlag) {
                        res += 11;
                        aceFlag = false;
                    } else {
                        res += 1;
                    }
                    break;
            }
        }
        return res;
    }

    //Game over window
    private void winLoseTie(String result, JPanel panel, JButton btn, JButton btn_1) {

        //TTS
        TTS tts = new TTS(result);
        Thread thread_1 = new Thread(tts);
        thread_1.start();
        endgame(panel, btn, btn_1);

        //Create the retry window
        int ans = JOptionPane.showConfirmDialog(frame, "YOU " + result.toUpperCase() + ". Want to retry?",
                "YOU " + result.toUpperCase(), JOptionPane.YES_NO_OPTION);
        if (ans == 0) {

            //Reinitialize
            frame.getContentPane().removeAll();
            initialize();
            frame.repaint();
            frame.revalidate();
        } else if (ans == 1) {

            //Close the app
            frame.dispose();
        }
    }

    //Update the board
    private void endgame(JPanel panel, JButton btn, JButton btn_1) {

        //Flip the first card of the dealer
        if (panel.getComponent(0) instanceof JLabel) {
            JLabel label = (JLabel) panel.getComponent(0);
            label.setIcon(Card.resizeImg(dealer.getHand().getFirst()));
        }

        //Disable the hit and stay button
        btn.setEnabled(false);
        btn_1.setEnabled(false);
        frame.repaint();
        frame.revalidate();
    }
}
