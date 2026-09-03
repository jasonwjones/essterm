package com.jasonwjones.essterm.grid;

/**
 * One independently-controllable element of the ad hoc options dialog. A connection reports which of
 * these it actually supports (see {@link EssGrid#getSupportedOptions()}); the dialog uses that to grey
 * out anything the current backend can't apply, rather than hiding it - the classic dialog is mirrored
 * in full where the layout allows, even for options a given backend doesn't (yet) support.
 */
public enum AdhocOptionCapability {

	INDENTATION,

	SUPPRESS_MISSING_ROWS,
	SUPPRESS_ZERO_ROWS,
	SUPPRESS_UNDERSCORE_ROWS,

	USE_ALIASES,
	USE_BOTH_MEMBER_NAME_AND_ALIAS,
	ALIAS_TABLE_SELECTION,

	REPEAT_MEMBER_LABELS,

	ZOOM_IN_NEXT_LEVEL,
	ZOOM_IN_ALL_LEVELS,
	ZOOM_IN_BOTTOM_LEVEL,
	ZOOM_IN_SIBLING_LEVEL,
	ZOOM_IN_SAME_LEVEL,
	ZOOM_IN_SAME_GENERATION,
	ZOOM_IN_FORMULAS,

	INCLUDE_SELECTION,
	WITHIN_SELECTED_GROUP,
	REMOVE_UNSELECTED_GROUP

}
