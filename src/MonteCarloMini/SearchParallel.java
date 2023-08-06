package MonteCarloMini;

import java.util.concurrent.RecursiveTask; 
import java.util.concurrent.ForkJoinPool; 

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

    protected Integer compute() // need implementation of compute for RecursiveTask must return the min
    {
        if ( isBelowSequentialCutoff()) // inside here we would have the implementation of the search task
        {
            
        }
        else // inside the else we need to split and keep splitting the threads.
        {

        }
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

	
}
