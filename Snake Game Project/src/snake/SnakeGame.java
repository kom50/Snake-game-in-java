package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.border.MatteBorder;

import code.OnDrag;
import code.PButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;

class SnakeScore implements Serializable {
	/**
	 * Set easy Game level score, normal Game level score, AND hard Game level score;
	 */
	private static final long serialVersionUID = 1L;
	private int easyGameScore, normalGameScore, hardGameScore;
	private transient static String gameLevel; // this field is not stored in file during serialization

	public SnakeScore(String gmLevel) {
		easyGameScore = normalGameScore = hardGameScore = 0;
		gameLevel = gmLevel;
	}

	public int getScore() {
		System.out.println("get " + easyGameScore + " " + normalGameScore + " , " + hardGameScore);
		if (gameLevel.equals("easy")) {
			return this.easyGameScore;
		} else if (gameLevel.equals("normal")) {
			return this.normalGameScore;
		} else {
			return this.hardGameScore;
		}
	}

	public void setScore(int score) {
		if (gameLevel.equals("easy")) {
			this.easyGameScore = score;
		} else if (gameLevel.equals("normal")) {
			this.normalGameScore = score;
		} else {
			this.hardGameScore = score;
		}
		System.out.println("set " + easyGameScore + " " + normalGameScore + " , " + hardGameScore);
	}
}

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// All variables
	private LinkedList<Integer> snakeX, snakeY;
	private LinkedList<JLabel> snake;
	private int x = 80, y = 60;
	private JPanel box;
//     Timer timer;
	private int speed = 100;
	private JLabel overLabel;
	private JLabel backBtn;

	private ImageIcon bodyIcon = new ImageIcon("src/snake/images/body.png");
	private ImageIcon headIconDown = new ImageIcon("src/snake/images/hdown.png");
	private ImageIcon headIconRight = new ImageIcon("src/snake/images/hright.png");
	private ImageIcon headIconLeft = new ImageIcon("src/snake/images/hleft.png");
	private ImageIcon headIconUp = new ImageIcon("src/snake/images/hup.png");
//    ImageIcon closmouthIcon = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/closmouth.png");

	// Food
	private int foodX, foodY;

