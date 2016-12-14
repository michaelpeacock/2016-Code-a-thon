/*******************************************************************************
 * Copyright (c) 2016 ASRC Federal Mission Solutions.
 * All rights reserved. No warranty, explicit or implicit, provided. This 
 * program and the accompanying materials are proprietary and 
 * confidential. Unauthorized copying or distribution of this file, 
 * via any medium, is strictly prohibited without consent from
 * ASRC Federal Mission Solutions.
 *******************************************************************************/
package com.missionse.tacsit.layers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.missionse.tacsit.window.TacsitManager;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.ScreenRelativeAnnotation;
import gov.nasa.worldwind.util.measure.LengthMeasurer;
import gov.nasa.worldwind.view.orbit.OrbitView;

public class HeaderLayer extends AnnotationLayer {
	private WorldWindowGLJPanel wwd;
	private ScreenRelativeAnnotation headerRange;
	private ScreenRelativeAnnotation headerLatLon;
	private ScreenRelativeAnnotation preHook;
	private ScreenRelativeAnnotation primaryHook;
	private ScreenRelativeAnnotation secondaryHook;
	private ScreenRelativeAnnotation cursorInfo;
	private int primaryHookNumber = -1;
	private int secondaryHookNumber = -1;
	Position cursorPosition;
	LengthMeasurer lengthMeasurer;
	private double METERS_TO_NM = 0.000539957;

	enum LATLONGTYPE {
		LATITUDE, LONGITUDE
	}

	public HeaderLayer() {
		this.setName("Header Layer");
		this.setPickEnabled(false);

		this.cursorPosition = new Position(Angle.ZERO, Angle.ZERO, 0.0);
		this.lengthMeasurer = new LengthMeasurer();
		this.lengthMeasurer.setFollowTerrain(false);

		createDateHeader();
		createRangeHeader();
		createLatitudeAndLongitude();
		createPreHook();
		createPrimaryHook();
		createSecondaryHook();
		createCursorInfo();

		wwd = TacsitManager.getInstance().getWorldWindowJPanel();
		wwd.addPositionListener(new PositionListener() {
			@Override
			public void moved(PositionEvent paramPositionEvent) {
				if (null != paramPositionEvent.getPosition()) {
					setCursorPosition(paramPositionEvent.getPosition());
				}

				updateLatitudeAndLongitude();
				updateRange();
			}
		});

	}

	public synchronized void setCursorPosition(Position position) {
		this.cursorPosition = position;
	}

	public synchronized Position getCursorPosition() {
		return (this.cursorPosition);
	}

