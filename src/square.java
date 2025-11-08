import java.io.Serializable;

public class square implements Serializable {

    String type;
    int align;                  //0 = "neutral", 1 = "player-1", 2 = "player-2", only for den/trap
                                //player-1 = bottom, player-2 = top
    String current;             //board representation of a square with current occupation
    boolean selectable = false;
    piece piece = null;         //what's the current piece on top of the square

    square(int x, int y){
        if(((x >= 3 && x <= 5) && ((y >= 1 && y <= 2)||(y >= 4 && y <= 5)))){
            type = "river";
            align = 0;
        } else if ((x == 0 && (y == 2 || y == 4)) || (x == 1 && y == 3)) {
            type = "trap(2)";
            align = 2;
        } else if ((x == 8 && (y == 2 || y == 4)) || (x == 7 && y == 3)) {
            type = "trap(1)";
            align = 1;
        } else if (x == 0 && y == 3) {
            type = "den(2)";
            align = 2;
        } else if (x == 8 && y == 3) {
            type = "den(1)";
            align = 1;
        } else{
            type = "normal";
            align = 0;
        }
        current = type;
    }

    public String getType(){return this.type;}

    public String getCurrent(){return this.current;}

    public String getTypeNoAlign(){return this.type.replaceAll("\\(\\d+\\)", "");}

    public piece getPiece(){return this.piece;}

    public int getAlign(){return this.align;}

    public void assignPiece(piece p){piece = p; if(type.equals("normal")){ current = piece.getName(); } else{current = type + " [" + piece.getName() + "]";}}

    public void clearPiece(){piece = null; current = type;}

    public void flipSelectable(){
        this.selectable = !(selectable);
        if(selectable){
            current += "[*]";
        }else{
            current = current.replaceAll("\\[\\*\\]", "");
        }
    }

    public boolean isSelectable() {return selectable;}
}
