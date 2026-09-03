package com.jasonwjones.essterm.essgrid;

import java.util.ArrayList;
import java.util.List;

import com.appliedolap.essbase.EssMember;
import com.jasonwjones.essterm.grid.EssMemberNode;

/**
 * Adapts essbase-rest-client's {@link EssMember} to essterm's backend-agnostic
 * {@link EssMemberNode}.
 */
class RestMemberNode implements EssMemberNode {

	private final EssMember member;

	RestMemberNode(EssMember member) {
		this.member = member;
	}

	@Override
	public String getName() {
		return member.getName();
	}

	@Override
	public boolean isLeaf() {
		return member.isLeaf();
	}

	@Override
	public List<EssMemberNode> getChildren() {
		List<EssMemberNode> children = new ArrayList<>();
		for (EssMember child : member.getChildren()) {
			children.add(new RestMemberNode(child));
		}
		return children;
	}

}
