package pieces;

import chess.Cell;

import java.util.ArrayList;

public class Empty extends Piece{
    public Empty (String i,int c)
    {
        setId(i);
        setColor(c);
    }
    @Override
    public ArrayList<Cell> move(Cell[][] pos, int x, int y) {
        return new ArrayList<>();
    }
}
