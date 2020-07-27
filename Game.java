//Run this to play game
import javax.swing.SwingUtilities;

public class Game{

	public static void main(String[] args) {
		
		final GameData gameData  = new GameData();
		final Moderator moderator  = new Moderator(gameData);
		
		int n = gameData.n;
		Players player[] = new Players[n];
		Thread playerThread[] = new Thread[n];
		
		for(int i = 0; i<n; i++) {
			player[i] = new Players(gameData,i);
			playerThread[i] = new Thread(player[i]);
			playerThread[i].start();
		}
		
		Thread moderatorThread  = new Thread(moderator);
		moderatorThread. start();
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new GUI(gameData,moderator,player);
			}
			});
	}
}