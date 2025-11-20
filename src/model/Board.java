package model;

import java.io.Serializable;

import model.Animal_chess.Cat;
import model.Animal_chess.Dog;
import model.Animal_chess.Elephant;
import model.Animal_chess.Leopard;
import model.Animal_chess.Lion;
import model.Animal_chess.Piece;
import model.Animal_chess.Rat;
import model.Animal_chess.Tiger;
import model.Animal_chess.Wolf;


public class Board implements Serializable {

    Piece[] p1 = new Piece[8];
    Piece[] p2 = new Piece[8];
    //n = chess#
    int p1n = 8;
    int p2n = 8;
    Square[][] boardMap = new Square[9][7]; //for the map

    public Board(){
        //creating chess for two players, p for player#
        p1[0] = new Rat(1);
        p1[1] = new Cat(1);
        p1[2] = new Dog(1);
        p1[3] = new Wolf(1);
        p1[4] = new Leopard(1);
        p1[5] = new Tiger(1);
        p1[6] = new Lion(1);
        p1[7] = new Elephant(1);

        p2[0] = new Rat(2);
        p2[1] = new Cat(2);
        p2[2] = new Dog(2);
        p2[3] = new Wolf(2);
        p2[4] = new Leopard(2);
        p2[5] = new Tiger(2);
        p2[6] = new Lion(2);
        p2[7] = new Elephant(2);
        
        for(int i = 0; i < 9; i++){//create the map
            for(int j = 0; j < 7; j++){
                boardMap[i][j] = new Square(i, j);
            }
        }

        //getting both player's chesses inplace, piece is also Animals
        for(int i = 0; i < 8; i++){//r= row# and c = column#
            int r = p1[i].getPiece().getP1R();
            int c = p1[i].getPiece().getP1C();
            boardMap[r][c].assignPiece(p1[i]);
            p1[i].assignPosition(r, c);
        }
        for(int i = 0; i < 8; i++){//mirror the position for p2's chess
            int r = 8 - p2[i].getPiece().getP1R();
            int c = 6 - p2[i].getPiece().getP1C();
            boardMap[r][c].assignPiece(p2[i]);
            p2[i].assignPosition(r, c);
        }
    }

