import java.io.*;
import java.util.Scanner;

public class game implements Serializable {
    board b;
    player[] players = new player[2];
    move[] moves = new move[1024];
    int mCounter = 0;
    int back = 3;
    int current;
    boolean cont = true;
    int count = 1;
    transient Scanner s;

    game(){
        b = new board();
        players[0] = new player("p-1", 1, b.getP1(), b.getP1n());
        players[1] = new player("p-2", 2, b.getP2(), b.getP2n());
        current = (int)(Math.random() * 2);
    }

    public String[] process(){

        s = new Scanner(System.in);

        if(count == 1){
            System.out.println("=============================================================================================================================");
            System.out.printf("%s (Player %d) goes first!\n", players[current].getID(), players[current].getAlign());
            System.out.println("=============================================================================================================================");
        }else{
            System.out.println("=============================================================================================================================");
            System.out.printf("%s (Player %d) resume the game!\n", players[current].getID(), players[current].getAlign());
            System.out.println("=============================================================================================================================");
        }


        while(cont){
            player currentPlayer = players[current];
            String pid = players[current].getID();
            int align = players[current].getAlign();
            int target;
            int p = -1;
            int[] sourceArray = new int[2];
            int[] targetArray = new int[2];
            int ret = 0;

            boolean flag = true;
            boolean terminated = false;

            do{
                target = select(currentPlayer, align, pid);
                if(target == -16){
                    continue;
                } else if (target == -8) {
                    break;
                } else if (target == -32) {
                    terminated = true;
                    break;
                }
                while(flag){
                    p = possible(currentPlayer, align, pid, target, targetArray, sourceArray);
                    if(p == -1){
                        break;
                    }
                    int c = confirm(align, pid);
                    if(c == 1){
                        flag = false;
                    }
                }
            }while(flag);

            if(terminated){return new String[]{"none", "none"};}

            if(p >= 0) {

                piece source = b.getPiece(sourceArray[0], sourceArray[1]);

                piece replaced = b.getPiece(targetArray[0], targetArray[1]);

                ret = b.change(currentPlayer.getPiece(target).getPositionX(), currentPlayer.getPiece(target).getPositionY(), targetArray);
                if(ret == 2){
                    players[1 - current].removePiece(replaced);
                }

                if(p == 1){
                    moves[mCounter] = new move(currentPlayer, align, source, replaced, "UP", "MOVE");
                } else if (p == 2) {
                    moves[mCounter] = new move(currentPlayer, align, source, replaced,"DOWN", "MOVE");
                } else if (p == 3) {
                    moves[mCounter] = new move(currentPlayer, align, source, replaced,"LEFT", "MOVE");
                } else if (p == 4) {
                    moves[mCounter] = new move(currentPlayer, align, source, replaced,"RIGHT", "MOVE");
                }

            }else{
                int t1 = -1;
                int valid = 0;
                piece added = null;
                for(int i = mCounter - 1; i >= 0; i--) {
                    if (moves[i].getType().equals("MOVE") && valid == 0) {
                        t1 = i;
                        break;
                    } else if (moves[i].getType().equals("BACK")) {
                        valid++;
                    } else if (moves[i].getType().equals("MOVE")){
                        valid--;
                    }
                }
                piece source = moves[t1].getPiece();
                ret = b.revert(moves, mCounter, back, t1);
                if(ret == 2){
                    int a1 = moves[t1].getEaten().getAlign();
                    players[a1 - 1].addPiece(moves[t1].getEaten(), moves[t1].getEaten().getRank());
                    added = moves[t1].getEaten();
                }
                back--;
                moves[mCounter] = new move(currentPlayer, align, source, added, moves[t1].flipMove(), "BACK");
            }

            mCounter++;

            System.out.println("=============================================================================================================================");

            if(ret == 1 || players[1 - current].getPN() == 0){
                cont = false;
                while(true){
                    System.out.println("The Game Has Ended! Record This Game? [Y/N] or [1(for Y)/2(for N)]");
                    String receive = s.nextLine();
                    try{
                        int x = Integer.parseInt(receive);
                        if(x == 1){
                            record(align, pid);
                        }else if(x == 2){
                            break;
                        }else{
                            System.out.println("Input Not Match. Try Again.");
                        }
                    }catch (NumberFormatException e){
                        receive = receive.toUpperCase();
                        if(receive.equals("Y")){
                            int r1 = record(align, pid);
                            if(r1 == 1){
                                break;
                            }
                        }else if(receive.equals("N")){
                            break;
                        }else{
                            System.out.println("Input Not Match. Try Again.");
                        }
                    }
                }
            }else{
                current = 1 - current;
                count++;
            }
        }
        return new String[]{String.valueOf(current + 1), players[current].getID()};
    }

