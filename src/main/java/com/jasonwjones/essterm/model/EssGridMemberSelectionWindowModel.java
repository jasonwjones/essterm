package com.jasonwjones.essterm.model;

import java.util.List;

import com.jasonwjones.essterm.dialogs.MemberSelectionWindow.MemberSelectionWindowModel;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssMemberNode;
import com.jasonwjones.essterm.grid.MemberPlacement;

public class EssGridMemberSelectionWindowModel implements MemberSelectionWindowModel {

	private final EssGrid grid;

	public EssGridMemberSelectionWindowModel(EssGrid grid) {
		this.grid = grid;
	}

	@Override
	public List<String> getDimensions() {
		return grid.getDimensionNames();
	}

	@Override
	public EssMemberNode getDimensionRoot(String dimensionName) {
		return grid.getDimensionRoot(dimensionName);
	}

	@Override
	public void setMembers(List<MemberPlacement> placements) {
		grid.setMembers(placements);
	}

}
