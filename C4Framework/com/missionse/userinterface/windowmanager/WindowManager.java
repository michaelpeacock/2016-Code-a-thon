package com.missionse.userinterface.windowmanager;

import java.util.ArrayList;

import com.missionse.userinterface.basewindow.Window;

public class WindowManager {
	private static ArrayList<Window> windows = new ArrayList<>();

	public WindowManager() {

	}

	public static void addWindow(Window newWindow) {
		windows.add(newWindow);
	}

	public static void showWindow(String windowName) {
		for (Window window : windows) {
			if (windowName.matches(window.getTitle())) {
				window.show();
				break;
			}
		}
	}

	public static void hideWindow(String windowName) {
		for (Window window : windows) {
			if (windowName.matches(window.getTitle())) {
				window.close();
				break;
			}
		}
	}

	public static void listManagedWindows() {
		for (Window window : windows) {
			System.out.println("Managed window: " + window.getTitle());
		}
	}

	public static Window getWindow(String windowName) {
		for (Window window : windows) {
			if (windowName.matches(window.getTitle())) {
				return window;
			}
		}

		return null;
	}
}
