        

        /*----------------------------------------------------------------------------------------------------------------
        //Este ficheiro contem a estrutura Tabela utilizada para a implementação do jogo dos 4 em linha (Connect Four). //
        //Juntamente com alguns métodos auxiliares para o funcionamento dos algoritmos que se encontram nos ficheiros   //
        //Algoritmos(Minimax e Alpha-Beta Prunning) e MC_AI(Monte Carlo Tree Search)                                    //
        ----------------------------------------------------------------------------------------------------------------*/

import java.util.*;
import java.lang.*;
public class Tabela {
    char[][] arr;
    int bestY;                  //melhor coluna --> utilizada para armazenar a melhor jogada no minimax e no alpha_beta_prunning
    char lastplayer;            // ultimo jogador a jogar 
   
    int lastMoveX;              //Linha onde foi colocada a ultima peça
    int lastMoveY;              //Coluna onde foi colocada a ultima peça
    char winner;                //vencedor do jogo
    
    int valor ;                 // Valor do tabuleiro

    //Construtores

    Tabela() {
        this.arr = new char[6][7];
        for(int j = 0; j < 6 ; j++) {
            for(int i = 0; i < 7 ; i++)this.arr[j][i] = '-';
        }
        this.valor = 0;
        this.lastplayer = 'N';
    }

    Tabela(Tabela t) {
        this.arr = new char[6][7];
        for(int j = 0; j < 6 ; j++) {
            for(int i = 0; i < 7 ; i++)
                this.arr[j][i] = t.arr[j][i];
        }
        this.lastMoveX = t.lastMoveX;
        this.lastMoveY = t.lastMoveY;
        this.lastplayer =t.lastplayer;
    }


    //Imprimir a tabela
    //OBS:Podiamos ter optado por realizar um override ao metodo toString para podermos fazer System.out.println(Tabela t) ao invés de usarmos um metodo auxiliar
        
        static void printTabela(Tabela t) {
        for(int j = 0; j < 6 ; j++) {
            for(int i = 0; i < 7 ; i++)System.out.print(t.arr[j][i] + " ");
            System.out.println();
        }
    }


    /*Método responsável por realizar as jogadas no tabuleiro 
    ,utilizado maioritariamente nos algoritmos de pesquisa */
   
    static Tabela play(Tabela t, char jogada, int y) {


        Tabela t0 = new Tabela(t);
        int x = 5;

        while(x >= 0 ) {
            if(t.arr[x][y] == '-')
                break;
            x--;
        }
        t0.arr[x][y] = jogada;
        t0.lastMoveY = y;
        t0.lastMoveX = x;
        t0.valor = scoreSum(t0);
        t0.lastplayer = jogada;

        return t0;

    }




    /*Metodo  que verifica se um determinado jogador 'x' OU 'o' ganho o jogo*/

    static boolean checkWinners(Tabela t, char player) {
        int counter = 0;
        if(winsLine(t, t.lastMoveX, t.lastMoveY, player))return true;
        if(winsColumn(t, t.lastMoveX, t.lastMoveY, player))return true;
        if(winsDiag(t, t.lastMoveX, t.lastMoveY, player))return true;


        return false;
    }
    
    //Verifica se existem vencedores nas diagonais

    static boolean winsDiag(Tabela t, int lastMoveX, int lastMoveY, char lastplayer) {
        int checkerTopRight = 0, checkerTopLeft = 0;

        //Diagonal top-right to bot-left
        for(int i = -3 ; i <= 3 /*maior diagonal possível*/; i++) {
            //verifica se está in bounds
            if(lastMoveX + i >= 0 && lastMoveY - i >= 0 && lastMoveX + i < 6 && lastMoveY - i < 7) {
                //char da posição a verificar é igual ao último jogador
                if(t.arr[lastMoveX + i][lastMoveY - i] == lastplayer)checkerTopRight++;

                else checkerTopRight = 0;
                //se o checker chegar a 4, quer dizer que são 4 em linha, ganha
                if(checkerTopRight == 4) {

                    return true;
                }
            } else checkerTopRight = 0;


            //Diagonal top-left to bot-right
            if(lastMoveX + i >= 0 && lastMoveY + i >= 0 && lastMoveX + i < 6 && lastMoveY + i < 7) {
                if(t.arr[lastMoveX + i][lastMoveY + i] == lastplayer)checkerTopLeft++;
                else checkerTopLeft = 0;
                if(checkerTopLeft == 4) {
                    t.winner = lastplayer;
                    return true;
                }
            } else checkerTopLeft = 0;
        }
        return false;
    }


