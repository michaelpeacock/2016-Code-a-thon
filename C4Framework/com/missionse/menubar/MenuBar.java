package com.missionse.menubar;

import java.util.HashMap;
import java.util.Map;

import com.missionse.application.ArcticWindowManager;
import com.missionse.utilities.notifications.Notification;
import com.missionse.utilities.notifications.NotificationSelectionHandler;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class MenuBar {
	private HBox buttonBar = null;
	private HBox fixedBar = null;
	private Map menuItems = new HashMap();
	
	public MenuBar() {
		ArcticWindowManager.setMenu(this);
	
		StackPane menuPane = new StackPane();
		menuPane.setId("background");
		menuPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0); -fx-border-color: white;");

		menuPane.setMaxWidth(ArcticWindowManager.getParentWindow().getWidth());
		menuPane.setMaxHeight(50);
		menuPane.getStylesheets().add(getClass().getResource("/resources/stylesheets/toolbar.css").toExternalForm());
		StackPane.setAlignment(menuPane, Pos.TOP_CENTER);


		ToolBar toolBar = new ToolBar();
		menuPane.getChildren().add(toolBar);

		Region spacer = new Region();
		spacer.getStyleClass().setAll("spacer");

		buttonBar = new HBox();
		buttonBar.getStyleClass().setAll("segmented-button-bar");
		HBox.setHgrow(buttonBar, Priority.ALWAYS);

		fixedBar = new HBox();
		fixedBar.getStyleClass().setAll("segmented-button-bar");
		fixedBar.setSpacing(10);
		toolBar.getItems().addAll(buttonBar, fixedBar);       

		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				ArcticWindowManager.getCenterPane().getChildren().add(menuPane);
			}
			
		});
		
		addPreferences();
	}
	
	public void addMenuItem(String menuName, MenuSubItem menuSubItem) {
		MenuItemWindow newMenuItem = getMenuItemWindow(menuName); 
		newMenuItem.addSubMenuItem(menuSubItem);
	}

	private MenuItemWindow getMenuItemWindow(String menuName) {
		if (menuItems.containsKey(menuName)) {
			return (MenuItemWindow) menuItems.get(menuName);
		}
		
		MenuItemWindow newMenuItem = new MenuItemWindow(menuName);
		
		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				buttonBar.getChildren().add(newMenuItem.getMenuParent());
			}
			
		});

		menuItems.put(menuName, newMenuItem);
		
		return newMenuItem;
		
	}
	
	private void addPreferences() {
		Image pref = new Image(getClass().getResource("/resources/images/preferences.png").toExternalForm());
		ImageView prefView = new ImageView(pref);
		prefView.setFitWidth(30);
		prefView.setFitHeight(30);

        Button helpButton = new Button();
        helpButton.setStyle(
        		"-fx-background-color: transparent;" +
        				"-fx-background-radius: 5em;" +
           		        "-fx-background-radius: 5em; " +
           		        "-fx-background-color: blue; " +
        		        "-fx-min-width: 30px; " +
        		        "-fx-min-height: 30px; " +
        		        "-fx-max-width: 30px; " +
        		        "-fx-max-height: 30px; " +
        				"-fx-background-insets: -1.4, 0;" +
        				"-fx-border-radius: 15;" +
        				"-fx-border-width: 2;" +
        				"-fx-border-color: blue;" +
        				"-fx-padding: 0;" +
        				"-fx-font-weight: bold;" +
        				"-fx-text-fill: white;" +
        				"-fx-font-size: 20;"
        		);
        helpButton.setText("?");

        Button notificationButton = new Button();
        notificationButton.setStyle(
        		"-fx-background-color: transparent;" +
        				"-fx-background-radius: 5em;" +
        		        "-fx-background-radius: 5em; " +
           		        "-fx-background-color: gray; " +
        		        "-fx-min-width: 30px; " +
        		        "-fx-min-height: 30px; " +
        		        "-fx-max-width: 30px; " +
        		        "-fx-max-height: 30px; " +
        				"-fx-background-insets: -1.4, 0;" +
        				"-fx-border-radius: 15;" +
        				"-fx-border-width: 2;" +
        				"-fx-border-color: gray;" +
        				"-fx-padding: 0;" +
        				"-fx-font-weight: bold;" +
        				"-fx-text-fill: white;" +
        				"-fx-font-size: 20;"
        		);
        notificationButton.setText("4");
        
        Image helpImage = new Image(getClass().getResource("/resources/images/help.png").toExternalForm());
		ImageView helpView = new ImageView(helpImage);
		helpView.setFitWidth(30);
		helpView.setFitHeight(30);

		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				fixedBar.getChildren().addAll(notificationButton, prefView, helpButton);
			}
			
		});
	}

}
