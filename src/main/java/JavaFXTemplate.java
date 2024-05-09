import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;


import java.util.ArrayList;

import javafx.stage.Stage;
import javafx.util.Duration;


import java.util.Scanner;
public class JavaFXTemplate extends Application {

	private static final int BOARD_WIDTH = 1000;
	private static final int BOARD_HEIGHT = BOARD_WIDTH;
	private static final int CARD_WIDTH = 110;
	private static final int CARD_HEIGHT = 154;

	private BlackjackDealer theDealer;
	private BlackjackGame blackjackGame;
	private BlackjackGameLogic gameLogic;
	private Pane gamePane;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		//new stuff


//new stuff
		BorderPane root = new BorderPane();
		GamePane gamePane = new GamePane();
		root.setCenter(gamePane);

		root.setStyle("-fx-background-color: #35654D;");

		HBox buttonPanel = new HBox(10);
		buttonPanel.setAlignment(Pos.CENTER);
		Button hitButton = new Button("Hit");
		Button stayButton = new Button("Stay");
		hitButton.setFocusTraversable(false);
		stayButton.setFocusTraversable(false);
		buttonPanel.getChildren().addAll(hitButton, stayButton);
		root.setBottom(buttonPanel);

		Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
		primaryStage.setScene(scene);

		primaryStage.setTitle("BlackJack");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> System.exit(0));
		primaryStage.show();

		initializeGame();

		int prePlayerSum = blackjackGame.gameLogic.handTotal(blackjackGame.playerHand);
		System.out.println("Player Hand Sum: " + prePlayerSum);


		hitButton.setOnAction(event -> {
			Card card = blackjackGame.theDealer.drawOne();
			blackjackGame.playerHand.add(card);
			gamePane.getChildren().clear();
			gamePane.updatePlayerHand();
			int playerSum = blackjackGame.gameLogic.handTotal(blackjackGame.playerHand);
			System.out.println("Player Hand Sum: " + playerSum);
			//int playerAceCount = blackjackGame.getPlayerAceCount();

			if (playerSum > 21) {
				hitButton.setDisable(true);
				stayButton.setDisable(true);

				Text bustText = new Text("Busted with " + playerSum + "!");
				bustText.setFont(Font.font("Arial", 30));
				bustText.setFill(Color.WHITE);


				bustText.setX(BOARD_WIDTH / 2 - bustText.getLayoutBounds().getWidth() / 2);
				bustText.setY(BOARD_HEIGHT / 2);

				gamePane.getChildren().add(bustText);

			}
		});

		stayButton.setOnAction(event -> {
			hitButton.setDisable(true);
			stayButton.setDisable(true);

			while (blackjackGame.gameLogic.evaluateBankerDraw(blackjackGame.bankerHand)) {
				Card card = blackjackGame.theDealer.drawOne();
				blackjackGame.bankerHand.add(card);
				//int bankerSum = blackjackGame.gameLogic.handTotal(blackjackGame.bankerHand);
				//int bankerAceCount = blackjackGame.getBankerAceCount();
			}

			gamePane.getChildren().clear();
			gamePane.updateDealerHand();
			gamePane.updatePlayerHand();


			String message = blackjackGame.gameLogic.whoWon(blackjackGame.playerHand, blackjackGame.bankerHand);
			Text resultText = new Text(message);
			resultText.setFont(Font.font("Arial", 30));
			resultText.setFill(Color.WHITE);
			resultText.setLayoutX(220);
			resultText.setLayoutY(250);
			gamePane.getChildren().add(resultText);
		});
	}



	private void initializeGame() {
		theDealer = new BlackjackDealer();
		blackjackGame = new BlackjackGame();
		gameLogic = new BlackjackGameLogic();
		gamePane = new GamePane();

		blackjackGame.initializeGame(); // Initialize the game
	}

	class GamePane extends Pane {
		@Override
		protected void layoutChildren() {
			super.layoutChildren();
			getChildren().clear();

			try {
				Image hiddenCardImg = new Image("cards/BACK.png");
				ImageView hiddenCardView = new ImageView(hiddenCardImg);
				hiddenCardView.setFitWidth(CARD_WIDTH);
				hiddenCardView.setFitHeight(CARD_HEIGHT);
				getChildren().add(hiddenCardView);

				//Draw Banker's Hand
				for(int i = 0; i < blackjackGame.bankerHand.size(); i++){
					Card card = blackjackGame.bankerHand.get(i);
					Image cardImg = new Image("cards/" + card.getImagePath());
					ImageView cardView = new ImageView(cardImg);
					cardView.setFitWidth(CARD_WIDTH);
					cardView.setFitHeight(CARD_HEIGHT);
					cardView.setLayoutX(CARD_WIDTH + 25 + (CARD_WIDTH + 5) * i);
					getChildren().add(cardView);
				}

				//Draw Player's Hand
				for(int i = 0; i < blackjackGame.playerHand.size(); i++){
					Card card = blackjackGame.playerHand.get(i);
					Image cardImg = new Image("cards/" + card.getImagePath());
					ImageView cardView = new ImageView(cardImg);
					cardView.setFitWidth(CARD_WIDTH);
					cardView.setFitHeight(CARD_HEIGHT);
					cardView.setLayoutX(CARD_WIDTH + 25 + (CARD_WIDTH + 5) * i);
					cardView.setLayoutY(320);
					getChildren().add(cardView);
				}

				String message = gameLogic.whoWon(blackjackGame.playerHand, blackjackGame.bankerHand);
				if (!message.isEmpty()) {
					Text text = new Text(message);
					text.setFont(Font.font("Arial", 30));
					text.setFill(Color.WHITE);
					text.setLayoutX(220);
					text.setLayoutY(250);
					getChildren().add(text);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}





		public void updatePlayerHand() {
			for (int i = 0; i < blackjackGame.playerHand.size(); i++) {
				Card card = blackjackGame.playerHand.get(i);
				Image cardImg = new Image("cards/" + card.getImagePath());
				ImageView cardView = new ImageView(cardImg);
				cardView.setFitWidth(CARD_WIDTH);
				cardView.setFitHeight(CARD_HEIGHT);
				cardView.setLayoutX(CARD_WIDTH + 25 + (CARD_WIDTH + 5) * i);
				cardView.setLayoutY(320); // Adjust the Y position to where the player's cards should be displayed
				getChildren().add(cardView);
			}
		}


		public void updateDealerHand() {
			double startX = (BOARD_WIDTH - (CARD_WIDTH + 5) * blackjackGame.bankerHand.size()) / 2;
			for (int i = 0; i < blackjackGame.bankerHand.size(); i++) {
				Card card = blackjackGame.bankerHand.get(i);
				Image cardImg = new Image("cards/" + card.getImagePath());
				ImageView cardView = new ImageView(cardImg);
				cardView.setFitWidth(CARD_WIDTH);
				cardView.setFitHeight(CARD_HEIGHT);
				cardView.setLayoutX(startX + (CARD_WIDTH + 5) * i); // Position cards with a gap of 5 pixels
				cardView.setLayoutY(50); // Position the cards 50 pixels from the top of the pane
				getChildren().add(cardView);
			}
		}



	}

}



