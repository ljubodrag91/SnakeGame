package logic;

import java.awt.EventQueue;
public class Main {
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Thread t = new Thread(new Gameplay());
				t.start();
				
			}
		});
	}
}
