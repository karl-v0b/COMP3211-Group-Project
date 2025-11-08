import java.io.Serializable;

public class board implements Serializable {

    piece[] p1 = new piece[8];
    piece[] p2 = new piece[8];
    int p1n = 8;
    int p2n = 8;
    square[][] board = new square[9][7];

    board(){

        p1[0] = new rat(1);
        p1[1] = new cat(1);
        p1[2] = new dog(1);
        p1[3] = new wolf(1);
        p1[4] = new leopard(1);
        p1[5] = new tiger(1);
        p1[6] = new lion(1);
        p1[7] = new elephant(1);

        p2[0] = new rat(2);
        p2[1] = new cat(2);
        p2[2] = new dog(2);
        p2[3] = new wolf(2);
        p2[4] = new leopard(2);
        p2[5] = new tiger(2);
        p2[6] = new lion(2);
        p2[7] = new elephant(2);

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 7; j++){
                board[i][j] = new square(i, j);
            }
        }

        for(int i = 0; i < 8; i++){
            int x = p1[i].getPiece().getP1X();
            int y = p1[i].getPiece().getP1Y();
            board[x][y].assignPiece(p1[i]);
            p1[i].assignPosition(x, y);
        }
        for(int i = 0; i < 8; i++){
            int x = 8 - p2[i].getPiece().getP1X();
            int y = 6 - p2[i].getPiece().getP1Y();
            board[x][y].assignPiece(p2[i]);
            p2[i].assignPosition(x, y);
        }
    }

    public void showBoard(){
        System.out.println(" ");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 7; j++){
                System.out.printf("%20s", board[i][j].getCurrent());
            }
            System.out.println(" ");
            System.out.println(" ");
        }
    }

    public int[][] selectedBoard(int x, int y, piece p){
        square up, right, down, left;
        int[][] temp = new int[4][2];
        int[] fail = {-1, -1};
        if((x - 1) == -1){
            up = null;
            temp[0] = fail;
        }else{
            up = board[x - 1][y];
            temp[0] = p.moveTo(up, x - 1, y, board, "up");
        }

        if((x + 1) == 9){
            down = null;
            temp[1] = fail;
        }else{
            down = board[x + 1][y];
            temp[1] = p.moveTo(down, x + 1, y, board, "down");
        }

        if((y - 1) == -1){
            left = null;
            temp[2] = fail;
        }else{
            left = board[x][y - 1];
            temp[2] = p.moveTo(left, x, y - 1, board, "left");
        }

        if ((y + 1) == 7) {
            right = null;
            temp[3] = fail;
        }else{
            right = board[x][y + 1];
            temp[3] = p.moveTo(right, x, y + 1, board, "right");
        }
        for(int i = 0; i < 4; i++){
            if(temp[i][0] == -1 && temp[i][1] == -1){
                continue;
            }
            board[temp[i][0]][temp[i][1]].flipSelectable();
        }

        return temp;
    }

    public void cleanSelectables(){
        for(int i = 0; i < 9; i ++){
            for(int j = 0 ; j < 7; j++){
                if(board[i][j].isSelectable()){
                    board[i][j].flipSelectable();
                }
            }
        }
    }

    public int change(int xs, int ys, int[] a){

        cleanSelectables();
        int r = 0;

        int xd = a[0];
        int yd = a[1];

        square source = board[xs][ys], destination = board[xd][yd];
        piece sourcePiece = source.getPiece();

        source.clearPiece();
        if(destination.getPiece() != null){
            r = 2;
        }
        destination.assignPiece(sourcePiece);

        sourcePiece.setPositionX(xd);
        sourcePiece.setPositionY(yd);

        if(sourcePiece instanceof rat && source.getType().equals("river")){
            if(!destination.getType().equals("river")){
                ((rat) sourcePiece).flipSwim();
            }
        }

        if(sourcePiece instanceof rat && destination.getType().equals("river")){
            if(!source.getType().equals("river")){
                ((rat) sourcePiece).flipSwim();
            }
        }

        if(source.getType().replaceAll("\\(\\d+\\)", "").equals("trap")){
            if(source.getAlign() != sourcePiece.getAlign()){
                sourcePiece.flipTrapped();
            }
        }

        if(destination.getType().replaceAll("\\(\\d+\\)", "").equals("trap")){
            if(destination.getAlign() != sourcePiece.getAlign()){
                sourcePiece.flipTrapped();
            }
        }

        if(destination.getType().replaceAll("\\(\\d+\\)", "").equals("den")){
            r = 1;
        }

        return r;
    }

    public int revert(move[] ms, int mC, int back, int target){

        String move = ms[target].getMove();
        int x = ms[target].getPiece().getPositionX();
        int y = ms[target].getPiece().getPositionY();
        int[] d = new int[2];
        if(move.equals("UP")){
            move = "down";
            d = ms[target].getPiece().getMove(board[x + 1][y], x + 1, y, move);
        } else if (move.equals("DOWN")) {
            move = "up";
            d = ms[target].getPiece().getMove(board[x - 1][y], x - 1, y, move);
        } else if (move.equals("LEFT")) {
            move = "right";
            d = ms[target].getPiece().getMove(board[x][y + 1], x, y + 1, move);
        } else if (move.equals("RIGHT")){
            move = "left";
            d = ms[target].getPiece().getMove(board[x][y - 1], x, y - 1, move);
        }

        change(x, y, d);

        piece eaten = ms[target].getEaten();
        if(eaten != null){
            board[x][y].assignPiece(eaten);
            return 2;
        }
        return 0;
    }

    public int simpleMove(piece p, int x, int y, String move, String type, piece eaten, int o1, int o2){
        int[] expected = {x, y};
        if((p instanceof lion || p instanceof tiger) && board[x][y].getType().equals("river")){
            if(move.equals("UP")){
                expected[0] = x - 3;
            } else if (move.equals("DOWN")) {
                expected[0] = x + 3;
            } else if (move.equals("LEFT")){
                expected[1] = y - 2;
            } else if (move.equals("RIGHT")) {
                expected[1] = y + 2;
            }
        }

        int xd = expected[0];
        int yd = expected[1];

        square source = board[o1][o2], destination = board[xd][yd];

        source.clearPiece();
        destination.assignPiece(p);

        p.setPositionX(xd);
        p.setPositionY(yd);

        if(type.equals("BACK")){
            if(eaten != null){
                board[eaten.getPositionX()][eaten.getPositionY()].assignPiece(eaten);
            }
        }

        if(destination.getTypeNoAlign().equals("den")){
            return 1;
        }else{
            return 0;
        }
    }

    public piece getPiece(int x, int y){return board[x][y].getPiece();}

    public piece[] getP1() {return p1;}

    public piece[] getP2() {return p2;}

    public int getP1n() {return p1n;}

    public int getP2n() {return p2n;}

    public square[][] getBoard() {return board;}


}