//	private ImageIcon foodIcon = new ImageIcon("G:/InteliJ 2021/src/Swing/code/snakegame/images/food.png");
	private ImageIcon foodIcon = new ImageIcon("src/snake/images/food.png");
	private JLabel food = new JLabel(foodIcon);
	private javax.swing.Timer timer;

	// Store snake highest score
	private SnakeScore score;
	private String gameLevel = "normal";

	private JPanel boardPanel;
	private JLabel lblScore, lblHighScore, lblStatus, lblGameLevel;

	private SnakeGame() {
		super("Snake Game ");
		setUndecorated(true);
		this.setBackground(Color.cyan);
		this.setBounds(10, 80, 1340, 660);
    	this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creating box
		box = new JPanel();

		//
		new OnDrag(box, this);
		new OnDrag(this, this);

		box.setBounds(10, 110, 1040, 539);

		box.setBackground(Color.getHSBColor(120, 50, 50));
		Color color = new Color(169, 245, 27, 255);
		getContentPane().setLayout(null);
		box.setBackground(color);

		box.setVisible(false);

		JPanel panel = new JPanel();
		panel.setBounds(369, 105, 523, 536);
		getContentPane().add(panel);
		panel.setBackground(new Color(204, 255, 255, 0));

		PButton startBtn = new PButton("Start Game");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(false);
				box.setVisible(true);
				boardPanel.setVisible(true);
					
				//
				initGame();
			}
		});
		startBtn.setBounds(156, 126, 211, 43);
		PButton levelBtn = new PButton("Game Level");
		levelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameLevelPanel.setVisible(true);
				panel.setVisible(false);

			}
		});
		levelBtn.setBounds(156, 191, 211, 43);
		PButton highScoreBtn = new PButton("New Game");
		highScoreBtn.setBounds(156, 251, 211, 43);
		PButton helpBtn = new PButton("Help ?");
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HelpDialog dialog = new HelpDialog();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				dialog.setAlwaysOnTop(true);
				
			}
		});
		helpBtn.setBounds(156, 313, 211, 43);
		panel.setLayout(null);
		Font font = new Font("Consolas", Font.BOLD, 16);

		startBtn.setFont(font);
		levelBtn.setFont(font);
		highScoreBtn.setFont(font);
		helpBtn.setFont(font);

		startBtn.setEndColor(Color.red);
		levelBtn.setEndColor(Color.red);
		highScoreBtn.setEndColor(Color.red);
		helpBtn.setEndColor(Color.red);

		startBtn.setStartColor(Color.magenta);
		levelBtn.setStartColor(Color.magenta);
		highScoreBtn.setStartColor(Color.magenta);
		helpBtn.setStartColor(Color.magenta);
		
		panel.add(startBtn);
		panel.add(levelBtn);
		panel.add(highScoreBtn);
		panel.add(helpBtn);

		gameLevelPanel = new JPanel();
		gameLevelPanel.setBackground(new Color(153, 204, 0, 0));
		gameLevelPanel.setBounds(387, 110, 503, 539);
		getContentPane().add(gameLevelPanel);
		gameLevelPanel.setLayout(null);

		lblGameLevel_1 = new JLabel("GAME LEVEL");
		lblGameLevel_1.setForeground(Color.BLACK);
		lblGameLevel_1.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
		lblGameLevel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameLevel_1.setBounds(141, 62, 207, 39);
		gameLevelPanel.add(lblGameLevel_1);

		final JComboBox<Object> comboBox = new JComboBox<Object>();
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] { "Easy", "Normal", "Hard" }));
		comboBox.setBounds(141, 181, 207, 39);
		gameLevelPanel.add(comboBox);

		PButton btnNewButton = new PButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = comboBox.getSelectedIndex();
				if (index == 0) {
					speed = 150;
					gameLevel = "easy";
					lblGameLevel.setText("Game Level : Easy");
				} else if (index == 1) {
					speed = 100;
					gameLevel = "normal";
					lblGameLevel.setText("Game Level : Normal");
				} else {
					speed = 50;
					gameLevel = "hard";
					lblGameLevel.setText("Game Level : Hard");
				}

				System.out.println("Speed : " + speed);
				gameLevelPanel.setVisible(false);
				panel.setVisible(true);
			}
		});
		btnNewButton.setBounds(141, 239, 207, 39);
		btnNewButton.setBorderRadius(10);
		btnNewButton.setStartColor(Color.red);
		btnNewButton.setEndColor(Color.green);
		btnNewButton.setFont(new Font("Consolas", Font.BOLD, 18));
		gameLevelPanel.add(btnNewButton);
		gameLevelPanel.setVisible(false);

		getContentPane().add(box);

		JLabel lblSnakeGame = new JLabel("Snake Game ");
		lblSnakeGame.setForeground(new Color(255, 255, 255));
		lblSnakeGame.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(255, 255, 255)));
		lblSnakeGame.setHorizontalAlignment(SwingConstants.CENTER);
		lblSnakeGame.setFont(new Font("Consolas", Font.BOLD, 32));
		lblSnakeGame.setBounds(335, 24, 622, 39);
		getContentPane().add(lblSnakeGame);

		JLabel lblX = new JLabel("X");
		lblX.setForeground(new Color(255, 255, 255));
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				SnakeGame.this.dispose();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				lblX.setForeground(Color.red);
			}

			@Override
			public void mouseExited(MouseEvent event) {
				lblX.setForeground(Color.white);
			}
		});
		lblX.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblX.setBounds(1310, 11, 20, 31);
		getContentPane().add(lblX);

		boardPanel = new JPanel();
		boardPanel.setVisible(false);
		boardPanel.setBackground(new Color(153, 0, 102));
		boardPanel.setBounds(1060, 110, 270, 539);
		getContentPane().add(boardPanel);
		boardPanel.setLayout(null);

		lblScore = new JLabel("Score : 0");
		lblScore.setForeground(new Color(255, 255, 255));
		lblScore.setFont(new Font("Consolas", Font.BOLD, 18));
		lblScore.setBounds(35, 203, 204, 22);
		boardPanel.add(lblScore);

		lblHighScore = new JLabel("High Score :");
		lblHighScore.setForeground(new Color(255, 255, 255));
		lblHighScore.setFont(new Font("Consolas", Font.BOLD, 18));
		lblHighScore.setBounds(35, 281, 204, 24);
		boardPanel.add(lblHighScore);

		lblStatus = new JLabel("Status : pause");
		lblStatus.setForeground(new Color(255, 255, 255));
		lblStatus.setFont(new Font("Consolas", Font.BOLD, 18));
		lblStatus.setBounds(35, 229, 204, 24);
		boardPanel.add(lblStatus);

		lblGameLevel = new JLabel("Game Level :  Nomal");
		lblGameLevel.setForeground(new Color(255, 255, 255));
		lblGameLevel.setFont(new Font("Consolas", Font.BOLD, 18));
		lblGameLevel.setBounds(35, 254, 204, 24);
		boardPanel.add(lblGameLevel);

		backBtn = new JLabel("");
		backBtn.setToolTipText("Back Button");
		backBtn.setBounds(10, 495, 41, 44);
		boardPanel.add(backBtn);
		backBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				panel.setVisible(true);
				box.setVisible(false);
				boardPanel.setVisible(false);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				backBtn.setIcon(
						new ImageIcon("src/snake/images/back_red.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				backBtn.setIcon(
						new ImageIcon("src/snake/images/back_white.png"));
			}

		});
		backBtn.setIcon(new ImageIcon("src/snake/images/back_white.png"));
		
		JLabel label = new JLabel("");
		label.setBounds(0, 0, 270, 539);
		boardPanel.add(label);

		backgroundImg = new JLabel("");
		backgroundImg.setBorder(new LineBorder(Color.WHITE, 2));
		backgroundImg.setIcon(new ImageIcon("src/snake/images/snake.png"));
		backgroundImg.setBounds(0, 0, 1340, 660);
		getContentPane().add(backgroundImg);

		this.addKeyListener(this);

		this.validate();
		box.validate();
	}

	void over() {
		for (int i = snakeX.size() - 1; i > 3; i--) {
			if (snakeX.get(i) == snakeX.get(0) && snakeY.get(i) == snakeY.get(0) - 20) {
				isGameOver = true;
			}
		}
		System.out.println("Game Over new function "+isGameOver);
	}

	boolean isGameOver;

	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println("Event Listener");

		switch (key) {
		case "R": // Right move
			x += 20;
			break;
		case "L": // LEft move
			x -= 20;
			break;
		case "U": // Up Move
			y -= 20;
			break;
		case "D": // Down move
			y += 20;
			break;
		}

		//
		over();
		
		// print Snake 
		printSnake();
	
		// Game over snippet
		if (x == 1040 || x == -20 || y == 540 || y == -20) {
			isGameOver = true;
			overLabel.setVisible(true);
			timer.stop();
			if (snake.size() - 3 > score.getScore()) {
				score.setScore(snake.size() - 3);
				try {
					writeScore();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		if (snakeX.getFirst() == foodX && snakeY.getFirst() == foodY) {
			foodGenerate();
			foodPrint();

			// Create new body and set icon
			snake.addLast(new JLabel(bodyIcon, SwingConstants.CENTER)); // add in snake
			snake.getLast().setBounds(snakeX.getLast(), snakeY.getLast(), 20, 20);
			box.add(snake.getLast()); // add on box panel

			snakeX.addLast(snakeX.getLast()); // add snake X and last
			snakeY.addLast(snakeY.getLast());

			System.out.println("foot eaten");
			lblScore.setText("Score : " + (snake.size() - 3));

		}
		snake.getFirst().setIcon(icon); // set icon left, right, down, right
		// reload
		reloadPosition(x, y);
		System.out.println(speed+" score.getScore() -------------------------------- >  " + score.getScore());
	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	String key = "R";
	boolean isStart = false;

	ImageIcon icon;
	private JLabel backgroundImg;
	private JPanel gameLevelPanel;
	private JLabel lblGameLevel_1;

	@Override
	public void keyPressed(KeyEvent event) {
		System.out.println("KeyEvent Trigger");
		int keyCode = event.getKeyCode();

		if (keyCode == KeyEvent.VK_UP && !key.equals("D")) {
			key = "U";
			icon = headIconUp;
//                snake.getFirst().setIcon(headIconRight);
		} else if (keyCode == KeyEvent.VK_DOWN && !key.equals("U")) {
			key = "D";
			icon = headIconDown;
		} else if (keyCode == KeyEvent.VK_LEFT && !key.equals("R")) {
			key = "L";
			icon = headIconLeft;
		} else if (keyCode == KeyEvent.VK_RIGHT && !key.equals("L")) {
			key = "R";
			icon = headIconRight;
		} else if (keyCode == KeyEvent.VK_SPACE) {
			if (!isStart) {
				timer.start();
				isStart = true;
				lblStatus.setText("Status : playing");
			} else {
				timer.stop();
				isStart = false;
				lblStatus.setText("Status : pause");
			}
			if (isGameOver) {
//				System.out.println("888888888888888888 ---------------------------- >>>>>> ");
				initGame();
				isGameOver = false;
			}
		}
		// snake.getFirst().setIcon(icon);
	}

	void foodGenerate() {
		Random rand = new Random();
		foodX = rand.nextInt(1020);
		foodY = rand.nextInt(530);
		while (foodX % 20 != 0 || foodY % 20 != 0) {
			foodX = rand.nextInt(1020);
			foodY = rand.nextInt(530);
			if (foodX % 20 == 0 && foodY % 20 == 0) {
				break;
			}
		}
		System.out.println(" food X " + foodX + " y : " + foodY);
	}

	void foodPrint() {
		food.setLocation(foodX, foodY);
	}

	void reloadPosition(int x1, int y1) {
		snakeX.addFirst(x1);
		snakeX.removeLast();
		snakeY.addFirst(y1);
		snakeY.removeLast();
	}

	void printSnake() {
		for (int i = 0; i < snake.size(); i++) {
			snake.get(i).setLocation(snakeX.get(i), snakeY.get(i));
		}
	}

	void initGame() {
		///
		box.removeAll();
		box.revalidate();
		box.repaint();

		timer = new javax.swing.Timer(speed, this);

		//
		score = new SnakeScore(gameLevel);

		List<Integer> list = Arrays.asList(80, 60, 40);
		List<Integer> list1 = Arrays.asList(60, 60, 60);
		snakeX = new LinkedList<>(list);
		snakeY = new LinkedList<>(list1);
		snake = new LinkedList<>();

		x = 80;
		y = 60;

		for (int i = 0; i < 3; i++) {
			snake.add(new JLabel(bodyIcon, SwingConstants.CENTER));
			snake.get(i).setBackground(Color.RED);
			snake.get(i).setBounds(snakeX.get(i), snakeY.get(i), 20, 20);
			box.add(snake.get(i));
			snake.get(i).setVisible(true);
		}
		icon = headIconRight;
		key = "R"; // RIGHT MOVE
		snake.getFirst().setIcon(headIconRight);
		box.setLayout(null);

		box.add(food);
		food.setSize(20, 20);
		food.setIcon(foodIcon);
		foodGenerate();
		foodPrint();

		// read Score from file
		readScore();

		// Game over label
		overLabel = new JLabel("Game over");
		overLabel.setBounds(450, 256, 200, 100);
		box.add(overLabel);
		overLabel.setBackground(Color.pink);
		overLabel.setFont(new Font("Arial", Font.BOLD, 38));
		overLabel.setForeground(Color.RED);
		overLabel.setVisible(false);

		// lblHighScore
		lblHighScore.setText("High Score : " + score.getScore());
		lblScore.setText("Score : 0");
	}

	void writeScore() throws FileNotFoundException {
		try(
			FileOutputStream fileOutputStream = new FileOutputStream(
					"G:/VS programs/Java 2021/Swing Programs/src/snake/Score.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
			) {
			objectOutputStream.writeObject(score);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	void readScore() {
		try(FileInputStream fileInputStream = new FileInputStream(
					"G:/VS programs/Java 2021/Swing Programs/src/snake/Score.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)
		    ) {
			score = (SnakeScore) objectInputStream.readObject();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new SnakeGame().setVisible(true);
		});
	}
}
