import java.util.*;


import java.util.*;

public class Connect4Main extends Algoritmos {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("1.AI vs PLAYER || 2.AI vs AI || 3.MCTS UNDER CONSTRUCTION ");
        int play = scan.nextInt();
        switch(play){
        case 1 :    
        Tabela t = new Tabela();
        while(!isCompleted(t)) {
            


            int x = alpha_beta_play(t,9, Integer.MIN_VALUE , Integer.MAX_VALUE , 'x');
            System.out.println("Computador jogou na :"+t.bestY);
            t = play(t, 'x', t.bestY);
            printTabela(t);
            System.out.println("0 1 2 3 4 5 6");
            

            if(checkWinners(t,'x')){
                System.out.println("Player x won");
                return;
            }

            if(checkWinners(t,'o')){
                System.out.println("Player o won");
                return;
            }

            t = playerMovement(t, 'o' );
            printTabela(t);
            System.out.println(scoreSum(t));

            System.out.println("0 1 2 3 4 5 6");
            

            if(checkWinners(t,'x')){
                System.out.println("Player x won");
                return;
            }

            if(checkWinners(t,'o')){
                System.out.println("Player o won");
                return;
            }

        }

        System.out.println("Não há vencedores");
        break;

        case 2: 
        System.out.println("t1 ==> x t2==> o");
        System.out.println("Pressione enter para continuar");
        
        String nada = scan.next();

        Tabela t1 = new Tabela();
        Tabela t2 = new Tabela();

        while(!isCompleted(t1)) {
            
            int x = alpha_beta_play(t1,1 , Integer.MIN_VALUE , Integer.MAX_VALUE , 'x');
            int bestPlay = t1.bestY;
            System.out.println("Computador jogou na :"+t1.bestY);
            t1 = play(t1, 'x', bestPlay);
            t2 = play(t2, 'o', bestPlay);
            printTabela(t1);
            System.out.println("0 1 2 3 4 5 6");            

            
            

            if(checkWinners(t1,'x')){
                System.out.println("t1 won");
                return;
            }

            if(checkWinners(t1,'o')){
                System.out.println("t2 won");
                return;
            }


            x = minimax_play(t2, 3 /*, Integer.MIN_VALUE , Integer.MAX_VALUE , 'x'*/);
            System.out.println("Computador jogou na :"+t2.bestY);
            bestPlay = t2.bestY;
            t1 = play(t1, 'o', bestPlay);
            t2 = play(t2, 'x', bestPlay);
            printTabela(t1);
            System.out.println("0 1 2 3 4 5 6");
            

            if(checkWinners(t2,'x')){
                System.out.println("t2 won");
                return;
            }

            if(checkWinners(t2,'o')){
                System.out.println("t1 won");
                return;
            }

        }

        System.out.println("Não há vencedores");
        break;
     
        case 3:

        System.out.println("Por favor insira o tempo de execução para o MCTS em segundos:");
        int tempo = scan.nextInt() * 1000;
        final long GIVEN_TIME = tempo;
        

        Tabela tabela = new Tabela();
        Tabela board = new Tabela();
        Tabela board2 = new Tabela();
        tabela.lastplayer = 'N';

        MonteCarlo ai = new MonteCarlo(tabela, GIVEN_TIME);

        System.out.println("1.AI vs AI");
        System.out.println("2.Jogador vs AI");
        System.out.println("3.AI vs Jogador");

        int option = scan.nextInt();

        switch(option) {
        case 1:
            System.out.println("1.MCTS vs Alpha-Beta");
            System.out.println("2.Alpha-Beta vs MCTS");
            int option2 = scan.nextInt();
            switch(option2) {

            case 1:
                System.out.println("Profundidade do Alpha-Beta :");
                int prof = scan.nextInt();

                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(tabela)) {
                    int bestMove;

                    //AI a jogar

                    bestMove = ai.getOptimalMove(tabela);
                    System.out.print("Movimento do MCTS :");
                    board = play(board, 'x', bestMove);
                    board2 = play(board2, 'o', bestMove);
                    System.out.println(bestMove);
                    ai.update(bestMove, 'x');


                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(board, 'x')) {
                        System.out.println("MCTS venceu");
                        return;
                    }

                    if(checkWinners(board, 'o')) {
                        System.out.println("Alpha_Beta venceu");
                        return;
                    }
                    //Jogador
                    int bestMoveA = alpha_beta_play(board, prof, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    int pos = board.bestY;
                    System.out.println("Movimento do Alpha Beta :" + pos);
                    board = play(board, 'o', pos);
                    board2 = play(board2, 'x', pos);
                    ai.update(pos, 'o');

                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(board2, 'x')) {
                        System.out.println("AlphaBeta venceu !");
                        return;
                    }

                    if(checkWinners(board2, 'o')) {
                        System.out.println("MCTS venceu!");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;


            case 2:
                System.out.println("Profundidade do Alpha-Beta :");
                prof = scan.nextInt();

                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(tabela)) {
                    System.out.println("\n\n");
                    int bestMove;

                    

                    //Alpha-Beta
                    int bestMoveA = alpha_beta_play(board, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    int pos = board.bestY;
                    System.out.println("Movimento do Alpha Beta :"+pos);
                    board = play(board, 'o', pos);
                    board2 = play(board2, 'x', pos);
                    ai.update(pos, 'o');

                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(board2, 'x')) {
                        System.out.println("AlphaBeta venceu !");
                        return;
                    }

                    if(checkWinners(board2, 'o')) {
                        System.out.println("MCTS venceu !");
                        return;
                    }
                    bestMove = ai.getOptimalMove(tabela);
                    System.out.print("Movimento do MCTS :");
                    board = play(board, 'x', bestMove);
                    board2 = play(board2, 'o', bestMove);
                    System.out.println(bestMove);
                    ai.update(bestMove, 'x');


                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(board, 'x')) {
                        System.out.println("MCTS venceu !");
                        return;
                    }

                    if(checkWinners(board, 'o')) {
                        System.out.println("Alpha_Beta venceu !");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;
            }

        case 2:
            while(!isCompleted(tabela)) {
                System.out.println("\n\n");
                int bestMove;

                //AI a jogar

                bestMove = ai.getOptimalMove(tabela);
                System.out.print("Movimento do MCTS :" + bestMove);
                board = play(board, 'x', bestMove);
                board2 = play(board2, 'o', bestMove);
                System.out.println(bestMove);
                ai.update(bestMove, 'x');


                System.out.println("\n\n");
                printTabela(board);
                System.out.println("0 1 2 3 4 5 6");


                if(checkWinners(board, 'x')) {
                    System.out.println("MCTS venceu !");
                    return;
                }

                if(checkWinners(board, 'o')) {
                    System.out.println("Alpha_Beta venceu !");
                    return;
                }
                //Jogador
                int bestMoveA = alpha_beta_play(board, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                int pos = board.bestY;
                System.out.println("Movimento do Alpha Beta :");
                board = play(board, 'o', pos);
                board2 = play(board2, 'x', pos);
                ai.update(pos, 'o');

                System.out.println("\n\n");
                printTabela(board);
                System.out.println("0 1 2 3 4 5 6");

                if(checkWinners(board2, 'x')) {
                    System.out.println("AlphaBeta won");
                    return;
                }

                if(checkWinners(board2, 'o')) {
                    System.out.println("MCTS won");
                    return;
                }

            }
            System.out.println("Tie.\n");
            break;

        case 3:
            while(!isCompleted(tabela)) {
                System.out.println("\n\n");
                int bestMove;

                //AI a jogar

                bestMove = ai.getOptimalMove(tabela);
                System.out.print("Movimento do MCTS :" + bestMove);
                board = play(board, 'x', bestMove);
                board2 = play(board2, 'o', bestMove);
                System.out.println(bestMove);
                ai.update(bestMove, 'x');


                System.out.println("\n\n");
                printTabela(board);
                System.out.println("0 1 2 3 4 5 6");


                if(checkWinners(board, 'x')) {
                    System.out.println("MCTS won");
                    return;
                }

                if(checkWinners(board, 'o')) {
                    System.out.println("Alpha_Beta won");
                    return;
                }
                //Jogador
                int bestMoveA = alpha_beta_play(board, 9, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                int pos = board.bestY;
                System.out.println("Movimento do Alpha Beta :");
                board = play(board, 'o', pos);
                board2 = play(board2, 'x', pos);
                ai.update(pos, 'o');

                System.out.println("\n\n");
                printTabela(board);
                System.out.println("0 1 2 3 4 5 6");

                if(checkWinners(board2, 'x')) {
                    System.out.println("AlphaBeta won");
                    return;
                }

                if(checkWinners(board2, 'o')) {
                    System.out.println("MCTS won");
                    return;
                }

            }
            System.out.println("Tie.\n");
            break;

        }
    }
}

}
