package simple_go;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class run_me {
    public static void main(String[] args) {

        //getting the board ready...
    	System.out.println("Ready? Go!");
        char[][] board = new char[19][19];
        for(int i = 0; i < 19; i++) {
            for(int j = 0; j < 19; j++) {
                board[i][j] = '.';
            }
        }
        board[3][3] = '+';
        board[3][15] = '+';
        board[15][3] = '+';
        board[15][15] = '+';

        //some more stuff to be used during the game...
        boolean cPl = true;//true is X is black
        int whiteScore = 0;
        int blackScore = 0;
        char[][] passiveKo = new char[19][19];
        char[][] activeKo = new char[19][19];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int passCount = 0;

        //game loop
        while(passCount < 2) {

            showGo(board);

            //take input
            String[] command;
            try {
                command = br.readLine().split(" ");
            } catch(IOException e/*TODO: find the other exception*/) {
                System.out.println("Invalid input, try again.");
                continue;
            }

            //initial exit condition
            if(command[0].equals("pass")) {
                passCount++;
                cPl = !cPl;
                continue;
            }

            //primitive input sanitization
            if(command.length != 2) {
                System.out.println("Invalid command. Try again.");
                continue;
            }

            //on with the meat of the program
            int xMove = "abcdefghijklmnopqrs".indexOf(command[0]);
            int yMove = -1;
            try {
                yMove = Integer.parseInt(command[1]) - 1;
            } catch(NumberFormatException e) {
                System.out.println("Invalid command. Try again.");
                continue;
            }
            
            //MOVE CONDITION VERIFYING!
            //OOB error
            if(xMove < 0 || yMove < 0 || yMove > 18) {
                System.out.println("Move is impossible. Try again.");
                continue;
            }

            //empty space?
            if(board[yMove][xMove] != '.' && board[yMove][xMove] != '+') {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            /*TODO: BUG FIX:
             * store board value in passiveKo
             * execute move on board
             * compare with active ko
             *     if equal, revert move with passiveKo
             *     else, change player, switch passive and active ko
             * BUGS:
             * -when ko is encoutered, alg seems to play anyway
             * -encounters ko when condition isnt valid (activeKo changed without direct access)
             * 
             * PLAUSIBLE SOURCES:
             * somehow, during execution, activeKo also receives the changes meant only for board
             * also, passiveKo seems to be affected as the player stays the same, but the execution is done
             * 
             * FIXES:
             * -manually compare and clone arrays?*/
            
            //ko check pt. 1
            for (int i = 0; i < 3; i++) {
                passiveKo[i] = Arrays.copyOf(board[i], board[i].length);
            }

            //preemptive execution
            if(cPl) {
                board[yMove][xMove] = 'X';
            } else {
                board[yMove][xMove] = 'O';
            }

            /*TODO: INSERT CAPTURE & SUICIDE CHECK, RETURN INT PTS
            * Where the hell did I put that code??? Find it!!!*/
            int pts = 0;//temporary workaround

            //Ko check pt. 2
            if(Arrays.deepEquals(board, activeKo)) {
                System.out.println("Cannot play there; ko rule prevents it.");
                for (int i = 0; i < 3; i++) {
                    board[i] = Arrays.copyOf(passiveKo[i], passiveKo[i].length);
                }
                continue;
            }
            for (int i = 0; i < 3; i++) {
                activeKo[i] = Arrays.copyOf(passiveKo[i], passiveKo[i].length);
            }

            //tally points
            if(cPl) {
                blackScore += pts;
            } else {
                whiteScore += pts;
            }

            //continuing game...
            passCount = 0;
            cPl = !cPl;
        }
        //Exited the main loop! 2 players passed.
        //TODO: territory calulations?
    }

    public static void showGo(char[][] board) {

        System.out.println("\ta b c d e f g h i j k l m n o p q r s");
        for(int i = 0; i < board.length; i++) {
            System.out.print((i+1) + "\t");
            for(int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static ArrayList<Object> exploreGroup(int y, int x, char team) {

        char[][] board = new char[19][19];//temp, remove before rolling in changes

        //HORRIBLY INEFFICIENT
		/*recursively explore group of stones 'team' at board[y][x]
		 * returns a list of stones in the group and a 'isAlive' boolean state*/

        ArrayList<Object> sequel = new ArrayList<Object>();
        sequel.add(new Boolean(false));

        //EXIT+isAlive -> true
        if(board[y][x] != team) {//TODO: OOB HANDLING
            if(board[y][x] == '+' || board[y][y] == '.') {
                //TODO: sequelHead = true somehow;
            }
            return sequel;
        }

        //the recursive part
        //TODO: OOB HANDLING again
        //look up
        ArrayList<Object> addMe = exploreGroup(y-1, x, team);
        //TODO: remove head
        sequel.addAll(addMe);
        //TODO: if(head) {sequel.head = true}

        //look right
        addMe = exploreGroup(y, x+1, team);
        //TODO: remove head
        sequel.addAll(addMe);
        //TODO: if(head) {sequel.head = true}

        //look down
        addMe = exploreGroup(y+1, x, team);
        //TODO: remove head
        sequel.addAll(addMe);
        //TODO: if(head) {sequel.head = true}

        //look left
        addMe = exploreGroup(y, x-1, team);
        //TODO: remove head
        sequel.addAll(addMe);
        //TODO: if(head) {sequel.head = true}

        return sequel;//final exit
    }

}
