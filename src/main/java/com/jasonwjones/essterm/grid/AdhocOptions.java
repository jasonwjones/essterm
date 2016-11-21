package com.jasonwjones.essterm.grid;

public class AdhocOptions {

	public enum Indentation {
		NONE, SUBITEMS, TOTALS
	}

	private Indentation indentation = Indentation.SUBITEMS;

	private boolean suppressMissingRows;

	private boolean suppressZeroRows;

	private boolean suppressUnderscores;

	private String missingLabel = "#Missing";

	private String noAccessLabel = "#NoAccess";

	private boolean useAliases = true;

	private boolean useBothMemberNameAndAlias;

	public AdhocOptions() {}
	
	public AdhocOptions(AdhocOptions source) {
		
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

}
