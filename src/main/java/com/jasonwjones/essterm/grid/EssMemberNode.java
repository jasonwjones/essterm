package com.jasonwjones.essterm.grid;

import java.util.List;

/**
 * A node in a dimension's member hierarchy, browsable lazily: a dimension's full member list never
 * needs to be known up front, only the current node's direct children, fetched on expansion.
 */
public interface EssMemberNode {

	String getName();

	boolean isLeaf();

	List<EssMemberNode> getChildren();

}
