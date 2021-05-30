package model;

/**
 * Kaï Everaerts
 * 1/02/2019
 */
public class Generation {
    private Cell[][] cellCollection;
    private Cell[][] nextCellCollection;
    private int cellsCurrentlyAlive = 0;
    private int size;
    private int xSize, ySize;

    public Generation(int xSize, int ySize) {
        size = xSize*ySize;
        this.xSize = xSize;
        this.ySize = ySize;
        cellCollection = new Cell[xSize][ySize];
    }

    public Cell[][] getCellCollection() {
        return cellCollection;
    }

    Cell[][] livesOn() {
        int liveCounter = 0;
        nextCellCollection = new Cell[xSize][ySize];
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < ySize; j++){
                nextCellCollection[i][j] = new Cell(false);
            }
        }

        //makes a counter of the amount of live cells surrounding the current cell
        for (int x = 0; x < cellCollection.length; x++) {
            for (int y = 0; y < cellCollection[x].length; y++) {

                int liveNeighbourCount = 0;
                for (int column = -1; column <= 1; column++){
                    for (int row = -1; row <= 1; row++){
                        try {
                            if (cellCollection[x+column][y+row].isAlive()){
                                liveNeighbourCount++;
                            }
                        } catch (ArrayIndexOutOfBoundsException e){
                            /*Catch when out of bounds*/
                        } catch (NullPointerException e){
                            /*Catch when pointing towards a null cell*/
                        }
                    }
                }
                if (cellCollection[x][y].isAlive()) {
                    liveNeighbourCount -= 1;
                }

                /*applies the rules to see if a cell should live on or die*/
                /*Cell is lonely and dies*/
                if ((cellCollection[x][y].isAlive()) && (liveNeighbourCount < 2)) {
                    nextCellCollection[x][y].setAlive(false);
                }
                /*Cell dies due to overpopulation*/
                else if ((cellCollection[x][y].isAlive()) && (liveNeighbourCount > 3)) {
                    nextCellCollection[x][y].setAlive(false);
                }
                /*A new cell is born*/
                else if ((!cellCollection[x][y].isAlive()) && (liveNeighbourCount == 3)) {
                    nextCellCollection[x][y].setAlive(true);
                }
                /*Cell remains the same like in previous gen*/
                else nextCellCollection[x][y] = cellCollection[x][y];
            }
            countAllCurrentLivingCells();
        }
        return nextCellCollection;
    }


    int countAlive(int x, int y) {
        if (cellCollection[x][y].isAlive()) {
            return 1;
        } else return 0;
    }

    public int countAllCurrentLivingCells() {
        cellsCurrentlyAlive = 0;
        for (int x = 0; x < cellCollection.length; x++) {
            for (int y = 0; y < cellCollection[x].length; y++) {
                cellsCurrentlyAlive += countAlive(x, y);
            }
        }
        return cellsCurrentlyAlive;
    }

    public int getCellsCurrentlyAlive() {
        return this.cellsCurrentlyAlive;
    }

    public void setCellCollection(Cell[][] cellCollection) {
        this.cellCollection = cellCollection;
    }
}
