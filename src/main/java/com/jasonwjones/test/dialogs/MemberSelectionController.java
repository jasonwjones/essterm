package com.jasonwjones.test.dialogs;

import java.util.Collection;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

public class MemberSelectionController {

	private MemberSelectionWindow memberSelectionWindow;
	
	private MemberSelectionModel model;
	
	public MemberSelectionController(MemberSelectionDataSource dataSource, MemberSelectionDelegate delegate) {
		this.memberSelectionWindow = new MemberSelectionWindow(null);
		
	}
	
	public void show(WindowBasedTextGUI gui) {
		gui.addWindow(memberSelectionWindow);
	}
	
	public interface MemberSelectionDataSource {
		
		public Collection<String> getDimensions();
		
		public Collection<String> getMembers(String dimension);
		
	}
	
	public interface MemberSelectionDelegate {
		
		// TODO: need orientation
		public void didSelectItems(Collection<String> items);
		
	}
	
	public static class MemberSelectionModel {
		
		private boolean isAcross;

		public boolean isAcross() {
			return isAcross;
		}

		public void setAcross(boolean isAcross) {
			this.isAcross = isAcross;
		}
		
	}
	
}
