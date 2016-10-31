package com.jasonwjones.essterm.dialogs;

import com.googlecode.lanterna.gui2.dialogs.ActionListDialog;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;

public class StartupDialogWindow {

	public static ActionListDialog create() {
		
		return new ActionListDialogBuilder()
			.setTitle("Essterm")
			.setDescription("This is desc and another line")
			.addAction("Connect", new Runnable() {

				public void run() {
					System.out.println("Want conncet");
				}})
			.addAction("Configure ad hoc options", new Runnable() {
				public void run() {
					//new AdhocOptionsDialogWindow2("Opts").showDialog()
				}})
			.addAction("About", null)
			.addAction("Exit", null)
			
			.build();			
	}

}
