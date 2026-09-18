package bobo.ui;

import bobo.Bobo;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;

/**
 * Controller and layout container for the main JavaFX chat view.
 */
public class MainWindow extends AnchorPane {

    private final ScrollPane scrollPane;
    private final VBox dialogContainer;
    private final TextField userInput;
    private final Button sendButton;

    private Bobo bobo;

    private final Image userImage;
    private final Image boboImage;

    /**
     * Constructs the main chat window layout and initializes GUI controls.
     */
    public MainWindow() {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        userInput = new TextField();
        sendButton = new Button("Send");

        userImage = loadImage("/images/user.png");
        boboImage = loadImage("/images/bobo.png");
        Image bgImage = loadImage("/images/bg.png");

        setupLayout(bgImage);
        setupEventHandlers();
    }

    private Image loadImage(String resourcePath) {
        try {
            var stream = getClass().getResourceAsStream(resourcePath);
            if (stream != null) {
                return new Image(stream);
            }
        } catch (Exception e) {
            // Ignore missing images gracefully
        }
        return null;
    }

    private void setupLayout(Image bgImage) {
        setPrefSize(400.0, 600.0);

        scrollPane.setContent(dialogContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dialogContainer.setSpacing(10);
        dialogContainer.setStyle("-fx-padding: 10;");

        if (bgImage != null && !bgImage.isError()) {
            BackgroundImage bg = new BackgroundImage(bgImage, BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
            dialogContainer.setBackground(new Background(bg));
        } else {
            dialogContainer.setStyle("-fx-padding: 10; -fx-background-color: #FAFAFA;");
        }

        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 45.0);

        userInput.setPrefHeight(40.0);
        AnchorPane.setLeftAnchor(userInput, 5.0);
        AnchorPane.setBottomAnchor(userInput, 5.0);

        sendButton.setPrefHeight(40.0);
        sendButton.setPrefWidth(70.0);
        AnchorPane.setRightAnchor(sendButton, 5.0);
        AnchorPane.setBottomAnchor(sendButton, 5.0);

        // Bind userInput width dynamically
        userInput.prefWidthProperty().bind(widthProperty().subtract(85.0));

        getChildren().addAll(scrollPane, userInput, sendButton);
    }

    private void setupEventHandlers() {
        sendButton.setOnAction((event) -> handleUserInput());
        userInput.setOnAction((event) -> handleUserInput());
    }

    /**
     * Sets the Bobo chatbot instance and displays initial welcome message.
     *
     * @param b Bobo instance.
     */
    public void setBobo(Bobo b) {
        assert b != null : "Bobo instance passed to MainWindow must not be null";
        this.bobo = b;
        dialogContainer.getChildren().add(DialogBox.getBoboDialog(bobo.getWelcomeMessage(), boboImage));
    }

    /**
     * Handles user input by displaying user and bot dialogs, and processing exit.
     */
    private void handleUserInput() {
        assert bobo != null : "Bobo instance must be set before handling user input";
        String input = userInput.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        String response = bobo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBoboDialog(response, boboImage)
        );
        userInput.clear();

        if (bobo.isExit()) {
            Platform.exit();
        }
    }
}
