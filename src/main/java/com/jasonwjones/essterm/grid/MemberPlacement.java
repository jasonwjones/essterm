package com.jasonwjones.essterm.grid;

/**
 * A member to place at a specific grid position, replacing whichever member currently occupies
 * that spot on its row or column axis tier.
 */
public class MemberPlacement {

	private final Point point;

	private final String memberName;

	public MemberPlacement(Point point, String memberName) {
		this.point = point;
		this.memberName = memberName;
	}

	public Point getPoint() {
		return point;
	}

	public String getMemberName() {
		return memberName;
	}

}
