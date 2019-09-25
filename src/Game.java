import javax.swing.*;
import java.awt.*;


public class Game {


    boolean mouseOver;
    int xLayout = 15;
    int yLayout = 75;
    Main source;
    private JFrame frame;
    private int size;
    private int bWidth;
    private JLabel firstScore, secondScore;
    private JLabel[][] horLine, verLine , square;

    public Game(Main source, JFrame frame, int size){
        this.source = source;
        this.frame = frame;
        this.size = size;
        startGame();
    }

    public void startGame(){
        bWidth = size * xLayout + (size-1) * yLayout;

        JPanel gameGrid = new JPanel(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        gameGrid.add(setEmptySpace(new Dimension(2 * bWidth, 10)), con);


        con.gridy = con.gridy + 1;
        gameGrid.add(showPlayerString(), con);
        con.gridy = con.gridy + 1;
        gameGrid.add(setEmptySpace(new Dimension(2 * bWidth, 10)), con);



        horLine = new JLabel[size-1][size];

        verLine = new JLabel[size][size-1];

        //Because if there are 4x4 dots , there are 3x3 boxes (9)
        square = new JLabel[size-1][size-1];

        for(int i = 0 ; i < (2*size-1) ; i++){
            JPanel linesAndCircles = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));

            //If the index is horizontal

            if(i%2==0) {

                linesAndCircles.add(drawSquare());

                for(int k=0; k<(size -1); k++) {
                    horLine[k][i/2] = getHorizontalEdge();
                    linesAndCircles.add(horLine[k][i/2]);
                    linesAndCircles.add(drawSquare());
                }

            }

            //The index is vertical
            else {
                for(int k=0; k<(size-1); k++) {
                    verLine[k][i/2] = drawVerEdge();
                    linesAndCircles.add(verLine[k][i/2]);
                    square[k][i/2] = getSquare();
                    linesAndCircles.add(square[k][i/2]);
                }
                verLine[size-1][i/2] = drawVerEdge();
                linesAndCircles.add(verLine[size-1][i/2]);
            }
            con.gridy = con.gridy + 1;
            gameGrid.add(linesAndCircles, con);
        }
        con.gridy = con.gridy + 1;
        gameGrid.add(setEmptySpace(new Dimension(2 * bWidth, 20)), con);

        con.gridy = con.gridy + 1;
        gameGrid.add(showScore(), con);

        //TODO
        gameManager();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();

        frame.setContentPane(gameGrid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private JLabel setEmptySpace(Dimension dim){
        JLabel space = new JLabel();
        space.setPreferredSize(dim);
        return space;
    }

    private JPanel showPlayerString(){
        JPanel showPLayer = new JPanel(new GridLayout(1, 2));
        showPLayer.setPreferredSize(new Dimension(2 * bWidth, 2 * yLayout));
        showPLayer.add(new JLabel("<html><font family = 'georgia' size='7' color='red'>Player-1:", SwingConstants.CENTER));
        showPLayer.add(new JLabel("<html><font size='7' color='blue'>Player-2:", SwingConstants.CENTER));
        return showPLayer;
    }

    private JPanel showScore(){
        JPanel score = new JPanel(new GridLayout(2, 2));
        score.setPreferredSize(new Dimension(2 * bWidth, yLayout));
        score.add(new JLabel("<html><p style=\"font-size:150%;color:#6c1c20\";> Score: </p>", SwingConstants.CENTER));
        score.add(new JLabel("<html><font  size='7' color='blue'>Score:", SwingConstants.CENTER));
        firstScore = new JLabel("<html><font size='7' color='red'>0", SwingConstants.CENTER);
        secondScore = new JLabel("<html><font size='7' color='blue'>0", SwingConstants.CENTER);
        firstScore.setForeground(Color.RED);
        secondScore.setForeground(Color.BLUE);
        score.add(firstScore);
        score.add(secondScore);
        return score;
    }
    private void gameManager(){
        mouseOver = false;
    }

    private JLabel getHorizontalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(yLayout, xLayout));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //This is why it appears like a double line
        label.setOpaque(true);
        return label;
    }

    private JLabel drawSquare() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(xLayout, xLayout));

        label.setBackground(Color.BLUE);
        label.setOpaque(true);
        return label;
    }
    private JLabel drawVerEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(xLayout, yLayout));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        return label;
    }
    private JLabel getSquare() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(yLayout, yLayout));
        label.setOpaque(true);
        return label;
    }



}
