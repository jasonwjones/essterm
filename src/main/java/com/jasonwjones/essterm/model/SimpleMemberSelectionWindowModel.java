package com.jasonwjones.essterm.model;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.jasonwjones.essterm.dialogs.MemberSelectionWindow.MemberSelectionWindowModel;

public class SimpleMemberSelectionWindowModel implements MemberSelectionWindowModel {

	private Collection<String> dimensions;

	public SimpleMemberSelectionWindowModel(Collection<String> dimensions) {
		this.dimensions = dimensions;
	}

	@Override
	public Collection<String> getDimensions() {
		return this.dimensions;
	}

	@Override
	public Collection<String> getMembers(String dimension) {
		return IntStream.range(1, 11)
				.mapToObj(Integer::toString)
				.map(num -> String.format("%s %s", dimension, num))
				.collect(Collectors.toList());
	}

}
