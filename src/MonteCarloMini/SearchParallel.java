package MonteCarloMini;

import java.util.concurrent.RecursiveTask; 
import java.util.concurrent.ForkJoinPool; 
import java.util.Random;

public class SearchParallel extends RecursiveTask<Integer>
{
    private int id;				// Searcher identifier
	private int pos_row, pos_col;		// Position in the grid
	private int steps; //number of steps to end of search
	private boolean stopped;			// Did the search hit a previous trail?
    
    static final int SEQUENTIAL_CUTOFF = 750; // exactly between 500 and 1000, will probably play around with 
                                        
    private double xmin,xmax,ymin,ymax ; // we are going to use this for our cut off
	private TerrainArea terrain;

    public SearchParallel(int id,TerrainArea terrainArea)
    {
        this.id = id ;
        this.terrain = terrain ;
        this.stopped = false ;
    }
   
    public SearchParallel(int id, int pos_row, int pos_col, TerrainArea terrain) {
		this.id = id;
		this.pos_row = pos_row; //randomly allocated
		this.pos_col = pos_col; //randomly allocated
		this.terrain = terrain;
		this.stopped = false;
	}

    private int lo,hi ;

    public SearchParallel(int lo, int hi, TerrainArea terrain)
    {
        this.lo = lo;
        this.hi = hi;
        this.terrain = terrain;
    }

	public int getID() {
		return id;
	}

	public int getPos_row() {
		return pos_row;
	}

	public int getPos_col() {
		return pos_col;
	}

	public int getSteps() {
		return steps;
	}
	public boolean isStopped() {
		return stopped;
	}

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
        
        return avgSize < SEQUENTIAL_CUTOFF ;
    }

    private int sequentialSearch(int lo, int hi) // our find_valley
    {
        int min =Integer.MAX_VALUE;
        for (int i = lo; i < hi; i++) {
            int numPoints = hi - lo; // Number of grid points in the subgrid
            int row = lo / numPoints; // Calculate row index
            int col = lo % numPoints; // Calculate column index

            Search search = new Search(i, row, col, terrain);
            int localMin = search.find_valleys();
            if (!search.isStopped() && localMin < min) {
                min = localMin;
            }
        }
        return min;
    }

    private int combineResults(int currentMin, int subtaskResult)
    {
        return Math.min(currentMin, subtaskResult) ;
    }

    protected Integer compute() // need implementation of compute for RecursiveTask must return the min
    {
        if (isBelowSequentialCutoff()) // inside here we would have the implementation of the search task
        {
            return sequentialSearch(lo, hi) ;
        }
        else // inside the else we need to split and keep splitting the threads.
        {
            int mid = (hi + lo) / 2;

            SearchParallel left = new SearchParallel(xmin, xmax, ymin, ymax, terrain);
            left.lo = lo;
            left.hi = mid;

            SearchParallel right = new SearchParallel(xmin, xmax, ymin, ymax, terrain);
            right.lo = mid;
            right.hi = hi;

            left.fork() ;
            int rightAns = right.compute() ;
            int leftAns = left.join() ;

            return combineResults(leftAns, rightAns);

        }
    }
	
}
