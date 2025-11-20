package model.Animal_chess;

import model.Square;

public class Tiger extends Piece{   
    public Tiger(int p){super("TIGER", p);} //indicates the player#

    public int[] moveTo(Square s, int r, int c, Square[][] b, String d){
        Piece sp = s.getPiece();
        int[] co = {r, c};
        if(s.getType().equals("~~")){   // river
            co = checkRiverMouse(r, c, b, d);
            if(co[0] == -1 && co[1] == -1){
                return new int[]{-1, -1};
            }else{
                sp = b[co[0]][co[1]].getPiece();
            }
        }
        if(s.getType().replaceAll("\\(\\d+\\)", "").equals("()") && this.getAlign() == s.getAlign()){   // "()" -> den
            return new int[]{-1, -1};
        }
        if(sp != null && !(this.eatable(sp))){return new int[]{-1, -1};}
        return new int[]{co[0], co[1]};
    }

    public int[] checkRiverMouse(int r, int c, Square[][] b, String d){
        if(d.equals("up")){
            for(int i = 0; i < 3; i++){
                if(b[r - i][c].getPiece() != null && b[r - i][c].getPiece() instanceof Rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{r - 3, c};
        } else if (d.equals("right")) {
            for(int i = 0; i < 2; i++){
                if(b[r][c + i].getPiece() != null && b[r][c + i].getPiece() instanceof Rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{r, c + 2};
        } else if (d.equals("down")) {
            for(int i = 0; i < 3; i++){
                if(b[r + i][c].getPiece() != null && b[r + i][c].getPiece() instanceof Rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{r + 3, c};
        } else if (d.equals("left")) {
            for(int i = 0; i < 2; i++){
                if(b[r][c - i].getPiece() != null && b[r][c - i].getPiece() instanceof Rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{r, c - 2};
        }else{
            System.out.println("checkRiverMouse Error: No Direction Selected.\n");
            return new int[]{-1, -1};
        }
    }

    public int[] getMove(Square s, int r, int c, String s1){
        if(s.getType().equals("~~")) {
            if (s1.equals("up")) {
                return new int[]{r - 3, c};
            } else if (s1.equals("down")) {
                return new int[]{r + 3, c};
            } else if (s1.equals("left")) {
                return new int[]{r, c - 2};
            } else if (s1.equals("right")) {
                return new int[]{r, c + 2};
            }else{
                return new int[]{-1, -1};
            }
        }
        return new int[]{r, c};
    }
}