package MonteCarloMini;

import java.util.concurrent.RecursiveTask; 
import java.util.concurrent.ForkJoinPool; 
import java.util.Random;

public class SearchParallel extends RecursiveTask<Integer>
{

    static final int SEQUENTIAL_CUTOFF = 750; // exactly between 500 and 1000, 
                                              // will probably play around with 

    private double xmin,xmax,ymin,ymax ; // we are going to use this for our cut off
	private TerrainArea terrain;

    SearchParallel (double xmin, double xmax, double ymin, double ymax, TerrainArea terrain) // constructor for search parallel.
    {
        this.xmin = xmin ;
        this.xmax = xmax ; 
        this.ymin = ymin ;
        this.ymax = ymax ;
        this.terrain = terrain ;
        // initialising instance variables
    }

    private Boolean isBelowSequentialCutoff() 
    // We're going to use a helper function to determine if our value is below the sequential cutoff
    {
        double xSize = xmax - xmin ;
        double ySize = ymax - ymin ;

        double avgSize = (xSize + ySize) /2 ; // we're going to use a single value so comparision is easier
        
        if (avgSize < SEQUENTIAL_CUTOFF)
        {
          return true ;  
        }
        else
        {
            return false ;
        }
    }

    private int sequentialSearch()
    {
        Random rand = new Random() ;
        int startX = rand.nextInt((int) (xmax - xmin + 1)) + (int) xmin;
        int startY = rand.nextInt((int) (ymax - ymin + 1)) + (int) ymin;

        Search searchMin = new Search(1, startX,startY,terrain);

        int localMin = searchMin.find_valleys();
        return localMin ;
    }

    protected Integer compute() // need implementation of compute for RecursiveTask must return the min
    {
        if ( isBelowSequentialCutoff()) // inside here we would have the implementation of the search task
        {
            return sequentialSearch() ;
        }
        else // inside the else we need to split and keep splitting the threads.
        {

        }
        return 1 ; // just so that it stops complaining for now
    }
	
}
