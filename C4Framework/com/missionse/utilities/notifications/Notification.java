package com.missionse.utilities.notifications;

import com.missionse.application.ArcticWindowManager;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Notification extends Group {
	private static final Image INFO_ICON = new Image(
			Notification.class.getResource("/resources/images/notifications/info.png").toExternalForm());
	private static final Image WARNING_ICON = new Image(
			Notification.class.getResource("/resources/images/notifications/warning.png").toExternalForm());
	private static final Image SUCCESS_ICON = new Image(
			Notification.class.getResource("/resources/images/notifications/success.png").toExternalForm());
	private static final Image ERROR_ICON = new Image(
			Notification.class.getResource("/resources/images/notifications/error.png").toExternalForm());
	private final String title;
	private final String message;
	private final Image image;
	private final double ICON_WIDTH = 24;
	private final double ICON_HEIGHT = 24;
	private final double BUTTON_SIZE = 30;
	private double width = 300;
	private double height = 80;
	private double spacingY = 5;
	private Stage stage;
	// private Group notificationGroup;
	private NotificationSelectionHandler handler = null;

	// ******************** Constructors **************************************
	public Notification(final String title, final String message) {
		this(title, message, null);

		initialize();
	}

	public Notification(final String message, final Image image) {
		this("", message, image);

		initialize();
	}

	public Notification(final String title, final String message, final Image image) {
		this.title = title;
		this.message = message;
		this.image = image;

		initialize();
	}

	public String getTitle() {
		return this.title;
	}

	public String getMessage() {
		return this.message;
	}

	public void setSelectionHandler(NotificationSelectionHandler handler) {
		this.handler = handler;
	}

	public NotificationSelectionHandler getSelectionHandler() {
		return this.handler;
	}

	private void initialize() {
		stage = ArcticWindowManager.getStage();
		Label title = new Label(this.title);
		title.getStyleClass().add("title");

		HBox hbox = new HBox();
		hbox.setSpacing(10);

		// StackPane stack = new StackPane();
		// stack.getStyleClass().add("closebutton");
		// Button button = new Button();
		// button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
		// button.setText("X");

		// stack.getChildren().addAll(button);
		// stack.setAlignment(Pos.CENTER_RIGHT); // Right-justify nodes in stack

		ImageView icon = new ImageView(INFO_ICON);
		icon.setFitWidth(ICON_WIDTH);
		icon.setFitHeight(ICON_HEIGHT);

		Label message = new Label(this.message, icon);
		message.getStyleClass().add("message");

		hbox.getChildren().add(title);
		// hbox.getChildren().add(stack); // Add to HBox from Example 1-2
		// HBox.setHgrow(stack, Priority.ALWAYS); // Give stack any extra space

		VBox popupLayout = new VBox();
		popupLayout.setSpacing(10);
		popupLayout.setPadding(new Insets(10, 10, 10, 10));
		popupLayout.getChildren().addAll(hbox, message);

		StackPane popupContent = new StackPane();
		popupContent.setPrefSize(width, height);
		popupContent.getStyleClass().add("notification");
		popupContent.getChildren().addAll(popupLayout);
		popupContent.getStylesheets()
				.add(getClass().getResource("/resources/stylesheets/notifier.css").toExternalForm());

		// notificationGroup = new Group();
		this.setTranslateX((stage.getWidth() / 2) - getWidth() / 2 - 10);
		this.setTranslateY(-stage.getHeight() / 2 + getHeight() / 2 + 10 + 50); // 50
																				// is
																				// menubar
																				// height
		this.getChildren().add(popupContent);
	}

	public void show() {
		NotificationManager.getInstance().notify(this);
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getSpacing() {
		return spacingY;
	}
}
