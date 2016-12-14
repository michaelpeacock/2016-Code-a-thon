/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.missionse.userinterface.widgets;

import com.missionse.application.ArcticWindowManager;
import com.missionse.utilities.notifications.Notification;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class WidgetFrame extends Group {

    protected double initX;
    protected double initY;
    protected Point2D dragAnchor;
    protected int click = 0;
    private VBox vb = new VBox();
	private final double BUTTON_SIZE = 30;
	
	
    public WidgetFrame(String windowName, double windowWidth, double windowHeight) {
    	//TODO: add initial position info
    	
        Label title = new Label(windowName);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setMaxHeight(Double.MAX_VALUE);
        title.setStyle(
        		"-fx-background-color: transparent;" +
           		        "-fx-background-color: black; " +
        				"-fx-background-insets: -1.4, 0;" +
        				"-fx-border-width: 2;" +
        				"-fx-border-color: gray;" +
        				"-fx-padding: 0;" +
        				"-fx-font-weight: bold;" +
        				"-fx-text-fill: white;" +
        				"-fx-font-size: 12;"
        		);

        HBox hbox = new HBox();

		StackPane stack = new StackPane();
		Button button = new Button();
		button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
		button.setStyle(
   		        "-fx-background-color: black; " +
				"-fx-background-insets: -1.4, 0;" +
				"-fx-border-width: 2;" +
				"-fx-border-color: gray;" +
				"-fx-padding: 0;" +
				"-fx-font-weight: bold;" +
				"-fx-text-fill: white;" +
				"-fx-font-size: 12;"
		  );
		button.setText("X");
		
		button.setOnAction(new EventHandler<ActionEvent> () {

			@Override
			public void handle(ActionEvent event) {
				getWidgetFrame().setVisible(false);
			}
		});
		stack.getChildren().addAll(button);
		stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack

		hbox.getChildren().add(title);
		hbox.getChildren().add(stack);            // Add to HBox from Example 1-2
		HBox.setHgrow(title, Priority.ALWAYS);    // Give stack any extra space
		
        title.setOnMousePressed((MouseEvent me) -> {
            initX = getTranslateX();
            initY = getTranslateY();
            dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
        });
        
        title.setOnMouseDragged((MouseEvent me) -> {
            if (me != null && dragAnchor != null) {
            	//System.out.println("in rec initEvt");
            	setTranslateX((int) (initX + me.getSceneX() - dragAnchor.getX()));
                setTranslateY((int) (initY + me.getSceneY() - dragAnchor.getY()));
            }
        });
        
        vb.getChildren().add(hbox);
        this.getChildren().add(vb);
    }
    
	public void show() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				ArcticWindowManager.getCenterPane().getChildren().add(getWidgetFrame());
			}
		});
		
		this.setVisible(true);
	}

	public void hide() {
		this.setVisible(false);
	}
	private WidgetFrame getWidgetFrame() {
    	return this;
    }

    public void addNode(Node node) {
    	node.setOpacity(0.60);
    	vb.getChildren().add(node);
    }
    
    public void setScale(double scale) {
        setScaleX(scale);
        setScaleY(scale);
    }

}
