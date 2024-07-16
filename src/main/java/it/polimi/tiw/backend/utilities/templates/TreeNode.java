package it.polimi.tiw.backend.utilities.templates;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private T nodeData;
    private List<TreeNode<T>> nodeChildren;

    public TreeNode(T nodeData) {
        this.nodeData = nodeData;
        this.nodeChildren = new ArrayList<>();
    }

    public T getNodeData() {
        return nodeData;
    }

    public void setNodeData(T nodeData) {
        this.nodeData = nodeData;
    }

    public List<TreeNode<T>> getNodeChildren() {
        return nodeChildren;
    }

    public void setNodeChildren(List<TreeNode<T>> nodeChildren) {
        this.nodeChildren = nodeChildren;
    }

    public void addChild(TreeNode<T> child) {
        this.nodeChildren.add(child);
    }
}