    public void namePlayer(){
        System.out.println("=============================================================================================================================");
        System.out.println("Select how to name your player: (By Using \"Number\" or \"Word in []\")");
        System.out.println("1. Input Name [INPUT]");
        System.out.println("2. Random Name [RANDOM]");
        System.out.println("=============================================================================================================================");
        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        System.out.println("=============================================================================================================================");
        try {
            int x = Integer.parseInt(str);
            if(x == 1){
                System.out.println("Input First(1st) Player Name:");
                String str2 = s.nextLine();
                this.players[0].setID(str2);
                System.out.println("Input Second(2nd) Player Name:");
                String str3 = s.nextLine();
                this.players[1].setID(str3);
            }else if(x == 2){
                String[][] temp = {{"Noah", "Oliver", "Arthur"},{"Olivia", "Amelia", "Lily"}};
                int p = (int)(Math.random() * 2);
                this.players[0].setID(temp[p][(int)(Math.random() * 3)]);
                this.players[1].setID(temp[1 - p][(int)(Math.random() * 3)]);
            }else{
                System.out.println("Integer Out Of Range. Try Again.");
            }
        } catch (NumberFormatException e) {
            str = str.toUpperCase();
            if(str.equals("INPUT")){
                System.out.println("Input First(1st) Player Name:");
                String str2 = s.nextLine();
                this.players[0].setID(str2);
                System.out.println("Input Second(2nd) Player Name:");
                String str3 = s.nextLine();
                this.players[0].setID(str3);
            }else if(str.equals("RANDOM")){
                String[][] temp = {{"Noah", "Oliver", "Arthur"},{"Olivia", "Amelia", "Lily"}};
                int p = (int)(Math.random() * 2);
                this.players[0].setID(temp[p][(int)(Math.random() * 3)]);
                this.players[1].setID(temp[1 - p][(int)(Math.random() * 3)]);
            }else{
                System.out.println("Option Not Available. Try Again.");
            }
        }

    }

