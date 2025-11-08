public class elephant extends piece{
    elephant(int p){super("ELEPHANT", p);}

    public boolean eatable(piece p){
        if(p.isTrapped()){return true;}
        return (this.align != p.align) && (this.getRank() >= p.getRank() && !(p instanceof rat));
    }
}