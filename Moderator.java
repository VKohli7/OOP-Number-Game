//Moderator Class for game moderator
import javax.swing.JLabel;
import java.util.Random;
import java.util.*;

public class Moderator implements Runnable {
	
	private GameData gameData; //game data shared with Moderator
	private int numAnnounced = 0; //set when GUI button pressed
		
	/* this label is used by the moderator to set the game status */
	public final JLabel GameStatus = new JLabel();  
	public final JLabel NextNum = new JLabel();
	
	
	public Moderator(GameData gameData) {
		this.gameData = gameData;			
		GameStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);		
		NextNum.setAlignmentX(JLabel.CENTER_ALIGNMENT);
	}
	
	
	public void run() {
		
		int n = gameData.n;
		
		synchronized(gameData.lock[0]) {			
			//continue in moderator until players set successFlag
			while (!gameData.successFlag[0] && !gameData.successFlag[1]) {
				// num announced flag set to false before number is announced
				gameData.numAnnouncedFlag = false;
				// check flag set to false before number is announced
				for(int i=0; i<n; i++) {
					gameData.chanceFlag[i] = false;
				}
				//until number is announced numAnnounced stays 0
				//if button pressed on GUI an exception is raised and caught
				synchronized(gameData.lock[1]){			
					while(0 == numAnnounced){
						try {
							gameData.lock[1].wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				//after num is pressed numAnnounced is updated to gameData
				gameData.announcedNum = numAnnounced;
				//num announced is reset
				numAnnounced = 0; 
				// tell players num is announced
				gameData.numAnnouncedFlag = true;
				//notify all players
				gameData.lock[0].notifyAll();
				
				while(!gameData.chanceFlag[0] || !gameData.chanceFlag[1]) {
					try {
						//wait till players find announced number
						gameData.lock[0].wait(); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}				
			}
			
			//Player win check
			Random rand = new Random();
			//collection of integers made to store player IDs that have won the game at this gamestate
			ArrayList<Integer> list = new ArrayList<Integer>();
			//String p = ""; //uncomment if not using q
			for(int i=0; i<n; i++) {
				if(gameData.successFlag[i]) {
					//p = p + (i+1) + ", "; //for use without generics and collections, more straightforward
					list.add(i); //updating list of indices
				}
			}
			
			//using the collection of index values made in list
			String q = "";
			Iterator<Integer> itr=list.iterator();
			while(itr.hasNext()) {
				String numstr = itr.next()+1 + "";
				q = q + numstr + ", ";
			}
			//use q for collections, p works as well
			GameStatus.setText("Winner!! Player(s):" + q); 
			
			gameData.gameCompleteFlag = true; 
			
			gameData.lock[0].notifyAll(); 	
			}	
		}		
	
	public void setAnnouncedNum(int i) {
		this.numAnnounced = i;	
	}
}
