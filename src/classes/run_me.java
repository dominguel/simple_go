package classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class run_me {
    public static void main(String[] args) {

        //getting the board ready...
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
        char[][] passiveKo;
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

            //input sanitization
            if(command.length != 2) {
                //TODO: error message
            }

            //on with the meat of the program
            int xMove = "abcdefghijklmnopqrs".indexOf(command[0]);
            int yMove = Integer.parseInt(command[1]) - 1;

            //MOVE CONDITION VERIFYING!
            //OOB error
            if(xMove < 0 || yMove < 0 || yMove > 18) {
                //TODO: error message
            }

            //empty space?
            if(board[yMove][xMove] != '.' && board[yMove][xMove] != '+') {
                //TODO: occupied space error
            }

            passiveKo = board.clone();//ko check pt. 1

            //preemptive execution
            if(cPl) {
                board[yMove][xMove] = 'X';
            } else {
                board[yMove][xMove] = 'O';
            }

            /*TODO: INSERT CAPTURE & SUICIDE CHECK, RETURN INT PTS
            * Where the hell did I put that code??? Find it!!!*/
            int pts = 0;//tempporary workaround

            //Ko check pt. 2
            if(Arrays.deepEquals(board, activeKo)) {
                //TODO: ko error
                board = passiveKo.clone();
            }
            activeKo = passiveKo.clone();

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

    /*public static ArrayList<Dimension> exploreGroup(int y, int x, char team) {
        //somehow returns a list of all stones in the group and a boolean (isAlive)

        //enemy groups
        ArrayList<Dimension> captures = new ArrayList<Dimension>();
        ArrayList<Dimension> temp = exploreGroup(y+1, x+1, team)
    }*/
}
