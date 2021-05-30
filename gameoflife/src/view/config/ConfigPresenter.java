package view.config;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import model.GameOfLife;

import java.io.File;

/**
 * Kaï Everaerts
 * 15/02/2019
 */
public class ConfigPresenter {
    private ConfigPhaseView view;
    private GameOfLife model;

    public ConfigPresenter(ConfigPhaseView view, GameOfLife model) {
        this.view = view;
        this.model = model;
        view.getBtnStart().setDisable(true);
        view.getBtnReset().setDisable(true);
        addEventListeners();
        updateView();
    }

    private void updateView() {

//        view.getLblCurrentGen().setText("Current generation: " + (model.getCurrentGen() + 1));
        for (int i = 0; i < model.getxSize(); i++) {
            for (int j = 0; j < model.getySize(); j++) {
                setPaneColors(i, j);
            }
        }
        if(!model.getGenCollection().isEmpty()){
            view.setStatus(1, "Current generation: " + (model.getCurrentGen()+1) + " & Current cells alive: " + model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells());
        }

    }

    private void clearPanes() {
        for (Pane node : view.getPanes()) {
            node.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        model.getGenCollection().clear();
        model.addCleanGen();
        model.setCurrentGen(0);
    }

    private void addEventListeners() {
        //menu handlers

        view.getSave().setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("GameOfLife File", "*.gol");
            fileChooser.getExtensionFilters().addAll(ext);
            fileChooser.setSelectedExtensionFilter(ext);
            File file = fileChooser.showSaveDialog(view.getScene().getWindow());
            if (file != null) {
                model.Save(file);
            } else {
                view.setStatus(2, "Saving was cancelled.");
            }
        });
        view.getLoad().setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("GameOfLife File", "*.gol");
            fileChooser.getExtensionFilters().addAll(ext);
            fileChooser.setSelectedExtensionFilter(ext);
            File file = fileChooser.showOpenDialog(view.getScene().getWindow());
            if (file != null) {
                model.Load(file);
                view.getBtnStart().setDisable(false);
                view.getBtnReset().setDisable(false);

                view.setGrid(model.getxSize(), model.getySize());
                view.getSliderxSize().setValue(model.getxSize());
                view.getSliderySize().setValue(model.getySize());
                removePaneHandlers();
                addPaneHandlers();
                updateView();
            } else {
                view.setStatus(2, "Loading was cancelled");
            }
        });
        view.getRules().setOnAction(event -> {
            Alert rules = new Alert(Alert.AlertType.INFORMATION);
            rules.setTitle("Rules");
            rules.setHeaderText("The rules:");
            rules.setContentText("1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.\n\n" +
                    "2. Any live cell with two or three live neighbours lives on to the next generation.\n\n" +
                    "3. Any live cell with more than three live neighbours dies, as if by overpopulation.\n\n" +
                    "4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.");
            rules.show();
        });
        view.getHowToUse().setOnAction(event -> {
            Alert rules = new Alert(Alert.AlertType.INFORMATION);
            rules.setTitle("How To Use");
            rules.setHeaderText("How To Use:");
            rules.setContentText("1. Set the grid size with the sliders and click on 'set grid size'.\n\n" +
                    "2. Click on cells or hold down ctrl and move your mouse to add cells to the grid. Click the cells again or hold down alt to remove the cells from the grid.\n\n" +
                    "3. Click on 'start simulation to start the simulation phase and click on next generation to continue the simulation.'");
            rules.show();
        });
        //main handlers
        view.getSliderxSize().setOnMouseDragged(event -> {
            view.getLblxsliderValue().setText(Integer.toString((int) view.getSliderxSize().getValue()));
            model.setxSize((int) view.getSliderxSize().getValue());
        });
        view.getSliderySize().setOnMouseDragged(event -> {
            view.getLblysliderValue().setText(Integer.toString((int) view.getSliderySize().getValue()));
            model.setySize((int) view.getSliderySize().getValue());
        });
        view.getSliderxSize().setOnMouseClicked(event -> {
            view.getLblxsliderValue().setText(Integer.toString((int) view.getSliderxSize().getValue()));
            model.setxSize((int) view.getSliderxSize().getValue());
        });
        view.getSliderySize().setOnMouseClicked(event -> {
            view.getLblysliderValue().setText(Integer.toString((int) view.getSliderySize().getValue()));
            model.setySize((int) view.getSliderySize().getValue());
        });
        view.getBtnStart().setOnAction(event -> {
            view.getBtnStart().setText("Next Generation");
            view.getBtnVorige().setDisable(false);
            view.getBtnReset().setDisable(false);
            view.getBtnSetGridSize().setDisable(true);
            view.getSliderxSize().setDisable(true);
            view.getSliderySize().setDisable(true);
            runSimulation();
        });
        view.getBtnVorige().setOnAction(event -> {

            if (model.getCurrentGen() > 0) {
                model.setCurrentGen(model.getCurrentGen() - 1);
                updateView();
                if (model.getCurrentGen() == 0) {
                    view.getBtnVorige().setDisable(true);
                }
            } else {
                view.getBtnVorige().setDisable(true);
            }
        });
        view.getBtnSetGridSize().setOnAction(event -> {

            if (view.getSliderySize().getValue() == 0 || view.getSliderxSize().getValue() == 0) {
                view.setStatus(1, "Grid size has 0 for value width or height, please change.");
            } else {
                view.getBtnStart().setDisable(false);
                view.getBtnReset().setDisable(false);

                model.getGenCollection().clear();
                model.addCleanGen();
                model.setCurrentGen(0);
                view.setGrid((int) view.getSliderxSize().getValue(), (int) view.getSliderySize().getValue());
                model.setxSize((int) view.getSliderxSize().getValue());
                model.setySize((int) view.getSliderySize().getValue());
                removePaneHandlers();
                addPaneHandlers();
                view.setStatus(1, "Grid size has been set to: " + model.getxSize() + "x" + model.getySize() + ".");
            }
        });

        view.getBtnReset().setOnAction(event ->
        {
            clearPanes();
            removePaneHandlers();
            view.getBtnStart().setText("Start Simulation");
            view.getBtnVorige().setDisable(true);
            view.getBtnSetGridSize().setDisable(false);
            view.getSliderxSize().setDisable(false);
            view.getSliderySize().setDisable(false);
            addPaneHandlers();
            view.setStatus(1, "Grid has been reset.");
        });

    }

    private void runSimulation() {
        model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells();
        view.setStatus(1, "is running");
        model.addNewGen();
        updateView();
        model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells();
    }

    private void removePaneHandlers() {
        for (Pane nd : view.getPanes()) nd.setOnMouseClicked(null);
    }

    private void setPaneColors(int i, int j) {
        for (Pane nd : view.getPanes()) {
            if (GridPane.getColumnIndex(nd) == i && GridPane.getRowIndex(nd) == j) {
                if (model.getGenCollection().get(model.getCurrentGen()).getCellCollection()[i][j].isAlive()) {
                    nd.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    break;
                } else {
                    nd.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                    break;
                }
            }
        }
    }

    private void addPaneHandlers() {
        for (Pane nd : view.getPanes()) {
            nd.setOnMouseEntered(event -> {
                if (event.isControlDown()) {
                    model.getGenCollection().get(model.getCurrentGen()).getCellCollection()[GridPane.getColumnIndex(nd)][GridPane.getRowIndex(nd)].setAlive(true);
                    view.setStatus(1, "x: " + GridPane.getColumnIndex(nd) + " | y: " + GridPane.getRowIndex(nd) + " is now alive");
                    view.setStatus(1, " (Current generation: " + (model.getCurrentGen()+1) + " & Current cells alive: " + model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells() + ")", true);
                    nd.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else if (event.isAltDown()) {
                    model.getGenCollection().get(model.getCurrentGen()).getCellCollection()[GridPane.getColumnIndex(nd)][GridPane.getRowIndex(nd)].setAlive(false);
                    view.setStatus(1, "x: " + GridPane.getColumnIndex(nd) + " | y: " + GridPane.getRowIndex(nd) + " is now dead");
                    view.setStatus(1, " (Current generation: " + (model.getCurrentGen()+1) + " & Current cells alive: " + model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells() + ")", true);
                    nd.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });
            nd.setOnMouseClicked(event -> {
                if (!model.getGenCollection().get(model.getCurrentGen()).getCellCollection()[GridPane.getColumnIndex(nd)][GridPane.getRowIndex(nd)].isAlive()) {
                    model.getGenCollection().get(model.getCurrentGen()).getCellCollection()[GridPane.getColumnIndex(nd)][GridPane.getRowIndex(nd)].setAlive(true);
                    view.setStatus(1, "x: " + GridPane.getColumnIndex(nd) + " | y: " + GridPane.getRowIndex(nd) + " is now alive");
                    view.setStatus(1, " (Current generation: " + (model.getCurrentGen()+1) + " & Current cells alive: " + model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells() + ")", true);
                    nd.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    model.getGenCollection().get(model.getCurrentGen()).getCellCollection()[GridPane.getColumnIndex(nd)][GridPane.getRowIndex(nd)].setAlive(false);
                    view.setStatus(1, "x: " + GridPane.getColumnIndex(nd) + " | y: " + GridPane.getRowIndex(nd) + " is now dead");
                    view.setStatus(1, " (Current generation: " + (model.getCurrentGen()+1) + " & Current cells alive: " + model.getGenCollection().get(model.getCurrentGen()).countAllCurrentLivingCells() + ")", true);
                    nd.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });
        }
    }
}

