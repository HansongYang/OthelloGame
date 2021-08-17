package othello;

public class OthelloModel{
	private int[][] board=new int[8][8];

	public static final int NORMAL=0;
	public static final int CORNER_TEST=1;
	public static final int OUTER_TEST=2;
	public static final int TEST_CAPTURE=3;
	public static final int TEST_CAPTURE2=4;
	public static final int UNWINNABLE=5;
	public static final int INNER_TEST=6;

	public static final int EMPTY=0;
	public static final int BLACK=1;
	public static final int WHITE=2;


	public void initialize(int mode){
		switch (mode){
		case CORNER_TEST:
			board=new int[][]{
				{2, 0, 0, 0, 0, 0, 0, 1},
				{0, 1, 0, 0, 0, 0, 2, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0, 1, 0},
        {2, 0, 0, 0, 0, 0, 0, 2}};
      break;
		case OUTER_TEST:
			board = new int[][] {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 2, 2, 2, 2, 2, 2, 0},
				{0, 2, 1, 1, 1, 1, 2, 0},
				{0, 2, 1, 0, 0, 1, 2, 0},
				{0, 2, 1, 0, 0, 1, 2, 0},
				{0, 2, 1, 1, 1, 1, 2, 0},
				{0, 2, 2, 2, 2, 2, 2, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
			break;
		case INNER_TEST:
			board = new int[][] {
				{2, 2, 2, 2, 2, 2, 2, 2},
				{2, 0, 0, 0, 0, 0, 0, 2},
				{2, 0, 2, 2, 2, 2, 0, 2},
				{2, 0, 2, 1, 1, 2, 0, 2},
				{2, 0, 2, 1, 1, 2, 0, 2},
				{2, 0, 2, 2, 2, 2, 0, 2},
				{2, 0, 0, 0, 0, 0, 0, 2},
				{2, 2, 2, 2, 2, 2, 2, 2}};
			break;
		case UNWINNABLE:
			board = new int[][] {
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
			break;
		case TEST_CAPTURE:
			board=new int[][]{
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 1, 1, 1, 1, 1, 0},
				{0, 1, 2, 2, 2, 1, 1, 0},
				{0, 1, 2, 0, 2, 1, 1, 0},
				{0, 1, 2, 2, 2, 1, 1, 0},
				{0, 1, 1, 1, 1, 1, 1, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
			break;
		case TEST_CAPTURE2:
			board=new int[][]{
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{1, 2, 2, 2, 1, 2, 1, 1},
				{1, 2, 2, 2, 2, 2, 1, 1},
				{1, 2, 2, 0, 2, 2, 1, 1},
				{1, 2, 2, 2, 2, 1, 1, 1},
				{1, 2, 1, 2, 2, 2, 1, 1},
				{1, 1, 1, 1, 1, 1, 1, 1}};
			break;
		default:
			board = new int[][]{
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 2, 1, 0, 0, 0},
				{0, 0, 0, 1, 2, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0}};
		}
	}

		//This function returns the contents of a given square (0 for empty, 1 for black, 2 for white)
		public int getBoard(int x, int y){
			return board[x][y];
		}

		//This function checks to see if a player (1 for black, 2 for white) may make a valid move at that particular square. True if yes, false if no.
		public boolean isValid(int x, int y, int player){
				if(board[x][y] != 0){
					return false;
				}
				boolean result = false;
				int row = 0;
				int col = 0;
				boolean found = false;
				int current = 0;

				for (int r = -1; r <= 1; r++){
					for (int c = -1; c <= 1; c++){
					row = y + r;
					col = x + c;

					if (row >= 8 || row < 0 || col >= 8 || col < 0){
		 					continue;
	 				}
					found = false;
					current = board[col][row];
					if (current == 0 || current == player){
						continue;
					}

					while (!found){
							row += r;
							col += c;
							if (row >= 8 || row < 0 || col >= 8 || col < 0){
				 					break;
			 				}
							current = board[col][row];
							if (current == player){
								found = true;
								result = true;
							}	else if (current == 0){
								found = true;
							}
						}
				}
			}
			return result;
		}

		//The player will attempt to move here. Returns the number of chips captured by this player.
		public int move(int x, int y, int player){
				if(!isValid(x, y, player)){
						return 0;
				}
				board[x][y]= player;
				int count = getChips(player);
				//check up & below
				flip(x, y, player, 0, -1);
				flip(x, y, player, 0, 1);
				//check left & right
				flip(x, y, player, 1,0);
				flip(x, y, player, -1, 0);
				//check corners
				flip(x, y, player, 1,1);
				flip(x, y, player, 1,-1);
				flip(x, y, player, -1,1);
				flip(x, y, player, -1,-1);
				count = getChips(player) - count;
				return count;
		}

		//This function returns true if the given player has a valid move they can do at all, anywhere on the board.
		public boolean canMove(int player){
			for(int i = 0; i < board.length; i++){
				for(int j = 0; j < board[i].length; j++){
					if(isValid(i, j, player)){
						return true;
					}
				}
			}
			return false;
		}

		//This function returns the total number of pieces the specified player has on the board at the time of calling.
		public int getChips(int player){
			int count = 0;
			for(int i = 0; i < board.length; i++){
				for(int j = 0; j < board[i].length; j++){
					if(board[i][j] == player){
						count++;
					}
				}
			}
			return count;
		}

	//This method tries to flip all the captured pieces for this player.
		private void flip(int x, int y, int player, int cDir, int rDir){
			int row = x + rDir;
			int col = y + cDir;
			if (row >= 8 || row < 0 || col >= 8 || col < 0){
				return;
			}
			while (board[row][col] == 1 || board[row][col] == 2){
				if (board[row][col] == player){
					while(!(x == row && y == col)){
						board[row][col] = player;
						row = row - rDir;
						col = col - cDir;
					}
					break;
				} else {
					row = row + rDir;
					col = col + cDir;
				}
				if (row < 0 || row >= 8 || col >= 8 || col < 0){
					break;
				}
			}
		}
}
