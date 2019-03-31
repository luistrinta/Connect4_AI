import java.util.*;
import java.lang.*;
import java.util.Random;
public class Node extends Tabela {
   
   public double visits; // number of times visited  N(v)
   public double reward; // accumulated reward value Q(v)
   public Node parent; // null if root
   public Node[] filhos;
    public boolean[] expanded ;
    Tabela tabela;


    Node() {
        expanded = new boolean[7];
        Arrays.fill(expanded, false);
        visits = 1;
        reward = 0;
        parent = null;
        filhos = new Node[7];
        Arrays.fill(filhos,null);
        tabela = new Tabela();
    }

    Node(Tabela t) {
        expanded = new boolean[7];
        Arrays.fill(expanded, false);
        visits = 1;
        reward = 0;
        parent = null;
        filhos = new Node[7];
        Arrays.fill(filhos,null);
        tabela = new Tabela(t);
    }



    Node(Tabela t, Node n) {
        expanded = n.expanded.clone();
        visits = n.visits;
        reward = n.reward;
        parent = n;
        filhos = new Node[7];
        Arrays.fill(filhos,null);
        tabela = new Tabela(t);
    }




    public static int MCTS_Search(Tabela tabela) {

       Node root = new Node(tabela);

        long n = 0;

        for (long stop = System.currentTimeMillis() + 3000; stop > System.currentTimeMillis();) {
            Node selectedNode = select(root);

            Node expandedNode ; 

            if(fullyExpanded(root)) {
               expandedNode = expandNode(root);
            }

             else expandedNode = expandNode(selectedNode);

            double resultado = simulate(expandedNode);
            //                    System.out.println(resultado);

            backPropagate(expandedNode, resultado);

        }

        int indexMax = -1;
        for(int i = 0; i < 7; i++) {
            if(root.filhos[i] != null) {
                if(indexMax == -1 || root.filhos[i].visits > root.filhos[indexMax].visits)
                    indexMax = i;
                System.out.printf("\nlocation" + i + ": p1wins: " + root.filhos[i].reward + "/" + root.filhos[i].visits + " = " + root.filhos[i].reward / root.filhos[i].visits);
            }
        }
        // System.out.println();*/
        return indexMax;
    }



    public static boolean fullyExpanded(Node n) {

        for(int i = 0 ; i < 7 ; i++) {

            if(!n.expanded[i]) {
                //System.out.println("This nigga false at " + i);
                return false;
            }
        }


        return true;
    }



    public static Node select(Node selectedNode) {

        for(int i = 0; i < 7; i++) {
            if(selectedNode.filhos[i] == null && canPlace(selectedNode.tabela, i)) {
                return selectedNode;
            }
        }
        //Se os filhos ja tiverem dados , vemos qual o proximo a abordar
        double valorMax = -1;
        int indexMax = -1;
        for(int i = 0; i < 7; i++) {

            //se não conseguirmos introduzir um valor numa determinada coluna , continuamos
            if(!canPlace(selectedNode.tabela, i))
                continue;

            Node currentChild = selectedNode.filhos[i];
            double wins = selectedNode.tabela.lastplayer == 'o'
                          ? currentChild.reward
                          : currentChild.visits - currentChild.reward;


            double valorSelecao = wins / currentChild.visits + Math.sqrt(2) * Math.sqrt(2 * Math.log(selectedNode.visits) / currentChild.visits); // UCT

            if(valorSelecao > valorMax) {
                valorMax = valorSelecao;
                indexMax = i;
            }
        }

        // Ás VEZES DÁ -1 ?
        if(indexMax == -1)
            return null;

        return select(selectedNode.filhos[indexMax]);
    }



    public static Node expandNode(Node selectedNode) {

        //Recebe uma lista com os filhos não visitados
        ArrayList<Integer> unvisitedChildren = new ArrayList<Integer>(7);

        for(int i = 0 ; i < 7 ; i++) {
            if(selectedNode.filhos[i] == null && canPlace(selectedNode.tabela, i )) {
                unvisitedChildren.add(i);
            }
        }

        //Escolher um filho aleatório e criar um nó para ele
        int selectedIndex = unvisitedChildren.get((int)(Math.random() * unvisitedChildren.size()));
        //System.out.println(selectedIndex);//teste


        if(selectedNode.tabela.lastplayer == 'N') {
            selectedNode.filhos[selectedIndex] = new Node(play(selectedNode.tabela, 'x', selectedIndex), selectedNode);
            return selectedNode.filhos[selectedIndex];
        }

        else if(selectedNode.tabela.lastplayer == 'x') {
            selectedNode.filhos[selectedIndex] = new Node(play(selectedNode.tabela, 'o', selectedIndex), selectedNode);
            return selectedNode.filhos[selectedIndex];
        }

        else if(selectedNode.tabela.lastplayer == 'o') {
            selectedNode.filhos[selectedIndex] =  new Node(play(selectedNode.tabela, 'x', selectedIndex), selectedNode);
            return selectedNode.filhos[selectedIndex];
        }


        //retorna o filho criado
        return selectedNode.filhos[selectedIndex];
    }




    public static double simulate(Node expandedNode) { //simulate

        Tabela simulationTabela = new Tabela(expandedNode.tabela);


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

        return 0.5;
    }


    public static void backPropagate(Node expandedNode, double resultado) {
        Node currentNode = expandedNode;
        while(currentNode.parent != null) {
            currentNode.incrementVisits();
            currentNode.reward +=resultado;
            currentNode = currentNode.parent;
        }
    }

    public static boolean canPlace(Tabela t, int i) {
        if(t.arr[0][i] == '-')return true;

        else return false;
    }


    //Auxiliar do Expand

    public static ArrayList<Integer> getExpandMoves(Tabela t) {
        ArrayList<Integer> actions = new ArrayList<Integer>();
        for(int j = 0; j < 7; j++)
            if(t.arr[0][j] == '-')
                actions.add(j);
        return actions;
    }

    public void incrementVisits(){
        this.visits++;
    }

    public void incrementReward(int points){
        this.reward +=points;
        
    }





    public static void main(String[] args) {
        Tabela t = new Tabela();
        while(!isCompleted(t)) {


            t = playerMovement(t, 'o' );
            printTabela(t);
            System.out.println("0 1 2 3 4 5 6");



            if(checkWinners(t, 'x')) {
                System.out.println("Player x won");
                return;
            }

            if(checkWinners(t, 'o')) {
                System.out.println("Player o won");
                return;
            }

            System.out.println("Computador jogou na :" + t.bestY);
            t = play(t, 'x', MCTS_Search(t));
            printTabela(t);
            System.out.println("0 1 2 3 4 5 6");



            if(checkWinners(t, 'x')) {
                System.out.println("Player x won");
                return;
            }

            if(checkWinners(t, 'o')) {
                System.out.println("Player o won");
                return;
            }

        }

        System.out.println("Não há vencedores");

    }
}