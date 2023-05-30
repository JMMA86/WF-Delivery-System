package com.wf.wfdeliverysystem.controller;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Random;

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
    private final Color[] COLOR_PALETTE = new Color[]{
            Color.rgb(128, 128, 64),
            Color.RED
    };
    private final Image[] pictures = new Image[]{
            new Image(System.getProperty("user.dir") + "/src/main/resources/img/D001.png"),
            new Image(System.getProperty("user.dir") + "/src/main/resources/img/H001.png"),
            new Image(System.getProperty("user.dir") + "/src/main/resources/img/HQ001.png"),
    };
    private Random rnd;
    private double diff;
    private boolean[][] takenPoints;
    private ArrayList<House> houses;
    private ArrayList<Pair<Point2D, Point2D>> edges;
    private DeliveryCycle cycle;
    private final int x_dim = 16, y_dim = 9;

    public Stage stage;
    // Init, refresh and close

    public void initialize() {

        takenPoints = new boolean[x_dim][y_dim];

        // init
        houses = new ArrayList<>();
        context = canvas.getGraphicsContext2D();

        edges = new ArrayList<>();
        cycle = new DeliveryCycle(new Point2D(takenPoints.length, takenPoints[0].length), pictures[2]);
        rnd = new Random();

        // System.out.println(diff);

        generateVertices();

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
        context.setFill(Color.GREEN);
        context.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), diff/2, diff/2);
        for(Pair<Point2D, Point2D> e : edges) drawLine(e.getKey(), e.getValue());
        for(House h : houses) h.drawCircle(COLOR_PALETTE[0], diff, context);
        cycle.drawCircle(COLOR_PALETTE[0], diff, context);
        cycle.move();
    }

    private void generateVertices() {
        for (int i = 0; i < 3; i++) {
            Point2D curr = new Point2D(
                    rnd.nextInt(1 + i * takenPoints.length / 3, (i + 1) * takenPoints.length / 3 - 1),
                    rnd.nextInt(1, takenPoints[0].length - 1)
            );
            takenPoints[(int) curr.getX()][(int) curr.getY()] = true;
            houses.add( new House(curr, pictures[0], "aa"));
            // repeat until the number of vertices is reached
            int verticesPerTree = 25;
            while (houses.size() % verticesPerTree != 0) {
                // generate a random point one cell separated from the current
                int nx = (int) curr.getX() + rnd.nextInt(-1, 2), ny = (int) curr.getY() + rnd.nextInt(-1, 2);
                // check if it is empty and inside the range
                boolean generalRestrictions = takenPoints[nx][ny] || nx == 0 || nx >= takenPoints.length-1 || ny == 0 || ny >= takenPoints[0].length - 1;
                boolean specificRestrictions = nx > (i + 1) * takenPoints.length / 3 || nx < i* takenPoints.length/3;
                if ( generalRestrictions || specificRestrictions ) {
                    // regenerate the vertices with a random previous vertex
                    curr = houses.get(rnd.nextInt(verticesPerTree *i, houses.size())).getCoords();
                } else {
                    takenPoints[nx][ny] = true;
                    Point2D next = new Point2D(nx, ny);
                    houses.add(new House(next, pictures[1], "aa"));
                    edges.add(new Pair<>(curr, next));
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
        generateVertices();
    }
    public void onCheckDelivery(ActionEvent actionEvent) {
    }

    public void onGenerateTour(ActionEvent actionEvent) {
    }

    public void onViewGraphs(ActionEvent actionEvent) {
    }

    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        running = false;
        stage.close();
    }
}