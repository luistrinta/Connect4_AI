import java.util.*;
import java.lang.*;

public class Algoritmos extends Tabela {


    //mini_max
    public static int minimax_play(Tabela t, int depth) {
        return max_value(t, depth);
    }



    //max_value

    public static int max_value(Tabela t, int depth ) {

        if(checkWinners(t, 'x'))return 512;
        if(checkWinners(t, 'o'))return -512;

        if(depth == 0) {
            //System.out.println(t.bestY);
            return scoreSum(t);
        }



        else {

            int best = Integer.MIN_VALUE;

            int next;

            for(int i : t.getMoves()) {
                next = min_value(play(t, 'x', i), depth - 1);
                if(next > best) {
                    best = next;
                    t.bestY = i;
                }

               /* if(depth == 7 ) {
                    System.out.println("Max value of " + i + " is :" + best);
                }*/
            }

            return best;
        }
    }


    //min_value

    public static int min_value(Tabela t, int depth) {

        if(checkWinners(t, 'x'))return 512;
        if(checkWinners(t, 'o'))return -512;


        if(depth == 0)
            return scoreSum(t);


        else {


            int best = Integer.MAX_VALUE;

            int next;
            for(int i : t.getMoves()) {
                next = max_value(play(t, 'o', i), depth - 1);
                if(next < best) {
                    best = next;
                    t.bestY = i;
                }
            }
            return best;
        }
    }



    public static int alpha_beta_play(Tabela t, int depth, int alpha, int beta, char player) {
        if(depth == 0 || checkWinners(t, 'x') || checkWinners(t, 'o') || isCompleted(t)) {
            return scoreSum(t);
        }

        if(player == 'x') {
            int maxEval = Integer.MIN_VALUE;

            for(int i : t.getMoves()) {
                int eval = alpha_beta_play(play(t, 'x', i), depth - 1, alpha, beta, 'o');
                if(maxEval < eval) {
                    t.bestY = i ;
                    maxEval = eval;
                }
                alpha = Math.max(alpha, eval);
                if(beta <= alpha)break;

            }
            return maxEval;
        }

        else {

            int minEval = Integer.MAX_VALUE;

            for(int i : t.getMoves()) {
                int eval = alpha_beta_play(play(t, 'o', i), depth - 1, alpha, beta, 'x');
                if(minEval > eval) {
                    t.bestY = i ;
                    minEval = eval;
                }
                beta = Math.min(beta, eval);
                if(beta <= alpha)break;

            }
            return minEval;
        }
    }


    

}
