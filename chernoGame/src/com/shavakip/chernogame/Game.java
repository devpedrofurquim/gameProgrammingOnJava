package com.shavakip.chernogame;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.shavakip.chernogame.graphics.Screen;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = 1L;
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;

	// Threads: A process within a process
	private Thread thread;
	// Game loop is running or not
	private boolean running = false;
	// Screen object
	private Screen screen;
	// Main image
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	// converting the image object into an array of integers
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	// Window
	private JFrame frame;

	public Game() {
		// Creates dimension
		Dimension size = new Dimension(width * scale, height * scale);
		// Canvas method to set the size of the window
		setPreferredSize(size);
		screen = new Screen(width, height);
		frame = new JFrame();
	}
	
	// Start Thread
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	// Stop Thread
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (running) {
			tick();
			render();
		}
	}
	
	public void tick() {
		
	}
	
	public void render() {
		BufferStrategy bufferStrategy = getBufferStrategy();
		
		// Create Buffer Strategy if it does not exist
		if (bufferStrategy == null) {
			createBufferStrategy(3); // Always have it at 3
			return;
		}
		
		Graphics graphics = bufferStrategy.getDrawGraphics();
		
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		graphics.dispose();
		bufferStrategy.show();
	}
	
	// Java main method. First method that runs.
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Shavakip");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}
}
