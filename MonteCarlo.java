    /*-----------------------------------------------------------------
    |    Implementação do algoritmo Monte Carlo Tree Search (MCTS)    |  
    |    Devido ao numero de classes auxiliares utilizadas decidimos  |
    |    que seria melhor por questoes de organização a criação de    |
    |    um ficheiro separado para o algoritmo em questão             |
    -----------------------------------------------------------------*/

import java.util.*; 


public class MonteCarlo extends Algoritmos {
    private Node root; // estado inicial da arvore
    private static final double CONSTANTE = Math.sqrt(2);  //Constante utilizada no Upper Confidence Bound
    private long tempoExec; //tempo de execução do ciclo responsavel pelo MCTS

    public MonteCarlo(Tabela tabela, long tempoExec) {  //Construtor da classe MonteCarlo , com isto conseguimos utilizar o MonteCralo no ficheiro Connect4  
        this.tempoExec = tempoExec; 
        Tabela newtabela = new Tabela(tabela);
        root = new Node(null, newtabela);
    }

    //Classe privada Node utilizada apenas dentro desta classe , responsável pelo funcionamento da tree

    private class Node {
   


        private Node parent;                
        private Node[] children;        //array de filhos de um certo nó.O Node[] children representa as localizações onde se pode jogar na proxima jogada
        private int visits;             //numero de vezes que foi visitado
        private double victory;         //numero de vitorias
        private final Tabela tabela;    


        //Construtor de um Node 

        public Node(Node parent, Tabela tabela) {
            this.parent = parent;
            this.tabela = new Tabela(tabela);
            this.visits = 0;
            this.victory = 0;
            children = new Node[7];
        }

        //Funções auxiliares da BackPropagation

        public int incrementVisits() {
            return ++visits;
        }
        public double incrementVictory(double result) {
            victory += result;
            return victory;
        }

    }


    public Tabela auxPlay(Tabela t, int move) {  //Método auxiliar utilizado no MCTS para facilitar o funcionamento do algoritmo na expansão dos Nodes
        if(t.lastplayer == 'x') {
            return play(t, 'o', move);
        } else return play(t, 'x', move);
    }

    // Atualiza a tabela da root definida no construtor MonteCarlo que será utilizado na main para podermos utilizar o minimax sem a utilização de extends 
    public void update(int move, char player) {

        if(root.children[move] != null) {
            root = root.children[move];
        } else root = new Node(null, play(root.tabela, player, move));

    }

    // INICIO MONTE CARLO: Retorna a melhor jogada

    public int getOptimalMove(Tabela t) {
        int counter = 0;

        for (long stop = System.currentTimeMillis() + tempoExec; stop > System.currentTimeMillis();) { //Tempo de execução definido

            Node selectedNode = select();   //Seleciona o no que iremos analisar
            if(selectedNode == null)    //caso o nó esteja vazio continuamos
                continue;               
            
            counter++;

            Node expandedNode = expand(selectedNode); //Expandimos o nó selecionado
            double result = simulate(expandedNode);   //Simulamos(rollout) um resultado para aquela expansão do nó  
            backpropagate(expandedNode, result);      //Aplicamos backPropagation para retornarmos ao root   
        }
        
        /*Quando é atingido o limite de tempo verificamos o melhor filho da root */
        int maxIndex = -1; //Index inicial

        for(int i = 0; i < 7; i++) {
            if(root.children[i] != null) { //Caso o filho exista
                if(maxIndex == -1 || root.children[i].visits > root.children[maxIndex].visits ) //se o numero de visitas foi maior , significa que é a melhor opção de escolha
                    maxIndex = i;
                
            }
        }
        System.out.println("Numero de nos gerados :" + counter);
        counter = 0;
        return maxIndex;  // Finalmente retorna a melhor posição 
    }


    //SELEÇÃO

    private Node select() {
        return select(root);  //Começamos a seleção pela root 
    }

