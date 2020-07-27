//Game State Variable Class
public class GameData {
	public int announcedNum = 0;
	
	//The GUI, and ALL THREADS AND OBJECTS are generalised for n players
	//Win conditions not debugged for n>2
	//Keep n=2 for testing 
	public int n = 2; 
	
	public boolean numAnnouncedFlag = false;
	public boolean gameCompleteFlag = false;	
	
	public boolean[] chanceFlag = new boolean[n];
	public boolean[] successFlag = new boolean[n];
	
	public Object lock[] = new Object[n];
	{
		for(int i=0; i<n; i++){
			lock[i] = new Object();
		}
	}
}

