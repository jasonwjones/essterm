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
	
	private boolean useAliases;
	
	private boolean useBothMemberNameAndAlias;
	
}