    //Verifica se existe um vencedor na linha
    static boolean winsLine(Tabela t, int lastMoveX, int lastMoveY, char lastplayer) {

        for(int i = 0 ; i <= 3 ; i++) {
            if((t.arr[lastMoveX][i] == lastplayer) && (t.arr[lastMoveX][i + 1] == lastplayer) && (t.arr[lastMoveX][i + 2] == lastplayer) && (t.arr[lastMoveX][i + 3] == lastplayer)) {
                t.winner = lastplayer;
                return true;
            }
        }
        return false;
    }
    //Verifica se existe um vencedor na coluna
    static boolean winsColumn(Tabela t, int lastMoveX, int lastMoveY, char lastplayer) {
        for(int i = 0 ; i <= 2 ; i++) {
            if(t.arr[i][lastMoveY] == lastplayer && t.arr[i + 1][lastMoveY] == lastplayer && t.arr[i + 2][lastMoveY] == lastplayer && t.arr[i + 3][lastMoveY] == lastplayer) {
                t.winner = lastplayer;
                return true;
            }
        }
        return false;
    }


    //verifica se o tabuleiro está cheio

    static boolean isCompleted(Tabela t) {
        for (int i = 0; i < 7 ; i++ ) {
            if(t.arr[0][i] == '-')return false;

        }
        return true;
    }

    //Método reponsável pelo Movimento do Jogador humano
    
    static Tabela playerMovement(Tabela t, char simbolo) {
        Tabela t0 = new Tabela(t);
        System.out.print("Jogador " + simbolo + ": ");
        int move = scan.nextInt();
        while(move < 0 || move > 6 || !isAvailable(t, move)) {
            System.out.println("Movimento inválido.\nJogador " + simbolo + ": ");
            move = scan.nextInt();
        }
        return play(t, simbolo, move);
    }
    static Scanner scan = new Scanner(System.in);

    //Verifica se a coluna não está cheia
    
    static boolean isAvailable(Tabela t, int column) {
        return t.arr[0][column] == '-';
    }


    //Retorna uma linkedList com as possições possiveis que a proxima jogada pode ocupar
    /*Optamos pela utilização de uma LinkedList por ser mais facil de utilizar num ciclo forEach*/

    public LinkedList<Integer> getMoves() {
        LinkedList<Integer> actions = new LinkedList<Integer>();
        for(int j = 0; j < 7; j++)
            if(this.arr[0][j] == '-')
                actions.add(j);
        return actions;
    }


    /*Função auxiliar da ScoreSum que nos retorna o valor do tabuleiro , de acordo 
    com os contadores xcounter(ocorencias de x) e ocounter(ocorrencia de o)*/
    
    static int pontos(int xcounter, int ocounter) {
        if(xcounter == 4 && ocounter == 0)
            return 512;
        else if(xcounter == 3 && ocounter == 0)
            return 50;
        else if(xcounter == 2 && ocounter == 0)
            return 10;
        else if(xcounter == 1 && ocounter == 0)
            return 1;
        else if(ocounter == 1 && xcounter == 0)
            return -1;
        else if(ocounter == 2 && xcounter == 0)
            return -10;
        else if(ocounter == 3 && xcounter == 0)
            return -50;
        else if(ocounter == 4 && xcounter == 0)
            return -512;
        else
            return 0;
    }
    

    //Método responsável pelo calculo do valor do tabuleiro