    public int select(player currentPlayer, int align, String pid){
        while(true){
            b.cleanSelectables();
            b.showBoard();
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Select the piece you want to move:\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Possible pieces: ", count, align, pid);
            currentPlayer.showPieces();
            System.out.printf("Turn %d (Player %d, %s): Accept Input: 1(Number of Item) or RAT(Name of Item, Case Insensitive) ...\n", count, align, pid);
            System.out.println(" ");
            System.out.printf("Turn %d (Player %d, %s): Alternative, Select one of the action option below:\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Possible option: A. END, B. STATUS, C. BACK, D. RECORD, E. SAVE\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: A(Character of Item) or END(Name of Item, Case Insensitive) ...\n", count, align, pid);
            System.out.println("=============================================================================================================================");
            String str = s.nextLine();
            System.out.println("=============================================================================================================================");
            try {
                int x = Integer.parseInt(str);
                if(!(currentPlayer.inRange(x))){
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }else{
                    System.out.printf("Turn %d (Player %d, %s): %d. %s is selected.\n", count, align, pid, x, currentPlayer.getPiece(x - 1).getName());
                    return x - 1;
                }
            } catch (NumberFormatException e) {
                int x = currentPlayer.findPiece(str);
                if(x == -1){
                    str = str.toUpperCase();
                    if(str.equals("A") || str.equals("END")){
                        int exid = exitConfirm(align, pid);
                        if(exid == 1){
                            return -32;
                        }
                    }else if(str.equals("B") || str.equals("STATUS")){
                        showStatus(count);
                        return -16;
                    } else if (str.equals("C") || str.equals("BACK")) {
                        if(back <= 0){
                            System.out.printf("Turn %d (Player %d, %s): No More Back Steps Left in This Current Game.\n", count, align, pid);
                        }else{
                            boolean flag = false;
                            int valid = 0;
                            for(int i = mCounter - 1; i >= 0; i--) {
                                if (moves[i].getType().equals("MOVE") && valid == 0) {
                                    flag = true;
                                } else if (moves[i].getType().equals("BACK")) {
                                    valid++;
                                } else if (moves[i].getType().equals("MOVE")){
                                    valid--;
                                }
                            }
                            if(flag){
                                int bid = backConfirm(align, pid);
                                if(bid == 1){
                                    return -8;
                                }
                            }else{
                                System.out.printf("Turn %d (Player %d, %s): No Valid Back Steps Left.\n", count, align, pid);
                            }
                        }
                    } else if (str.equals("D") || str.equals("RECORD")) {
                        int r = record(align, pid);
                        if(r == 1){
                            return -16;
                        }
                    } else if (str.equals("E") || str.equals("SAVE")) {
                        int s = save(align, pid);
                        if(s == 1){
                            return -16;
                        }
                    } else{
                        System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                    }
                }else{
                    System.out.printf("Turn %d (Player %d, %s): %d. %s is selected.\n", count, align, pid, x + 1, currentPlayer.getPiece(x).getName());
                    return x;
                }
            }
        }
    }

