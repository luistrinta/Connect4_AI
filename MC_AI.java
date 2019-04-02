// Monte Carlo Tree Search AI for Connect 4

import java.util.ArrayList;
import java.util.Scanner;

public class MC_AI extends Algoritmos {
    private Node root; // estado inicial

    private static final double EXPLORATION_PARAMETER = Math.sqrt(2);

    private long execTime;

    public MC_AI(Tabela tabela, long execTime) {
        this.execTime = execTime;
        Tabela newtabela = new Tabela(tabela);
        root = new Node(null, newtabela);
    }

    public Tabela auxPlay(Tabela t, int move) {
        if(t.lastplayer == 'x') {
            return play(t, 'o', move);
        } else return play(t, 'x', move);
    }

    // sets root to new tabela state given move
    public void update(int move, char player) {


        if(root.children[move] != null) {
            root = root.children[move];
        } else root = new Node(null, play(root.tabela, player, move));
    }

    // returns the optimal move for the current player
    public int getOptimalMove(Tabela t) {
        int counter = 0;

        for (long stop = System.currentTimeMillis() + execTime; stop > System.currentTimeMillis();) {

            Node selectedNode = select();
            counter++;
            if(selectedNode == null)
                continue;

            Node expandedNode = expand(selectedNode);
            double result = simulate(expandedNode);
            backpropagate(expandedNode, result);
        }

        int maxIndex = -1;

        for(int i = 0; i < 7; i++) {
            if(root.children[i] != null) {
                if(maxIndex == -1 || root.children[i].visits > root.children[maxIndex].visits )
                    maxIndex = i;
                // System.out.printf("\nlocation%d: p1wins: %f/%d = %f", i, root.children[i].player1Wins, root.children[i].visits, root.children[i].player1Wins / root.children[i].visits);
            }
        }
        System.out.println("Numero de nos gerados " + counter);

        return maxIndex;
    }




    private Node select() {
        return select(root);
    }

    private Node select(Node parent) {
        // if parent has at least child without statistics, select parent
        for(int i = 0; i < 7; i++) {
            if(parent.children[i] == null && canPlace(parent.tabela, i)) {
                return parent;
            }
        }
        // if all children have statistics, use UCT to select next node to visit

        double maxSelectionVal = -1;
        int maxIndex = -1;

        for(int i = 0; i < 7; i++) {
            if(!canPlace(parent.tabela, i))
                continue;
            Node currentChild = parent.children[i];
            double wins ;
            if(parent.tabela.lastplayer == 'o' || parent.tabela.lastplayer == 'N')wins = currentChild.player1Wins;

            else wins = (currentChild.visits - currentChild.player1Wins);

            double selectionVal = wins / currentChild.visits + EXPLORATION_PARAMETER * Math.sqrt(Math.log(parent.visits) / currentChild.visits); //Cálculo do melhor filho

            if(selectionVal > maxSelectionVal) {
                maxSelectionVal = selectionVal;
                maxIndex = i;
            }
        }
        //System.out.println(maxSelectionVal + "at pos " + maxIndex);
        // SOMETIMES -1???
        if(maxIndex == -1)
            return null;
        return select(parent.children[maxIndex]);
    }

    private Node expand(Node selectedNode) {
        // get unvisited child nodes
        ArrayList<Integer> unvisitedChildrenIndices = new ArrayList<Integer>(7);
        for(int i = 0; i < 7; i++) {
            if(selectedNode.children[i] == null && canPlace(selectedNode.tabela, i)) {
                unvisitedChildrenIndices.add(i);
            }
        }

        // randomly select unvisited child and create node for it
        int selectedIndex = unvisitedChildrenIndices.get((int)(Math.random() * unvisitedChildrenIndices.size()));
        selectedNode.children[selectedIndex] = new Node(selectedNode, auxPlay(selectedNode.tabela, selectedIndex));
        return selectedNode.children[selectedIndex];
    }

    // returns result of simulation
    private double simulate(Node expandedNode) { //simulate

        Tabela simulationTabela = new Tabela(expandedNode.tabela);


        while(!isCompleted(simulationTabela) && !checkWinners(simulationTabela, 'o') && !checkWinners(simulationTabela, 'x')) {
            int rand = (int)(Math.random() * 7);

            if(canPlace(simulationTabela, rand)) {
                if(simulationTabela.lastplayer == 'o' || simulationTabela.lastplayer == 'N')
                    simulationTabela = play(simulationTabela, 'x', rand);

                else simulationTabela = play(simulationTabela, 'o', rand);

            }

            if(checkWinners(simulationTabela, 'o')) {

                return 0;
            }

            if(checkWinners(simulationTabela, 'x')) {

                return 1;
            }

        }
        return 0.5;
    }

    private void backpropagate(Node expandedNode, double simulationResult) {
        Node currentNode = expandedNode;
        while(currentNode != null) {
            currentNode.visits++;
            currentNode.incrementPlayer1Wins(simulationResult);
            currentNode = currentNode.parent;
        }
    }

    private class Node {
        private Node parent;
        // children[i] represents the next game state in which current player places disc at location i
        private Node[] children;
        private int visits;
        private double player1Wins;
        private final Tabela tabela;

        public Node(Node parent, Tabela tabela) {
            this.parent = parent;
            this.tabela = new Tabela(tabela);
            this.visits = 0;
            this.player1Wins = 0;
            children = new Node[7];
        }

        public int incrementVisits() {
            return ++visits;
        }
        public double incrementPlayer1Wins(double result) {
            player1Wins += result;
            return player1Wins;
        }
    }
    static Tabela playerMovementMC(Tabela t, char simbolo, MC_AI test) {
        Tabela t0 = new Tabela(t);
        System.out.print("Jogador " + simbolo + ": ");
        int move = scan.nextInt();
        while(move < 0 || move > 6 || !isAvailable(t, move)) {
            System.out.println("Movimento inválido.\nJogador " + simbolo + ": ");
            move = scan.nextInt();
        }
        test.update(move, simbolo);
        return play(t, simbolo, move);
    }
    static Scanner scan = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("Por favor insira o tempo de execução para o MCTS em segundos:");
        int tempo = scan.nextInt() * 1000;
        final long GIVEN_TIME = tempo;
        Scanner scan = new Scanner(System.in);

        Tabela tabela = new Tabela();
        Tabela board = new Tabela();
        Tabela board2 = new Tabela();
        tabela.lastplayer = 'N';

        MC_AI ai = new MC_AI(tabela, GIVEN_TIME);

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
                int prof = scan.nextInt();

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

