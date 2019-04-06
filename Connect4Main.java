import java.util.*;


import java.util.*;

public class Connect4Main extends Algoritmos {


    public static void main(String[] args) {
        final int AI_VS_PLAYER = 1;
        final int AI_VS_AI = 2;
        final int PLAYER_VS_AI = 3;
        long GIVEN_TIME = 0;
        Scanner scan = new Scanner(System.in);
        System.out.println("1.AI vs PLAYER");
        System.out.println("2.AI vs AI");
        System.out.println("3.PLAYER VS AI");

        int play = scan.nextInt();
        switch(play) {

        //------------------------------------------AI VS PLAYER------------------------------------------------------

        case AI_VS_PLAYER :

            System.out.println("1.Minimax");
            System.out.println("2.Alpha-Beta");
            System.out.println("3.MCTS");

            int play2 = scan.nextInt();
            switch(play2) {
            case 1:
                Tabela t = new Tabela();
                System.out.println("Profundidade do Minimax :");
                int minimaxProf = scan.nextInt();

                System.out.println("\n\n");
                printTabela(t);
                System.out.println("0 1 2 3 4 5 6");


                while(!isCompleted(t)) {


                    int x = minimax_play(t, minimaxProf);
                    System.out.println("Computador jogou na :" + t.bestY);
                    t = play(t, 'x', t.bestY);
                    printTabela(t);
                    System.out.println("0 1 2 3 4 5 6");




                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("O jogador venceu");
                        return;
                    }

                    t = playerMovement(t, 'o' );
                    printTabela(t);
                    System.out.println(scoreSum(t));

                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("O jogador venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");

                break;
            case 2:
                t = new Tabela();
                System.out.println("Profundidade do Alpha-Beta :");
                int alphaBetaProf = scan.nextInt();

                System.out.println("\n\n");
                printTabela(t);
                System.out.println("0 1 2 3 4 5 6");

                while(!isCompleted(t)) {


                    int x = alpha_beta_play(t, alphaBetaProf, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    System.out.println("Computador jogou na :" + t.bestY);
                    t = play(t, 'x', t.bestY);
                    printTabela(t);
                    System.out.println("0 1 2 3 4 5 6");



                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("Jogador venceu");
                        return;
                    }

                    t = playerMovement(t, 'o' );
                    printTabela(t);
                    System.out.println(scoreSum(t));

                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("Jogador venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");

                break;
            case 3:
                System.out.println("Tempo para MCTS em segundos:");
                int tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;


                Tabela tabela = new Tabela();
                Tabela board = new Tabela();
                Tabela board2 = new Tabela();
                tabela.lastplayer = 'N';

                MonteCarlo ai = new MonteCarlo(tabela, GIVEN_TIME);


                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(board)) {
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
                    board = playerMovement(board,'o');
                    board2 = play(board2,'x',board.lastMoveY);
                   
                    System.out.println("Movimento do Alpha Beta :" + board2.lastMoveY);
                    
                    ai.update(board.lastMoveY, 'o');

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
            }
            break;


        //-------------------------------------AI VS AI-------------------------------------------------------------


        case AI_VS_AI:
            System.out.println("1.Minimax VS Minimax");
            System.out.println("2.Minimax VS Alpha-Beta");
            System.out.println("3.Minimax VS Monte Carlo");
            System.out.println("4.Alpha-Beta VS Alpha-Beta");
            System.out.println("5.Alpha-Beta VS Minimax");
            System.out.println("6.Alpha-Beta VS Monte Carlo");
            System.out.println("7.Monte Carlo VS Monte Carlo");
            System.out.println("8.Monte Carlo VS Minimax");
            System.out.println("9.Monte Carlo VS Alpha-Beta");

            int option = scan.nextInt();
            switch(option) {

            //------------------------------------Minimax vs Minimax-----------------------------------------------
            case 1:

                Tabela t1 = new Tabela();
                Tabela t2 = new Tabela();

                System.out.println("Profundidade Jogador 1");
                int prof1 = scan.nextInt();
                System.out.println("Profundidade Jogador 2");
                int prof2 = scan.nextInt();

                while(!isCompleted(t1)) {

                    int x = minimax_play(t1, prof1);
                    int bestPlay = t1.bestY;
                    System.out.println("PC 1 jogou na :" + t1.bestY);
                    t1 = play(t1, 'x', bestPlay);
                    t2 = play(t2, 'o', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");




                    if(checkWinners(t1, 'x')) {
                        System.out.println("PC1 won");
                        return;
                    }

                    if(checkWinners(t1, 'o')) {
                        System.out.println("PC2 won");
                        return;
                    }


                    x = minimax_play(t2, prof2);
                    System.out.println("PC 2 jogou na :" + t2.bestY);
                    bestPlay = t2.bestY;
                    t1 = play(t1, 'o', bestPlay);
                    t2 = play(t2, 'x', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(t2, 'x')) {
                        System.out.println("PC 2 won");
                        return;
                    }

                    if(checkWinners(t2, 'o')) {
                        System.out.println("PC 1 won");
                        return;
                    }

                }

                System.out.println("Não há vencedores");
                break;


            //-------------------------Minimax vs Alpha Beta----------------------------------------

            case 2:
                t1 = new Tabela();
                t2 = new Tabela();

                System.out.println("Profundidade Minimax");
                prof1 = scan.nextInt();
                System.out.println("Profundidade Alpha-Beta");
                prof2 = scan.nextInt();

                while(!isCompleted(t1)) {

                    int x = minimax_play(t1, prof1);
                    int bestPlay = t1.bestY;
                    System.out.println("Minimax jogou na :" + t1.bestY);
                    t1 = play(t1, 'x', bestPlay);
                    t2 = play(t2, 'o', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");




                    if(checkWinners(t1, 'x')) {
                        System.out.println("Minimax venceu");
                        return;
                    }

                    if(checkWinners(t1, 'o')) {
                        System.out.println("Alpha-Beta venceu");
                        return;
                    }


                    x = alpha_beta_play(t2, prof2, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    System.out.println("Alpha-Beta jogou na :" + t2.bestY);
                    bestPlay = t2.bestY;
                    t1 = play(t1, 'o', bestPlay);
                    t2 = play(t2, 'x', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(t2, 'x')) {
                        System.out.println("Alpha-Beta venceu");
                        return;
                    }

                    if(checkWinners(t2, 'o')) {
                        System.out.println("Minimax venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");
                break;
            //-------------------------------------Minimax VS Monte Carlo-------------------------------------------------
            case 3:

                System.out.println("Tempo para MCTS em segundos:");
                int tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;


                Tabela tabela = new Tabela();
                Tabela board = new Tabela();
                Tabela board2 = new Tabela();
                tabela.lastplayer = 'N';

                MonteCarlo ai = new MonteCarlo(tabela, GIVEN_TIME);


                System.out.println("Profundidade do Minimax :");
                int prof = scan.nextInt();

                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(board)) {
                    System.out.println("\n\n");
                    int bestMove;



                    //Alpha-Beta
                    int bestMoveA = minimax_play(board, prof);
                    int pos = board.bestY;
                    System.out.println("Movimento do Minimax :" + pos);
                    board = play(board, 'o', pos);
                    board2 = play(board2, 'x', pos);
                    ai.update(pos, 'o');

                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(board2, 'x')) {
                        System.out.println("Minimax venceu !");
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
                        System.out.println("Minimax venceu !");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;

            //-------------------------------Alpha-Beta VS Alpha-Beta-------------------------------

            case 4:

                t1 = new Tabela();
                t2 = new Tabela();

                System.out.println("Profundidade Jogador 1");
                prof1 = scan.nextInt();
                System.out.println("Profundidade Jogador 2");
                prof2 = scan.nextInt();

                while(!isCompleted(t1)) {

                    int x = alpha_beta_play(t1, prof1, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    int bestPlay = t1.bestY;
                    System.out.println("PC 1 jogou na :" + t1.bestY);
                    t1 = play(t1, 'x', bestPlay);
                    t2 = play(t2, 'o', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");




                    if(checkWinners(t1, 'x')) {
                        System.out.println("PC 1 venceu");
                        return;
                    }

                    if(checkWinners(t1, 'o')) {
                        System.out.println("PC 2 venceu");
                        return;
                    }


                    x = alpha_beta_play(t2, prof2, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    System.out.println("PC 2 jogou na :" + t2.bestY);
                    bestPlay = t2.bestY;
                    t1 = play(t1, 'o', bestPlay);
                    t2 = play(t2, 'x', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(t2, 'x')) {
                        System.out.println("PC 2 venceu");
                        return;
                    }

                    if(checkWinners(t2, 'o')) {
                        System.out.println("PC 1 venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");
                break;

            //------------------------------------------Alpha-Beta VS Minimax------------------------------------------------
            case 5:

                t1 = new Tabela();
                t2 = new Tabela();

                System.out.println("Profundidade Alpha-Beta");
                prof1 = scan.nextInt();
                System.out.println("Profundidade Minimax");
                prof2 = scan.nextInt();

                while(!isCompleted(t1)) {

                    int x = alpha_beta_play(t1, prof1, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    int bestPlay = t1.bestY;
                    System.out.println("Alpha-Beta jogou na :" + t1.bestY);
                    t1 = play(t1, 'x', bestPlay);
                    t2 = play(t2, 'o', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");




                    if(checkWinners(t1, 'x')) {
                        System.out.println("Alpha-Beta venceu");
                        return;
                    }

                    if(checkWinners(t1, 'o')) {
                        System.out.println("Minimax venceu");
                        return;
                    }


                    x = minimax_play(t2, prof2);
                    System.out.println("Minimax jogou na :" + t2.bestY);
                    bestPlay = t2.bestY;
                    t1 = play(t1, 'o', bestPlay);
                    t2 = play(t2, 'x', bestPlay);
                    printTabela(t1);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(t2, 'x')) {
                        System.out.println("Minimax venceu");
                        return;
                    }

                    if(checkWinners(t2, 'o')) {
                        System.out.println("Alpha-Beta venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");
                break;

            //-------------------------------Alpha-Beta VS Monte Carlo--------------------------------------------

            case 6:

                System.out.println("Tempo para MCTS em segundos:");
                tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;


                tabela = new Tabela();
                board = new Tabela();
                board2 = new Tabela();
                tabela.lastplayer = 'N';

                ai = new MonteCarlo(tabela, GIVEN_TIME);


                System.out.println("Profundidade do Alpha-Beta :");
                prof = scan.nextInt();

                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(board)) {
                    System.out.println("\n\n");
                    int bestMove;



                    //Alpha-Beta
                    int bestMoveA = alpha_beta_play(board, prof, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    int pos = board.bestY;
                    System.out.println("Movimento do Alpha Beta :" + pos);
                    board = play(board, 'x', pos);
                    board2 = play(board2, 'o', pos);
                    ai.update(pos, 'o');

                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(board2, 'x')) {
                        System.out.println("MCTS venceu !");
                        return;
                    }

                    if(checkWinners(board2, 'o')) {
                        System.out.println("Alpha-Beta venceu !");
                        return;
                    }
                    bestMove = ai.getOptimalMove(tabela);
                    System.out.print("Movimento do MCTS :");
                    board = play(board, 'o', bestMove);
                    board2 = play(board2, 'x', bestMove);
                    System.out.println(bestMove);
                    ai.update(bestMove, 'x');


                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(board, 'x')) {
                        System.out.println("Alpha-Beta venceu !");
                        return;
                    }

                    if(checkWinners(board, 'o')) {
                        System.out.println("MCTS venceu !");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;

            case 7:
                System.out.println("Tempo para MCTS em segundos:");
                tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;

                tabela = new Tabela();
                board = new Tabela();
                board2 = new Tabela();
                board.lastplayer = 'N';
                board2.lastplayer = 'N';

                ai = new MonteCarlo(board, GIVEN_TIME);


                System.out.println("Tempo para MCTS em segundos:");
                tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;

                MonteCarlo ai2 = new MonteCarlo(board2, GIVEN_TIME);

                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(board)) {
                    int bestMove;

                    //MCTS 1

                    bestMove = ai.getOptimalMove(board);
                    System.out.print("Movimento do MCTS :");
                    tabela = play(tabela, 'x', bestMove);
                    System.out.println(bestMove);
                    ai.update(bestMove, 'x');
                    ai2.update(bestMove, 'o');

                    System.out.println("\n\n");
                    printTabela(tabela);
                    System.out.println("0 1 2 3 4 5 6");


                    if(checkWinners(tabela, 'x')) {
                        System.out.println("MCTS venceu");
                        return;
                    }

                    if(checkWinners(tabela, 'o')) {
                        System.out.println("MCTS 2 venceu");
                        return;
                    }
                    //MCTS 2
                    int bestMoveA = ai2.getOptimalMove(board2);
                    System.out.print("Movimento do MCTS 2:");
                    tabela = play(tabela, 'o', bestMove);
                    System.out.println(bestMove);
                    ai.update(bestMove, 'o');
                    ai2.update(bestMove, 'x');

                    System.out.println("\n\n");
                    printTabela(tabela);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(tabela, 'o')) {
                        System.out.println("MCTS 2 venceu !");
                        return;
                    }

                    if(checkWinners(tabela, 'x')) {
                        System.out.println("MCTS venceu!");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;

            case 8:

                System.out.println("Tempo para MCTS em segundos:");
                tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;


                tabela = new Tabela();
                board = new Tabela();
                board2 = new Tabela();
                tabela.lastplayer = 'N';

                ai = new MonteCarlo(tabela, GIVEN_TIME);

                System.out.println("Profundidade do Minimax :");
                prof = scan.nextInt();

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
                        System.out.println("Minimax venceu");
                        return;
                    }
                    //Jogador
                    int bestMoveA = minimax_play(board, prof);
                    int pos = board.bestY;
                    System.out.println("Movimento do Minimax :" + pos);
                    board = play(board, 'o', pos);
                    board2 = play(board2, 'x', pos);
                    ai.update(pos, 'o');

                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(board2, 'x')) {
                        System.out.println("Minimax venceu !");
                        return;
                    }

                    if(checkWinners(board2, 'o')) {
                        System.out.println("MCTS venceu!");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;


            case 9:

                System.out.println("Tempo para MCTS em segundos:");
                tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;


                tabela = new Tabela();
                board = new Tabela();
                board2 = new Tabela();
                tabela.lastplayer = 'N';

                ai = new MonteCarlo(tabela, GIVEN_TIME);

                System.out.println("Profundidade do Minimax :");
                prof = scan.nextInt();

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


            }
            break;

        //---------------------------------------------PLAYER VS AI---------------------------------------------------

        case PLAYER_VS_AI:
            System.out.println("1.Minimax");
            System.out.println("2.Alpha-Beta");
            System.out.println("3.MCTS");

            play2 = scan.nextInt();
            switch(play2) {
            case 1:
                Tabela t = new Tabela();
                System.out.println("Profundidade do Minimax :");
                int minimaxProf = scan.nextInt();

                System.out.println("\n\n");
                printTabela(t);
                System.out.println("0 1 2 3 4 5 6");


                while(!isCompleted(t)) {




                    //player
                    t = playerMovement(t, 'o' );
                    printTabela(t);
                    System.out.println(scoreSum(t));

                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("O jogador venceu");
                        return;
                    }

                    int x = minimax_play(t, minimaxProf);
                    System.out.println("Computador jogou na :" + t.bestY);
                    t = play(t, 'x', t.bestY);
                    printTabela(t);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("O jogador venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");

                break;

            case 2:
                t = new Tabela();
                System.out.println("Profundidade do Alpha-Beta :");
                int alphaBetaProf = scan.nextInt();

                System.out.println("\n\n");
                printTabela(t);
                System.out.println("0 1 2 3 4 5 6");

                while(!isCompleted(t)) {

                    t = playerMovement(t, 'o' );
                    printTabela(t);
                    System.out.println(scoreSum(t));

                    System.out.println("0 1 2 3 4 5 6");



                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("Jogador venceu");
                        return;
                    }

                    int x = alpha_beta_play(t, alphaBetaProf, Integer.MIN_VALUE, Integer.MAX_VALUE, 'x');
                    System.out.println("Computador jogou na :" + t.bestY);
                    t = play(t, 'x', t.bestY);
                    printTabela(t);
                    System.out.println("0 1 2 3 4 5 6");



                    if(checkWinners(t, 'x')) {
                        System.out.println("PC venceu");
                        return;
                    }

                    if(checkWinners(t, 'o')) {
                        System.out.println("Jogador venceu");
                        return;
                    }

                }

                System.out.println("Não há vencedores");

                break;

            case 3:
                System.out.println("Tempo para MCTS em segundos:");
                int tempo = scan.nextInt() * 1000;
                GIVEN_TIME = tempo;


                Tabela tabela = new Tabela();
                Tabela board = new Tabela();
                Tabela board2 = new Tabela();
                tabela.lastplayer = 'N';

                MonteCarlo ai = new MonteCarlo(tabela, GIVEN_TIME);


                System.out.println("\nA simulação irá agora começar \n");
                while(!isCompleted(tabela)) {
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6\n");
                    int bestMove;


                    //Jogador
                    board = playerMovement(board,'o');
                    board2 = play(board2,'x',board.lastMoveY);
                    
                    ai.update(board.lastMoveY, 'o');

                    System.out.println("\n\n");
                    printTabela(board);
                    System.out.println("0 1 2 3 4 5 6");

                    if(checkWinners(board, 'x')) {
                        System.out.println("MCTS venceu");
                        return;
                    }

                    if(checkWinners(board, 'o')) {
                        System.out.println("Jogador venceu");
                        return;
                    }
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

                    if(checkWinners(board2, 'x')) {
                        System.out.println("Jogador venceu !");
                        return;
                    }

                    if(checkWinners(board2, 'o')) {
                        System.out.println("MCTS venceu!");
                        return;
                    }

                }
                System.out.println("Empate!\n");
                break;

            }

        }
    }
}