    public int possible(player currentPlayer, int align, String pid, int target, int[] targetArray, int[] sourceArray){

        while(true){
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Possible Movement: (Space with \"[*]\")\n", count, align, pid);
            b.cleanSelectables();
            int[][] sb = b.selectedBoard(currentPlayer.getPiece(target).getPositionX(), currentPlayer.getPiece(target).getPositionY(), currentPlayer.getPiece(target));
            sourceArray[0] = currentPlayer.getPiece(target).getPositionX();
            sourceArray[1] = currentPlayer.getPiece(target).getPositionY();
            b.showBoard();

            System.out.printf("Turn %d (Player %d, %s): Accept Input: 1. UP(up) / 2. DOWN(down) / 3. (LEFT)left / 4. (RIGHT)right / 5. (RETURN) Return [To Previous]\n", count, align, pid);
            System.out.println("=============================================================================================================================");
            String str2 = s.nextLine();
            str2 = str2.toUpperCase();
            System.out.println("=============================================================================================================================");
            try {
                int x = Integer.parseInt(str2);
                if(x >= 1 && x <= 4){
                    if(sb[x - 1][0] == -1 && sb[x - 1][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", count, align, pid);
                        continue;
                    }
                    targetArray[0] = sb[x - 1][0];
                    targetArray[1] = sb[x - 1][1];
                    return x;
                } else if(x == 5){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str2.equals("UP")){
                    if(sb[0][0] == -1 && sb[0][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", count, align, pid);
                        continue;
                    }
                    targetArray[0] = sb[0][0];
                    targetArray[1] = sb[0][1];
                    return 1;
                } else if (str2.equals("DOWN")) {
                    if(sb[1][0] == -1 && sb[1][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", count, align, pid);
                        continue;
                    }
                    targetArray[0] = sb[1][0];
                    targetArray[1] = sb[1][1];
                    return 2;
                } else if (str2.equals("LEFT")) {
                    if(sb[2][0] == -1 && sb[2][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", count, align, pid);
                        continue;
                    }
                    targetArray[0] = sb[2][0];
                    targetArray[1] = sb[2][1];
                    return 3;
                } else if (str2.equals("RIGHT")) {
                    if(sb[3][0] == -1 && sb[3][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", count, align, pid);
                        continue;
                    }
                    targetArray[0] = sb[3][0];
                    targetArray[1] = sb[3][1];
                    return 4;
                } else if(str2.equals("RETURN")){
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                }
            }
        }
    }

    public void showStatus(int turn){
        System.out.println("=============================================================================================================================");
        b.showBoard();
        System.out.printf("Turn %d (Player %d, %s): Possible pieces: ", turn, players[current].getAlign(), players[current].getID());
        players[current].showPieces();
        System.out.printf("Turn %d (Player %d, %s): Possible pieces: ", turn, players[1 - current].getAlign(), players[1 - current].getID());
        players[1 - current].showPieces();
        System.out.printf("Turn %d (Player %d, %s): Turn Now Is %d.\n", turn, players[1 - current].getAlign(), players[1 - current].getID(), turn);
        System.out.println("=============================================================================================================================");
    }

    public int confirm(int align, String pid){                           //return 1: confirm/2: return to previous
        while(true){
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Confirm [1.Y / 2.N]\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N or 1 / 2 Only\n", count, align, pid);
            System.out.println("=============================================================================================================================");
            String str3 = s.nextLine();
            System.out.println("=============================================================================================================================");
            str3 = str3.toUpperCase();
            try {
                int x = Integer.parseInt(str3);
                if(x == 1){
                    System.out.printf("Turn %d (Player %d, %s): The Move Has Been Implemented\n", count, align, pid);
                    return 1;
                }else if(x == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): The Move Has Been Implemented\n", count, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                }
            }
        }
    }

    public int exitConfirm(int align, String pid){                           //return 1: confirm/2: return to previous
        System.out.println("=============================================================================================================================");
        System.out.printf("Turn %d (Player %d, %s): The game will ended immediately after your confirmation, continue?\n", count, align, pid);
        System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N or 1 / 2 Only\n", count, align, pid);
        System.out.println("=============================================================================================================================");
        String str3 = s.nextLine();
        System.out.println("=============================================================================================================================");
        str3 = str3.toUpperCase();
        while(true){
            try {
                int x = Integer.parseInt(str3);
                if(x == 1){
                    System.out.printf("Turn %d (Player %d, %s): End the Ongoing Game.\n", count, align, pid);
                    return 1;
                }else if(x == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): End the Ongoing Game.\n", count, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                }
            }
        }
    }

    public void showMoves(){
        if(mCounter == 0){
            System.out.println("No Available Moves Committed");
            return;
        }
        for(int i = 0; i < mCounter; i++){
            System.out.println(moves[i].getPlayer() + " | " + moves[i].getAlign() + " | " + moves[i].getPieceName() + " | "
                    + moves[i].getEatenName() + " | "+ moves[i].getMove() + " | " + moves[i].getType());
        }
    }

    public int backConfirm(int align, String pid){                           //return 1: confirm/2: return to previous
        System.out.println("=============================================================================================================================");
        System.out.printf("Turn %d (Player %d, %s): take back 1 step (Except for Step Back) from the your opponent immediately after your confirmation, continue?\n", count, align, pid);
        System.out.printf("Turn %d (Player %d, %s): Remaining Step Back in current game: %d\n", count, align, pid, back);
        System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N or 1 / 2 Only\n", count, align, pid);
        System.out.println("=============================================================================================================================");
        String str3 = s.nextLine();
        System.out.println("=============================================================================================================================");
        str3 = str3.toUpperCase();
        while(true){
            try {
                int x = Integer.parseInt(str3);
                if(x == 1){
                    System.out.printf("Turn %d (Player %d, %s): Back To Previous Steps.\n", count, align, pid);
                    return 1;
                } else if (x == 2) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                } else {
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): Back To Previous Steps.\n", count, align, pid);
                    back--;
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                }
            }
        }
    }

    public int record(int align, String pid){

        while(true){
            System.out.printf("Turn %d (Player %d, %s): Record all the moves made by both player into a file.\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Input your record file name. (No File Extension)\n", count, align, pid);
            String str4 = s.nextLine();
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Confirm [1.Y / 2.N] [3. Exit]\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N / Exit or 1 / 2 / 3 Only\n", count, align, pid);
            System.out.println("=============================================================================================================================");
            String str3 = s.nextLine();
            System.out.println("=============================================================================================================================");
            str3 = str3.toUpperCase();
            try {
                int x = Integer.parseInt(str3);
                if(x == 1){
                    System.out.printf("Turn %d (Player %d, %s): Record Saving.\n", count, align, pid);
                    recordFile(str4, align, pid);
                    return 1;
                }else if(x == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", count, align, pid);
                } else if (x == 3) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                } else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): Record Saving\n", count, align, pid);
                    recordFile(str4, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", count, align, pid);
                }else if (str3.equals("EXIT")) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                }
            }
        }
    }

