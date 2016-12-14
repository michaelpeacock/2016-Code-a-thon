package com.missionse.menubar;

public class MenuSubItem {
	private String menuSubItemName;
	private MenuItemSelectionHandler theHandler = null;
	
	public MenuSubItem(String menuSubItemName, MenuItemSelectionHandler handler) {
		this.menuSubItemName = menuSubItemName;
		this.theHandler = handler;
	}
	
	public String getSubMenuItemName() {
		return menuSubItemName;
	}

	public void setSubMenuItemName(String menuItemName) {
		this.menuSubItemName = menuItemName;
	}
	
	public MenuItemSelectionHandler getHandler() {
		return theHandler;
	}
	
	public void setHandler(MenuItemSelectionHandler theHandler) {
		this.theHandler = theHandler;
	}
}
