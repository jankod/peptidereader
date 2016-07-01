package hr.semgen.ms.ufb.taxon;

import java.util.ArrayList;
import java.util.List;

public class Node {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + taxID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taxID != other.taxID)
			return false;
		return true;
	}

	private int taxID;
	private int parentTaxID;

	private String name;

	private String rank;

	private Node parent;
	private ArrayList<Node> chields;

	private static final Node[] EMPTY_NODES = new Node[] {};
	private Node[] childCache;

	final public Node[] getChildren() {
		if (chields == null || chields.isEmpty()) {
			return EMPTY_NODES;
		}
		if (childCache == null) {
			childCache = chields.toArray(new Node[chields.size()]);
		}
		return childCache;

	}

	@Override
	public String toString() {
		return "Node [taxID=" + taxID + ", parentTaxID=" + parentTaxID + ", name=" + name + "]";
	}

	public void addChild(Node c) {
		if (chields == null) {
			chields = new ArrayList<Node>(1);
		}
		childCache = null;
		chields.add(c);

	}

	public int getTaxID() {
		return taxID;
	}

	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}

	public int getParentTaxID() {
		return parentTaxID;
	}

	public void setParentTaxID(int parentTaxID) {
		this.parentTaxID = parentTaxID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public ArrayList<Node> getChields() {
		return chields;
	}

	public void setChields(ArrayList<Node> chields) {
		this.chields = chields;
	}

	/**
	 * Obican array napravljen zbog brzine u tree strukturi
	 * 
	 * @return
	 */
	public Node[] getChildCache() {
		return childCache;
	}

	public void setChildCache(Node[] childCache) {
		this.childCache = childCache;
	}

	public int getChieldsSize() {
		if (chields == null) {

			return 0;
		} else {
			return chields.size();
		}
	}

//	public static void callRecursive(List<Node> nodes, CallBackResult<Node> callBackResult) {
//		for (Node node : nodes) {
//			callBackResult.finish(node);
//			if (node.chields != null && !node.chields.isEmpty()) {
//				callRecursive(node.getChields(), callBackResult);
//			}
//		}
//	}

	public static void callRecursive(List<Node> nodes, RecursionWalk callBackResult) {
		for (Node node : nodes) {
			if (callBackResult.gotNode(node)) {
				if (node.chields != null && !node.chields.isEmpty()) {
					callRecursive(node.getChields(), callBackResult);
				}
			} else {
				return;
			}
		}
	}

	public static interface RecursionWalk {

		/**
		 * Ako vrati false, onda staje sa rekurzijom i izlazi van.
		 * 
		 * @param node
		 * @return
		 */
		boolean gotNode(Node node);
	}
}
