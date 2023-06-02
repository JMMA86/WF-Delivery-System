package com.wf.wfdeliverysystem.controller;

import com.wf.wfdeliverysystem.Launcher;
import com.wf.wfdeliverysystem.exceptions.*;
import com.wf.wfdeliverysystem.model.*;
import com.wf.wfdeliverysystem.model.Character;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.security.spec.EllipticCurve;
import java.util.*;

public class InitController {

    @FXML
    private Button showInformation;
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
    private RadioButton matrixTB;
    @FXML
    private RadioButton listTB;
    @FXML
    private ToggleGroup typeTG;

    public boolean running;

    // Structures and constants for graphical purposes

    private GraphicsContext context;
    private final int BACKGROUND =0, PATH = 1;
    private final Color[] COLOR_PALETTE = new Color[]{
            Color.web("#89d05b"), // background
            Color.web("#f9cc23") // streets
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
    House selectedHouse, selectedHQ;
    private ArrayList<Street> streets;
    private final int x_dim = 21, y_dim = 9;
    public Stage stage;
    DeliveryCycle cycle;
    private ArrayList<Pair<Point2D, Point2D>> path;
    Affine affine;
    double zoomLevel = 1, preX, preY;
    boolean limitBounds;

    // Init, refresh and close

    public void initialize() throws LoopsNotAllowedException, VertexAlreadyAddedException, MultipleEdgesNotAllowedException, VertexNotFoundException {

        takenPoints = new boolean[x_dim][y_dim];

        // init
        houses = new ArrayList<>();
        context = canvas.getGraphicsContext2D();
        cycle = new DeliveryCycle(diff,  context, new Point2D(takenPoints.length, takenPoints[0].length), pictures[2]);
        cycle.setState(1);
        preX = canvas.getWidth();
        preY = canvas.getHeight();
        limitBounds = true;

        streets = new ArrayList<>();
        rnd = new Random();
        path = new ArrayList<>();

        // System.out.println(diff);

        generateVertices();

        typeTG.selectedToggleProperty().addListener( changeListener -> {
            resetMovements();
            Launcher.getManager().setMatrix(matrixTB.isSelected());
        });

        affine = new Affine();

        double ZOOM_FACTOR = 1.1;
        canvas.setOnScroll(event -> {
            double zoomFactor = event.getDeltaY() > 0 ? ZOOM_FACTOR : 1 / ZOOM_FACTOR;
            double newZoomLevel = zoomLevel * zoomFactor;
            if (newZoomLevel >= 1.0) {
                // System.out.println(preX + " " + preY);
                if(zoomFactor > 1) {
                    preX = event.getX();
                    preY = event.getY();
                    affine.appendScale(zoomFactor, zoomFactor, preX, preY);
                } else {
                    affine = new Affine();
                }
                zoomLevel = newZoomLevel;
            }
        });

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
        context.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        // resizing
        context.setTransform(affine);
        // Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Rectangle2D bounds = new Rectangle2D(0, 0 , stage.getWidth(), stage.getHeight());
        canvas.setWidth(bounds.getWidth() * 0.9);
        canvas.setHeight(bounds.getHeight() * 0.6);

        // labels
        Set<Node> labels = new HashSet<>(stage.getScene().getRoot().lookupAll(".label"));
        labels.addAll( stage.getScene().getRoot().lookupAll(".button") );
        labels.addAll( stage.getScene().getRoot().lookupAll(".radio-button") );
        double textSize = Math.min(bounds.getWidth()*0.01, bounds.getHeight()*0.015);
        for(Node b : labels) {
            b.setStyle(String.format("-fx-font-size: %fpt;", textSize));
        }


        if( canvas.getWidth()/ (x_dim-1) > canvas.getHeight()/ (y_dim-1) ) {
            diff = Math.floor(canvas.getHeight()/ (y_dim-1));
            canvas.setWidth(diff*(x_dim-1));
        } else {
            diff = Math.floor(canvas.getWidth()/ (x_dim-1));
            canvas.setHeight(diff*(y_dim-1));
        }

        double h_padding = (bounds.getWidth() - canvas.getWidth())/2, v_padding = bounds.getHeight()*0.05;
        layout.setPadding( new Insets( v_padding, h_padding, v_padding, h_padding ));
        wfTitleLbl.setStyle( String.format("-fx-font-weight: bold; -fx-font-size: %fpt;", bounds.getHeight()*0.05 ));

        // painting
        context.setFill(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        context.setFill(COLOR_PALETTE[BACKGROUND]);
        context.fillRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), diff/2, diff/2);

        for(Street s : streets) {
            drawLine( s.getP1(), s.getP2(), Color.GRAY , 1);
        }

        for(Pair<Point2D, Point2D> s : path) {
            drawLine( s.getKey(), s.getValue(), COLOR_PALETTE[PATH], 1);
        }

        Pair<Point2D, Point2D> currentEdge = cycle.move();
        if(currentEdge != null) {
            path.add(currentEdge);
            try {
                List<House> toVisit = houses.stream().filter( h -> currentEdge.getKey().equals(h.getCoords()) || currentEdge.getValue().equals(h.getCoords()) ).toList();
                for(House h : toVisit) {
                    h.setState(1);
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }

        for(Character h : houses) h.draw(diff, 0.75);



        for(Street s : streets) {
            fillWeights(s.getP1(), s.getP2(), 0.75, canvas.getHeight()*0.02);
        }

        cycle.draw(diff, 0.75);
        // System.out.println(selectedHQ.getCoords());
    }

    private void generateVertices() throws VertexAlreadyAddedException, LoopsNotAllowedException, MultipleEdgesNotAllowedException, VertexNotFoundException {
        for (int i = 0; i < 3; i++) {
            Point2D curr = new Point2D(
                    rnd.nextInt(1 + i * takenPoints.length / 3, (i + 1) * takenPoints.length / 3 - 1),
                    rnd.nextInt(1, takenPoints[0].length - 1)
            );
            takenPoints[(int) curr.getX()][(int) curr.getY()] = true;
            House currElement = new House(diff, context, curr, pictures[0], String.format("%d,%d", (int)curr.getX(), (int)curr.getY()));
            Launcher.getManager().addHouse(currElement);
            houses.add( currElement);
            if(selectedHQ == null) {
                selectedHQ = houses.get(0);
                selectedHQ.setState(1);
            }
            // repeat until the number of vertices is reached
            int verticesPerTree = 34;
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
                    House newElement = new House(diff, context, next, pictures[1],  String.format("%d,%d", (int)next.getX(), (int)next.getY()));
                    int weight = rnd.nextInt(1, 100);
                    streets.add( new Street(context, currElement.getCoords(), newElement.getCoords(), weight) );
                    houses.add( newElement );
                    Launcher.getManager().addHouse(newElement);
                    Launcher.getManager().addEdge(currElement, newElement, ""+houses.size(), weight);
                }
            }
            int edgeCnt = 0;
            while(edgeCnt < 25) {
                House h1 = houses.get(rnd.nextInt(verticesPerTree *i, houses.size()));
                House h2 = houses.get(rnd.nextInt(verticesPerTree *i, houses.size()));
                if(Math.abs(h1.getCoords().getY() - h2.getCoords().getY()) > 1 || Math.abs(h1.getCoords().getX()-h2.getCoords().getX()) > 1) {
                    continue;
                }
                try {
                    int weight = rnd.nextInt(1, 100);
                    Launcher.getManager().addEdge(h1, h2, ""+houses.size(), weight);
                    streets.add( new Street(context, h1.getCoords(), h2.getCoords(), weight ) );
                    edgeCnt++;
                } catch ( LoopsNotAllowedException | MultipleEdgesNotAllowedException e ) {
                    e.getStackTrace();
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

    private void drawLine(Point2D p1, Point2D p2, Color color, double scale) {
        // border
        context.setStroke(Color.BLACK);
        context.setLineWidth(diff/4*scale);
        context.strokeLine(p1.getX()* diff, p1.getY()* diff, p2.getX()* diff, p2.getY()* diff);
        // gray street
        context.setStroke(color);
        context.setLineWidth(diff/6*scale);
        context.strokeLine(p1.getX()* diff, p1.getY()* diff, p2.getX()* diff, p2.getY()* diff);
        // white dashes
        context.setStroke(Color.web("#dbdbdb"));
        context.setLineWidth(diff/40*scale);
        context.setLineDashes(diff/9*scale);
        context.strokeLine(p1.getX()* diff, p1.getY()* diff, p2.getX()* diff, p2.getY()* diff);
    }

    private void fillWeights(Point2D p1, Point2D p2, double scale, double size) {
        double dx = (p2.getX()*diff - p1.getX()*diff)/3, dy = (p2.getY()*diff - p1.getY()*diff)/3;
        int weight = streets.stream().filter( s -> (s.getP1().equals(p1) && s.getP2().equals(p2)) || (s.getP1().equals(p1) && s.getP2().equals(p1)) ).map( s -> s.getWeight() ).findFirst().get();
        context.setFont(Font.font(weight+"", FontWeight.BOLD, size));
        context.setFill(Color.web("#f30c2c"));
        context.fillText(weight+"" ,(p1.getX()*diff+dx - diff/8*scale), (p1.getY()*diff+dy) + diff/8*scale );
    }


    // Requirement Buttons
    public void onVertexSelected(MouseEvent mouseEvent) {
        resetMovements();
        double currX = mouseEvent.getX()/diff, currY = mouseEvent.getY()/diff;
        Point2D closest = new Point2D((int)Math.round(currX), (int)Math.round(currY));

        // circunference equation (x-h)^2 + (y-k)^2 = r^2

        boolean isInsideCircle = Math.pow(currX*diff - closest.getX()*diff,2) + Math.pow(currY*diff - closest.getY()*diff, 2) <= Math.pow(diff/4,2);

        if(isInsideCircle && takenPoints[(int)closest.getX()][(int)closest.getY()]) {
            try {
                House house = houses.stream().filter( h -> h.getCoords().equals(closest)).findFirst().get();
                if( house.getPicture() == pictures[0] ) {
                    selectedHQ.setState(0);
                    selectedHQ = house;
                    selectedHQ.setState(1);
                } else {
                    if(selectedHouse != null) selectedHouse.setState(0);
                    selectedHouse = house;
                    selectedHouse.setState(1);
                }
            } catch(NoSuchElementException e) {
                e.getStackTrace();
            }
            checkDeliveryBtn.setDisable(false);
        } else {
            checkDeliveryBtn.setDisable(true);
            resetHouses();
        }
    }
    public void onCheckDelivery(ActionEvent actionEvent) throws VertexNotAchievableException, VertexNotFoundException {
        resetMovements();
        if(selectedHouse == null) {
            Launcher.showAlert(Alert.AlertType.ERROR, "Unselected house", "First select a house and try again.");
            return;
        }
        if( !Launcher.getManager().checkPathBetweenHouses(selectedHQ, selectedHouse) ) {
            ArrayList<Pair<House, House>> edges = Launcher.getManager().generateDeliveryTour(selectedHQ);
            edges.removeIf(e -> !Launcher.getManager().checkPathBetweenHouses( selectedHQ, e.getKey() ) );
            path.addAll( edges.stream().map( e -> new Pair<>(e.getKey().getCoords(), e.getValue().getCoords()) ).toList() );
            for(House h : houses.stream().filter( h -> Launcher.getManager().checkPathBetweenHouses(selectedHQ, h)).toList()) {
                h.setState(1);
            }
            Launcher.showAlert(Alert.AlertType.INFORMATION, "Unreachable House", "House is not connected to the current headquarter");
            return;
        }
        ArrayList<Pair<House, House>> edges = Launcher.getManager().calculateMinimumPath(selectedHQ, selectedHouse);
        // path.addAll( edges.stream().map( e -> new Pair<>(e.getKey().getCoords(), e.getValue().getCoords()) ).toList() );
        cycle.setTour(edges, false);
    }

    public void onGenerateTour(ActionEvent actionEvent) throws VertexNotFoundException, VertexNotAchievableException {
        resetMovements();
        resetHouses();
        ArrayList<Pair<House, House>> edges = Launcher.getManager().generateDeliveryTour(selectedHQ);
        edges.removeIf(e -> !Launcher.getManager().checkPathBetweenHouses( selectedHQ, e.getKey() ) );

        // path.addAll( edges.stream().map( e -> new Pair<>(e.getKey().getCoords(), e.getValue().getCoords()) ).toList() );
        cycle.setTour(edges, true);
    }

    public void onViewGraphs(ActionEvent actionEvent) {
        resetMovements();
        Pair<FXMLLoader, Stage> handlers = Launcher.renderView("graph-representations.fxml", 940, 480);
        GraphRepresentationController controller = handlers.getKey().getController();
        controller.initialize(Launcher.getManager().printRepresentation()[0], Launcher.getManager().printRepresentation()[1]);
    }

    private void resetMovements() {
        for(House h : houses) {
            if(h != selectedHQ && h != selectedHouse) {
                h.setState(0);
            }
        }
        path = new ArrayList<>();
        cycle.resetMovement();
    }

    private void resetHouses() {
        if(selectedHouse != null) selectedHouse.setState(0);
        selectedHouse = null;
    }

    public void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage) ( (Node) actionEvent.getSource()).getScene().getWindow();
        running = false;
        stage.close();
    }

    public void onShowInformation(ActionEvent actionEvent) {
        Stage info = new Stage();
        info.setTitle("Delivery System Information");
        TextArea textArea = new TextArea(
                "On the right you will see a Toggle Button to change the way the program calculates the paths, either via adjacency list or matrix. " +
                        "You can select any house or headquarter by clicking it. " +
                        "To unselect a house, click in any place of the canvas.\n\n" +
                        "The three buttons on the left bottom corner allows you to generate paths and see the graph representations, there must be a house selected to check the delivery path.\n\n" +
                        "The window is completely responsive, so you can adjust the size in any way. You can also make scroll to zoom in and out, " +
                        "if you want to see more clearly the weights of the streets.\n\n" +
                        "It is not recommendable to scroll while the path is being generated, because it can cause a bad performance. " +
                        "The minimum supported resolution is 800x600.");
        textArea.setStyle("-fx-text-alignment: justify; -fx-font-size: 14");
        textArea.setWrapText(true);
        textArea.setEditable(false);

        VBox vbox = new VBox(textArea);
        vbox.setPadding(new Insets(16, 16, 16,16));
        VBox.setVgrow(textArea, Priority.ALWAYS);
        info.setScene(new Scene(vbox));
        info.setResizable(false);
        info.setMinWidth(400);
        info.setMinHeight(400);
        info.show();
    }
}