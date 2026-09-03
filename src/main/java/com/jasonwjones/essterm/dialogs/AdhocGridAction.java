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

	ZOOM_IN("Zoom in on the selected cell", "zoom-in"),
	ZOOM_IN_INCLUDE_SELECTION("Zoom in and include selection", "zoom-in-sel"),
	ZOOM_OUT("Zoom out", "zoom-out"),
	KEEP_ONLY("Keep only", "keep-only"),
	REMOVE_ONLY("Remove only", "remove-only"),
	RUN_CALC("Run calc script", "run-calc"),
	ADHOC_OPTIONS("Configure ad hoc options", "conf-ad-hoc"),
	PIVOT("Pivot the existing cell to a new location", "pivot"),
	DATA_CELL_ACTION("Perform action on data cell", "data-cell"),
	KEY_BINDING_OPTIONS("View/edit key bindings", "keybindings"),
	REDUCE_VISIBLE_COLUMNS("Reduce visible columns", "reduce-vislble"),
	INCREASE_VISIBLE_COLUMNS("Increase visible columns", "increase-visible"),
	EDIT_CELL("Edit cell", "edit-cell"),
	MEMBER_SELECTION("Select members and place in grid", "member-selection");

	private String description;
	
	private String code;
	
	private AdhocGridAction(String description, String code) {
		this.description = description;
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getCode() {
		return this.code;
	}

}
