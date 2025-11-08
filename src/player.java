import java.io.Serializable;

public class player implements Serializable {
    String id;
    int align;      //1: player-1, 2: player-2
    piece[] pieces;
    int pN;

    player(String n, int p, piece[] ps, int psN){
        id = n;
        align = p;
        pieces = ps;
        pN = psN;
    }

    public String getID(){return this.id;}

    public void setID(String s){this.id = s;}

    public int getAlign(){return this.align;}

    public int getPN() {return pN;}

    public piece[] getPieces() {return pieces;}

    public piece getPiece(int i) {return pieces[i];}

    public void showPieces(){
        for(int i = 0; i < pN; i++){
            System.out.printf("%d. %s ", i + 1, pieces[i].getName());
        }
        System.out.println(" ");
    }

    public int findPiece(String s){
        s = s.toUpperCase();
        for(int i = 0; i < pN; i++){
            if(pieces[i].getName().replaceAll("[^a-zA-Z]", "").equals(s)){
                return i;
            }
        }
        return -1;
    }

    public void removePiece(piece p) {
        int i = findPiece(p.getName().replaceAll("[^a-zA-Z]", ""));
        for(int j = i; j < pN - 1; j++){
            pieces[j] = pieces[j + 1];
        }
        pN--;
    }
    public boolean inRange(int i){return (i > 0 && i <= pN);}

    public void addPiece(piece p, int r){
        this.pN++;
        for(int i = pN - 1; i > r; i--){
            pieces[i] = pieces[i--];
        }
        pieces[r] = p;
    }

}
