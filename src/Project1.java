import java.util.Random;

public class Project1 {

    public static void main(String[] args){
        boolean[][] grid = genGrid(5, 0.25f);
        printGrid(grid);
    }

    /**
     * Generates a gridworld object of size dim x dim. index 0,0 is the start
     * state and index dim-1, dim-1 is the goal state.
     *
     * Blocked spaces are set to true; open spaces are set to false.
     * @param dim The dimensions of the gridworld.
     * @param p The probability of any generated cell initializing as blocked.
     * @return The 2D boolean array gridworld object.
     */
    public static boolean[][] genGrid(int dim, float p){
        boolean[][] gridworld = new boolean[dim][dim];
        for (int i=0; i<dim; i++){
            for (int j=0; j<dim; j++){
                gridworld[i][j] = new Random().nextDouble() <= p;
            }
        }
        gridworld[0][0] = false;
        gridworld[dim-1][dim-1] = false;
        return gridworld;
    }

    public static void printGrid(boolean[][] gridworld){
        for (int i=0; i<gridworld.length; i++){
            for (int j=0; j<gridworld.length; j++){
                if(gridworld[i][j]){
                    System.out.print("B ");
                } else{
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
    }


}
