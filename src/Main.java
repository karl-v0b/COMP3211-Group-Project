import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.println("=============================================================================================================================");
            System.out.println("The Jungle Game");
            System.out.println("Select (By Using \"Number\" or \"Word in []\") A Option From Below:");
            System.out.println("1. New Game [NEW]");
            System.out.println("2. Replay A Game [REPLAY]");
            System.out.println("3. Continue / Load A Game [CONT][CONTINUE][LOAD]");
            System.out.println("4. Exit [EXIT]");
            System.out.println("=============================================================================================================================");
            String str = s.nextLine();
            System.out.println("=============================================================================================================================");
            try {
                int x = Integer.parseInt(str);
                if(x == 1){
                    newGame();
                }else if(x == 2){
                    replayGame();
                }else if (x == 3){
                    continueGame();
                }else if(x == 4){
                    return;
                }else{
                    System.out.println("Integer Out Of Range. Try Again.");
                }
            } catch (NumberFormatException e) {
                str = str.toUpperCase();
                if(str.equals("NEW")){
                    newGame();
                }else if(str.equals("REPLAY")){
                    replayGame();
                }else if (str.equals("CONT") || str.equals("CONTINUE") || str.equals("LOAD")){
                    continueGame();
                }else if(str.equals("EXIT")){
                    return;
                }else{
                    System.out.println("Option Not Available. Try Again.");
                }
            }
        }
    }

    public static void newGame(){
        game g = new game();
        g.namePlayer();
        String[] winner = g.process();
        if(winner[0].equals("none")){
            System.out.println("No winner is determined at this point of the game.");
        }else{
            System.out.printf("Winner is Player %s, %s!\n", winner[0], winner[1]);
        }
        System.out.println("The Game Has Ended!");
    }

    public static void replayGame(){
        while(true){
            System.out.println("Input your record file name. (No File Extension)");
            System.out.println("Input ESCAPE To Leave \"Replay Game.\"");
            Scanner s = new Scanner(System.in);
            String file1 = s.nextLine();
            if(file1.equalsIgnoreCase("ESCAPE")){
                break;
            }
            String fileName = file1 + ".record";
            String filePath = "records" + File.separator + fileName;

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                int counter = 0;
                int mCounter = 0;
                String[] movement = new String[1024];
                while ((line = reader.readLine()) != null && !(line.isEmpty())) {
                    if(counter == 0){
                        mCounter = Integer.parseInt(line);
                    }else{
                        movement[counter - 1] = line;
                    }
                    counter++;
                }
                System.out.printf("File read successfully, %d lines read\n", counter);
                replay replay = new replay(mCounter, movement);
                replay.replaying();
            } catch (IOException e) {
                System.out.println("Failed to read file: " + e.getMessage());
            }
        }
    }

    public static void continueGame(){
        while(true){
            System.out.println("Input your record file name. (No File Extension)");
            System.out.println("Input ESCAPE To Leave \"Continue / Load Game.\"");
            Scanner s = new Scanner(System.in);
            String file1 = s.nextLine();
            if(file1.equalsIgnoreCase("ESCAPE")){
                break;
            }
            String fileName = file1 + ".jungle";
            String filePath = "saves" + File.separator + fileName;

            File file = new File(filePath);

            if (!file.exists()) {
                System.err.println("Load failed: File does not exist.");
                continue;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof game) {
                    game g = (game) obj;
                    String[] winner = g.process();
                    if(winner[0].equals("none")){
                        System.out.println("No winner is determined at this point of the game.");
                    }else{
                        System.out.printf("Winner is Player %s, %s!\n", winner[0], winner[1]);
                    }
                    System.out.println("The Game Has Ended!");
                    break;
                } else {
                    System.err.println("Load failed: Object type mismatch.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading object: " + e.getMessage());
            }

        }
    }
}