    public void showBoard(){//display the board
        System.out.println("┌────────────────┬────────────────┬────────────────┬────────────────┬────────────────┬────────────────┬────────────────┐");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 7; j++){
                String s = boardMap[i][j].getCurrent();
                s += "        ".substring(0,(16-s.length())/2);
                System.out.printf("│%16s", s);
            }
            System.out.println("│");
            if (i < 8)
                System.out.println("├────────────────┼────────────────┼────────────────┼────────────────┼────────────────┼────────────────┼────────────────┤");
        }
        System.out.println("└────────────────┴────────────────┴────────────────┴────────────────┴────────────────┴────────────────┴────────────────┘");
    }

    public int[][] selectedBoard(int r, int c, Piece p){
        Square up, right, down, left;   //refers to four direction on the board
        int[][] temp = new int[4][2];   // [] for directions, [][] for positions
        int[] fail = {-1, -1};
        if((r - 1) == -1){
            up = null;
            temp[0] = fail;
        }else{
            up = boardMap[r - 1][c];
            temp[0] = p.moveTo(up, r - 1, c, boardMap, "up");
        }

        if((r + 1) == 9){
            down = null;
            temp[1] = fail;
        }else{
            down = boardMap[r + 1][c];
            temp[1] = p.moveTo(down, r + 1, c, boardMap, "down");
        }

        if((c - 1) == -1){
            left = null;
            temp[2] = fail;
        }else{
            left = boardMap[r][c - 1];
            temp[2] = p.moveTo(left, r, c - 1, boardMap, "left");
        }

        if ((c + 1) == 7) {
            right = null;
            temp[3] = fail;
        }else{
            right = boardMap[r][c + 1];
            temp[3] = p.moveTo(right, r, c + 1, boardMap, "right");
        }
        for(int i = 0; i < 4; i++){
            if(temp[i][0] == -1 && temp[i][1] == -1){
                continue;
            }
            boardMap[temp[i][0]][temp[i][1]].flipSelectable();
        }

        return temp;
    }

    public void cleanSelectables(){
        for(int i = 0; i < 9; i ++){
            for(int j = 0 ; j < 7; j++){
                if(boardMap[i][j].isSelectable()){
                    boardMap[i][j].flipSelectable();
                }
            }
        }
    }

    public int change(int rs, int cs, int[] a){// rs, cs refers to source position

        cleanSelectables();
        int state = 0;  //0 = normal movement, 1 = enter den, 2 = eat something
        //destination
        int rd = a[0];  int cd = a[1];

        Square source = boardMap[rs][cs], destination = boardMap[rd][cd];
        Piece sourcePiece = source.getPiece();

        //if there is piece in the destination, assign source to it
        source.clearPiece();
        if(destination.getPiece() != null){
            state = 2;
        }
        destination.assignPiece(sourcePiece);
        sourcePiece.setPositionR(rd);
        sourcePiece.setPositionC(cd);

        //check rat in river and flip its swim
        if(sourcePiece instanceof Rat && source.getType().equals("~~")){    //if rat get out of river
            if(!destination.getType().equals("~~")){
                ((Rat) sourcePiece).flipSwim();
            }
        }
        if(sourcePiece instanceof Rat && destination.getType().equals("~~")){   //if rat get in river
            if(!source.getType().equals("~~")){
                ((Rat) sourcePiece).flipSwim();
            }
        }

        //check got trapped or not
        if(source.getType().replaceAll("\\(\\d+\\)", "").equals("<>")){//if get out of the trap
            if(source.getAlign() != sourcePiece.getAlign()){
                sourcePiece.flipTrapped();
            }
        }
        if(destination.getType().replaceAll("\\(\\d+\\)", "").equals("<>")){//if get in the trap
            if(destination.getAlign() != sourcePiece.getAlign()){
                sourcePiece.flipTrapped();
            }
        }

        if(destination.getType().replaceAll("\\(\\d+\\)", "").equals("()")){    //check get in the den
            state = 1;
        }

        return state;
    }

    public int revert(Move[] m, int mC, int back, int target){//revert the move
        String move_record = m[target].getMove();
        int r = m[target].getPiece().getPositionR();
        int c = m[target].getPiece().getPositionC();
        int[] d = new int[2];
        if(move_record.equals("UP")){
            move_record = "down";
            d = m[target].getPiece().getMove(boardMap[r + 1][c], r + 1, c, move_record);
        } else if (move_record.equals("DOWN")) {
            move_record = "up";
            d = m[target].getPiece().getMove(boardMap[r - 1][c], r - 1, c, move_record);
        } else if (move_record.equals("LEFT")) {
            move_record = "right";
            d = m[target].getPiece().getMove(boardMap[r][c + 1], r, c + 1, move_record);
        } else if (move_record.equals("RIGHT")){
            move_record = "left";
            d = m[target].getPiece().getMove(boardMap[r][c - 1], r, c - 1, move_record);
        }

        change(r, c, d);

        Piece eaten = m[target].getEaten();
        if(eaten != null){
            boardMap[r][c].assignPiece(eaten);
            return 2;
        }
        return 0;
    }

    public int simpleMove(Piece p, int r, int c, String move, String type, Piece eaten, int o1, int o2){
        int[] expected = {r, c};
        if((p instanceof Lion || p instanceof Tiger) && boardMap[r][c].getType().equals("~~")){    //lion or tiger jump river
            if(move.equals("UP")){
                expected[0] = r - 3;
            } else if (move.equals("DOWN")) {
                expected[0] = r + 3;
            } else if (move.equals("LEFT")){
                expected[1] = c - 2;
            } else if (move.equals("RIGHT")) {
                expected[1] = c + 2;
            }
        }

        int rd = expected[0];
        int cd = expected[1];

        Square source = boardMap[o1][o2], destination = boardMap[rd][cd];

        source.clearPiece();
        destination.assignPiece(p);
        p.setPositionR(rd);
        p.setPositionC(cd);

        if(type.equals("BACK")){//if something got eaten before
            if(eaten != null){
                boardMap[eaten.getPositionR()][eaten.getPositionC()].assignPiece(eaten);
            }
        }

        if(destination.getTypeNoAlign().equals("()")){  //if get into den
            return 1;
        }else{ 
            return 0;
        }
    }

    public Piece getPiece(int r, int c){return boardMap[r][c].getPiece();}

    public Piece[] getP1() {return p1;}

    public Piece[] getP2() {return p2;}

    public int getP1n() {return p1n;}

    public int getP2n() {return p2n;}

    public Square[][] getBoard() {return boardMap;}


}
