package view_and_controller;

//importing class from model package
import model.Board;
import model.Move;
import model.Player;
import model.Animal_chess.Piece;

import java.io.*;
import java.util.Scanner;

//writeObject is required for saving the file, as a result, serializable is implemented
public class Game implements Serializable {
    //declaring variable
    Board b;
    Player[] players = new Player[2];
    Move[] moves = new Move[1024];  //action player has taken
    int mCounter = 0;   //variable for current move# counter
    int available_back = 3;
    int current;
    boolean cont = true;
    int turn_count = 1;
    transient Scanner s;

    Game(){//method for storing basic info for game
        b = new Board();
        players[0] = new Player("p-1", 1, b.getP1(), b.getP1n());
        players[1] = new Player("p-2", 2, b.getP2(), b.getP2n());
        current = (int)(Math.random() * 2); //start from p1 or p2
    }

    public String[] process(){

        s = new Scanner(System.in);

        if(turn_count == 1){
            System.out.println("=============================================================================================================================");
            System.out.printf("%s (Player %d) goes first!\n", players[current].getID(), players[current].getAlign());
            System.out.println("=============================================================================================================================");
        }else{
            System.out.println("=============================================================================================================================");
            System.out.printf("%s (Player %d) resume the game!\n", players[current].getID(), players[current].getAlign());
            System.out.println("=============================================================================================================================");
        }


        while(cont){ //round
            Player currentPlayer = players[current];
            String pid = players[current].getID();
            int current_p_align = players[current].getAlign();
            int target; //+ve indicates a piece, negative for other command
            int direction = -1;
            int[] sourceArray = new int[2];
            int[] targetArray = new int[2];
            int response = 0;//response =0 nth special, response =1 enter den, response =2 ate something

            boolean flag = true;
            boolean terminated = false;

            do{ 
                target = select(currentPlayer, current_p_align, pid); //positive indicates the piece
                if(target == -16){ //STATUS, RECORD, SAVE
                    continue;
                } else if (target == -8) {  //BACK
                    break;
                } else if (target == -32) { //END
                    terminated = true;
                    break;
                }
                while(flag){
                    direction = Direction_select(currentPlayer, current_p_align, pid, target, targetArray, sourceArray);  //return the direction of the piece
                    if(direction == -1){//RETURN
                        break;
                    }
                    //int c = confirm(align, pid);
                    flag = false;
                }
            }while(flag);

            if(terminated){return new String[]{"none", "none"};}

            if(direction >= 0) {//if piece and direction is being selected
                Piece source = b.getPiece(sourceArray[0], sourceArray[1]);
                Piece replaced = b.getPiece(targetArray[0], targetArray[1]);

                response = b.change(currentPlayer.getPiece(target).getPositionR(), currentPlayer.getPiece(target).getPositionC(), targetArray);
                if(response == 2){//ate something
                    players[1 - current].removePiece(replaced);
                }

                if(direction == 1){
                    moves[mCounter] = new Move(currentPlayer, current_p_align, source, replaced, "UP", "MOVE");
                } else if (direction == 2) {
                    moves[mCounter] = new Move(currentPlayer, current_p_align, source, replaced,"DOWN", "MOVE");
                } else if (direction == 3) {
                    moves[mCounter] = new Move(currentPlayer, current_p_align, source, replaced,"LEFT", "MOVE");
                } else if (direction == 4) {
                    moves[mCounter] = new Move(currentPlayer, current_p_align, source, replaced,"RIGHT", "MOVE");
                }

            }else{//BACK
                int t1 = 0;
                int valid = 0;
                Piece added = null;
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
                Piece source = moves[t1].getPiece();
                response = b.revert(moves, mCounter, available_back, t1);
                if(response == 2){
                    int a1 = moves[t1].getEaten().getAlign();//chess that have been eaten, return its align
                    players[a1 - 1].addPiece(moves[t1].getEaten(), moves[t1].getEaten().getRank());
                    added = moves[t1].getEaten();
                }
                available_back--;
                moves[mCounter] = new Move(currentPlayer, current_p_align, source, added, moves[t1].flipMove(), "BACK");
            }
            mCounter++;

            System.out.println("=============================================================================================================================");

            if(response == 1 || players[1 - current].getPN() == 0){//no chess or enter den
                cont = false;
                b.showBoard();
                while(true){
                    System.out.println("The Game Has Ended! Record This Game? [Y/N] or [1(for Y)/2(for N)]");
                    String receive = s.nextLine();
                    try{
                        int input = Integer.parseInt(receive);
                        if(input == 1){
                            record(current_p_align, pid);
                            break;
                        }else if(input == 2){
                            break;
                        }else{
                            System.out.println("Input Not Match. Try Again.");
                        }
                    }catch (NumberFormatException e){//if enter text
                        receive = receive.toUpperCase();
                        if(receive.equals("Y")){
                            int r1 = record(current_p_align, pid);
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
                turn_count++;
            }
        }
        return new String[]{String.valueOf(current + 1), players[current].getID()};
    }

    public void namePlayer(){//name the player
        boolean retry = false;
        do{
            System.out.println("=============================================================================================================================");
            System.out.println("Select how to name your player: (By Using \"Number\" or \"Word in []\")");
            System.out.println("1. Input Name [INPUT]");
            System.out.println("2. Random Name [RANDOM]");
            System.out.println("=============================================================================================================================");
            Scanner s = new Scanner(System.in);
            String str = s.nextLine();
            System.out.println("=============================================================================================================================");    
        try {
            int input = Integer.parseInt(str);
            if(input == 1){//input player name
                System.out.println("Input First(1st) Player Name:");
                String str2 = s.nextLine();
                this.players[0].setID(str2);
                System.out.println("Input Second(2nd) Player Name:");
                String str3 = s.nextLine();
                this.players[1].setID(str3);
                retry = false;
            }else if(input == 2){//random name
                String[][] temp = {{"Noah", "Oliver", "Arthur"},{"Olivia", "Amelia", "Lily"}};
                int p = (int)(Math.random() * 2);
                this.players[0].setID(temp[p][(int)(Math.random() * 3)]);
                this.players[1].setID(temp[1 - p][(int)(Math.random() * 3)]);
                retry=false;
            }else{
                System.out.println("Integer Out Of Range. Try Again.");
                retry=true;
            }
        } catch (NumberFormatException e) {//if enter text INPUT or RANDOM
            str = str.toUpperCase();
            if(str.equals("INPUT")){
                System.out.println("Input First(1st) Player Name:");
                String str2 = s.nextLine();
                this.players[0].setID(str2);
                System.out.println("Input Second(2nd) Player Name:");
                String str3 = s.nextLine();
                this.players[0].setID(str3);
                retry=false;
            }else if(str.equals("RANDOM")){
                String[][] temp = {{"Noah", "Oliver", "Arthur"},{"Olivia", "Amelia", "Lily"}};
                int p = (int)(Math.random() * 2);
                this.players[0].setID(temp[p][(int)(Math.random() * 3)]);
                this.players[1].setID(temp[1 - p][(int)(Math.random() * 3)]);
                retry=false;
            }else{
                System.out.println("Option Not Available. Try Again.");
                retry= true;
            }
        }
        }while(retry==true);

    }

    public int select(Player currentPlayer, int align, String pid){
        while(true){
            b.cleanSelectables();
            b.showBoard();
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Select the piece you want to move:\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Possible pieces: ", turn_count, align, pid);
            currentPlayer.showPieces();
            System.out.printf("Turn %d (Player %d, %s): Accept Input: 1(Number of Item) or RAT(Name of Item, Case Insensitive) ...\n", turn_count, align, pid);
            System.out.println(" ");
            System.out.printf("Turn %d (Player %d, %s): Alternative, Select one of the action option below:\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Possible option: A. END, B. STATUS, C. BACK, D. RECORD, E. SAVE\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: A(Character of Item) or END(Name of Item, Case Insensitive) ...\n", turn_count, align, pid);
            System.out.println("=============================================================================================================================");
            String str = s.nextLine();
            System.out.println("=============================================================================================================================");
            try {
                int input = Integer.parseInt(str);
                if(!(currentPlayer.inRange(input))){
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }else{
                    System.out.printf("Turn %d (Player %d, %s): %d. %s is selected.\n", turn_count, align, pid, input, currentPlayer.getPiece(input - 1).getName());
                    return input - 1;
                }
            } catch (NumberFormatException e) {// if user input text instead of integer
                int input_piece = currentPlayer.findPiece(str);
                if(input_piece == -1){
                    str = str.toUpperCase();
                    if(str.equals("A") || str.equals("END")){//END the game
                        int exid = exitConfirm(align, pid);
                        if(exid == 1){
                            return -32;
                        }
                    }else if(str.equals("B") || str.equals("STATUS")){//show STATUS
                        showStatus(turn_count);
                        return -16;
                    } else if (str.equals("C") || str.equals("BACK")) {//BACK to previous move
                        if(available_back <= 0){
                            System.out.printf("Turn %d (Player %d, %s): No More Back Steps Left in This Current Game.\n", turn_count, align, pid);
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
                                System.out.printf("Turn %d (Player %d, %s): No Valid Back Steps Left.\n", turn_count, align, pid);
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
                        System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
                    }
                }else{
                    System.out.printf("Turn %d (Player %d, %s): %d. %s is selected.\n", turn_count, align, pid, input_piece + 1, currentPlayer.getPiece(input_piece).getName());
                    return input_piece;
                }
            }
        }
    }

    public int Direction_select(Player currentPlayer, int align, String pid, int target, int[] targetdirection, int[] sourcelocation){//return the direction index of the piece and also check whether it is possible
        while(true){
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Possible Movement: (Space with \"[*]\")\n", turn_count, align, pid);
            b.cleanSelectables();
            int[][] selected_b = b.selectedBoard(currentPlayer.getPiece(target).getPositionR(), currentPlayer.getPiece(target).getPositionC(), currentPlayer.getPiece(target));
            sourcelocation[0] = currentPlayer.getPiece(target).getPositionR();
            sourcelocation[1] = currentPlayer.getPiece(target).getPositionC();
            b.showBoard();

            System.out.printf("Turn %d (Player %d, %s): Accept Input: 1. UP(up) / 2. DOWN(down) / 3. (LEFT)left / 4. (RIGHT)right / 5. (RETURN) Return [To Previous]\n", turn_count, align, pid);
            System.out.println("=============================================================================================================================");
            String str2 = s.nextLine();
            str2 = str2.toUpperCase();
            System.out.println("=============================================================================================================================");
            try {
                int input = Integer.parseInt(str2);
                if(input >= 1 && input <= 4){
                    if(selected_b[input - 1][0] == -1 && selected_b[input - 1][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", turn_count, align, pid);
                        continue;
                    }
                    targetdirection[0] = selected_b[input - 1][0];
                    targetdirection[1] = selected_b[input - 1][1];
                    return input;
                } else if(input == 5){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }
            } catch (NumberFormatException e) {//if player enter text instead of int
                if(str2.equals("UP")){
                    if(selected_b[0][0] == -1 && selected_b[0][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", turn_count, align, pid);
                        continue;
                    }
                    targetdirection[0] = selected_b[0][0];
                    targetdirection[1] = selected_b[0][1];
                    return 1;
                } else if (str2.equals("DOWN")) {
                    if(selected_b[1][0] == -1 && selected_b[1][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", turn_count, align, pid);
                        continue;
                    }
                    targetdirection[0] = selected_b[1][0];
                    targetdirection[1] = selected_b[1][1];
                    return 2;
                } else if (str2.equals("LEFT")) {
                    if(selected_b[2][0] == -1 && selected_b[2][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", turn_count, align, pid);
                        continue;
                    }
                    targetdirection[0] = selected_b[2][0];
                    targetdirection[1] = selected_b[2][1];
                    return 3;
                } else if (str2.equals("RIGHT")) {
                    if(selected_b[3][0] == -1 && selected_b[3][1] == -1){
                        System.out.printf("Turn %d (Player %d, %s): Option Not Available. Try Again.\n", turn_count, align, pid);
                        continue;
                    }
                    targetdirection[0] = selected_b[3][0];
                    targetdirection[1] = selected_b[3][1];
                    return 4;
                } else if(str2.equals("RETURN")){
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
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

    public int confirm(int align, String pid){  //return =1 confirm, return =-1 return to previous
        while(true){
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Confirm [1.Y / 2.N]\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N or 1 / 2 Only\n", turn_count, align, pid);
            System.out.println("=============================================================================================================================");
            String str3 = s.nextLine();
            System.out.println("=============================================================================================================================");
            str3 = str3.toUpperCase();
            try {
                int input = Integer.parseInt(str3);
                if(input == 1){
                    System.out.printf("Turn %d (Player %d, %s): The Move Has Been Implemented\n", turn_count, align, pid);
                    return 1;
                }else if(input == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): The Move Has Been Implemented\n", turn_count, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
                }
            }
        }
    }

    public int exitConfirm(int align, String pid){                           //return 1: confirm/2: return to previous
        System.out.println("=============================================================================================================================");
        System.out.printf("Turn %d (Player %d, %s): The game will ended immediately after your confirmation, continue?\n", turn_count, align, pid);
        System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N or 1 / 2 Only\n", turn_count, align, pid);
        System.out.println("=============================================================================================================================");
        String str3 = s.nextLine();
        System.out.println("=============================================================================================================================");
        str3 = str3.toUpperCase();
        while(true){
            try {
                int input = Integer.parseInt(str3);
                if(input == 1){
                    System.out.printf("Turn %d (Player %d, %s): End the Ongoing Game.\n", turn_count, align, pid);
                    return 1;
                }else if(input == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }
            } catch (NumberFormatException e) {//if user input text instead of integer
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): End the Ongoing Game.\n", turn_count, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
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

    public int backConfirm(int align, String pid){  //return integer 1 = back, integer -1 = don't back
        System.out.println("=============================================================================================================================");
        System.out.printf("Turn %d (Player %d, %s): take back 1 step (Except for Step Back) from the your opponent immediately after your confirmation, continue?\n", turn_count, align, pid);
        System.out.printf("Turn %d (Player %d, %s): Remaining Step Back in current game: %d\n", turn_count, align, pid, available_back);
        System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N or 1 / 2 Only\n", turn_count, align, pid);
        System.out.println("=============================================================================================================================");
        String str3 = s.nextLine();
        System.out.println("=============================================================================================================================");
        str3 = str3.toUpperCase();
        while(true){
            try {
                int input = Integer.parseInt(str3);
                if(input == 1){
                    System.out.printf("Turn %d (Player %d, %s): Back To Previous Steps.\n", turn_count, align, pid);
                    return 1;
                } else if (input == 2) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                } else {
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }
            } catch (NumberFormatException e) {//input text instead of integer
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): Back To Previous Steps.\n", turn_count, align, pid);
                    available_back--;
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
                }
            }
        }
    }

    public int record(int align, String pid){//record confirm
        while(true){
            System.out.printf("Turn %d (Player %d, %s): Record all the moves made by both player into a file.\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Input your record file name. (Avoid Using The Name \"ESCAPE\") (No File Extension)\n", turn_count, align, pid);
            String str4 = s.nextLine();
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Confirm [1.Y / 2.N] [3. Exit]\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N / Exit or 1 / 2 / 3 Only\n", turn_count, align, pid);
            System.out.println("=============================================================================================================================");
            String str3 = s.nextLine();
            System.out.println("=============================================================================================================================");
            str3 = str3.toUpperCase();
            try {
                int input = Integer.parseInt(str3);
                if(input == 1){
                    System.out.printf("Turn %d (Player %d, %s): Record Saving.\n", turn_count, align, pid);
                    recordFile(str4, align, pid);
                    return 1;
                }else if(input == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", turn_count, align, pid);
                } else if (input == 3) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                } else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): Record Saving\n", turn_count, align, pid);
                    recordFile(str4, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", turn_count, align, pid);
                }else if (str3.equals("EXIT")) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
                }
            }
        }
    }

    public void recordFile(String filename, int align, String pid){//record the game
        String directory = "records";

        File folder = new File(directory);
        if (!folder.mkdir() && !folder.exists()) {
            System.out.printf("Turn %d (Player %d, %s): Failed to create folder. Record Not Saved.\n", turn_count, align, pid);
        }

        String fn = filename + ".record";

        try (PrintWriter writer = new PrintWriter(new FileWriter(directory + File.separator + fn))) {

            writer.println(mCounter);

            for(int i = 0; i < mCounter; i++){
                writer.println(moves[i].getPlayer() + " | " + moves[i].getAlign() + " | " + moves[i].getPieceName() + " | "
                        + moves[i].getEatenName() + " | "+ moves[i].getMove() + " | " + moves[i].getType());
            }

            System.out.println(System.out.printf("Turn %d (Player %d, %s): Record data saved to %s successfully.\n", turn_count, align, pid, directory + File.separator + fn));

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public int save(int align, String pid){//save confirm
        while(true){
            System.out.printf("Turn %d (Player %d, %s): Save the current state of the game into a file.\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Input your save file name. (Avoid Using The Name \"ESCAPE\") (No File Extension)\n", turn_count, align, pid);
            String str4 = s.nextLine();
            System.out.println("=============================================================================================================================");
            System.out.printf("Turn %d (Player %d, %s): Confirm [1.Y / 2.N] [3. Exit]\n", turn_count, align, pid);
            System.out.printf("Turn %d (Player %d, %s): Accept Input: Y / N / Exit or 1 / 2 / 3 Only\n", turn_count, align, pid);
            System.out.println("=============================================================================================================================");
            String str3 = s.nextLine();
            System.out.println("=============================================================================================================================");
            str3 = str3.toUpperCase();
            try {
                int input = Integer.parseInt(str3);
                if(input == 1){
                    System.out.printf("Turn %d (Player %d, %s): Data Saving.\n", turn_count, align, pid);
                    saveFile(str4, align, pid);
                    return 1;
                }else if(input == 2){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", turn_count, align, pid);
                } else if (input == 3) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                } else{
                    System.out.printf("Turn %d (Player %d, %s): Integer Out Of Range. Try Again.\n", turn_count, align, pid);
                }
            } catch (NumberFormatException e) {
                if(str3.equals("Y")){
                    System.out.printf("Turn %d (Player %d, %s): Data Saving\n", turn_count, align, pid);
                    saveFile(str4, align, pid);
                    return 1;
                }else if(str3.equals("N")){
                    System.out.printf("Turn %d (Player %d, %s): Return to Name Inputting.\n", turn_count, align, pid);
                }else if (str3.equals("EXIT")) {
                    System.out.printf("Turn %d (Player %d, %s): Return to Previous Stage.\n", turn_count, align, pid);
                    return -1;
                }else{
                    System.out.printf("Turn %d (Player %d, %s): Input Doesn't Match. Try Again.\n", turn_count, align, pid);
                }
            }
        }
    }

    public void saveFile(String filename, int align, String pid){//save the game
        String directory = "saves";

        File folder = new File(directory);
        if (!folder.mkdir() && !folder.exists()) {
            System.out.printf("Turn %d (Player %d, %s): Failed to create folder. Data Not Saved.\n", turn_count, align, pid);
        }

        String fn = filename + ".jungle";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(directory + File.separator + fn))) {
            oos.writeObject(this);
            System.out.println(System.out.printf("Turn %d (Player %d, %s): Save data saved to %s successfully.\n", turn_count, align, pid, directory + File.separator + fn));
        } catch (IOException e) {
            System.err.println("Error saving object: " + e.getMessage());
        }

    }
}
