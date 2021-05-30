package model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Kaï Everaerts
 * 1/02/2019
 */
public class GameOfLife {
    private int xSize;
    private int ySize;
    private int currentGen;
    private ArrayList<Generation> genCollection;

    public GameOfLife() {
        this.xSize = 0;
        this.ySize = 0;
        currentGen = 0;
        genCollection = new ArrayList<>();
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public ArrayList<Generation> getGenCollection() {
        return genCollection;
    }

    public int getCurrentGen() {
        return currentGen;
    }

    public void addNewGen() {
        Generation newGen = new Generation(xSize, ySize);
        newGen.setCellCollection(genCollection.get(currentGen).livesOn());
        genCollection.add(newGen);
        currentGen++;
    }

    public void Save(File file) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(xSize + ";" + ySize + ";" + true);
            writer.newLine();
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    writer.write(x + ";" + y + ";" + getGenCollection().get(getCurrentGen()).getCellCollection()[x][y].isAlive());
                    writer.newLine();
                }
            }
        } catch (IOException ignored) {

        }
    }

    public void Load(File file) {
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            scanner.useDelimiter(";");
            int xVal = 0, yVal = 0;
            int sizeCounter = 0;
            boolean alive;

            String[] str = scanner.nextLine().split(";");
            xSize = Integer.parseInt(str[0]);
            ySize = Integer.parseInt(str[1]);
            Generation gen = new Generation(xSize, ySize);
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    gen.getCellCollection()[x][y] = new Cell(false);
                }
            }
            while (scanner.hasNext()) {
                if(sizeCounter > 0) {
                    String line = scanner.nextLine();
                    String[] strings = line.split(";");
                    xVal = Integer.parseInt(strings[0]);
                    yVal = Integer.parseInt(strings[1]);
                    if (strings[2].equals("true")) {
                        alive = true;
                    } else {
                        alive = false;
                    }
                    Cell cell = new Cell(alive);
                    gen.getCellCollection()[xVal][yVal] = cell;

                }
                sizeCounter++;
            }
            genCollection.clear();
            genCollection.add(gen);
            currentGen = 0;
        } catch (IOException ignored) {
        }
    }

    public void setCurrentGen(int currentGen) {
        this.currentGen = currentGen;
    }

    public void addCleanGen() {
        Generation gen = new Generation(xSize, ySize);
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                gen.getCellCollection()[x][y] = new Cell(false);
            }
        }
        genCollection.add(gen);
        gen.countAllCurrentLivingCells();
    }


}
