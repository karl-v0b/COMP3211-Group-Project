package model.Animal_chess;

import model.Square;

public class Rat extends Piece{
    boolean swim = false;
    public Rat(int p){super("RAT", p);} //indicates the player#

    public boolean eatable(Piece p){
        if(p.isTrapped()){return true;}
        return (this.align != p.align) && ((this.getRank() >= p.getRank()) || (p instanceof Elephant));
    }

    public int[] moveTo(Square s, int r, int c, Square[][] b, String d){
        Piece sp = s.getPiece();
        if (sp != null && (swim != s.getType().equals("~~"))) {     // river
            return new int[]{-1, -1};
        }
        if(s.getType().replaceAll("\\(\\d+\\)", "").equals("()") && this.getAlign() == s.getAlign()){   // "()" -> den
            return new int[]{-1, -1};
        }
        if((sp != null) && !(this.eatable(sp))){
            return new int[]{-1, -1};
        }
        return new int[]{r, c};
    }

    public void flipSwim(){this.swim = !swim;}

    // public boolean getSwim(){ return this.swim;}
}
