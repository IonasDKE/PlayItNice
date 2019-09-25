import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {

    private int size;
    boolean launchGame;
    JFrame frame;
    String[] playersType = {"Select an option", "Human", "AI"};
    String[] gameSizes = {"3 x 3", " 3 x 2" , "4 x 4", "5 x 4" , "8 x 8", "11 x 9"};
    JComboBox playerSelection;
    ButtonGroup buttongroup;
    JRadioButton[] sizeSelectionButton;


    public Main(){


        frame = new JFrame();
        frame.setTitle("Dots and Boxes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setRadioButtons();

    }

    /**
     * @see {https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html}
     */
    public void GUI(){

        JLabel welcome = new JLabel("Dots-and-Boxes");

        //GridBagConstaints set to a default value
        JPanel gameGrid = new JPanel(new GridBagLayout());
        GridBagConstraints setZero = new GridBagConstraints();


        setZero.gridx = 0;
        setZero.gridy = 0;
        gameGrid.add(welcome, setZero);

        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(setEmptySpace(new Dimension( 500,150)),setZero);

        JPanel chooseMode = new JPanel(new GridLayout(2,1));
        chooseMode.setPreferredSize(new Dimension(400, 50));
        chooseMode.add(new JLabel("Opponent:"));
        chooseMode.add(playerSelection);
        playerSelection.setSelectedIndex(0);  //Sets the "selection" label as the first index of the string

        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(chooseMode, setZero);

        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(setEmptySpace(new Dimension(500, 25)), setZero);

        setZero.gridy = setZero.gridy + 1;

        JLabel sizeLabel = new JLabel("Size of the board:");
        sizeLabel.setPreferredSize(new Dimension(400, 50));
        gameGrid.add(sizeLabel, setZero);

        setZero.gridy = setZero.gridy + 1;
        JPanel sizePanel = new JPanel(new GridLayout(3, 2));
        sizePanel.setPreferredSize(new Dimension(400, 100));
        sizePanel.setAlignmentX(250);
        for(int i = 0; i < 6 ; i++){
            sizePanel.add(sizeSelectionButton[i]);
        }
        buttongroup.clearSelection();
        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(sizePanel, setZero);

        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(setEmptySpace(new Dimension(500,25)), setZero);

        JButton startButton = new JButton("Start Game");
        startButton.setForeground(Color.BLACK);
        startButton.addActionListener(setButtonOnAction);
        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(startButton, setZero);

        setZero.gridy = setZero.gridy + 1;
        gameGrid.add(setEmptySpace(new Dimension(500,25)), setZero);

        frame.setContentPane(gameGrid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        launchGame = false;
        while(!launchGame){
            try{

                Thread.sleep(100);

            }   catch(InterruptedException e){

                e.printStackTrace();
            }
        }
        new Game(this, frame, size);
    }

    private JLabel setEmptySpace(Dimension dim){
        JLabel space = new JLabel();
        space.setPreferredSize(dim);
        return space;
    }

    private void setRadioButtons(){
        sizeSelectionButton = new JRadioButton[gameSizes.length];
        buttongroup = new ButtonGroup();

        for(int i = 0; i < gameSizes.length; i++){
            sizeSelectionButton[i] = new JRadioButton(gameSizes[i]);
            buttongroup.add(sizeSelectionButton[i]);
        }


        playerSelection = new JComboBox<>(playersType);
    }

    private ActionListener setButtonOnAction = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent event){

            for(int i = 0; i < 6; i++){
                if(sizeSelectionButton[i].isSelected()){
                    size = i + 3;
                    launchGame = true;
                    return;
                }
            }
        }
    };

    public static void main(String[] args){
       new Main().GUI();
    }

}
