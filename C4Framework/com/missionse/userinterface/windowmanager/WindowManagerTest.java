package com.missionse.userinterface.windowmanager;

public class WindowManagerTest {

	public WindowManagerTest() {
		WindowManager.listManagedWindows();

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				int counter = 0;
				while (true) {
					try {
						if (counter == 0) {
							WindowManager.showWindow("Layer Manager");
							counter++;
						} else {
							WindowManager.hideWindow("Layer Manager");
							counter = 0;
						}
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();

	}

}
