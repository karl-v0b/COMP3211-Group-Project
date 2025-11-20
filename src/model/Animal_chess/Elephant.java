package model.Animal_chess;

public class Elephant extends Piece{
    public Elephant(int p){super("ELEPHANT", p);}   //indicates the player#

    public boolean eatable(Piece p){
        if(p.isTrapped()){return true;}
        return (this.align != p.align) && (this.getRank() >= p.getRank() && !(p instanceof Rat));
    }
}