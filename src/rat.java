public class rat extends piece{
    boolean swim = false;
    rat(int p){super("RAT", p);}

    public boolean eatable(piece p){
        if(p.isTrapped()){return true;}
        return (this.align != p.align) && ((this.getRank() >= p.getRank()) || (p instanceof elephant));
    }

    public int[] moveTo(square s, int x, int y, square[][] b, String d){
        piece sp = s.getPiece();
        if (sp != null && (swim != s.getType().equals("river"))) {
            return new int[]{-1, -1};
        }
        if(s.getType().replaceAll("\\(\\d+\\)", "").equals("den") && this.getAlign() == s.getAlign()){
            return new int[]{-1, -1};
        }
        if((sp != null) && !(this.eatable(sp))){
            return new int[]{-1, -1};
        }
        return new int[]{x, y};
    }

    public void flipSwim(){this.swim = !swim;}

    public boolean getSwim(){ return this.swim;}
}
