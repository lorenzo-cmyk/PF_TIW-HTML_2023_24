package it.polimi.tiw.backend.utilities.templates;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic tree node that can store any type of data and its children nodes.
 *
 * @param <T> The type of the data stored in the node.
 */
public class TreeNode<T> {
    private final T nodeData;
    private final List<TreeNode<T>> nodeChildren;

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
     * Returns the list of children nodes.
     *
     * @return The list of children nodes.
     */
    public List<TreeNode<T>> getNodeChildren() {
        return nodeChildren;
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
