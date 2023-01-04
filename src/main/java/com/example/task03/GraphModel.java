package com.example.task03;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class GraphModel {
    public ObservableList<Node> nodes = FXCollections.observableArrayList();
    public ObservableList<Edge> edges = FXCollections.observableArrayList();
    public Property<Node> selectedNodeProperty = new SimpleObjectProperty<>();
    public void addNode(Node node) {
        nodes.add(node);
    }

    public void removeNode(Node node) {
        nodes.remove(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Edge> removeEdges(Node node) {
        List<Edge> temp = edges.stream()
                .filter(edge -> edge.source == node || edge.target == node)
                .toList();
        edges.removeAll(temp);
        return temp;
    }

    public boolean edgeExists(Edge e) {
        return edges.stream().anyMatch(edge -> edge == e);
    }

    public boolean edgeExists(Node node1, Node node2) {
        return edges.stream().
                anyMatch(edge -> (edge.source == node1 && edge.target == node2) || (edge.target == node1 && edge.source == node2));
    }
}
