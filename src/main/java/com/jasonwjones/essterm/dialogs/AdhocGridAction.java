package com.jasonwjones.essterm.dialogs;

/**
 * Represents an action that can be taken on the adhoc grid view. This includes
 * the typical grid actions as well as other things that can be invoked, such as
 * calling up the options dialog.
 * 
 * @author jasonwjones
 *
 */
public enum AdhocGridAction {

	ZOOM_IN("Zoom in on the selected cell"),
	ZOOM_IN_INCLUDE_SELECTION("Zoom in and include selection"),
	ZOOM_OUT("Zoom out"),
	KEEP_ONLY("Keep only"),
	REMOVE_ONLY("Remove only"),
	RUN_CALC("Run calc script"),
	ADHOC_OPTIONS("Configure ad hoc options"),
	PIVOT("Pivot the existing cell to a new location"),
	DATA_CELL_ACTION("Perform action on data cell");

	private String description;

	private AdhocGridAction(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

}
