import java.util.*;
import java.lang.*;
public class Tabela {
    char[][] arr;
    int bestY;
    char lastplayer;
    //verificam se o t.arr ja acabou
    int lastMoveX;
    int lastMoveY;
    char winner;
    //pontuação do tabuleiro
    int valor ;

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


    static void printTabela(Tabela t) {
        for(int j = 0; j < 6 ; j++) {
            for(int i = 0; i < 7 ; i++)System.out.print(t.arr[j][i] + " ");
            System.out.println();
        }
    }



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




    //Boolean functions
    static boolean checkWinners(Tabela t, char player) {
        int counter = 0;
        if(winsLine(t, t.lastMoveX, t.lastMoveY, player))return true;
        if(winsColumn(t, t.lastMoveX, t.lastMoveY, player))return true;
        if(winsDiag(t, t.lastMoveX, t.lastMoveY, player))return true;


        return false;
    }

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

    //Vez humana
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



    //Limpa a ultima jogada
    public static void clear_Move(Tabela t) {
        t.arr[t.lastMoveX][t.lastMoveY] = '-';
        t.valor = scoreSum(t);
    }

    //IA a jogar

    static Tabela aiMovement(Tabela t, char simbolo, int move) {

        //System.out.print("Jogador " + simbolo + ": ");

        while(move < 1 || move > 7 || !isAvailable(t, move - 1)) {
            System.out.println("Movimento inválido.\nJogador " + simbolo + ": ");
            return t;
        }
        return play(t, simbolo, move);
    }

    //ver filhos possiveis

    public LinkedList<Integer> getMoves() {
        LinkedList<Integer> actions = new LinkedList<Integer>();
        for(int j = 0; j < 7; j++)
            if(this.arr[0][j] == '-')
                actions.add(j);
        return actions;
    }

    //gerar filhos possiveis

    public Tabela gerarFilhos(char player, int action) {

        int row;
        for(row = 0; row < 6 && this.arr[row][action] != 'x' && this.arr[row][action] != 'o'; row++);
        Tabela t = new Tabela(this);
        t.arr[row - 1][action] = player;

        return t;
    }


    //mini_max
    public static int minimax_play(Tabela t, int depth) {
        return max_value(t, depth);
    }



    //max_value

    public static int max_value(Tabela t, int depth) {

        if(depth == 0) {
            //System.out.println(t.bestY);
            return scoreSum(t);
        } 

        
        else {

            int best = Integer.MIN_VALUE;

            int next;

            for(int i : t.getMoves()) {
                next = min_value(t.gerarFilhos('x', i), depth - 1);
                if(next > best) {
                    best = next;
                    t.bestY = i;
                }

                if(depth == 7 ) {
                    System.out.println("Max value of " + i + " is :" + best);
                }
            }

            return best;
        }
    }


    //min_value

    public static int min_value(Tabela t, int depth) {


        if(depth == 0)
            return scoreSum(t);

        
        else {


            int best = Integer.MAX_VALUE;

            int next;
            for(int i : t.getMoves()) {
                next = max_value(t.gerarFilhos('o', i), depth - 1);
                if(next < best) {
                    best = next;
                    t.bestY = i;
                }
            }
            return best;
        }
    }

    //Valor dos pontos
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
    //Pontuações
    public static  int scoreSum(Tabela t) {
        int xcounter = 0;
        int ocounter = 0;
        int total = 0;
        int l = 6 ;
        int c = 7;

        if(checkWinners(t, 'x'))
            return 512;

        if(checkWinners(t, 'o'))
            return -512;

        if(isCompleted(t))
            return 0;
        //Pontos das linhas
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
                //if(winsLine(t,i,j,'x'))return Integer.MAX_VALUE;
                //if(winsLine(t,i,j,'o'))return Integer.MIN_VALUE;
            }
        }

        //Pontos das colunas
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
                //if(winsColumn(t,i,j,'x'))return Integer.MAX_VALUE;
               // if(winsColumn(t,i,j,'o'))return Integer.MIN_VALUE;
            }
        }

        //Pontos da diagonal principal
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
                //if(winsDiag(t,i,j,'x'))return Integer.MAX_VALUE;
                //if(winsDiag(t,i,j,'o'))return Integer.MIN_VALUE;
            }
        }

        //Pontos da diagonal secundária
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
                //if(winsDiag(t,i,j,'x'))return Integer.MAX_VALUE;
                //if(winsDiag(t,i,j,'o'))return Integer.MIN_VALUE;
            }
        }
        return total; //Return dos Pontos do tabuleiro
    }

}