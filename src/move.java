import java.io.Serializable;

public class move implements Serializable {
    player player;
    int align;
    piece piece;
    piece eaten;
    String move;
    String type;

    move(player p1, int a, piece p2, piece e, String m, String t){
        player = p1;
        align = a;
        piece = p2;
        eaten = e;
        move = m;
        type = t;
    }

    public String getPlayer(){
        return this.player.getID();
    }

    public int getAlign() {
        return align;
    }

    public piece getPiece() {
        return piece;
    }

    public String getPieceName(){
        if(this.piece == null){
            return "NULL";
        }
        return this.piece.getName();
    }

    public piece getEaten() {
        return eaten;
    }

    public String getEatenName() {
        if(this.eaten == null){
            return "NULL";
        }
        return this.eaten.getName();
    }

    public String getMove() {
        return move;
    }

    public String getType() {
        return type;
    }

    public String flipMove(){
        if(this.move.equals("UP")){
            return "DOWN";
        } else if (this.move.equals("DOWN")) {
            return "UP";
        } else if (move.equals("LEFT")) {
            return "RIGHT";
        } else if (move.equals("RIGHT")){
            return "LEFT";
        }
        return move;
    }
}
