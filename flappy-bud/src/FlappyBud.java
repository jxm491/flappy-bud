import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBud extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image backgroundImg;
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
    int pipeWidth = 154;
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
        addKeyListener(this);

        // load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        budImg = new ImageIcon(getClass().getResource("./flappybud.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();

        // bud
        bud = new Bud(budImg);
        pipes = new ArrayList<Pipe>();

        // place pipes timer
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();


        // game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 16.6
        gameLoop.start();
    }

    public void placePipes() {
        // (0-1) * pipeHeight/2 -> (0-256)
        // 128
        // 0 - 128 - (0-256) --> pipeHeight/4 -> 3/4 piepHeight
 
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = boardHeight/4;
        
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY; 
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