	public void createDateHeader() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.WHITE);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter Bold", 1, 16));
		defaultAttributes.setBorderWidth(0);

		ScreenRelativeAnnotation headerDate = new ScreenRelativeAnnotation("Date", 0.5, .9);
		headerDate.setKeepFullyVisible(true);
		headerDate.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(headerDate);

		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		TimerTask timerTask = new TimerTask() {
			Date now;

			@Override
			public void run() {
				now = new Date();
				headerDate.setText(
						"DATE: " + dateFormat.format(now).toUpperCase() + "  TIME: " + timeFormat.format(now) + " GMT");
				TacsitManager.getInstance().getWorldWindowJPanel().redraw();

			}
		};

		new Timer().schedule(timerTask, 0, 1000);
	}

	public void createRangeHeader() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.WHITE);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter Bold", 1, 16));
		defaultAttributes.setBorderWidth(0);

		headerRange = new ScreenRelativeAnnotation("RANGE SCALE:", 0.5, .87);
		headerRange.setKeepFullyVisible(true);
		headerRange.setXMargin(5);
		headerRange.setYMargin(25);

		headerRange.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(headerRange);

		headerRange.setText("RANGE SCALE: 1000");
	}

	public void updateRange() {
		OrbitView view = (OrbitView) wwd.getView();
		headerRange.setText(String.format("RANGE SCALE: %.0f NM", view.getZoom() / 6076));

	}

	public void createLatitudeAndLongitude() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.YELLOW);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use
																		// strict
																		// dimension
																		// width
																		// - 200
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter Bold", 1, 16));
		// defaultAttributes.setFont(new Font("Lucida Sans Typewriter",
		// Font.BOLD, 12));
		defaultAttributes.setTextAlign(AVKey.LEFT);
		defaultAttributes.setBorderWidth(0);

		headerLatLon = new ScreenRelativeAnnotation(" ", 0.5, .03);
		headerLatLon.setKeepFullyVisible(true);
		headerLatLon.setXMargin(5);
		headerLatLon.setYMargin(20);
		headerLatLon.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(headerLatLon);
	}

	public void createPreHook() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.YELLOW);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use
																		// strict
																		// dimension
																		// width
																		// - 200
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 12));
		defaultAttributes.setTextAlign(AVKey.LEFT);
		defaultAttributes.setBorderWidth(0);

		preHook = new ScreenRelativeAnnotation(" ", 1, .15);
		preHook.setXMargin(5);
		preHook.setYMargin(20);
		preHook.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(preHook);

	}

	public void createPrimaryHook() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.YELLOW);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use
																		// strict
																		// dimension
																		// width
																		// - 200
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 12));
		defaultAttributes.setTextAlign(AVKey.LEFT);
		defaultAttributes.setBorderWidth(0);

		primaryHook = new ScreenRelativeAnnotation(" ", 1, .7);
		primaryHook.setXMargin(5);
		primaryHook.setYMargin(20);
		primaryHook.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(primaryHook);

	}

	public void createSecondaryHook() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.YELLOW);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use
																		// strict
																		// dimension
																		// width
																		// - 200
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 12));
		defaultAttributes.setTextAlign(AVKey.LEFT);
		defaultAttributes.setBorderWidth(0);

		secondaryHook = new ScreenRelativeAnnotation(" ", 1, .45);
		secondaryHook.setXMargin(5);
		secondaryHook.setYMargin(20);
		secondaryHook.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(secondaryHook);

	}

	public void createCursorInfo() {
		AnnotationAttributes defaultAttributes = new AnnotationAttributes();
		defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
		defaultAttributes.setTextColor(Color.YELLOW);
		defaultAttributes.setSize(new Dimension(600, 0));
		defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use
																		// strict
																		// dimension
																		// width
																		// - 200
		defaultAttributes.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 12));
		defaultAttributes.setTextAlign(AVKey.RIGHT);
		defaultAttributes.setBorderWidth(0);

		cursorInfo = new ScreenRelativeAnnotation(" ", 1, .03);
		// cursorInfo = new ScreenRelativeAnnotation(" ", 1, .5);
		cursorInfo.setKeepFullyVisible(true);
		cursorInfo.setXMargin(5);
		cursorInfo.setYMargin(20);
		cursorInfo.getAttributes().setDefaults(defaultAttributes);
		this.addAnnotation(cursorInfo);

	}

	public void updateLatitudeAndLongitude() {
		headerLatLon.setText(degreesToDMSString(cursorPosition.getLatitude().degrees, LATLONGTYPE.LATITUDE) + "  "
				+ degreesToDMSString(cursorPosition.getLongitude().degrees, LATLONGTYPE.LONGITUDE));
	}

	public final double bearingBetweenPoints(double lat1, double long1, double lat2, double long2) {
		double degToRad = Math.PI / 180.0;
		double phi1 = lat1 * degToRad;
		double phi2 = lat2 * degToRad;
		double lam1 = long1 * degToRad;
		double lam2 = long2 * degToRad;

		return Math.atan2(Math.sin(lam2 - lam1) * Math.cos(phi2),
				Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lam2 - lam1)) * 180
				/ Math.PI;
	}

	public final String degreesToDMSString(double degrees, LATLONGTYPE latlong) {
		double d = degrees;
		int i = (int) Math.signum(d);
		d *= i;
		int j = (int) Math.floor(d);
		d = (d - j) * 60.0D;
		int k = (int) Math.floor(d);
		d = (d - k) * 60.0D;
		int m = (int) Math.round(d);
		if (m == 60) {
			k++;
			m = 0;
		}
		if (k == 60) {
			j++;
			k = 0;
		}

		if (latlong == LATLONGTYPE.LATITUDE) {
			return (String.format("Lat :%3d:%02d:%02d %s", j, k, m, (i == -1 ? "S" : "N")));
		} else {
			return (String.format("Lon :%3d:%02d:%02d %s", j, k, m, (i == -1 ? "W" : "E")));
		}
	}

}
