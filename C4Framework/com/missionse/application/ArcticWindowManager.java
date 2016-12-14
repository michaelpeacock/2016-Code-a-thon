package com.missionse.application;

import com.missionse.menubar.MenuBar;
import com.missionse.tacsit.window.TacsitManager;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ArcticWindowManager {
	private static Scene theScene = null;
	private static Pane theCenterPane = null;
	private static MenuBar theMenu = null;
	private static Stage theStage = null;
	private static Pane theParent = null;

	public static Pane getCenterPane() {
		return theCenterPane;
	}

	public static void setCenterPane(Pane centerPane) {
		theCenterPane = centerPane;
	}

	public static TacsitManager getTacsitManager() {
		return TacsitManager.getInstance();
	}

	public static void setMenu(MenuBar menu) {
		theMenu = menu;
	}

	public static MenuBar getMenu() {
		return theMenu;
	}

	public static Pane getParentWindow() {
		return theParent;
	}

	public static void setParentWindow(Pane parent) {
		theParent = parent;
	}

	public static Stage getStage() {
		return theStage;
	}

	public static void setStage(Stage theStage) {
		ArcticWindowManager.theStage = theStage;
	}

}
