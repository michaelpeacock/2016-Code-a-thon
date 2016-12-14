package com.missionse.menubar.test;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;

public class MenuTest {

	public MenuTest() {
		MenuBar menu = ArcticWindowManager.getMenu();
		MenuItemSelectionHandler handler = 	new MenuItemSelectionHandler() {

			@Override
			public void onMenuItemSelection(String parent, MenuSubItem item) {
				System.out.println("Menu selection: parent: " + parent + " item: " + item.getSubMenuItemName());

			}

		};

		MenuSubItem subMenu = new MenuSubItem("Test SubMenu", handler);
		MenuSubItem subMenu2 = new MenuSubItem("Test SubMenu 2", handler);

		menu.addMenuItem("Test Menu", subMenu);
		menu.addMenuItem("Test Menu", subMenu2);
		menu.addMenuItem("Test Menu 2", subMenu);
		menu.addMenuItem("Test Menu 2", subMenu2);
	}
}
