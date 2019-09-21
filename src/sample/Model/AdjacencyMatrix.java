package Model;

public class AdjacencyMatrix {

    public static void setMatrix(int width, int height) {
        int[][] dots = new int[width][height];
        for(int i = 0; i < dots.length; i++){
            for(int j = 0 ; j < dots[0].length; j++){
                dots[i][j] = 0;
            }
        }
        for(int[] i : dots){
            for(int j : i){
                System.out.print(j + "\t");
            }
           System.out.println("\n");
        }
    }
}
