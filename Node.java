import java.util.*;
import java.lang.*;
import java.util.Random;
public class Node extends Tabela {

    int visits; // number of times visited  N(v)
    int reward; // accumulated reward value Q(v)
    Node parent; // null if root
    Node[] filhos;
    boolean[] expanded ;
    Tabela tabela;


    Node() {
        expanded = new boolean[7];
        Arrays.fill(expanded, false);
        visits = 1;
        reward = 0;
        parent = null;
        filhos = new Node[7];
        tabela = new Tabela();
    }

    Node(Tabela t) {
        expanded = new boolean[7];
        Arrays.fill(expanded, false);
        visits = 1;
        reward = 0;
        parent = null;
        filhos = new Node[7];
        tabela = new Tabela(t);
    }



    Node(Tabela t, Node n) {
        expanded = n.expanded.clone();
        visits = n.visits;
        reward = n.reward;
        parent = n;
        filhos = new Node[7];
        tabela = new Tabela(t);
    }




    public static int MCTS_Search(Tabela tabela) {
        Node root = new Node(tabela);

        long n = 0;

        for (long stop = System.currentTimeMillis()+5000; stop>System.currentTimeMillis();) {
        Node selectedNode = select(root);

        Node expandedNode = expandNode(selectedNode);
        
                    double resultado = simulate(expandedNode.tabela);
                    //System.out.println(resultado);

                    backPropagate(expandedNode, resultado);

                }
           
        int indexMax = -1;
        for(int i = 0; i < 7; i++) {
            if(root.filhos[i] != null) {
                if(indexMax == -1 || root.filhos[i].visits > root.filhos[indexMax].visits)
                    indexMax = i;
                // System.out.printf("\nlocation%d: p1wins: %f/%d = %f", i, root.children[i].player1Wins, root.children[i].visits, root.children[i].player1Wins/root.children[i].visits);
            }
        }
        // System.out.println();*/
        return indexMax;
    }






    public static Node select(Node selectedNode) {

        for(int i = 0; i < 7; i++) {
            if(selectedNode.filhos[i] == null && canPlace(selectedNode.tabela, i)) {
                //System.out.println("Penis");
                return selectedNode;
            }
        }
        //Se os filhos ja tiverem dados , vemos qual o proximo a abordar
        double valorMax = -1;
        int indexMax = -1;
        for(int i = 0; i < 7; i++) {
            //System.out.println("Entrou");
            //se não conseguirmos introduzir um valor numa determinada coluna , continuamos
            if(!canPlace(selectedNode.tabela, i))
                continue;

            Node currentChild = selectedNode.filhos[i];
            double wins;
            if(selectedNode.tabela.lastplayer == 'o')
                wins = currentChild.reward;
            else
                wins = currentChild.visits - currentChild.reward;

            double valorSelecao = wins / currentChild.visits + Math.sqrt(2) * Math.sqrt(Math.log(selectedNode.visits) / currentChild.visits); // UCT

            if(valorSelecao > valorMax) {
                valorMax = valorSelecao;
                indexMax = i;
            }
        }
        // SOMETIMES -1???
        if(indexMax == -1)
            return null;

        return select(selectedNode.filhos[indexMax]);
    }



    public static Node expandNode(Node selectedNode) {

        //Recebe uma lista com os filhos não visitados
        ArrayList<Integer> unvisitedChildren = new ArrayList<Integer>(7);

        for(int i = 0 ; i < 7 ; i++) {
            if(selectedNode.filhos[i] == null && getExpandMoves(selectedNode.tabela).contains(i)) {
                //System.out.println("O filho na posição " + i + " não existe");
                unvisitedChildren.add(i);
            }
        }

        //Escolher um filho aleatório e criar um nó para ele
        int selectedIndex = unvisitedChildren.get((int)(Math.random() * unvisitedChildren.size()));
        //System.out.println(selectedIndex);//teste


        if(selectedNode.tabela.lastplayer == 'N') {
            selectedNode.filhos[selectedIndex] = new Node(play(selectedNode.tabela, 'x', selectedIndex), selectedNode);
        } 
        else if(selectedNode.tabela.lastplayer == 'x')
            selectedNode.filhos[selectedIndex] = new Node(play(selectedNode.tabela, 'o', selectedIndex), selectedNode);

        else if(selectedNode.tabela.lastplayer == 'o')
            selectedNode.filhos[selectedIndex] =  new Node(play(selectedNode.tabela, 'x', selectedIndex), selectedNode);


        //retorna o filho criado
        return selectedNode.filhos[selectedIndex];
    }




    public static double simulate(Tabela t) { //simulate

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
            //System.out.println("x wins");
            return 1;
        }

        if(checkWinners(t0, 'o')) {
            //System.out.println("o wins");
            return 0;
        }

        else {
           // System.out.println("Tie");
            return 0;
        }
    }


    public static void backPropagate(Node expandedNode, double resultado) {
        Node node = expandedNode;

        while(node.parent != null) {
            node.visits++;

            node.reward += resultado;

            node = node.parent;
        }

    }





    public static boolean canPlace(Tabela t, int i) {
        if(t.arr[0][i] == '-')return true;

        else return false;
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
    //Auxiliar do Expand

    public static ArrayList<Integer> getExpandMoves(Tabela t) {
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int j = 0; j < 7; j++)
            if(t.arr[0][j] == '-')
                actions.add(j);
        return actions;
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