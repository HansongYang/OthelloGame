package othello;

public class Othello {
    public static void main(String[] args) {
    	OthelloSplashScreen oss = new OthelloSplashScreen(5000);
    	oss.displaySplashWindow();
    	OthelloViewController otc = new OthelloViewController();
    }
}
