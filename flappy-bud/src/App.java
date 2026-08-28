import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bud");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
        FlappyBud flappyBud = new FlappyBud();
        frame.add(flappyBud);
        frame.pack();
        flappyBud.requestFocus();
        frame.setVisible(true);

    }
}
