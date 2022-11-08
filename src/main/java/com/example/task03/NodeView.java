package com.example.task03;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class NodeView extends Circle {
    public Node node;
    public Text label;
    public DoubleProperty xProperty = new SimpleDoubleProperty();
    public DoubleProperty yProperty = new SimpleDoubleProperty();
    public BooleanProperty isSelectedProperty = new SimpleBooleanProperty();
    public Cursor oldCursor;
    public double anchorX, anchorY;
    public DropShadow shortShadow;
    public DropShadow longShadow;
    public Pane pane;
    public ArrayList<EdgeView> edgeViews;
    public Edge currentEdge;
    public GraphModel model;
    public EdgeView currentEdgeView;
    public ArrayList<NodeView> nodeViews;
    public Property<Paint> colorProperty = new SimpleObjectProperty<>(Color.WHITE);
    public Property<NodeView> selectedNodeViewProperty = new SimpleObjectProperty<>();
    public Line tempLine;
    public NodeView tempNodeView;
    public NodeView(double x, double y, Pane pane, ArrayList<NodeView> nodeViews, ArrayList<EdgeView>  edgeViews, GraphModel model, NodeView selectedNodeView) {
        super(x, y, 20);
        this.pane = pane;
        this.edgeViews = edgeViews;
        this.model = model;
        this.nodeViews = nodeViews;

        node = new Node("?");
        node.xProperty.setValue(x);
        node.yProperty.setValue(y);

        model.addNode(node);

        selectedNodeView = this;

        this.xProperty.bindBidirectional(this.node.xProperty);
        this.yProperty.bindBidirectional(this.node.yProperty);

        isSelectedProperty.bindBidirectional(this.node.isSelectedProperty);

        this.label = new Text();
        this.label.layoutXProperty().bindBidirectional(xProperty);
        this.label.layoutYProperty().bindBidirectional(yProperty);
        this.label.textProperty().bindBidirectional(node.labelProperty);

        centerXProperty().bindBidirectional(xProperty);
        centerYProperty().bindBidirectional(yProperty);

        pane.getChildren().addAll(this.label, this);
        nodeViews.add(this);
        label.toFront();

        setStrokeWidth(5);
        setStroke(Color.BLACK);
        fillProperty().bindBidirectional(colorProperty);

        select();

        shortShadow = new DropShadow();
        shortShadow.setColor(new Color(0, 0,0, .5));
        shortShadow.setRadius(5);
        shortShadow.setOffsetX(5);
        shortShadow.setOffsetY(5);

        setEffect(shortShadow);

        longShadow = new DropShadow();
        longShadow.setColor(new Color(0, 0,0, .5));
        longShadow.setRadius(5);
        longShadow.setOffsetX(10);
        longShadow.setOffsetY(10);

        attachEventHandlers();
    }

    public void attachEventHandlers() {
        setOnMouseEntered(event -> {
            oldCursor = getCursor();
            setCursor(Cursor.OPEN_HAND);
        });

        setOnMousePressed(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                setEffect(longShadow);
                setCursor(Cursor.CLOSED_HAND);
                anchorX = event.getSceneX() - getCenterX();
                anchorY = event.getSceneY() - getCenterY();
                toFront();
                label.toFront();
            }
            else if(event.getButton() == MouseButton.SECONDARY) {
                if(currentEdge == null) {
                    currentEdge = new Edge();
                    currentEdge.source = node;
                    currentEdge.target = new Node(event.getX(), getCenterY());

                    tempLine = new Line();
                    tempLine.setStartX(event.getX());
                    tempLine.setStartY(event.getY());
                }
            }
        });

        setOnMouseDragged(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                select();
                setCenterX(event.getSceneX() - anchorX);
                setCenterY(event.getSceneY() - anchorY);
;            }
            else if(event.getButton() == MouseButton.SECONDARY) {
                if(tempLine != null) {
                    tempNodeView = getNodeViewUnderCursor(event.getX(), event.getY());
                    if(tempNodeView != null && tempNodeView != this) {
                        if(model.edgeExists(tempNodeView.node, this.node))
                            tempNodeView.colorProperty.setValue(Color.RED);
                        else
                            tempNodeView.colorProperty.setValue(Color.GREEN);
                    }
                    else if(tempNodeView == null) {
                        for (NodeView nodeView: nodeViews) {
                            nodeView.colorProperty.setValue(Color.WHITE);
                        }
                    }
                    setCursor(Cursor.CROSSHAIR);
                    int index = pane.getChildren().indexOf(tempLine);
                    tempLine.setEndX(event.getX());
                    tempLine.setEndY(event.getY());
                    if(index != -1) {
                        pane.getChildren().set(index, tempLine);
                    }
                    else {
                        pane.getChildren().add(tempLine);
                        tempLine.toBack();
                    }
                }

            }
        });

        setOnMouseReleased(event -> {
            if(event.getButton() == MouseButton.PRIMARY) {
                setEffect(shortShadow);
                setCursor(Cursor.OPEN_HAND);
            }
            else if(event.getButton() == MouseButton.SECONDARY) {
                setCursor(Cursor.OPEN_HAND);
                pane.getChildren().remove(tempLine);
                tempLine = null;
                NodeView nodeView = getNodeViewUnderCursor(event.getX(), event.getY());
                if(currentEdge != null && nodeView != null && nodeView != this) {
                    if(!model.edgeExists(currentEdge)) {
                        currentEdge.target = nodeView.node;
                        model.addEdge(currentEdge);

                        currentEdgeView = new EdgeView(pane, currentEdge, edgeViews);

                        currentEdge = null;

                        select();
                    }
                }
                if (nodeView != null)
                    nodeView.deselect();
            }
        });

        setOnMouseExited(event -> {
            setCursor(oldCursor);
        });

        setOnMouseClicked(event -> {
            select();
            deselectAll();
            colorProperty.setValue(Color.GREEN);
            if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                textField.setLayoutX(this.xProperty.getValue() - 20);
                textField.setLayoutY(this.yProperty.getValue() - 10);
                pane.getChildren().add(textField);

                textField.setOnKeyReleased(event2 -> {
                    if(event2.getCode() == KeyCode.ENTER) {
                        this.label.setText(textField.getText());
                        pane.getChildren().remove(textField);
                    }
                });
            }
        });
    }

    public ArrayList<EdgeView> removeEdgeViews(ArrayList<Edge> edges) {
        ArrayList<EdgeView> temp = new ArrayList<>();
        for (Edge edge:edges) {
            for (EdgeView edgeView: edgeViews) {
                if(edgeView.edge == edge) {
                    temp.add(edgeView);
                }
            }
        }
        edgeViews.removeAll(temp);
        return temp;
    }

    private void deselectAll() {
        for (NodeView nodeView: nodeViews)
            if(nodeView != this)
                if(nodeView.isSelectedProperty.getValue()) {
                    nodeView.deselect();
                    model.selectedNodeProperty.setValue(null);
                    selectedNodeViewProperty.setValue(null);
                }
    }

    public static void deselectAll(ArrayList<NodeView> nodeViews, GraphModel model) {
        for (NodeView nodeView: nodeViews)
            if(nodeView.isSelectedProperty.getValue()) {
                nodeView.deselect();
                model.selectedNodeProperty.setValue(null);
                nodeView.selectedNodeViewProperty.setValue(null);
            }
    }

    public void deselect() {
        isSelectedProperty.setValue(false);
        colorProperty.setValue(Color.WHITE);
    }

    public void  select() {
        isSelectedProperty.setValue(true);
        colorProperty.setValue(Color.GREEN);
        selectedNodeViewProperty.setValue(this);
        model.selectedNodeProperty.setValue(this.node);
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

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1,2) + Math.pow(y2 - y1, 2));
    }
}
