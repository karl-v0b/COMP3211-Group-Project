import java.io.Serializable;

public class piece implements Serializable {

    String[] PIECES = {"RAT", "CAT", "DOG", "WOLF", "LEOPARD", "TIGER", "LION", "ELEPHANT"};
    //with ranking order
    Animals piece;
    String name;                        //board representation of a piece (with align)
    int rank;
    int align;                          //1: player-1, 2:player-2

    boolean trapped;
    int positionX;
    int positionY;

    piece(String n, int p){
        piece = Animals.valueOf(n);
        name = n + "(" + p + ")";
        rank = piece.getRank();
        align = p;
        trapped = false;
    }

    public Animals getPiece(){return this.piece;}

    public String getName(){return this.name;}

    public String getNameNoAlign(){return this.name.replaceAll("\\(\\d+\\)", "");}

    public int getRank(){return this.rank;}

    public int getAlign(){return this.align;}

    public boolean isTrapped() {return trapped;}

    public void flipTrapped(){this.trapped = !trapped;}

    public int getPositionX() {return positionX;}

    public void setPositionX(int positionX) {this.positionX = positionX;}

    public int getPositionY() {return positionY;}

    public void setPositionY(int positionY) {this.positionY = positionY;}

    public void assignPosition(int x, int y){this.positionX = x; this.positionY = y;}

    public boolean eatable(piece p){
        if(p.isTrapped()){return true;}
        return (this.align != p.align) && (this.getRank() >= p.getRank());
    }

    public int[] moveTo(square s, int x, int y, square[][] b, String d){
        piece sp = s.getPiece();
        if(s.getType().replaceAll("\\(\\d+\\)", "").equals("den") && this.getAlign() == s.getAlign()){
            return new int[]{-1, -1};
        }
        if((sp != null && !(this.eatable(sp))) || s.getType().equals("river")){
            return new int[]{-1, -1};
        }
        return new int[]{x, y};
    }

    public int[] getMove(square s, int x, int y, String s1){
        return new int[]{x, y};
    }
}
