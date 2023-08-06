package MonteCarloMini;


import java.util.Random;
import java.util.concurrent.ForkJoinPool ;

public class MonteCarloMinimizationParallel {


	static final boolean DEBUG=false;
	
	static long startTime = 0;
	static long endTime = 0; 
    //timers - note milliseconds

	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static void tock(){
		endTime=System.currentTimeMillis(); 
	}
    
    public static void main(String[] args)  {

    	int rows, columns; //grid size
    	double xmin, xmax, ymin, ymax; //x and y terrain limits
    	//TerrainArea terrain ;//object to store the heights and grid points visited by searches
    	double searches_density;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

     	int num_searches;		// Number of searches
    	SearchParallel [] searches;		// Array of searches
    	Random rand = new Random();  //the random number generator

        rows =Integer.parseInt( args[0] );
    	columns = Integer.parseInt( args[1] );
    	xmin = Double.parseDouble(args[2] );
    	xmax = Double.parseDouble(args[3] );
    	ymin = Double.parseDouble(args[4] );
    	ymax = Double.parseDouble(args[5] );
    	searches_density = Double.parseDouble(args[6] );

        TerrainArea terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
        SearchParallel rootTask = new SearchParallel(xmin, xmax, ymin, ymax, terrain);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int globalMin = forkJoinPool.invoke(rootTask);

        System.out.println("Global minimum: " + globalMin);
        System.out.println("Coordinates (x, y): (" + terrain.getXcoord(rootTask.getPos_col()) + ", " + terrain.getYcoord(rootTask.getPos_row()) + ")");

    	
    }
}