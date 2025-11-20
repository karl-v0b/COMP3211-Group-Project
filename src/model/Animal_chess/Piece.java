package model.Animal_chess;

import java.io.Serializable;

import model.Animals;
import model.Square;

public class Piece implements Serializable {

    String[] PIECES = {"RAT", "CAT", "DOG", "WOLF", "LEOPARD", "TIGER", "LION", "ELEPHANT"};
    Animals piece;
    String name;                        //board representation of a piece (with align)
    int rank;
    int align;                          //1: player-1, 2:player-2

    boolean trapped;
    int positionR;
    int positionC;

    public Piece(String n, int p){
        piece = Animals.valueOf(n);
        name = n + "(" + p + ")";   //e.g. RAT(1)
        rank = piece.getRank();
        align = p;
        trapped = false;
    }

    //Get variable function
    public Animals getPiece(){return this.piece;}
    public String getName(){return this.name;}
    public String getNameNoAlign(){return this.name.replaceAll("\\(\\d+\\)", "");}
    public int getRank(){return this.rank;}
    public int getAlign(){return this.align;}

    public boolean isTrapped() {return trapped;}
    public void flipTrapped(){this.trapped = !trapped;}

    //position
    public int getPositionR() {return positionR;}
    public void setPositionR(int positionR) {this.positionR = positionR;}
    public int getPositionC() {return positionC;}
    public void setPositionC(int positionC) {this.positionC = positionC;}
    public void assignPosition(int r, int c){this.positionR = r; this.positionC = c;}

    public boolean eatable(Piece p){//whether the chess can eat the target
        if(p.isTrapped()){return true;}
        return (this.align != p.align) && (this.getRank() >= p.getRank());
    }

    public int[] moveTo(Square s, int r, int c, Square[][] b, String direction){//move to new square
        Piece next_square = s.getPiece();
        if(s.getType().replaceAll("\\(\\d+\\)", "").equals("()") && this.getAlign() == s.getAlign()){   
            // "()" -> den
            return new int[]{-1, -1};
        }
        if((next_square != null && !(this.eatable(next_square))) || s.getType().equals("~~")){    // river
            return new int[]{-1, -1};
        }
        return new int[]{r, c};
    }

    public int[] getMove(Square s, int r, int c, String s1){
        return new int[]{r, c};
    }
}