    public static  int scoreSum(Tabela t) {
        int xcounter = 0; //Guarda o numero de ocorrencias do 'x'
        int ocounter = 0; // Gaurda o numero de ocorrencias do 'o'
        int total = 0; //Armazenamos o valor total da Score do tabuleiro
        int l = 6 ; //numero de linhas
        int c = 7;  //numero de colunas
        
        //Se o 'x'(ai) ganhar retorna 512
        if(checkWinners(t, 'x'))
            return 512;
        
        //Se o 'o'(jogador) ganhar retorna -512
        if(checkWinners(t, 'o'))
            return -512;
        //Caso o tabuleiro esteja completo , é inutil retornar uma Score , logo é 0
        if(isCompleted(t))
            return 0;

        //Calcula a Score das linhas
        for(int i = 0; i < l; i++) {
            for(int j = 0; j < c - 3; j++) {
                if(t.arr[i][j] == 'x')
                    xcounter++;
                if(t.arr[i][j + 1] == 'x')
                    xcounter++;
                if(t.arr[i][j + 2] == 'x')
                    xcounter++;
                if(t.arr[i][j + 3] == 'x')
                    xcounter++;

                if(t.arr[i][j] == 'o')
                    ocounter++;
                if(t.arr[i][j + 1] == 'o')
                    ocounter++;
                if(t.arr[i][j + 2] == 'o')
                    ocounter++;
                if(t.arr[i][j + 3] == 'o')
                    ocounter++;

                total += pontos(xcounter, ocounter);
                xcounter = 0;
                ocounter = 0;
                
            }
        }

        //Calcula a Score das Colunas
        for(int i = 0; i < c - 4; i++) {
            for(int j = 0; j < l + 1; j++) {
                if(t.arr[i][j] == 'x')
                    xcounter++;
                if(t.arr[i + 1][j] == 'x')
                    xcounter++;
                if(t.arr[i + 2][j] == 'x')
                    xcounter++;
                if(t.arr[i + 3][j] == 'x')
                    xcounter++;

                if(t.arr[i][j] == 'o')
                    ocounter++;
                if(t.arr[i + 1][j] == 'o')
                    ocounter++;
                if(t.arr[i + 2][j] == 'o')
                    ocounter++;
                if(t.arr[i + 3][j] == 'o')
                    ocounter++;


                total += pontos(xcounter, ocounter);
                xcounter = 0;
                ocounter = 0;
                
            }
        }

        //Calcula a Score das Diagonais

        /*Diagonal Principal*/
        for(int i = 3; i < l; i++) {
            for(int j = 0; j < c - 3; j++) {
                if(t.arr[i][j] == 'x')
                    xcounter++;
                if(t.arr[i - 1][j + 1] == 'x')
                    xcounter++;
                if(t.arr[i - 2][j + 2] == 'x')
                    xcounter++;
                if(t.arr[i - 3][j + 3] == 'x')
                    xcounter++;

                if(t.arr[i][j] == 'o')
                    ocounter++;
                if(t.arr[i - 1][j + 1] == 'o')
                    ocounter++;
                if(t.arr[i - 2][j + 2] == 'o')
                    ocounter++;
                if(t.arr[i - 3][j + 3] == 'o')
                    ocounter++;


                total += pontos(xcounter, ocounter);
                xcounter = 0;
                ocounter = 0;
               
            }
        }

        /*Diagonal Secundária*/
        for(int i = 3; i < l; i++) {
            for(int j = c - 1; j >= c - 4; j--) {
                if(t.arr[i][j] == 'x')
                    xcounter++;
                if(t.arr[i - 1][j - 1] == 'x')
                    xcounter++;
                if(t.arr[i - 2][j - 2] == 'x')
                    xcounter++;
                if(t.arr[i - 3][j - 3] == 'x')
                    xcounter++;

                if(t.arr[i][j] == 'o')
                    ocounter++;
                if(t.arr[i - 1][j - 1] == 'o')
                    ocounter++;
                if(t.arr[i - 2][j - 2] == 'o')
                    ocounter++;
                if(t.arr[i - 3][j - 3] == 'o')
                    ocounter++;


                total += pontos(xcounter, ocounter);
                 
                xcounter = 0;
                ocounter = 0;

            }
        }
        return total; 
    }
            
            //Método para determinar se uma poça pode ser colocada numa determinada coluna
         public boolean canPlace(Tabela t, int  i) {
            if(t.arr[0][i] == '-')return true;

            return false;
        }
            //Função auxiliar do play utilizada no MonteCarlo
         public Tabela auxPlay(Tabela t, int move) {
            if(t.lastplayer == 'x') 
                return play(t, 'o', move);
            else return play(t, 'x', move);
        }

}