package view_and_controller;

import model.Board;
import model.Piece;

public class Replay {
    int mCounter;
    String[] movement;
    Board b;

    Replay(int mc, String[] m){
        mCounter = mc;
        movement = m;
    }

    public void replaying(){
        if(mCounter == 0){
            System.out.println("No Moves In This Game.");
            System.out.println("Replay Ended Here.");
            return;
        }
        b = new Board();
        Piece[] p1 = b.getP1();
        Piece[] p2 = b.getP2();
        int p1n = b.getP1n();
        int p2n = b.getP2n();
        String[] l1 = movement[0].split(" \\| ");
        int player = Integer.parseInt(l1[1]);
        int turn = 0;

        for(int i = 0; i < mCounter; i++){

            turn++;

            String[] split = movement[i].split(" \\| ");
            String targetPiece = split[2];
            String targetEaten = split[3];
            String targetMove = split[4];
            String targetType = split[5];

            Piece piece = null;
            Piece eaten = null;

            int ret = 0;

            if(player == 1){
                if(targetType.equals("MOVE")){
                    for(int j = 0; j < p1n; j++){
                        if(p1[j].getName().equals(targetPiece)){
                            piece = p1[j];
                            break;
                        }
                    }
                    for(int j = 0; j < p2n; j++){
                        if(p2[j].getName().equals(targetEaten)){
                            eaten = p2[j];
                            break;
                        }
                    }
                }else{
                    for(int j = 0; j < p2n; j++){
                        if(p2[j].getName().equals(targetPiece)){
                            piece = p2[j];
                            break;
                        }
                    }
                    for(int j = 0; j < p1n; j++){
                        if(p1[j].getName().equals(targetEaten)){
                            eaten = p1[j];
                            break;
                        }
                    }
                }
            }else{
                if(targetType.equals("MOVE")){
                    for(int j = 0; j < p2n; j++){
                        if(p2[j].getName().equals(targetPiece)){
                            piece = p2[j];
                            break;
                        }
                    }
                    for(int j = 0; j < p1n; j++){
                        if(p1[j].getName().equals(targetEaten)){
                            eaten = p1[j];
                            break;
                        }
                    }
                }else{
                    for(int j = 0; j < p1n; j++){
                        if(p1[j].getName().equals(targetPiece)){
                            piece = p1[j];
                            break;
                        }
                    }
                    for(int j = 0; j < p2n; j++){
                        if(p2[j].getName().equals(targetEaten)){
                            eaten = p2[j];
                            break;
                        }
                    }
                }
            }

            int[] loc = {piece.getPositionR(), piece.getPositionC()};
            if(targetMove.equals("UP")){
                ret = b.simpleMove(piece, loc[0] - 1, loc[1], "UP", targetType, eaten, loc[0], loc[1]);
            } else if (targetMove.equals("DOWN")) {
                ret = b.simpleMove(piece, loc[0] + 1, loc[1], "DOWN", targetType, eaten, loc[0], loc[1]);
            } else if (targetMove.equals("LEFT")) {
                ret = b.simpleMove(piece, loc[0], loc[1] - 1, "LEFT", targetType, eaten, loc[0], loc[1]);
            } else if (targetMove.equals("RIGHT")) {
                ret = b.simpleMove(piece, loc[0], loc[1] + 1, "RIGHT", targetType, eaten, loc[0], loc[1]);
            }

            System.out.println("=============================================================================================================================");
            b.showBoard();
            if(targetType.equals("MOVE")){
                System.out.printf("Player %s - %s (Turn %d): %s move %s, eating %s\n", player, split[0], turn, targetPiece, targetMove, getEatenString(eaten));
            }else{
                System.out.printf("Player %s - %s (Turn %d): %s back to previous step (moving %s), %s is returning\n", player, split[0], turn, targetPiece, targetMove, getEatenString(eaten));
            }
            System.out.println("=============================================================================================================================");
            if(ret == 1){
                System.out.printf("Player %s - %s (Turn %d): reach the opponent den, and is the winner of this game. \n", player, split[0], turn);
                break;
            }
            player = player % 2 + 1;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Program was interrupted while sleeping.");
            }
        }

        System.out.printf("Replay Ended Here. Turn Used: %d Turns, Last Player: Player %d - %s.\n", turn, player, movement[mCounter - 1].split(" \\| ")[0]);
    }

    public String getEatenString(Piece o){
        if(o == null){
            return "Nothing";
        }
        return o.getName();
    }
}
