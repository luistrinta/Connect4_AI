import java.util.*;


import java.util.*;

public class FourInLine extends Algoritmos {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("1.AI vs PLAYER || 2.AI vs AI");
        int play = scan.nextInt();
        switch(play){
        case 1 :    
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
            int x = alpha_beta_play(t, 9 , Integer.MIN_VALUE , Integer.MAX_VALUE , 'x');
            System.out.println("Computador jogou na :"+t.bestY);
            t = play(t, 'x', t.bestY);
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
        break;

        case 2: 
        System.out.println("t1 ==> x t2==> o");
        System.out.println("Pressione enter para continuar");
        
        String nada = scan.next();

        Tabela t1 = new Tabela();
        Tabela t2 = new Tabela();

        while(!isCompleted(t1)) {
            
            int x = alpha_beta_play(t1,1 , Integer.MIN_VALUE , Integer.MAX_VALUE , 'x');
            int bestPlay = t1.bestY;
            System.out.println("Computador jogou na :"+t1.bestY);
            t1 = play(t1, 'x', bestPlay);
            t2 = play(t2, 'o', bestPlay);
            printTabela(t1);
            System.out.println("0 1 2 3 4 5 6");            

            
            

            if(checkWinners(t1,'x')){
                System.out.println("t1 won");
                return;
            }

            if(checkWinners(t1,'o')){
                System.out.println("t2 won");
                return;
            }


            x = minimax_play(t2, 3 /*, Integer.MIN_VALUE , Integer.MAX_VALUE , 'x'*/);
            System.out.println("Computador jogou na :"+t2.bestY);
            bestPlay = t2.bestY;
            t1 = play(t1, 'o', bestPlay);
            t2 = play(t2, 'x', bestPlay);
            printTabela(t1);
            System.out.println("0 1 2 3 4 5 6");
            

            if(checkWinners(t2,'x')){
                System.out.println("t2 won");
                return;
            }

            if(checkWinners(t2,'o')){
                System.out.println("t1 won");
                return;
            }

        }

        System.out.println("Não há vencedores");
        break;
     
    }
}
}