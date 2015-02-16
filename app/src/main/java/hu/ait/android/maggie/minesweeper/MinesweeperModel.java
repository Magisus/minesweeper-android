package hu.ait.android.maggie.minesweeper;

import java.util.Random;

/**
 * Created by Magisus on 2/15/2015.
 */
public class MinesweeperModel {

    private static MinesweeperModel instance = null;

    public static MinesweeperModel getInstance(){
        if (instance == null){
            instance = new MinesweeperModel();
        }
        return instance;
    }

    public static final int GRID_WIDTH = 10;

    public static final int MINE_COUNT = 10;

    public static final int[][] OFFSETS = {{-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    private Tile[][] grid;
    private Random random;

    private MinesweeperModel() {
        random = new Random();
        grid = new Tile[GRID_WIDTH][GRID_WIDTH];
        initializeGrid();
        setUpMinefield();
    }

    public int[][] getGrid() {
        return gridAsNumbers();
    }

    private int[][] gridAsNumbers() {
        int[][] numbers = new int[GRID_WIDTH][GRID_WIDTH];
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (grid[i][j].isExpanded()) {
                    numbers[i][j] = grid[i][j].getNeighborMines();
                } else {
                    numbers[i][j] = -1;
                }
            }
        }
        return numbers;
    }

    public boolean expanded(int row, int col) {
        return grid[row][col].isExpanded();
    }

    public void toggleMine(int row, int col) {
        grid[row][col].setMarked(!grid[row][col].isMarked());
    }

    public boolean allMinesMarked() {
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (grid[i][j].hasMine() && !grid[i][j].isMarked()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void expand(int row, int col) {
        if (grid[row][col].hasMine()) {
            return;
        }
        //Expand this tile
        grid[row][col].setExpanded(true);
        //Expand each neighbor if there are no real or suspected mines adjacent to this tile
        if (grid[row][col].getNeighborMines() == 0 && !hasMarkedNeighbors(row, col)) {
            for (int i = 0; i < OFFSETS.length; i++) {
                int nRow = row + OFFSETS[i][0];
                int nCol = col + OFFSETS[i][1];
                if (onGrid(nRow, nCol) && !grid[nRow][nCol].isExpanded()) {
                    expand(nRow, nCol);
                }
            }
        }
    }

    private boolean onGrid(int row, int col){
        return row >= 0 && row < GRID_WIDTH && col >= 0 && col < GRID_WIDTH;
    }

    private boolean hasMarkedNeighbors(int row, int col) {
        for (int i = 0; i < OFFSETS.length; i++) {
            int nRow = row + OFFSETS[i][0];
            int nCol = col + OFFSETS[i][1];
            if (onGrid(nRow, nCol)) {
                if (grid[nRow][nCol].isMarked()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setUpMinefield() {
        int mines = 0;
        while (mines < MINE_COUNT) {
            int loc = random.nextInt(GRID_WIDTH * GRID_WIDTH);
            int row = loc / 10;
            int col = loc % 10;
            if (!grid[row][col].hasMine()) {
                grid[row][col].setMine(true);
                updateNeighbors(row, col);
                mines++;
            }
        }
    }

    private void updateNeighbors(int row, int col) {
        for (int i = 0; i < OFFSETS.length; i++) {
            int nRow = row + OFFSETS[i][0];
            int nCol = col + OFFSETS[i][1];
            if (nRow >= 0 && nRow < GRID_WIDTH && nCol >= 0 && nCol < GRID_WIDTH) {
                grid[row + OFFSETS[i][0]][col + OFFSETS[i][1]].incrementNeighborMines();
            }
        }
    }

    private void initializeGrid(){
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                grid[i][j] = new Tile();
            }
        }
    }

    public void resetModel(){
        initializeGrid();
        setUpMinefield();
    }
}
