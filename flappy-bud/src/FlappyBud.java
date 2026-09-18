import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBud extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
    Image openingImage;
    Image btnTapToPlayImage;
    Image budImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Bud
    int budX = boardWidth/8;
    int budY = boardHeight/2;
    int budWidth = 68;
    int budHeight = 48;

    class Bud {
        int x = budX;
        int y = budY;
        int width = budWidth;
        int height = budHeight;
        Image img; 

        Bud(Image img) {
            this.img = img;
        }
    }

    //Pipes
    int pipeX = boardWidth;
    int pipeY = -30;
    int pipeWidth = 164;
    int pipeHeight = 412;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    // game logic
    boolean gameStarted = false;

    JButton playButton;

    Bud bud;

    int velocityX = -4; // rate at which the pipes move to the left
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    FlappyBud() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        setLayout(null);

        addKeyListener(this);

        // load images
        openingImage = new ImageIcon(getClass().getResource("./openingimage.png")).getImage();
        btnTapToPlayImage = new ImageIcon(getClass().getResource( "./btn-taptoplay.png")).getImage();
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        budImg = new ImageIcon(getClass().getResource("./flappybud.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bud
        bud = new Bud(budImg);
        pipes = new ArrayList<Pipe>();

        // play button
        int buttonWidth = 220; 
        int buttonHeight = 70; 

        Image scaledButtonImage = btnTapToPlayImage.getScaledInstance( buttonWidth, buttonHeight, Image.SCALE_SMOOTH );
        playButton = new JButton(new ImageIcon(scaledButtonImage));
        
        int buttonX = (boardWidth - buttonWidth) / 2;

        int buttonY = boardHeight - 120;
        
        playButton.setBounds( buttonX, buttonY, buttonWidth, buttonHeight );
        
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setOpaque(false);

        playButton.setMargin(new Insets(0, 0, 0, 0));

        playButton.addActionListener(e -> startGame());

        add(playButton);
        
        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });


        // game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 16.6
    }

    public void startGame() {
        gameStarted = true;

        playButton.setVisible(false);

        pipes.clear();

        bud.y = boardHeight / 2;
        velocityY = 0;

        placePipesTimer.start();
        gameLoop.start();

        requestFocusInWindow();

        repaint();
    }

    public void placePipes() {
        // (0-1) * pipeHeight/2 -> (0-256)
        // 128
        // 0 - 128 - (0-256) --> pipeHeight/4 -> 3/4 piepHeight
 
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = 200;
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY; 
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // opening screen
       if (!gameStarted) {
            g.drawImage(openingImage, 0, 0, boardWidth, boardHeight, null);
            return;
        }

        draw(g);
    }
    
    public void draw(Graphics g) {

        //background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
    
        //bud
        g.drawImage(bud.img, bud.x, bud.y, bud.width, bud.height, null);
  
        // pipes
        for (int i=0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
    }

    public void move() {
        // bud
        velocityY += gravity;
        bud.y += velocityY;
        bud.y = Math.max(bud.y, 0);

        // pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX; 
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
       repaint(); 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
