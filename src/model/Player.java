package model;

import java.io.Serializable;

import model.Animal_chess.Piece;

public class Player implements Serializable {
    String id;
    int align;      //1: player-1, 2: player-2
    Piece[] pieces;
    int pN; //current effective number of pieces

    public Player(String n, int p, Piece[] ps, int psN){
        id = n;
        align = p;
        pieces = ps;
        pN = psN;
    }

    public String getID(){return this.id;}

    public void setID(String s){this.id = s;}

    public int getAlign(){return this.align;}

    public int getPN() {return pN;}

    // public Piece[] getPieces() {return pieces;}

    public Piece getPiece(int i) {return pieces[i];}

    public void showPieces(){
        for(int i = 0; i < pN; i++){
            System.out.printf("%d. %s ", i + 1, pieces[i].getName());
        }
        System.out.println(" ");
    }

    public int findPiece(String s){//if not piece, return -1
        s = s.toUpperCase();
        for(int i = 0; i < pN; i++){
            if(pieces[i].getName().replaceAll("[^a-zA-Z]", "").equals(s)){
                return i;
            }
        }
        return -1;
    }

    public void removePiece(Piece p) {
        int i = findPiece(p.getName().replaceAll("[^a-zA-Z]", ""));
        if (i >= 0) {
            for (int j = i; j < pN - 1; j++) {
                pieces[j] = pieces[j + 1];
            }
            pN--;
        }
    }

    public boolean inRange(int i){return (i > 0 && i <= pN);}//check index out of range

    public void addPiece(Piece p, int r){
        this.pN++;
        for(int i = pN - 1; i > r; i--){
            pieces[i] = pieces[i-1];
        }
        pieces[r] = p;
    }

}
