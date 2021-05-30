package view.config;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Kaï Everaerts
 * 8/02/2019
 */
public class ConfigPhaseView extends BorderPane {
    private int btnSize = 150;
    private Label lblSize;
    private Label lblxSize;
    private Label lblySize;
    private Label lblxsliderValue;
    private Label lblysliderValue;
    private Slider sliderxSize;
    private Slider sliderySize;
    private Button btnStart;
    private Button btnVorige;
    private Button btnSetGridSize;
    private Button btnReset;
    private VBox vBox;
    private HBox btnBox;
    private HBox xHBox;
    private HBox yHBox;
    private BorderPane bottomBP;
    private GridPane grid;
    private ArrayList<Pane> panes;
    private HBox statusBar;
    private Label statusText;
    private MenuBar menuBar;
    private Menu file;
    private Menu about;
    private MenuItem save;
    private MenuItem load;
    private MenuItem rules;
    private MenuItem howToUse;


    public ConfigPhaseView() {
        initNodes();
        layoutNodes();
    }

    private void layoutNodes() {
        setTop(menuBar);
        setCenter(grid);
        setBottom(bottomBP);
        bottomBP.setLeft(vBox);
        bottomBP.setBottom(statusBar);
        statusBar.getChildren().addAll(statusText);
        bottomBP.setPadding(new Insets(10));
        grid.setPadding(new Insets(10));
    }

    private void initNodes() {
        this.setPrefSize(230, 180);
        lblSize = new Label("Size:");
        lblxSize = new Label("X Size:");
        lblySize = new Label("Y Size:");
        lblxsliderValue = new Label("");
        lblysliderValue = new Label("");
        sliderxSize = new Slider(0, 50, 0);
        sliderySize = new Slider(0, 50, 0);
        sliderxSize.valueProperty().addListener((obs, oldVal, newVal) -> sliderxSize.setValue(5 * (Math.round(newVal.doubleValue() / 5))));
        sliderySize.valueProperty().addListener((obs, oldVal, newVal) -> sliderySize.setValue(5 * (Math.round(newVal.doubleValue() / 5))));
        sliderxSize.setShowTickLabels(true);
        sliderySize.setShowTickLabels(true);
        btnStart = new Button("Start Simulation");
        btnVorige = new Button("Previous Generation");
        btnSetGridSize = new Button("Set Grid Size");
        btnReset = new Button("Reset");
        btnStart.setMinWidth(btnSize);
        btnVorige.setMinWidth(btnSize);
        btnVorige.setDisable(true);
        save = new MenuItem("Save");
        load = new MenuItem("Load");
        rules = new MenuItem("Rules");
        howToUse = new MenuItem("How To Use");
        file = new Menu("File");
        about = new Menu("About");
        file.getItems().addAll(save, load);
        about.getItems().addAll(rules, howToUse);
        menuBar = new MenuBar(file, about);
        btnSetGridSize.setMinWidth(btnSize);
        btnReset.setMinWidth(btnSize);
        statusBar = new HBox();
        statusText = new Label("Use Control to draw cells and Alt to delete them");
        statusBar.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        statusBar.setBorder((new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2)))));
        statusBar.setPadding(new Insets(5));
        statusBar.setPrefHeight(30);
        vBox = new VBox();
        xHBox = new HBox();
        btnBox = new HBox();
        btnBox.setSpacing(5);
        btnBox.getChildren().addAll(btnVorige, btnStart);
        xHBox.getChildren().addAll(lblxSize, sliderxSize, lblxsliderValue);
        yHBox = new HBox();
        yHBox.getChildren().addAll(lblySize, sliderySize, lblysliderValue);
        vBox.getChildren().addAll(lblSize, xHBox, yHBox, btnBox, btnSetGridSize, btnReset);
        vBox.setSpacing(5);
        bottomBP = new BorderPane();
        BorderPane.setMargin(statusBar, new Insets(10, 0, 0, 0));
        grid = new GridPane();
        panes = new ArrayList<>();
    }

    private void layoutGrid() {
        setCenter(grid);
        setBottom(bottomBP);
    }

    void setGrid(int xSize, int ySize) {
        panes.clear();
        grid = new GridPane();
        for (int i = 0; i < xSize; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / xSize);
            getGrid().getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < ySize; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / ySize);
            getGrid().getRowConstraints().add(rowConst);
        }
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                addPane(i, j);
            }
        }
        grid.setGridLinesVisible(true);
        layoutGrid();
    }

    //TODO Beter schrijven => EventHandler naar Presenter!
    private void addPane(int i, int j) {
        Pane pane = new Pane();
        pane.minHeight(0);
        getGrid().add(pane, i, j);
        panes.add(pane);
    }

    void setStatus(int status, String message) {
        switch (status) {
            case 1:
                statusBar.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                statusText.setText(message);
                break;
            case 2:
                statusBar.setBackground(new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY)));
                statusText.setText(message);
                break;
        }
    }

    void setStatus(int status, String message, boolean add) {
        switch (status) {
            case 1:
                statusBar.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                if (add) {
                    statusText.setText(statusText.getText() + message);
                }
                break;
            case 2:
                statusBar.setBackground(new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY)));
                if (add) {
                    statusText.setText(statusText.getText() + message);
                }
                break;
        }
    }

    Label getLblxsliderValue() {
        return lblxsliderValue;
    }

    Label getLblysliderValue() {
        return lblysliderValue;
    }

    Slider getSliderxSize() {
        return sliderxSize;
    }

    Slider getSliderySize() {
        return sliderySize;
    }

    Button getBtnVorige() {
        return btnVorige;
    }

    Button getBtnStart() {
        return btnStart;
    }

    GridPane getGrid() {
        return grid;
    }

    Button getBtnSetGridSize() {
        return btnSetGridSize;
    }

    Button getBtnReset() {
        return btnReset;
    }

    ArrayList<Pane> getPanes() {
        return panes;
    }

    MenuItem getSave() {
        return save;
    }

    MenuItem getLoad() {
        return load;
    }

    MenuItem getRules() {
        return rules;
    }

    MenuItem getHowToUse() {
        return howToUse;
    }
}
