package com.example.task03;

import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;

import java.util.ArrayList;

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

    public void moveNode(Node node, double x, double y) {
        int index = nodes.indexOf(node);
        Node temp = new Node(node.labelProperty.getValue());
        temp.xProperty.setValue(x);
        temp.yProperty.setValue(y);
        nodes.set(index, temp);
    }

    public void relabelNode(Node node, String label) {
        int index = nodes.indexOf(node);
        nodes.set(index, new Node(label));
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public ArrayList<Edge> removeEdges(Node node) {
        ArrayList<Edge> temp = new ArrayList<>();
        for (Edge edge: edges) {
            if(edge.source == node || edge.target == node) {
                temp.add(edge);
            }
        }
        edges.removeAll(temp);
        return temp;
    }

    public boolean edgeExists(Edge e) {
        for (Edge edge: edges)
            if(edge == e)
                return true;
        return false;
    }

    public boolean edgeExists(Node node1, Node node2) {
        for (Edge edge: edges) {
            if(edge.source == node1 && edge.target == node2) {
                return  true;
            }
            else if(edge.target == node1 && edge.source == node2) {
                return true;
            }
        }
        return false;
    }
}
