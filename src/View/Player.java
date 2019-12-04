package View;
        import AI.AISolver;
        import AI.AlphaBeta;
        import AI.Mcts;
        import GameTree.Node;
        import GameTree.State;
//import AI.AlphaBeta;
        import javafx.scene.paint.Color;
        import Controller.Controller;
        import java.util.ArrayList;

public class Player {
    private AISolver solver;
    private Color color;
    private int moves;
    private int score;
    private String name;
    private static ArrayList<Player> players = new ArrayList<>();
    private String ai;

    public Player(Color color, String name, String ai) {
        this.color = color;
        this.name = name;
        this.moves = 1;
        this.score = 0;
        this.ai= ai;
        players.add(this);
        switch (ai) {
            case  "Mcts":
                solver = new Mcts();
                break;
            case "Rule Based":
                solver = new RuleBased();
                break;
            case "Alpha Beta" :
                solver = new AlphaBeta();
                break;
        }
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public void addScore(int toAdd) {
        this.score += toAdd;
    }

    public void addMoves() {
        this.moves += 1;
    }

    public boolean isAi() {
        //System.out.println("ai = " + ai);
        if (ai == "Human"){
            return false;
        }else{
            return true;
        }
    }

    public String getAiType(){
        return ai;
    }


    public static Player getActualPlayer(){
        return players.get(Controller.turn);
    }

    public int getScore(){
        return this.score;
    }

    public Color getColor() {
        return this.color;
    }

    public int getMoves() {
        return this.moves;
    }

    public String getName() {
        return this.name;
    }

    public void decreaseMoves() {
        this.moves -= 1;
    }


    public void aiPlay() {
        //System.out.println("called ai player");
        Line chosenLine = solver.nextMove(State.currentState(), Integer.parseInt(name));
        chosenLine.fill();
        System.out.println("ai fill "+chosenLine.getid());
    }

    public boolean isAlpha() {
        if (ai == "Alpha Beta") {
            return true;
        }
        else{
            return false;
        }
    }


    private static boolean mctsFirstIteration=true;
    public void mcts() {
        if (mctsFirstIteration) {
            solver.nextMove(State.currentState(), Integer.parseInt(name));
            mctsFirstIteration=false;
        }else {
            solver.setNewRoots(State.currentState());
            solver.nextMove(State.currentState(), Integer.parseInt(name));
        }

    }

    public static void display(){
        for(Player p : players){
            System.out.println("p = " + p.ai);
        }
    }

    public static boolean getFistTurn() {
        return mctsFirstIteration;
    }

}