import java.util.*;

public class FourInLine extends Tabela {


    static int calcScore(int aiScore, int moreMoves) {
        int moveScore = 4 - moreMoves;
        if(aiScore == 0)return 0;
        else if(aiScore == 1)return 1 * moveScore;
        else if(aiScore == 2)return 10 * moveScore;
        else if(aiScore == 3)return 100 * moveScore;
        else return 1000;
    }

    //Heuristica para calcular a melhor hipotese
    public static  int scoreSum(Tabela b){
      
        int aiScore=1;
        int score=0;
        int blanks = 0;
        int k=0, moreMoves=0;
        for(int i=5;i>=0;--i){
            for(int j=0;j<=6;++j){
                
                if(b.arr[i][j]=='-' || b.arr[i][j]==playerChar) continue; 
                
                if(j<=3){ 
                    for(k=1;k<4;++k){
                        if(b.arr[i][j+k]==aiChar){
                        	aiScore++;
                        }
                        else if(b.arr[i][j+k]==playerChar){aiScore=0;blanks = 0;break;}
                        else blanks++;
                    }
                     
                    moreMoves = 0; 
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int column = j+c;
                            for(int m=i; m<= 5;m++){
                             if(b.arr[m][column]=='-')moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += calcScore(aiScore, moreMoves);
                    aiScore=1;   
                    blanks = 0;
                } 
                
                if(i>=3){
                    for(k=1;k<4;++k){
                        if(b.arr[i-k][j]==aiChar)aiScore++;
                        else if(b.arr[i-k][j]==playerChar){aiScore=0;break;} 
                    } 
                    moreMoves = 0; 
                    
                    if(aiScore>0){
                        int coluna = j;
                        for(int m=i-k+1; m<=i-1;m++){
                         if(b.arr[m][coluna]=='-')moreMoves++;
                            else break;
                        }  
                    }
                    if(moreMoves!=0) score += calcScore(aiScore, moreMoves);
                    aiScore=1;  
                    blanks = 0;
                }
                 
                if(j>=3){
                    for(k=1;k<4;++k){
                        if(b.arr[i][j-k]==aiChar)aiScore++;
                        else if(b.arr[i][j-k]==playerChar){aiScore=0; blanks=0;break;}
                        else blanks++;
                    }
                    moreMoves=0;
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int coluna = j- c;
                            for(int m=i; m<= 5;m++){
                             if(b.arr[m][coluna]=='-')moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += calcScore(aiScore, moreMoves);
                    aiScore=1; 
                    blanks = 0;
                }
                 
                if(j<=3 && i>=3){
                    for(k=1;k<4;++k){
                        if(b.arr[i-k][j+k]==aiChar)aiScore++;
                        else if(b.arr[i-k][j+k]==playerChar){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int coluna = j+c, row = i-c;
                            for(int m=row;m<=5;++m){
                                if(b.arr[m][coluna]=='-')moreMoves++;
                                else if(b.arr[m][coluna]==aiChar);
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += calcScore(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                }
                 
                if(i>=3 && j>=3){
                    for(k=1;k<4;++k){
                        if(b.arr[i-k][j-k]==aiChar)aiScore++;
                        else if(b.arr[i-k][j-k]==playerChar){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int coluna = j-c, row = i-c;
                            for(int m=row;m<=5;++m){
                                if(b.arr[m][coluna]=='-')moreMoves++;
                                else if(b.arr[m][coluna]==aiChar);
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += calcScore(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                }
                /*if(score<0){
                	System.out.println("AIScore: "+score+"="+calculateScore(aiScore, moreMoves)+"("+aiScore+","+moreMoves+") : "+i+"."+j);
                            printTabela(b);
                	System.exit(0);
                }*/
            }
        }
        return score;
    } 



    static int maxDepth = 9;
    static int aiNextMove = -1;
    public static int minimax(Tabela t, int turno, int alpha, int beta, int depth) {
        if(beta <= alpha) {
            if(turno == 1) return Integer.MAX_VALUE;
            else return Integer.MIN_VALUE;
        }
        if(isCompleted(t)) return 0;
        else {
            Tabela t2 = t;
            t2.lastplayer = playerChar;
            if(checkWinners(t2))return Integer.MIN_VALUE / 2;
            t2.lastplayer = aiChar;
            if(checkWinners(t2))return Integer.MAX_VALUE / 2;
        }
        if(depth==maxDepth){
            int bg=scoreSum(t);
            //System.out.println(bg);
            return bg;
        }
        //System.out.println(scoreSum(t));
        int maxScore = Integer.MIN_VALUE;
        int minScore = Integer.MAX_VALUE;
        for(int i = 0; i <= 6; ++i) {
            int currentScore = 0;
        	//System.out.println(i+" hey");
        	//printTabela(t);
            if(!isAvailable(t, i)) {
            	continue;
            }
            if(turno == 1) {
                t=play(t, aiChar, i+1);
                currentScore = minimax(t, 2, alpha, beta, depth + 1);
                if(depth == 0) {
                    System.out.println("Score for location " + i + " = " + currentScore);
                    if(currentScore > maxScore)aiNextMove = i;
                    if(currentScore == Integer.MAX_VALUE / 2) {
                        t = deleteMove(t, i);
                        break;
                    }
                }
                maxScore = Math.max(currentScore, maxScore);
                alpha = Math.max(currentScore, alpha);
            } else if(turno == 2) {
                t=play(t, playerChar, i+1);
                currentScore = minimax(t, 1, alpha, beta, depth + 1);
                minScore = Math.min(currentScore, minScore);
                beta = Math.min(currentScore, beta);
            }
            t = deleteMove(t, i);
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
        }
        return turno == 1 ? maxScore : minScore;
    }
    public static Tabela deleteMove(Tabela t, int col) {
        for(int i = 0; i <= 5; i++) {
            if(t.arr[i][col] != '-') {
                t.arr[i][col] = '-';
                return t;
            }
        }
        return t;
    }
    public static int aiMovement(Tabela t) {
        aiNextMove = -1;
        minimax(t, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        System.out.println("Next move: "+aiNextMove);
        return aiNextMove+1;
    }
    public static void main(String[] args) {

        Tabela t = new Tabela();
        //printTabela(t);
        /*System.out.println("Jogador 1 escolha o seu simbolo : ");
        String p1 = scan.next();
        System.out.println("Jogador 2 escolha o seu simbolo : ");
        String p2 = scan.next();*/
            play(t, aiChar, 3+1);
        printTabela(t);
        while(!isCompleted(t)) {
            t=playerMovement(t, playerChar);
            printTabela(t);
            //System.out.println("Ultima jogada " + t.lastplayer);
            if(checkWinners(t)) {
                System.out.println("O jogador " + t.winner + " venceu!");
                return;
            }
            t=play(t, aiChar, aiMovement(t));
            //playerMovement(t, aiChar);
            printTabela(t);
            System.out.println("Ultima jogada " + t.lastplayer);
            if(checkWinners(t)) {
                System.out.println("O jogador " + t.winner + " venceu!");;
                return;
            }
        }
        System.out.println("Não há vencedores");

    }
}