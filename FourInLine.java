import java.util.*;


import java.util.*;

public class FourInLine extends Algoritmos {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Tabela t = new Tabela();
        while(!isCompleted(t)) {
            

            int x = alpha_beta_play(t, 7 , Integer.MIN_VALUE , Integer.MAX_VALUE , 'x');
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

        }

        System.out.println("Não há vencedores");

    }
}