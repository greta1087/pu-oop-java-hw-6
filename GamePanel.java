import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 120;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 1;
    int foodEaten;
    int foodX;
    int foodY;
    int obstacleX;
    int obstacleY;
    char direction = 'R';
    boolean gameRunning = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        snakeFood();
        gameRunning = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if(gameRunning) {

                g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(Color.green);
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(45, 180, 0));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }

                g.setColor(Color.white);
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + (foodEaten * 10), (SCREEN_WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2, g.getFont().getSize());

        } else {

             if (foodEaten >= 30) {
                 youWin(g);
             } else {
                 gameOver(g);
             }
        }
    }


    public void snakeFood(){
        foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void obstacle(){
        obstacleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        obstacleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkSnakeFood() {
        if((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            snakeFood();
        }
    }

    public void checkGameEnd() {
        //checks if the snake collides with it's body
        for(int i = bodyParts;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                gameRunning = false;
            }
        //checks if the goal food is reached
            if (foodEaten >= 30) {
                gameRunning = false;
            }
        }
        if(!gameRunning) {
            timer.stop();
        }
    }

    public void goThroughWalls() {
        //checks if snake touches left border
        if(x[0] < 0) {
            x[0] = SCREEN_WIDTH;
        }
        //checks if snake touches right border
        if(x[0] > SCREEN_WIDTH) {
            x[0] = 0;
        }
        //checks if snake touches top border
        if(y[0] < 0) {
            y[0] = SCREEN_HEIGHT;
        }
        //checks if snake touches bottom border
        if(y[0] > SCREEN_HEIGHT) {
            y[0] = 0;
        }
    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Comic Sans MS",Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ (foodEaten*10), (SCREEN_WIDTH - metrics1.stringWidth("Score: "+ foodEaten))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Comic Sans MS",Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    public void youWin(Graphics g) {

        //Score
        g.setColor(Color.green);
        g.setFont( new Font("Comic Sans MS",Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ (foodEaten*10), (SCREEN_WIDTH - metrics1.stringWidth("Score: "+ foodEaten))/2, g.getFont().getSize());
        //You win text
        g.setColor(Color.green);
        g.setFont( new Font("Comic Sans MS",Font.PLAIN, 60));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Congrats! You won!", (SCREEN_WIDTH - metrics2.stringWidth("Congrats! You won!"))/2, SCREEN_HEIGHT/2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(gameRunning) {
            move();
            checkSnakeFood();
            checkGameEnd();
            goThroughWalls();
        }
        repaint();
    }

    //The game is played with the arrow keys
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
