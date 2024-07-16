package it.polimi.tiw.backend.utilities.templates;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic tree node that can store any type of data and its children nodes.
 *
 * @param <T> The type of the data stored in the node.
 */
public class TreeNode<T> {
    private T nodeData;
    private List<TreeNode<T>> nodeChildren;

    /**
     * Constructs a new TreeNode with the specified data and no children.
     *
     * @param nodeData The data to be stored in the node.
     */
    public TreeNode(T nodeData) {
        this.nodeData = nodeData;
        this.nodeChildren = new ArrayList<>();
    }

    /**
     * Returns the data stored in the node.
     *
     * @return The data stored in the node.
     */
    public T getNodeData() {
        return nodeData;
    }

    /**
     * Sets the data for the node.
     *
     * @param nodeData The data to be stored in the node.
     */
    public void setNodeData(T nodeData) {
        this.nodeData = nodeData;
    }

    /**
     * Returns the list of children nodes.
     *
     * @return The list of children nodes.
     */
    public List<TreeNode<T>> getNodeChildren() {
        return nodeChildren;
    }

    /**
     * Sets the list of children nodes.
     *
     * @param nodeChildren The list of children nodes to be set.
     */
    public void setNodeChildren(List<TreeNode<T>> nodeChildren) {
        this.nodeChildren = nodeChildren;
    }

    /**
     * Adds a child node to this node.
     *
     * @param child The child node to be added.
     */
    public void addChild(TreeNode<T> child) {
        this.nodeChildren.add(child);
    }
}