    public void recordFile(String filename, int align, String pid){

        String directory = "records";

        File folder = new File(directory);
        if (!folder.mkdir() && !folder.exists()) {
            System.out.printf("Turn %d (Player %d, %s): Failed to create folder. Record Not Saved.\n", count, align, pid);
        }

        String fn = filename + ".record";

        try (PrintWriter writer = new PrintWriter(new FileWriter(directory + File.separator + fn))) {

            writer.println(mCounter);

            for(int i = 0; i < mCounter; i++){
                writer.println(moves[i].getPlayer() + " | " + moves[i].getAlign() + " | " + moves[i].getPieceName() + " | "
                        + moves[i].getEatenName() + " | "+ moves[i].getMove() + " | " + moves[i].getType());
            }

            System.out.println(System.out.printf("Turn %d (Player %d, %s): Record data saved to %s successfully.\n", count, align, pid, directory + File.separator + fn));

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public int save(int align, String pid){

        while(true){
            System.out.printf("Turn %d (Player %d, %s): Save the current state of the game into a file.\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Input your save file name. (No File Extension)\n", count, align, pid);
            String str4 = s.nextLine();
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Confirm [1.Y / 2.N] [3. Exit]\n", count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N / Exit or 1 / 2 / 3 Only\n", count, align, pid);
            System.out.println("=============================================================================================================================");
            String str3 = s.nextLine();
            System.out.println("=============================================================================================================================");
            str3 = str3.toUpperCase();
            try {
                int x = Integer.parseInt(str3);
                if(x == 1){
                    System.out.printf("Turn %d (Player %d, %s): Data Saving.\n", count, align, pid);
                    saveFile(str4, align, pid);
                    return 1;
                }else if(x == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", count, align, pid);
                } else if (x == 3) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                } else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): Data Saving\n", count, align, pid);
                    recordFile(str4, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", count, align, pid);
                }else if (str3.equals("EXIT")) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", count, align, pid);
                }
            }
        }
    }

    public void saveFile(String filename, int align, String pid){

        String directory = "saves";

        File folder = new File(directory);
        if (!folder.mkdir() && !folder.exists()) {
            System.out.printf("Turn %d (Player %d, %s): Failed to create folder. Data Not Saved.\n", count, align, pid);
        }

        String fn = filename + ".jungle";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory + File.separator + fn))) {
            oos.writeObject(this);
            System.out.println(System.out.printf("Turn %d (Player %d, %s): Save data saved to %s successfully.\n", count, align, pid, directory + File.separator + fn));
        } catch (IOException e) {
            System.err.println("Error saving object: " + e.getMessage());
        }

    }
}
