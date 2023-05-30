package com.wf.wfdeliverysystem.controller;

import com.wf.wfdeliverysystem.Launcher;
import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;

public class InitController {

    @FXML
    private Label wfTitleLbl;
    // FXML
    @FXML
    private Button exitBtn;
    @FXML
    private VBox layout;
    @FXML
    private Canvas canvas;
    @FXML
    private Button checkDeliveryBtn;
    @FXML
    private Button generateTourBtn;
    @FXML
    private Button viewGraphsBtn;
    @FXML
    private RadioButton hq1TB;
    @FXML
    private RadioButton h12TB;
    @FXML
    private RadioButton hq3TB;
    @FXML
    private RadioButton matrixTB;
    @FXML
    private RadioButton listTB;
    @FXML
    private ToggleGroup hqTG;
    @FXML
    private ToggleGroup typeTG;

    public boolean running;

    // Structures and constants for graphical purposes

    private GraphicsContext context;
    private final int BACKGROUND =0, UNSELECTED = 1, SELECTED_HOUSE = 2, SELECTED_HQ = 3;
    private final Color[] COLOR_PALETTE = new Color[]{
            Color.web("#5dab54"), // background
            Color.web("#4293bd"), // unselected
            Color.web("#5748b7"), // selected house
            Color.web("#de213e"), // selected hq
    };
    String imgDir = System.getProperty("user.dir") + "/src/main/resources/img/";
    private final Image[] pictures = new Image[]{
            new Image(imgDir + "HQ001.png"),
            new Image(imgDir + "H001.png"),
            new Image(imgDir + "D001.png"),
    };
    private Random rnd;
    private double diff;
    private boolean[][] takenPoints;
    private ArrayList<House> houses;
    private ArrayList<House> headquarters;
    House selectedHouse, selectedHQ;
    private ArrayList<Pair<Point2D, Point2D>> edges;
    private final int x_dim = 16, y_dim = 9;
    public Stage stage;
    DeliveryCycle cycle;
    // Init, refresh and close

    public void initialize() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {

        takenPoints = new boolean[x_dim][y_dim];

        // init
        houses = new ArrayList<>();
        headquarters = new ArrayList<>();
        context = canvas.getGraphicsContext2D();
        cycle = new DeliveryCycle(new Point2D(takenPoints.length, takenPoints[0].length), pictures[2]);

        edges = new ArrayList<>();
        rnd = new Random();

        // System.out.println(diff);

        generateVertices();

        hqTG.selectedToggleProperty().addListener( changeListener -> {
            if( hq1TB.isSelected() ) {
                selectedHQ = headquarters.get(0);
            } else if(h12TB.isSelected()) {
                selectedHQ = headquarters.get(1);
            } else {
                selectedHQ = headquarters.get(2);
            }
        } );

        // paint
        this.running = true;
        new Thread( () -> {
            while(running) {
                Platform.runLater( () -> {
                    paint();
                } );
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } ).start();
    }

    private void paint() {
        // resizing

        // Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Rectangle2D bounds = new Rectangle2D(0, 0 , stage.getWidth(), stage.getHeight());
        canvas.setHeight(bounds.getHeight() * 0.65);
        canvas.setWidth(bounds.getWidth() - 48);

        if( canvas.getWidth()/ (x_dim-1) > canvas.getHeight()/ (y_dim-1) ) {
            diff = Math.floor(canvas.getHeight()/ (y_dim-1));
            canvas.setWidth(diff*(x_dim-1));
        } else {
            diff = Math.floor(canvas.getWidth()/ (x_dim-1));
            canvas.setHeight(diff*(y_dim-1));
        }
        double h_padding = bounds.getWidth()*0.1, v_padding = bounds.getHeight()*0.1;
        layout.setPadding( new Insets( v_padding, h_padding, v_padding, h_padding ));
        wfTitleLbl.setFont(Font.font( wfTitleLbl.getFont().toString(), FontWeight.BOLD, bounds.getHeight()*0.01 + 18));

        // painting
        context.setFill(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        context.setFill(COLOR_PALETTE[BACKGROUND]);
        context.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), diff/2, diff/2);
        for(Pair<Point2D, Point2D> e : edges) drawLine(e.getKey(), e.getValue());
        for(Element h : houses) h.drawCircle(COLOR_PALETTE[UNSELECTED], diff, context);
        if(selectedHouse != null) selectedHouse.drawCircle(COLOR_PALETTE[SELECTED_HOUSE], diff, context);
        if(selectedHQ != null) selectedHQ.drawCircle(COLOR_PALETTE[SELECTED_HQ], diff, context);
        cycle.drawCircle(COLOR_PALETTE[UNSELECTED], diff, context);
        cycle.move();

        // System.out.println(selectedHQ.getCoords());
    }

