package com.missionse.arcticday;

import java.io.File;
import java.util.List;

import com.missionse.application.ArcticWindowManager;
import com.missionse.menubar.MenuBar;
import com.missionse.menubar.MenuItemSelectionHandler;
import com.missionse.menubar.MenuSubItem;

public class ArcticDayMenu {
	public ArcticDayMenu() {
		makeFakeMenu();
	}

	protected static void makeFakeMenu()
	{
		MenuBar menu = ArcticWindowManager.getMenu();

		// UAS Menu

		// Decision Aids
		MenuSubItem graphics = new MenuSubItem("Graphics Entry", null);
		MenuSubItem region = new MenuSubItem("Region of Interest", null);
		menu.addMenuItem("Decision Aids", graphics);
		menu.addMenuItem("Decision Aids", region);
		
		// Communications Menu
		MenuSubItem comms = new MenuSubItem("District Communications", null);
		MenuSubItem commander = new MenuSubItem("Voice Commander", null);
		menu.addMenuItem("Communications", comms);
		menu.addMenuItem("Communications", commander);

		// Sensors Menu
		MenuSubItem sensorsGrid = new MenuSubItem("Sensors Grid", null);
		menu.addMenuItem("Sensors", sensorsGrid);

		// Scenario Menu
		MenuSubItem scenario = new MenuSubItem("Scenario Management", null);
		menu.addMenuItem("Scenario", scenario);

	}
};
