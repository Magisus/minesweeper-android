package hu.ait.android.maggie.minesweeper;

/**
 * Created by Magisus on 2/15/2015.
 */
public class Tile {

    private boolean hasMine;
    private boolean isExpanded;
    private boolean isMarked;

    private int neighborMines;

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean value) {
        isMarked = value;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }

    public int getNeighborMines() {
        if (hasMine) { //no info given for tiles that have a mine
            return -1;
        }
        return neighborMines;
    }

    public void incrementNeighborMines() {
        neighborMines++;
    }

    public void setMine(boolean value) {
        hasMine = value;
    }

    public boolean hasMine() {
        return hasMine;
    }
}
