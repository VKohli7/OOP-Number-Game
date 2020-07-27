//Player class for players

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Players implements Runnable {

	private int ID;	//player ID
	private int totNumFound;  //number of numbers found(matched) with moderator
	private GameData gameData;	//data of the game
	private JPanel playerBoard;	//for adding player panels
	private JButton[] btnTicket; //buttons for players (cannot be pressed) just to cross out matched numbers

	private final static int UB = 50;
	private final static int winNUM = 3;
	private final static int numBTN = 10;		

	private int[] ticket = new int[numBTN];
			
	
	public Players(GameData gameData, int ID) { 
		
		this.ID = ID; 		
		this.gameData = gameData;	
		this.totNumFound = 0;
		
		// initialize player board
		playerBoard = new JPanel();
		playerBoard.setLayout(new GridLayout(1,10));
		btnTicket = new JButton[numBTN];
		
		//generate random numbers for players
		Random rand = new Random();
		for(int i=0; i<numBTN; i++) {
			int p = 0;
			while(p==0) {
				p = rand.nextInt(UB+1);
			}
			ticket[i] = p;
		}
		//initialise player buttons
		for(int i = 0; i < numBTN; i++) {
			btnTicket[i] = new JButton(String.valueOf(ticket[i]));
			btnTicket[i].setEnabled(false);
			playerBoard.add(btnTicket[i]);
		}
	}

	public void run() {
		synchronized(gameData.lock[0]) {			
			//condition for game to continue
			while(!gameData.gameCompleteFlag) {
				//players wait until their chance number is announced
				while(!gameData.numAnnouncedFlag || gameData.chanceFlag[ID]) {
					try {
						gameData.lock[0].wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//if game is not complete yet
				if(!gameData.gameCompleteFlag) {					
					//increment totNumFound if there is a match
					for(int i = 0; i < numBTN; i++) {						
						if(gameData.announcedNum == ticket[i]) {
							this.totNumFound++;		
							//update player buttons
							this.btnTicket[i].setBackground(Color.YELLOW);
							ticket[i] = 0;
							break;
						}
					}
					
					//if 3 numbers are found, player wins
					if(this.totNumFound == winNUM) {
						// player set the success flag 
						gameData.successFlag[this.ID] = true;						
					}
					// player sets its chance flag 
					gameData.chanceFlag[ID] = true;
					//notify all others waiting for lock[0]
					gameData.lock[0].notifyAll();
				}
			}
		}
	}

	public JPanel getPlayerTicketPanel() {		
		return playerBoard;
	}
}

