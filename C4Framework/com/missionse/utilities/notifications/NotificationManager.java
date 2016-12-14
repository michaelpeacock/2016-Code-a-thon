package com.missionse.utilities.notifications;

import java.util.stream.IntStream;

import com.missionse.application.ArcticWindowManager;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NotificationManager {
	private static NotificationManager theInstance = null;
	private static ObservableList<Notification> notifications = FXCollections.observableArrayList();
	private Duration popupLifetime;
	private Duration popupAnimationTime;
	private Pane centerPane;

	private NotificationManager() {
		popupLifetime = Duration.millis(5000);
		popupAnimationTime = Duration.millis(500);

		centerPane = ArcticWindowManager.getCenterPane();

	}

	public static NotificationManager getInstance() {
		if (null == theInstance) {
			theInstance = new NotificationManager();
		}

		return theInstance;
	}

	public void notify(Notification notification) {

		preOrder();
		notifications.add(notification);
		centerPane.getChildren().add(notification);

		// Add a timeline for popup fade out
		KeyValue fadeOutBegin = new KeyValue(notification.opacityProperty(), 1.0);
		KeyValue fadeOutEnd = new KeyValue(notification.opacityProperty(), 0.0);

		KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
		KeyFrame kfEnd = new KeyFrame(popupAnimationTime, fadeOutEnd);

		Timeline timeline = new Timeline(kfBegin, kfEnd);
		timeline.setDelay(popupLifetime);
		// timeline.setOnFinished(actionEvent -> Platform.runLater(() -> {
		// notifications.remove(notification);
		// System.out.println("timer stopped");
		// //fireNotificationEvent(new NotificationEvent(NOTIFICATION,
		// Notifier.this, POPUP, NotificationEvent.HIDE_NOTIFICATION));
		// }));

		notification.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println("mouse clicked for notification");
				notification.setVisible(false);

				if (notification.getSelectionHandler() != null) {
					notification.getSelectionHandler().onNotificationSelected(notification);
				}

				notifications.remove(notification);
			}

		});

		timeline.play();
	}

	private void preOrder() {
		if (notifications.isEmpty())
			return;
		IntStream.range(0, notifications.size()).parallel().forEachOrdered(i -> {
			Notification notification = notifications.get(i);
			notification
					.setTranslateY(notification.getTranslateY() + notification.getHeight() + notification.getSpacing());
		});
	}

}