    private void generateVertices() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        for (int i = 0; i < 3; i++) {
            Point2D curr = new Point2D(
                    rnd.nextInt(1 + i * takenPoints.length / 3, (i + 1) * takenPoints.length / 3 - 1),
                    rnd.nextInt(1, takenPoints[0].length - 1)
            );
            takenPoints[(int) curr.getX()][(int) curr.getY()] = true;
            House currElement = new House(curr, pictures[0], "aa");
            Launcher.getManager().getList().addVertex(currElement);
            Launcher.getManager().getMatrix().addVertex(currElement);
            houses.add( currElement);
            headquarters.add(currElement);
            if(selectedHQ == null) selectedHQ = headquarters.get(0);
            // repeat until the number of vertices is reached
            int verticesPerTree = 25;
            while (houses.size() % verticesPerTree != 0) {
                // generate a random point one cell separated from the current
                int nx = (int) currElement.getCoords().getX() + rnd.nextInt(-1, 2), ny = (int) currElement.getCoords().getY() + rnd.nextInt(-1, 2);
                // check if it is empty and inside the range
                boolean generalRestrictions = takenPoints[nx][ny] || nx == 0 || nx >= takenPoints.length-1 || ny == 0 || ny >= takenPoints[0].length - 1;
                boolean specificRestrictions = nx > (i + 1) * takenPoints.length / 3 || nx < i* takenPoints.length/3;
                if ( generalRestrictions || specificRestrictions ) {
                    // regenerate the vertices with a random previous vertex
                    currElement = houses.get(rnd.nextInt(verticesPerTree *i, houses.size()));
                } else {
                    takenPoints[nx][ny] = true;
                    Point2D next = new Point2D(nx, ny);
                    House newElement = new House(next, pictures[1], "aa");
                    houses.add( newElement );
                    edges.add(new Pair<>(currElement.getCoords(), newElement.getCoords()));
                    Launcher.getManager().getList().addVertex(newElement);
                    Launcher.getManager().getList().addEdge(currElement, newElement, ""+houses.size(), 5);
                    Launcher.getManager().getMatrix().addVertex(newElement);
                    Launcher.getManager().getMatrix().addEdge(currElement, newElement, ""+houses.size(), 5);
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    // Drawing

    private void drawLine(Point2D p1, Point2D p2) {
        // border
        context.setStroke(Color.BLACK);
        context.setLineWidth(diff/3.5);
        context.strokeLine(p1.getX()* diff, p1.getY()* diff, p2.getX()* diff, p2.getY()* diff);
        // gray street
        context.setStroke(Color.GRAY);
        context.setLineWidth(diff/5);
        context.strokeLine(p1.getX()* diff, p1.getY()* diff, p2.getX()* diff, p2.getY()* diff);
        // white dashes
        context.setStroke(Color.WHITE);
        context.setLineWidth(diff/33);
        context.setLineDashes(diff/7);
        context.strokeLine(p1.getX()* diff, p1.getY()* diff, p2.getX()* diff, p2.getY()* diff);
    }

    // Requirement Buttons
    public void onVertexSelected(MouseEvent mouseEvent) {
        cycle.resetMovement();
        double currX = mouseEvent.getX()/diff, currY = mouseEvent.getY()/diff;
        Point2D closest = new Point2D((int)Math.round(currX), (int)Math.round(currY));

        // circunference equation (x-h)^2 + (y-k)^2 = r^2

        boolean isInsideCircle = Math.pow(currX*diff - closest.getX()*diff,2) + Math.pow(currY*diff - closest.getY()*diff, 2) <= Math.pow(diff/4,2);

        if(isInsideCircle && takenPoints[(int)closest.getX()][(int)closest.getY()]) {
            try {
                selectedHouse = (House)houses.stream().filter( h -> h.getCoords().equals(closest) && h.getPicture() != pictures[0]).findFirst().get();
            } catch(NoSuchElementException e) {
                e.getStackTrace();
            }
        }
    }
    public void onCheckDelivery(ActionEvent actionEvent) throws VertexNotAchievableException, VertexNotFoundException {
        if(selectedHouse == null) {
            Launcher.showAlert(Alert.AlertType.ERROR, "Unselected house", "First select a house and try again.");
            return;
        }
        if( !Launcher.getManager().checkPathBetweenHouses(selectedHQ, selectedHouse) ) {
            Launcher.showAlert(Alert.AlertType.INFORMATION, "Unreachable House", "House is not connected to the current headquarter");
            return;
        }
        ArrayList<Pair<Element, Element>> edges = Launcher.getManager().calculateMinimumPath(selectedHQ, selectedHouse);
        cycle.setTour(edges);
    }

    public void onGenerateTour(ActionEvent actionEvent) throws VertexNotFoundException {
        selectedHouse = null;
        Launcher.getManager().setMatrix(true);
        ArrayList<Pair<Element, Element>> edges = Launcher.getManager().generateDeliveryTour(selectedHQ);
        cycle.setTour(edges);
    }

    public void onViewGraphs(ActionEvent actionEvent) {

    }

    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        running = false;
        stage.close();
    }
}