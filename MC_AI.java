// Monte Carlo Tree Search AI for Connect 4

import java.util.ArrayList;
import java.util.Scanner;

public class MC_AI extends Algoritmos
{
    private Node root; // starting state
    private final int width;
    private static final double EXPLORATION_PARAMETER = Math.sqrt(2);
    private long givenTime;

    public MC_AI(Tabela board, long givenTime)
    {
        this.width = 7;
        this.givenTime = givenTime;
        Tabela newboard = new Tabela(board);
        root = new Node(null, newboard);
    }

    public Tabela auxPlay(Tabela t, int move)
    {
        if(t.lastplayer == 'x')
        {
            return play(t, 'o', move);
        }
        else return play(t, 'x', move);
    }

    // sets root to new board state given move
    public void update(int move)
    {   
      System.out.println();
        root = root.children[move] != null
               ? root.children[move]
               : new Node(null, auxPlay(root.board, move));
    }

    // returns the optimal move for the current player
    public int getOptimalMove()
    {     
        for (long stop = System.currentTimeMillis() + 500; stop > System.currentTimeMillis();)
        { 
          
            Node selectedNode = select();
            if(selectedNode == null)
                continue;
              
            Node expandedNode = expand(selectedNode);
              
            double result = simulate(expandedNode);
      
            backpropagate(expandedNode, result);
        }

        int maxIndex = -1;
        for(int i = 0; i < width; i++)
        {
            if(root.children[i] != null)
            {
                if(maxIndex == -1 || root.children[i].visits > root.children[maxIndex].visits )
                    maxIndex = i;
                System.out.printf("\nlocation%d: p1wins: %f/%d = %f", i, root.children[i].player1Wins, root.children[i].visits, root.children[i].player1Wins / root.children[i].visits);
            }
        }
        // System.out.println();
        return maxIndex;
    }


    
    public static boolean canPlace(Tabela t, int  i)
    {
        if(t.arr[0][i] == '-')return true;

        return false;
    }
    private Node select()
    {
        return select(root);
    }

    private Node select(Node parent)
    {
        // if parent has at least child without statistics, select parent
        for(int i = 0; i < width; i++)
        {
            if(parent.children[i] == null && canPlace(parent.board, i))
            {
                return parent;
            }
        }
        // if all children have statistics, use UCT to select next node to visit
        double maxSelectionVal = -1;
        int maxIndex = -1;
        for(int i = 0; i < width; i++)
        {
            if(!canPlace(parent.board, i))
                continue;
            Node currentChild = parent.children[i];
            double wins = 0;
            if(parent.board.lastplayer == 'x')wins = currentChild.player1Wins;

            else wins = (currentChild.visits - currentChild.player1Wins);

            double selectionVal = wins / currentChild.visits + EXPLORATION_PARAMETER * Math.sqrt(Math.log(parent.visits) / currentChild.visits); //Cálculo do melhor filho

            if(selectionVal > maxSelectionVal)
            {
                maxSelectionVal = selectionVal;
                maxIndex = i;
            }
        }
        // SOMETIMES -1???
        if(maxIndex == -1)
            return null;
        return select(parent.children[maxIndex]);
    }

    private Node expand(Node selectedNode)
    {
        // get unvisited child nodes
        ArrayList<Integer> unvisitedChildrenIndices = new ArrayList<Integer>(width);
        for(int i = 0; i < width; i++)
        {
            if(selectedNode.children[i] == null && canPlace(selectedNode.board, i))
            {
                unvisitedChildrenIndices.add(i);
            }
        }

        // randomly select unvisited child and create node for it
        int selectedIndex = unvisitedChildrenIndices.get((int)(Math.random() * unvisitedChildrenIndices.size()));
        selectedNode.children[selectedIndex] = new Node(selectedNode, auxPlay(selectedNode.board, selectedIndex));
        return selectedNode.children[selectedIndex];
    }

    // returns result of simulation
    public static double simulate(Node expandedNode) { //simulate

        Tabela simulationTabela = new Tabela(expandedNode.board);


        while(!isCompleted(simulationTabela)) {
            int rand = (int)(Math.random() * 7);

            if(canPlace(simulationTabela, rand)) {

                if(simulationTabela.lastplayer == 'o') {
                    simulationTabela = play(simulationTabela, 'x', rand);
                } else if(simulationTabela.lastplayer == 'x') {
                    simulationTabela = play(simulationTabela, 'o', rand);
                } else if(simulationTabela.lastplayer == 'N') {
                    simulationTabela = play(simulationTabela, 'x', rand);
                }

            }

            if(checkWinners(simulationTabela, 'o')) return 0;

            if(checkWinners(simulationTabela, 'x')) return 1;
        }

        return 0;
    }

    private void backpropagate(Node expandedNode, double simulationResult)
    {
        Node currentNode = expandedNode;
        while(currentNode != null)
        {
            currentNode.incrementVisits();
            currentNode.incrementPlayer1Wins(simulationResult);
            currentNode = currentNode.parent;
        }
    }

    private class Node
    {
        private Node parent;
        // children[i] represents the next game state in which current player places disc at location i
        private Node[] children;
        private int visits;
        private double player1Wins;
        private final Tabela board;

        public Node(Node parent, Tabela board)
        {
            this.parent = parent;
            this.board = board;
            this.visits = 0;
            this.player1Wins = 0;
            children = new Node[width];
        }

        public int incrementVisits()
        {
            return ++visits;
        }
        public double incrementPlayer1Wins(double result)
        {
            player1Wins += result;
            return player1Wins;
        }
    }

    public static void main(String[] args)
    {
        final long GIVEN_TIME = 2000;
        Scanner scan = new Scanner(System.in);

        Tabela board = new Tabela();
       
        MC_AI ai = new MC_AI(board, GIVEN_TIME);

        while(!isCompleted(board))
        {
            System.out.println("\n\n");
            printTabela(board);

            int moveColumn;

            //Jogador
            board = playerMovement(board, 'o');
            System.out.println("\n\n");
            printTabela(board);

            if(checkWinners(board,'x')){
                System.out.println("Player x won");
                return;
            }

            if(checkWinners(board,'o')){
                System.out.println("Player o won");
                return;
            }

            //AI a jogar
            System.out.print("Movimento do computador :");

            moveColumn = ai.getOptimalMove();
            System.out.println(moveColumn);
            board = play(board,'x', moveColumn);

            System.out.println("\n\n");
            printTabela(board);

            if(checkWinners(board,'x')){
                System.out.println("Player x won");
                return;
            }

            if(checkWinners(board,'o')){
                System.out.println("Player o won");
                return;
            }

            ai.update(moveColumn);
        }
      System.out.println("Tie.\n");
    }
}

