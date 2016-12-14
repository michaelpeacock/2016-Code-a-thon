package com.missionse.application;

import com.missionse.core.Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ArcticWindowTest extends Application {
	private static int counter = 1;
	private Controller applicationController;
	private static final String APP_CONFIGURATION = "config/AppConfiguration.xml";

	private void init(Stage primaryStage) {
		final Pane root = new Pane();

		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 1600, 1000));
		primaryStage.setTitle("Arctic Iri");
		ArcticWindowManager.setStage(primaryStage);
		ArcticWindowManager.setParentWindow(root);

		StackPane centerPane = new StackPane();
		centerPane.setMinSize(1600, 1000);
		root.getChildren().add(centerPane);
		ArcticWindowManager.setCenterPane(centerPane);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		init(primaryStage);
		primaryStage.show();

		applicationController = new Controller();
		try {
			applicationController.start(APP_CONFIGURATION);
		} catch (Exception e) {
			String msg = "Fatal application error";
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
