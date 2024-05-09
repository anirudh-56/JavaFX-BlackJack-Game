import java.util.ArrayList;

public class Card {
    String suit;
    int value;
    int realValue;

    private String cardImage;

    Card(String theSuit, int theValue) {
        suit = theSuit;
        value = theValue;
        String valuestring = "";
        if (value >= 2 && value <= 10) {
            valuestring = Integer.toString(value);
            realValue = value;
        } else if (value == 1) {
            valuestring = "A";
            realValue = 11;
        } else if (value == 11) {
            valuestring = "J";
            realValue = 10;
        } else if (value == 12) {
            valuestring = "Q";
            realValue = 10;
        } else {
            valuestring = "K";
            realValue = 10;
        }

        cardImage = valuestring + "-" + suit;
    }


    public int getValue() {
        return realValue;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isAce() {
        return value == 1;
    }

    public String getImagePath() {
        return cardImage + ".png";
    }
}