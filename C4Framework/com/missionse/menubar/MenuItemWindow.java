package com.missionse.menubar;

import java.util.ArrayList;
import java.util.List;

import com.missionse.application.ArcticWindowManager;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MenuItemWindow {
	private Button menuParent;
	private ContextMenu subMenu = new ContextMenu();
	private Boolean menuParentActive = false;
	private List<MenuSubItem> menuSubItems = new ArrayList<MenuSubItem>();
	
	
	public MenuItemWindow(String menuName) {
		menuParent = new Button(menuName);
		

		menuParent.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				Bounds boundsInScreen = menuParent.localToScreen(menuParent.getBoundsInLocal());
				subMenu.show(menuParent, boundsInScreen.getMinX(), boundsInScreen.getMinY() + menuParent.getHeight());
				}
		});
		
//		subMenu.setStyle(
//        		"-fx-background-color: transparent;"
//				);

	}

	public Button getMenuParent() {
		return menuParent;
	}
	
	public void addSubMenuItem(MenuSubItem subMenuItem) {
		
		MenuItem newSubMenu = new MenuItem(subMenuItem.getSubMenuItemName());
		newSubMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (null != subMenuItem.getHandler()) {
					subMenuItem.getHandler().onMenuItemSelection(menuParent.getText(), subMenuItem);
				}
			}
		});
		
		
		newSubMenu.setStyle(
        		"-fx-background-color: transparent;" +
        				"-fx-text-fill: black;"
				);
		
		subMenu.getItems().addAll(newSubMenu);

	}
	
}