    private Node select(Node parent) {
        // Se o pai apresentar algum filho sem dados definidos , retorna o pai  
        for(int i = 0; i < 7; i++) {
            if(parent.children[i] == null && canPlace(parent.tabela, i)) {
                return parent;
            }
        }
        // Se todos os filhos foram expandidos , usamos UCB para escolhermos o proximo nó a visitar

        double maxSelectionVal = -1;
        int maxIndex = -1;

        for(int i = 0; i < 7; i++) {
            if(!canPlace(parent.tabela, i))
                continue;
            Node currentChild = parent.children[i];
            double wins ;
            if(parent.tabela.lastplayer == 'o' || parent.tabela.lastplayer == 'N')wins = currentChild.victory;

            else wins = (currentChild.visits - currentChild.victory);

            double selectionVal = wins / currentChild.visits + CONSTANTE * Math.sqrt(Math.log(parent.visits) / currentChild.visits); //Cálculo do melhor filho

            if(selectionVal > maxSelectionVal) {
                maxSelectionVal = selectionVal;
                maxIndex = i;
            }
        }
                //System.out.println(maxSelectionVal + "at pos " + maxIndex);
        

        // Ás vezes dá -1 
        if(maxIndex == -1)
            return null;

        return select(parent.children[maxIndex]); // aplicamos o select novamente caso este filho nao seja uma folha , desta forma 
    }


     //EXPANSÃO

    private Node expand(Node selectedNode) {
        // Lista dos nos não visitados
        ArrayList<Integer> unvisitedChildren = new ArrayList<Integer>(7);
        for(int i = 0; i < 7; i++) {
            if(selectedNode.children[i] == null && canPlace(selectedNode.tabela, i)) {
                unvisitedChildren.add(i);
            }
        }

        // Seleciona um filho aleatoriamente e cria um no para o mesmo 

        int selectedIndex = unvisitedChildren.get((int)(Math.random() * unvisitedChildren.size()));
        
        selectedNode.children[selectedIndex] = new Node(selectedNode, auxPlay(selectedNode.tabela, selectedIndex)); //cria o nó do filho com uma jogada aleatória
        
        return selectedNode.children[selectedIndex]; // retorna o nó filho para realizar a simulação
    }


    //SIMULAÇÃO

    // retorna o resultado da simulação
    private double simulate(Node expandedNode) { 

        Tabela simulationTabela = new Tabela(expandedNode.tabela);

        //Enquanto o tabuleiro não estiver completo iremos realizar jogadas aleatórias
        while(!isCompleted(simulationTabela) && !checkWinners(simulationTabela, 'o') && !checkWinners(simulationTabela, 'x')) {
            int rand = (int)(Math.random() * 7); //selecionar um valor aleatorio

            if(canPlace(simulationTabela, rand)) {  //se podermos realizar a jogada aleatoria no tabuleiro este realiza a jogada
                if(simulationTabela.lastplayer == 'o' || simulationTabela.lastplayer == 'N')
                    simulationTabela = play(simulationTabela, 'x', rand);

                else simulationTabela = play(simulationTabela, 'o', rand);

            }

            if(checkWinners(simulationTabela, 'o')) {
                                                            //Em caso de perda retorna 0
                return 0;
            }

            if(checkWinners(simulationTabela, 'x')) {   
                                                            //Em caso de vitória retorna 1
                return 1;
            }

        }
        return 0;                                         //Em caso de empate retorna 0
    
  
    
    }

    
    //RETROPROPAGAÇÃO

    /*realiza a retropropagação até á raiz , incrementando o victory e
     as visitas dos nós acima do expandedNode até á root*/
    private void backpropagate(Node expandedNode, double simulationResult) {
        Node currentNode = expandedNode;
        while(currentNode != null) {
            currentNode.visits++;                            //Aumenta o numero de visitas
            currentNode.incrementVictory(simulationResult);  //Incrementa o numero de vitorias
            currentNode = currentNode.parent;                //Passa para o Node Pai
        }
    }


    
}

