package xerxes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import xerxes.parser.CommandResult;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Xerxes xerxes;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/oldMan1.jpg"));
    private Image xerxesImage = new Image(this.getClass().getResourceAsStream("/images/oldMan2.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Xerxes instance. */
    public void setXerxes(Xerxes xerxes) {
        this.xerxes = xerxes;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        CommandResult result = xerxes.executeCommand(input.trim());
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(result.getMessage(), xerxesImage)
        );
        userInput.clear();

        if (result.shouldExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
