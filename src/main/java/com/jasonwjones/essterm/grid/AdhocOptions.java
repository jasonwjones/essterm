package com.jasonwjones.essterm.grid;

public class AdhocOptions {

	public enum Indentation {
		NONE, SUBITEMS, TOTALS
	}
	
	private Indentation indentation;
	
	private boolean suppressMissingRows;
	
	private boolean suppressZeroRows;
	
	private boolean suppressUnderscores;
	
	private String missingLabel;
	
	private String noAccessLabel;
	
	private boolean useAliases = true;
	
	private boolean useBothMemberNameAndAlias;

	public boolean isUseAliases() {
		return useAliases;
	}

	public void setUseAliases(boolean useAliases) {
		this.useAliases = useAliases;
	}
	
}
