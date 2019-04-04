import java.util.*;
import java.lang.*;

public class Algoritmos extends Tabela {


    /*--------------------------
    |          MINIMAX         |          
    --------------------------*/

    public static int minimax_play(Tabela t, int depth) {
        return max_value(t, depth);         //retorna o max_value da jogada do pc
    }

    //max_value

    public static int max_value(Tabela t, int depth ) {

        if(checkWinners(t, 'x'))return 512;  //Caso o PC ganhe nesta jogada retorna a score máxima posivel
        if(checkWinners(t, 'o'))return -512; //Caso o PC perca retorna a score minima possivel

        if(depth == 0) {            //Condição para o fim da recursão
            return scoreSum(t);     //Quando obtemos uma depth == 0 retornamos o Score do tabuleiro
        }



        else {  //Enquanto estamos a realizar a recursão

            int best = Integer.MIN_VALUE;   //definimos a melhor jogada como o menor valor possivel (-inf em pseudocódigo)

            int next;              

            for(int i : t.getMoves()) {     //ciclo forEach responsável pela obtenção das scores das várias posições
                next = min_value(play(t, 'x', i), depth - 1);  //aplicamos aqui o min_value da próxima jogada
                if(next > best) {   
                    best = next;            //Caso o next seja maior que o best , atualizamos o valor do best e introduzimos i como a melhor jogada possivel
                    t.bestY = i;
                }

            }

            return best;  //retorna o melhor score
        }
    }

    //min_value

    public static int min_value(Tabela t, int depth) {

        if(checkWinners(t, 'x'))return 512;
        if(checkWinners(t, 'o'))return -512;
                                                    //condições de paragem da recursão iguais á do max_value

        if(depth == 0)
            return scoreSum(t);


        else {


            int best = Integer.MAX_VALUE;       //definimos a melhor jogada como o maior valor possivel (+inf em pseudocódigo)

            int next;
            for(int i : t.getMoves()) {
                next = max_value(play(t, 'o', i), depth - 1); //aplicamos max_value da proxima jogada
                if(next < best) {
                    best = next;                //Caso o next seja menor que o best , atualizamos o valor do best e introduzimos i como a melhor jogada possivel
                    t.bestY = i;
                }
            }
            return best;    //retorna o melhor score
        }
    }


    /*--------------------------
    |                          |
    |    MINIMAX ALPHA-BETA    | 
    |        PRUNNING          | 
    |                          | 
    --------------------------*/



    public static int alpha_beta_play(Tabela t, int depth, int alpha, int beta, char player) {
        //Condições de paragem da recursividade do algoritmo
        if(depth == 0 || checkWinners(t, 'x') || checkWinners(t, 'o') || isCompleted(t)) {
            return scoreSum(t);
        }


        if(player == 'x') {                     //caso seja o PC a "jogar"
            int maxEval = Integer.MIN_VALUE;    //definimos a avaliação máxima (Score do tabuleiro) como o menor valor possível (-inf em pseudocódigo)

            for(int i : t.getMoves()) {
                int eval = alpha_beta_play(play(t, 'x', i), depth - 1, alpha, beta, 'o'); //aplicamos o método de forma recursiva 
                if(maxEval < eval) {
                    t.bestY = i ;           //Realizamos a atualização da maior avaliação e da melhor coluna a ser jogada
                    maxEval = eval;
                }
                alpha = Math.max(alpha, eval);      //Atualizamos o valor do alpha
                if(beta <= alpha)break;             //Condição responsável pelo corte dos ramos desnecessários

            }
            return maxEval;         //Retorna a avaliação máxima
        }

        else {                              //Caso seja o jogador a "jogar"

            int minEval = Integer.MAX_VALUE;        //definimos a avaliação mínima (Score do tabuleiro) como o maior valor possível (+inf em pseudocódigo)

            for(int i : t.getMoves()) {
                int eval = alpha_beta_play(play(t, 'o', i), depth - 1, alpha, beta, 'x'); //Aplicamos o método de forma recursiva
                if(minEval > eval) {
                    t.bestY = i ;           //Realizamos a atualização da menor avaliação e da melhor coluna a ser jogada
                    minEval = eval;
                }
                beta = Math.min(beta, eval);        //Atualizamos o valor do beta
                if(beta <= alpha)break;             //Condição responsável pelo corte dos ramos desnecessários 

            }
            return minEval;         //Retorna a avaliação mínima
        }
    }


    

}
