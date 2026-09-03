package com.jasonwjones.essterm.grid;

/**
 * Ad hoc display/navigation options, mirroring the classic Essbase "Options" dialog's Display and
 * Zoom tabs. Not every field here is supported by every backend - see {@link EssGrid#getSupportedOptions()}
 * for which ones a given connection can actually apply.
 */
public class AdhocOptions {

	public enum Indentation {
		NONE, SUBITEMS, TOTALS
	}

	/**
	 * The server's zoom-in depth when a plain zoom-in is performed (no per-click override). Mirrors
	 * the classic dialog's "Zoom In" radio group. Only NEXT_LEVEL, ALL_LEVELS, and BOTTOM_LEVEL have a
	 * REST equivalent (see {@code EssCubeView.ZoomInPreference}) - the rest are JAPI-only.
	 */
	public enum ZoomInPreference {
		NEXT_LEVEL, ALL_LEVELS, BOTTOM_LEVEL, SIBLING_LEVEL, SAME_LEVEL, SAME_GENERATION, FORMULAS
	}

	private Indentation indentation = Indentation.SUBITEMS;

	private boolean suppressMissingRows;

	private boolean suppressZeroRows;

	private boolean suppressUnderscores;

	private String missingLabel = "#Missing";

	private String noAccessLabel = "#NoAccess";

	private boolean useAliases = true;

	private boolean useBothMemberNameAndAlias;

	private String aliasTableName;

	private boolean repeatMemberLabels = true;

	private ZoomInPreference zoomInPreference = ZoomInPreference.NEXT_LEVEL;

	private boolean includeSelection = true;

	private boolean withinSelectedGroup;

	private boolean removeUnselectedGroup;

	public AdhocOptions() {
	}

	public AdhocOptions(AdhocOptions source) {
		applyFrom(source);
	}

	/**
	 * Copies every field from {@code source} onto this instance in place. Used to persist an edited
	 * dialog copy back onto a settings object held by reference elsewhere (no setter to swap out the
	 * whole instance) - see {@code AdhocOptionsDialogWindow}.
	 */
	public void applyFrom(AdhocOptions source) {
		this.indentation = source.indentation;
		this.suppressMissingRows = source.suppressMissingRows;
		this.suppressZeroRows = source.suppressZeroRows;
		this.suppressUnderscores = source.suppressUnderscores;
		this.missingLabel = source.missingLabel;
		this.noAccessLabel = source.noAccessLabel;
		this.useAliases = source.useAliases;
		this.useBothMemberNameAndAlias = source.useBothMemberNameAndAlias;
		this.aliasTableName = source.aliasTableName;
		this.repeatMemberLabels = source.repeatMemberLabels;
		this.zoomInPreference = source.zoomInPreference;
		this.includeSelection = source.includeSelection;
		this.withinSelectedGroup = source.withinSelectedGroup;
		this.removeUnselectedGroup = source.removeUnselectedGroup;
	}

	public boolean isUseAliases() {
		return useAliases;
	}

	public void setUseAliases(boolean useAliases) {
		this.useAliases = useAliases;
	}

	public Indentation getIndentation() {
		return indentation;
	}

	public void setIndentation(Indentation indentation) {
		this.indentation = indentation;
	}

	public boolean isSuppressMissingRows() {
		return suppressMissingRows;
	}

	public void setSuppressMissingRows(boolean suppressMissingRows) {
		this.suppressMissingRows = suppressMissingRows;
	}

	public boolean isSuppressZeroRows() {
		return suppressZeroRows;
	}

	public void setSuppressZeroRows(boolean suppressZeroRows) {
		this.suppressZeroRows = suppressZeroRows;
	}

	public boolean isSuppressUnderscores() {
		return suppressUnderscores;
	}

	public void setSuppressUnderscores(boolean suppressUnderscores) {
		this.suppressUnderscores = suppressUnderscores;
	}

	public String getMissingLabel() {
		return missingLabel;
	}

	public void setMissingLabel(String missingLabel) {
		this.missingLabel = missingLabel;
	}

	public String getNoAccessLabel() {
		return noAccessLabel;
	}

	public void setNoAccessLabel(String noAccessLabel) {
		this.noAccessLabel = noAccessLabel;
	}

	public boolean isUseBothMemberNameAndAlias() {
		return useBothMemberNameAndAlias;
	}

	public void setUseBothMemberNameAndAlias(boolean useBothMemberNameAndAlias) {
		this.useBothMemberNameAndAlias = useBothMemberNameAndAlias;
	}

	/**
	 * The alias table to use, or {@code null} to use the connection's default table.
	 */
	public String getAliasTableName() {
		return aliasTableName;
	}

	public void setAliasTableName(String aliasTableName) {
		this.aliasTableName = aliasTableName;
	}

	public boolean isRepeatMemberLabels() {
		return repeatMemberLabels;
	}

	public void setRepeatMemberLabels(boolean repeatMemberLabels) {
		this.repeatMemberLabels = repeatMemberLabels;
	}

	public ZoomInPreference getZoomInPreference() {
		return zoomInPreference;
	}

	public void setZoomInPreference(ZoomInPreference zoomInPreference) {
		this.zoomInPreference = zoomInPreference;
	}

	public boolean isIncludeSelection() {
		return includeSelection;
	}

	public void setIncludeSelection(boolean includeSelection) {
		this.includeSelection = includeSelection;
	}

	public boolean isWithinSelectedGroup() {
		return withinSelectedGroup;
	}

	public void setWithinSelectedGroup(boolean withinSelectedGroup) {
		this.withinSelectedGroup = withinSelectedGroup;
	}

	public boolean isRemoveUnselectedGroup() {
		return removeUnselectedGroup;
	}

	public void setRemoveUnselectedGroup(boolean removeUnselectedGroup) {
		this.removeUnselectedGroup = removeUnselectedGroup;
	}

}
