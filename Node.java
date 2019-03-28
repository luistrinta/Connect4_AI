import java.util.*;
import java.lang.*;
import java.util.Random;
public class Node extends Tabela {

    int action;
    int visits; // number of times visited  N(v)
    int reward; // accumulated reward value Q(v)
    Node parent; // null if root
    LinkedList<Node> filhos;
    boolean[] expanded ;
    Tabela tabela;


    Node() {
        expanded = new boolean[7];
        Arrays.fill(expanded, false);
        visits = 1;
        reward = 0;
        parent = null;
        filhos = new LinkedList<Node>();
        tabela = new Tabela();
    }

    Node(Tabela t) {
        expanded = new boolean[7];
        Arrays.fill(expanded, false);
        visits = 1;
        reward = 0;
        parent = null;
        filhos = new LinkedList<Node>();
        tabela = new Tabela(t);
    }



    Node(Tabela t, Node n) {
        expanded = n.expanded.clone();
        visits = n.visits;
        reward = n.reward;
        parent = n;
        n.filhos.add(this);
        filhos = new LinkedList<Node>();
        tabela = new Tabela(t);
    }


    // update node and backpropagate to parent

    void addChild(Node child) {
        this.filhos.add(child);   // add child node
    }



    //legal actions
    public  ArrayList<Integer> movimentosPossiveis() {

        ArrayList<Integer> filhos = new ArrayList<Integer>();

        for(int i = 0 ; i < 7 ; i++ ) {
            if(this.arr[0][i] == '-')
                filhos.add(i);
        }
        return filhos;
    }


    //applyAction: A
    //play(tabela , 'x' , i );

    //playout:  f
    //if(isWinner(t,'x')) node reward plus 1
    //if(isWinner(t,'o')) node reward minus 1
    //  else draw





    public static int MCTS_Search(Tabela tabela) {
        long n = 0;
        Node root = new Node(tabela);



        while(n < 5000) {

            Node next = Select(root);

            int valorização = Simmulate(next.tabela); //valorização do tabela do filho

            next = backUp(next, valorização);


            n++;

        }

      


        System.out.println( bestChild(root).reward);

        return bestChild(root).tabela.bestY;
    }


    public static Node Select(Node node) {
        while(isCompleted(node.tabela) == false && checkWinners(node.tabela, 'x') == false && checkWinners(node.tabela, 'o') == false) {

            if(fullyExpanded(node) == false) {
               // System.out.println("Not fully Expanded");
                return  Expand(node);

            }

            else {
                node = bestChild(node);
            }
        }
        return node;
    }

    //Auxiliar da Select
    public static double calculateScore(Node child, Node father) {

        double val = Math.sqrt(2);


        return ((child.reward / child.visits) + val * Math.sqrt((2 * Math.log(father.visits)) / child.visits));
    }

    public static Node bestChild(Node father) {
        Node best = new Node();
            best.reward = Integer.MIN_VALUE;
        for(Node n0 : father.filhos) {
            //System.out.println(n0.reward + " " + n0.tabela.lastMoveY);
            //if(calculateScore(n0, father) > calculateScore(best, father)) {
              if(best.reward < n0.reward){
                best = n0;
                best.tabela.bestY = n0.tabela.lastMoveY;
            }
            

    }
        //printTabela(best.tabela);
        //System.out.println(best.tabela.bestY);
        return best; //fórmula matemática
}

    //verifica se tem os fihos todos  //NAO TA OPERACIONAL
    public static boolean fullyExpanded(Node n) {

        for(int i = 0 ; i < 7 ; i++) {

            if(!n.expanded[i]) {
                //System.out.println("This nigga false at " + i);
                return false;
            }
        }


        return true;
    }

    public static Node Expand(Node n) {
        Random rand = new Random();
        //Expande o 'x' e de seguida simula uma jogada 'x' e uma 'o'

        int aleatoria = getExpandMoves(n.tabela).get(rand.nextInt(n.tabela.getMoves().size()));//escolhe um numero aleatório das jogadas possiveis
        
        //System.out.println("Jogada aleatória : " + aleatoria);

        Node novoFilho = new Node(n.tabela, n) ;

        if(n.tabela.lastplayer == 'N') { //Caso seja a primeira jogada
            novoFilho.tabela = play(n.tabela, 'x', aleatoria);
            n.expanded[aleatoria] = true;

        }


        else if(n.tabela.lastplayer == 'o') { //Caso a ultima jogada tenha sido o , ele vai jogar a contrária
                (novoFilho.tabela) = play(n.tabela, 'x', aleatoria);
                n.expanded[aleatoria] = true;
        }

        else if(n.tabela.lastplayer == 'x') { //Caso a ultima jogada tenha sido x , ele vai jogar a contrária
            novoFilho.tabela = play(n.tabela, 'o', aleatoria);
            n.expanded[aleatoria] = true;
        }

        //printTabela(novoFilho.tabela);
       
        return novoFilho;
    }

    //Auxiliar do Expand

    public static ArrayList<Integer> getExpandMoves(Tabela t) {
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int j = 0; j < 7; j++)
            if(t.arr[0][j] == '-')
                actions.add(j);
        return actions;
    }


    public static Node backUp(Node n, int val) {
        //System.out.println("Backpropagation");
        while(n.parent != null) {
            n.visits++;
          //  System.out.println("Number of visits : "+n.visits);
            n.reward += val;
          //  System.out.println("Reward points : "+ n.reward);
          //  System.out.println("Table ");
          //  printTabela(n.tabela);
          //  System.out.println("Score : "+calculateScore(n,n.parent));
            n = n.parent;
        }
        return n;
    }

    public static int Simmulate(Tabela t) { //Simmulate
       
        Tabela t0 = new Tabela(t);

        while(!isCompleted(t0) && !checkWinners(t0, 'x') && !checkWinners(t0, 'o') ) {
            Random rand = new Random();
            int randomNum = rand.nextInt(getExpandMoves(t0).size());
            int aleatoria = (getExpandMoves(t0)).get(randomNum);//escolhe um numero aleatório das jogadas possiveis
            if(t0.lastplayer == 'o') {              //Caso a ultima jogada tenha sido o , ele vai jogar a contrária
                t0 = play(t0, 'x', aleatoria);
               // printTabela(t0);
            }

            else if(t0.lastplayer == 'x') { //Caso a ultima jogada tenha sido x , ele vai jogar a contrária
                t0 = play(t0, 'o', aleatoria);
                //printTabela(t0);
            }

        }
      
        if(checkWinners(t0, 'x')) {
            System.out.println("x wins");
            return 1;
        }

        if(checkWinners(t0, 'o')) {
            System.out.println("o wins");
            return -1;
        }

        else {
            System.out.println("Tie");
            return 0;
        }
    }



    public static void main(String[] args) {
        Tabela t = new Tabela();
        
        while(!isCompleted(t)) {
            

            t = playerMovement(t, 'o' );
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
            //int x = MCTS_Search(t);
            System.out.println("Computador jogou na :"+t.bestY);
            t = play(t, 'x', MCTS_Search(t));
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

        }

        System.out.println("Não há vencedores");


    }
}