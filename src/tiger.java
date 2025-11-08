public class tiger extends piece{
    tiger(int p){super("TIGER", p);}

    public int[] moveTo(square s, int x, int y, square[][] b, String d){
        piece sp = s.getPiece();
        int[] co = {x, y};
        if(s.getType().equals("river")){
            co = checkRiverMouse(x, y, b, d);
            if(co[0] == -1 && co[1] == -1){
                return new int[]{-1, -1};
            }else{
                sp = b[co[0]][co[1]].getPiece();
            }
        }
        if(s.getType().replaceAll("\\(\\d+\\)", "").equals("den") && this.getAlign() == s.getAlign()){
            return new int[]{-1, -1};
        }
        if(sp != null && !(this.eatable(sp))){return new int[]{-1, -1};}
        return new int[]{co[0], co[1]};
    }

    public int[] checkRiverMouse(int x, int y, square[][] b, String d){
        if(d.equals("up")){
            for(int i = 0; i < 3; i++){
                if(b[x - i][y].getPiece() != null && b[x - i][y].getPiece() instanceof rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{x - 3, y};
        } else if (d.equals("right")) {
            for(int i = 0; i < 2; i++){
                if(b[x][y + i].getPiece() != null && b[x][y + i].getPiece() instanceof rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{x, y + 2};
        } else if (d.equals("down")) {
            for(int i = 0; i < 3; i++){
                if(b[x + i][y].getPiece() != null && b[x + i][y].getPiece() instanceof rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{x + 3, y};
        } else if (d.equals("left")) {
            for(int i = 0; i < 2; i++){
                if(b[x][y - i].getPiece() != null && b[x][y - i].getPiece() instanceof rat){
                    return new int[]{-1, -1};
                }
            }
            return new int[]{x, y - 2};
        }else{
            System.out.println("checkRiverMouse Error: No Direction Selected.\n");
            return new int[]{-1, -1};
        }
    }

    public int[] getMove(square s, int x, int y, String s1){
        if(s.getType().equals("river")) {
            if (s1.equals("up")) {
                return new int[]{x - 3, y};
            } else if (s1.equals("down")) {
                return new int[]{x + 3, y};
            } else if (s1.equals("left")) {
                return new int[]{x, y - 2};
            } else if (s1.equals("right")) {
                return new int[]{x, y + 2};
            }
        }
        return new int[]{x, y};
    }
}