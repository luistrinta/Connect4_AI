import java.util.*;

public class FourInLine extends Tabela{
	
	public static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		
		Tabela t = new Tabela();
		printTabela(t);	
		System.out.println("Jogador 1 escolha o seu simbolo : ");
		String p1 = scan.next();
		System.out.println("Jogador 2 escolha o seu simbolo : ");
		String p2 = scan.next();
		while(!isCompleted(t)){
			System.out.println("Player 1 :");
			play(t,p1.charAt(0),scan.nextInt());
			printTabela(t);
			//System.out.println("Ultima jogada " + t.lastplayer);
				if(checkWinners(t)){
					System.out.println("O jogador "+t.winner+" venceu!");
					break;
				}			
			
			System.out.println("Player 2 :");
			play(t,p2.charAt(0),scan.nextInt());
			printTabela(t);
			System.out.println("Ultima jogada " + t.lastplayer);
				if(checkWinners(t)){
					System.out.println("O jogador "+t.winner+" venceu!");;
					break;
				}
		}
			System.out.println("Não há vencedores");

	}
}