/*IO Method: SWING*/
//GUI Widget toolkit
//Platform independent
//Lightweight
//GUI CODE
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.Random;

public class GUI implements ActionListener{
	
	private Moderator moderator;
	private GameData gameData;	
	private JButton[] btnModeratorNumbers;	
	//private JButton ModNum;
	public int mod[] = new int[50];
	
	
	GUI(GameData gameData, Moderator moderator, Players [] player) {
		
		this.moderator = moderator;
		this.gameData = gameData;
				
		JFrame gameFrame = new JFrame("OOP NUMBER GAME");
		gameFrame.setSize(800,600);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblModerator = new JLabel("Moderator",JLabel.CENTER);
		gameFrame.setLayout(new BoxLayout(gameFrame.getContentPane(),BoxLayout.Y_AXIS));		
		gameFrame.add(lblModerator);
		
		Random rand1 = new Random();
		int q=0;
		while(q==0) {
			q = rand1.nextInt(51);
		}
		String nextnum = q + "";
		moderator.NextNum.setText("Next Number: " + nextnum);
		gameFrame.add(moderator.NextNum);
		
		// board for moderator buttons
		JPanel moderatorBoard = new JPanel();
		moderatorBoard.setLayout(new GridLayout(10,5));

		// initialise moderator board number buttons
		btnModeratorNumbers = new JButton[50];
		Random rand2 = new Random();
		for(int i = 0; i < 50; i++) {
			int p=0;
			while(p==0) {
				p = rand2.nextInt(51);
			}
			mod[i] = p; //actual array to be used
			btnModeratorNumbers[i] = new JButton(String.valueOf(i+1)); //replace i+1 with p
			btnModeratorNumbers[i].addActionListener(this);
			moderatorBoard.add(btnModeratorNumbers[i]);
		}
		
		gameFrame.add(moderatorBoard);
		
		int n = gameData.n;
		
		//player board buttons
		JLabel lblPlayer[] = new JLabel[n];
		for(int i = 0; i<n; i++) {
			String numstr = i + 1 + "";
			lblPlayer[i] = new JLabel("Player"+numstr, JLabel.CENTER);
			gameFrame.add(lblPlayer[i]);
			gameFrame.add(player[i].getPlayerTicketPanel());
		}

		gameFrame.add(moderator.GameStatus);
		
		gameFrame.setVisible(true);
	
	}
	//performed when user presses button
	public void actionPerformed(ActionEvent e) {
		Random rand1 = new Random();
		int q=0;
		while(q==0) {
			q = rand1.nextInt(51);
		}
		String nextnum = q + "";
		moderator.NextNum.setText("Next Number: " + nextnum);
		//checks which button is pressed
		for(int i = 0; i < 50; i++) {	
			if(e.getSource() == btnModeratorNumbers[i]) {	
				synchronized(gameData.lock[1]) {
					
					moderator.setAnnouncedNum(i+1);
					btnModeratorNumbers[i].setForeground(Color.gray);
					btnModeratorNumbers[i].setEnabled(true);
					gameData.lock[1].notify();
				}				
				break;
			}
		}
	}		
}
