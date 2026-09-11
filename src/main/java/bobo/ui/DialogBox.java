package bobo.ui;

import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Custom control representing a dialog box with an avatar ImageView and a Label for message text.
 */
public class DialogBox extends HBox {

    private final Label textLabel;
    private final ImageView displayPicture;

    private DialogBox(String text, Image img, boolean isUser) {
        textLabel = new Label(text);
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(300);

        displayPicture = new ImageView();
        displayPicture.setFitWidth(40);
        displayPicture.setFitHeight(40);
        displayPicture.setPreserveRatio(true);

        if (img != null && !img.isError()) {
            displayPicture.setImage(img);
            Circle clip = new Circle(20, 20, 20);
            displayPicture.setClip(clip);
        }

        setSpacing(10);

        if (isUser) {
            textLabel.setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 10; "
                    + "-fx-padding: 10; -fx-font-size: 13px;");
            setAlignment(Pos.TOP_RIGHT);
            getChildren().addAll(textLabel, displayPicture);
        } else {
            textLabel.setStyle("-fx-background-color: #E8E8E8; -fx-background-radius: 10; "
                    + "-fx-padding: 10; -fx-font-size: 13px;");
            setAlignment(Pos.TOP_LEFT);
            getChildren().addAll(displayPicture, textLabel);
        }
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a user dialog box with avatar on the right.
     *
     * @param text Message text.
     * @param img  User avatar image.
     * @return User DialogBox instance.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img, true);
    }

    /**
     * Creates a Bobo dialog box with avatar on the left.
     *
     * @param text Message text.
     * @param img  Bobo avatar image.
     * @return Bobo DialogBox instance.
     */
    public static DialogBox getBoboDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img, false);
        return db;
    }
}
