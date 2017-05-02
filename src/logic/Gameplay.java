package logic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Gameplay extends JPanel  implements Runnable{
	int snakeXsize = 20;
	int snakeYsize = 20;
	
	Image upMouth = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("upmouth.png"));
	Image downMouth = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("downmouth.png"));
	Image leftMouth = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("leftmouth.png"));
	Image rightMouth = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("rightmouth.png"));
	Image body = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("body.png"));
	Image fruit = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("melon.png"));
	
	Image resizedImageUp = upMouth.getScaledInstance(snakeXsize, snakeYsize, Image.SCALE_DEFAULT);
	Image resizedImageDown = downMouth.getScaledInstance(snakeXsize, snakeYsize, Image.SCALE_DEFAULT);
	Image resizedImageLeft = leftMouth.getScaledInstance(snakeXsize, snakeYsize, Image.SCALE_DEFAULT);
	Image resizedImageRight = rightMouth.getScaledInstance(snakeXsize, snakeYsize, Image.SCALE_DEFAULT);
	Image resizedImageBody = body.getScaledInstance(snakeXsize, snakeYsize, Image.SCALE_DEFAULT);
	Image resizedImageFruit = fruit.getScaledInstance(snakeXsize, snakeYsize, Image.SCALE_DEFAULT);
	
	ImageIcon imageUp=new ImageIcon(resizedImageUp);
	ImageIcon imageDown=new ImageIcon(resizedImageDown);
	ImageIcon imageLeft=new ImageIcon(resizedImageLeft);
	ImageIcon imageRight=new ImageIcon(resizedImageRight);
	ImageIcon imageBody=new ImageIcon(resizedImageBody);
	ImageIcon imageFruit=new ImageIcon(resizedImageFruit);
	
	
	
	
	List<Positions> snake = new ArrayList<>();
	List<Positions> apples = new ArrayList<>();

	private int x = 200;
	private	int y = 200;
	private int move = 20;
	private int score = 0;
	private int FRAME_WIDTH = 850;
	private int FRAME_HEIGHT = 730;
	
	private int SCORE_BOARD_WIDTH = 800;
	private int SCORE_BOARD_HEIGHT = 60;
	
	private int BOARD_WIDTH = 800;
	private int BOARD_HEIGHT = 600;
	
	int north = snakeYsize + SCORE_BOARD_HEIGHT;
	int south = SCORE_BOARD_HEIGHT + BOARD_HEIGHT;
	int east = BOARD_WIDTH;
	int west = snakeXsize;

	
	private JFrame frame;
	boolean GAME_OVER = false;
	boolean up = false;
	boolean down = false;
	boolean right = false;
	boolean left = false;
	public Gameplay(){
		spawnFruit();
	    frame = new JFrame();
	    frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setBackground(Color.LIGHT_GRAY);
		frame.setResizable(false);
		frame.setContentPane(this);
		frame.addKeyListener(k);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(resizedImageUp);
		frame.setTitle("Adder");
		frame.setVisible(true);
	}
	KeyListener k = new KeyListener() {
		
		@Override
		public void keyTyped(KeyEvent e) {
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			int k = e.getKeyCode();
			if(k == KeyEvent.VK_UP && down == false) {up = true; down = false; left = false;right = false;}
			if(k == KeyEvent.VK_DOWN && up == false) {up = false; down = true; left = false;right = false;}
			if(k == KeyEvent.VK_LEFT && right == false) {up = false; down = false; left = true;right = false;}
			if(k == KeyEvent.VK_RIGHT && left == false) {up = false; down = false; left = false;right = true;}
			frame.repaint();
		}
	};

	
	public void paint(Graphics g){
		
		g.setColor(Color.black);
		g.drawRect(snakeXsize,snakeYsize,SCORE_BOARD_WIDTH,SCORE_BOARD_HEIGHT);
		g.drawRect(snakeXsize, snakeYsize + SCORE_BOARD_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT);
		Font f = new Font("Dialog", Font.PLAIN, 40);
		g.setFont(f);
		g.drawString("Score : "+score, snakeXsize + 540, snakeYsize + 45);
		
		for(int i = 0;i<snake.size();i++){
			if(i == snake.size()-1){
				if(up){
					imageUp.paintIcon(this, g, snake.get(i).getX(), snake.get(i).getY());
				}else if(down){
					imageDown.paintIcon(this, g, snake.get(i).getX(), snake.get(i).getY());
				}else if(left){
					imageLeft.paintIcon(this, g, snake.get(i).getX(), snake.get(i).getY());
				}else if(right){
					imageRight.paintIcon(this, g, snake.get(i).getX(), snake.get(i).getY());
				}
			}else{
				imageBody.paintIcon(this, g, snake.get(i).getX(), snake.get(i).getY());
				frame.repaint();
			}
		}
		if(apples.size() > 0){
			for(int i = 0;i<apples.size();i++){
				imageFruit.paintIcon(this, g, apples.get(i).getX(), apples.get(i).getY());
			}
		}
	}
	public int getRandomX(){
		int n = ThreadLocalRandom.current().nextInt(west, east + 1);
		if(n % snakeYsize == 0){
			return n;
		}else{
			return getRandomX();
		}
	}
	public int getRandomY(){
		int n = ThreadLocalRandom.current().nextInt(north, south + 1);
		if(n % snakeYsize == 0){
			return n;
		}else{
			return getRandomY();
		}
	}
	public void spawnFruit(){
		Positions pos = new Positions(getRandomX(), getRandomY());
		if(!apples.contains(pos) && !snake.contains(pos)){
			apples.add(pos);
		}else{
			spawnFruit();
		}
	}
	@Override
	public void run() {
		while(!GAME_OVER){
    		Positions headPos = new Positions(x, y);
    		for(int i = 0;i < snake.size();i++){
    			if(snake.get(i).getX() == headPos.getX() && snake.get(i).getY() == headPos.getY()){
    				GAME_OVER = true;
    				JOptionPane.showMessageDialog(null, "Game Over");
    			}
    		}
    		snake.add(headPos);
    		if(apples.size() > 0){
    			for(int i = 0;i < apples.size();i++){
	        		if(snake.get(snake.size()-1).getX() == apples.get(i).getX() && snake.get(snake.size()-1).getY() == apples.get(i).getY()){
	        			score+=10;
	        			apples.remove(i);
	        			snake.add(new Positions(x, y));
	        			spawnFruit();
	        			frame.repaint();
	        		}
	        	}
    		}
    		if(up){
    			y-=move;
    			if(y < north){
    				y = south;
    			}
    		}else if(down){
    			y+=move;
    			if(y > south){
    				y = north;
    			}
    		}else if(left){
    			x-=move;
    			if(x < west){
    				x = east;
    			}
    		}else if(right){
    			x+=move;
    			if(x > east){
    				x = west;
    			}
    		}
    		try {Thread.sleep(130);}catch (InterruptedException e) {e.printStackTrace();}
    			snake.remove(0);
    		frame.repaint();
    	}
		
	}

}
