import java.util.*;
import java.lang.*;
import java.util.Random;
public class Node extends Tabela
    {

        int action;
        int visits; // number of times visited  N(v)
        int reward; // accumulated reward value Q(v)
        Node parent; // null if root
        LinkedList<Node> filhos;
        Tabela tabela;


        Node(){
            visits = 0;
            reward = 0;
            parent = null;
            filhos = new LinkedList<Node>();
            tabela = new Tabela();
        }

        Node(Tabela t)
        {
            visits = 0;
            reward = 0;
            parent = null;
            filhos = new LinkedList<Node>();
            tabela = new Tabela(t);
        }



        Node(Tabela t, Node n)
        {
            visits = 0;
            reward = 0;
            parent = n;
            filhos = new LinkedList<Node>();
            tabela = new Tabela(t);
        }


       
        // update node and backpropagate to parent

        void addChild(Node child)
        {
            this.filhos.add(child);   // add child node
        }



        //legal actions
        public  ArrayList<Integer> movimentosPossiveis()
        {

            ArrayList<Integer> filhos = new ArrayList<Integer>();

            for(int i = 0 ; i < 7 ; i++ )
            {
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
        //	else draw





        public static int MCTS_Search(Tabela tabela)
        {   long n =0;
            Node root = new Node(tabela);
            while(n < 1)
            {

                Node next = treePolicy(root);
                int valorização = defaultPolicy(next.tabela); //valorização do tabela do filho
                backUp(next, valorização);
                n++;
            }
            return bestChild(root,0).tabela.lastMoveY;
        }


        public static Node treePolicy(Node n)
        {
            while(!isCompleted(n.tabela) || !checkWinners(n.tabela, 'x') || !checkWinners(n.tabela, 'o'))
            {   
                System.out.println("Inside treePolicy");
                if(!fullyExpanded(n))
                {
                   expand(n);
                }
                else
                {
                    n = bestChild(n , n.reward);
                }
            }
            return n;
        }

        //Auxiliar da treePolicy


        public static boolean fullyExpanded(Node n)
        {
            Node n0 = new Node(n.tabela , n);
            for(int i : n.tabela.getMoves())
            {
                if(n.tabela.lastplayer == 'o')
                    n0.tabela = play(n.tabela, 'x', i);

                else if(n.tabela.lastplayer == 'x')
                    n0.tabela = play(n.tabela, 'o', i);

                if(!(n.filhos.contains(n0)))
                {   
                    System.out.println("Nah");
                    return false;
                }
            }
            return true;
        }



        public static Node expand(Node n)
        {   Random rand = new Random();
            //Expande o 'x' e de seguida simula uma jogada 'x' e uma 'o'
            int randomNum = rand.nextInt(n.tabela.getMoves().size());
            int aleatoria = getExpandMoves(n.tabela).get(randomNum);//escolhe um numero aleatório das jogadas possiveis
            Node novoFilho = new Node(n.tabela , n) ;
            if(n.tabela.lastplayer == 'o') //Caso a ultima jogada tenha sido o , ele vai jogar a contrária
            {
                (novoFilho.tabela) = play(n.tabela, 'x', aleatoria);
            }

            else if(n.tabela.lastplayer == 'x')//Caso a ultima jogada tenha sido x , ele vai jogar a contrária
            {
                 novoFilho.tabela = play(n.tabela, 'o', aleatoria);
            }

            n.filhos.add(novoFilho);
            return novoFilho;
        }

        //Auxiliar do expand

        public static ArrayList<Integer> getExpandMoves(Tabela t)
        {
            ArrayList<Integer> actions = new ArrayList<Integer>();
            for(int j = 0; j < 7; j++)
                if(t.arr[0][j] == '-')
                    actions.add(j);
            return actions;
        }

        public static double calculateScore(Node n0, Node n,int val)
        {

            return ((n0.reward / n.visits) + val * Math.sqrt((2 * Math.log(n.visits)) / n0.visits));
        }

        public static Node bestChild(Node n, int val)
        {
            Node best = new Node();

            for(Node n0 : n.filhos)
            {
                if(calculateScore(n0, n , val) > calculateScore(best, n , val))
                {
                    best = n0;
                }
            }
            return best; //fórmula matemática
        }

        public static Node backUp(Node n, int val)
        {
            while(n != null)
            {
                n.visits++;
                n.reward += n.parent.reward;
                n = n.parent;
            }
            return n;
        }

        public static int defaultPolicy(Tabela t)
        {

            while(!isCompleted(t) || !checkWinners(t, 'x') || !checkWinners(t ,'o') )
            {   Random rand = new Random();
                int randomNum = rand.nextInt(getExpandMoves(t).size());
                int aleatoria = (getExpandMoves(t)).get(randomNum);//escolhe um numero aleatório das jogadas possiveis
                if(t.lastplayer == 'o') //Caso a ultima jogada tenha sido o , ele vai jogar a contrária
                {
                    Tabela t0 = play(t, 'x', aleatoria);
                }

                else if(t.lastplayer == 'x')//Caso a ultima jogada tenha sido x , ele vai jogar a contrária
                {
                    Tabela t0 = play(t, 'o', aleatoria);
                }

            }
            if(checkWinners(t, 'x'))
            {
                return 1;
            }

            if(checkWinners(t, 'o'))
            {
                return -1;
            }
            return 0;
        }
    

    public static void main(String[] args) {
        
        Tabela t = new Tabela();
        
   }
}