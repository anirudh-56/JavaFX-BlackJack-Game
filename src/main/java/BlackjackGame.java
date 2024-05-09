import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.util.ArrayList;


import java.util.Scanner;
public class BlackjackGame{

    ArrayList<Card> playerHand;
    ArrayList<Card> bankerHand;
    BlackjackDealer theDealer;
    BlackjackGameLogic gameLogic;
    double currentBet;
    double totalWinnings;
    int bankerSum;
    int bankerAceCount;

    int playerSum;
    int playerAceCount;


    void initializeGame(){
        theDealer = new BlackjackDealer();
        theDealer.generateDeck();
        theDealer.shuffleDeck();
        gameLogic = new BlackjackGameLogic();

        playerHand = new ArrayList<>();
        bankerHand = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            playerHand.add(theDealer.drawOne());
        }

        for (int i = 0; i < 2; i++) {
            bankerHand.add(theDealer.drawOne());
        }

        gameLogic = new BlackjackGameLogic();
        int bankerSum = gameLogic.handTotal(bankerHand);
        int bankerAceCount = AceCount(bankerHand);

        int playerSum = gameLogic.handTotal(playerHand);
        int playerAceCount = AceCount(playerHand);

//        placeBet();
//
//        String winner = whoWon();
//        if(winner.equals("Player")){
//            totalWinnings += currentBet;
//        }
//        else if (winner.equals("Dealer")) {
//            totalWinnings -= currentBet;
//        }
//        System.out.println(winner);

    }
    public int getBankerAceCount() {
        return bankerAceCount;
    }

    public int getPlayerAceCount() {
        return playerAceCount;
    }

    private int AceCount(ArrayList<Card> hand){
        int aceCount = 0;
        for(Card card: hand){
            if(card.isAce()){
                aceCount++;
            }
        }
        return aceCount;
    }

    public void setCurrentBet(double currentBet){
        this.currentBet = currentBet;
    }

    public double getCurrentBet(){
        return currentBet;
    }

    public void addTotalWinnings(double amount){
        this.totalWinnings += amount;
    }

    public double getTotalWinnings(){
        return totalWinnings;
    }


    public String whoWon(){
        return gameLogic.whoWon(playerHand, bankerHand);
    }

}