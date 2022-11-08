package com.example.task03;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public MenuItem newNode;
    public MenuItem deleteNode;
    public MenuItem exit;
    public MenuBar menuBar;
    public Group canvasGroup;
    public Pane pane;
    public ArrayList<EdgeView> edgeViews = new ArrayList<>();
    public ArrayList<NodeView> nodeViews = new ArrayList<>();
    public NodeView selectedNodeView;
    public GraphModel model;

    public void setOnMouseClicked(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY && !touchesOnCircles(event.getX(), event.getY())) {
            NodeView nodeView = new NodeView(event.getX(), event.getY(), pane, nodeViews, edgeViews, model, selectedNodeView);
            nodeViews.add(nodeView);
        }

        selectedNodeView = getNodeViewUnderCursor(event.getX(), event.getY());
        if(selectedNodeView == null)
            model.selectedNodeProperty.setValue(null);
        else
            model.selectedNodeProperty.setValue(selectedNodeView.node);
    }

    public void setOnMouseReleased(MouseEvent event) {
        for (NodeView nodeView: nodeViews) {
            if (nodeView.currentEdge != null)
                nodeView.currentEdge = null;
            if(nodeView.isSelectedProperty.getValue())
                nodeView.deselect();
        }
    }

    public void onActionDeleteNode(Event event) {
        if(model.selectedNodeProperty.getValue() != null) {
            ArrayList<EdgeView> edgeViews = selectedNodeView.removeEdgeViews(model.removeEdges(model.selectedNodeProperty.getValue()));

            for (EdgeView edgeView: edgeViews) {
                pane.getChildren().remove(edgeView);
            }

            pane.getChildren().removeAll(selectedNodeView, selectedNodeView.label);
            model.removeNode(model.selectedNodeProperty.getValue());

            model.selectedNodeProperty.setValue(null);
        }
    }

    public void onActionAddNode(Event event) {
        NodeView nodeView = new NodeView(50, 50, pane, nodeViews, edgeViews, model, selectedNodeView);
        nodeViews.add(nodeView);
        selectedNodeView = nodeView;
    }

    public void onActionExit(Event event) {
        pane.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new GraphModel();
        setupPaneBackGround();
    }

    private void setupPaneBackGround() {
        BackgroundFill backgroundFill = new BackgroundFill(Color.color(0.75, 0.75, 1.0), CornerRadii.EMPTY, Insets.EMPTY);
        pane.setBackground(new Background(backgroundFill));
    }

    private NodeView getNodeViewUnderCursor(double x1, double y1) {
        for (NodeView nodeView: nodeViews) {
            double x2 = nodeView.getCenterX();
            double y2 = nodeView.getCenterY();
            double radius = nodeView.getRadius();
            if(distance(x1, y1, x2, y2) <= radius)
                return nodeView;
        }
        return null;
    }

    private boolean touchesOnCircles(double x1, double y1) {
        for (NodeView nodeView: nodeViews) {
            double x2 = nodeView.getCenterX();
            double y2 = nodeView.getCenterY();
            double radius = nodeView.getRadius();
            if(distance(x1, y1, x2, y2) <= radius)
                return true;
        }
        return false;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1, 2));
    }
}
