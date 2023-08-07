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

    public SearchParallel(int id, int pos_row, int pos_col, TerrainArea terrain) {
		this.id = id;
		this.pos_row = pos_row; //randomly allocated
		this.pos_col = pos_col; //randomly allocated
		this.terrain = terrain;
		this.stopped = false;
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
        
        if (avgSize < SEQUENTIAL_CUTOFF)
        {
          return true ;  
        }
        else
        {
            return false ;
        }
    }

    private int sequentialSearch() // our find_valley
    {
        int height=Integer.MAX_VALUE;
		Directions.Direction next = Directions.Direction.STAY_HERE;
		while(terrain.visited(pos_row, pos_col)==0) { // stop when hit existing path
			height=terrain.get_height(pos_row, pos_col);
			terrain.mark_visited(pos_row, pos_col, id); //mark current position as visited
			steps++;
			next = terrain.next_step(pos_row, pos_col);
			switch(next) {
				case STAY_HERE: return height; //found local valley
				case LEFT: 
					pos_row--;
					break;
				case RIGHT:
					pos_row=pos_row+1;
					break;
				case UP: 
					pos_col=pos_col-1;
					break;
				case DOWN: 
					pos_col=pos_col+1;
					break;
			}
		}
		stopped=true;
		return height; 
    }

    protected Integer compute() // need implementation of compute for RecursiveTask must return the min
    {
        if ( isBelowSequentialCutoff()) // inside here we would have the implementation of the search task
        {
            return sequentialSearch() ;
        }
        else // inside the else we need to split and keep splitting the threads.
        {
            double xMid = (xmax + xmin) / 2.0 ; // 2.0 for double, doesnt really matter.
            double yMid = (ymax + ymin) / 2.0 ;

            SearchParallel topLeft = new SearchParallel(xmin, xMid, ymin, yMid, terrain);
            SearchParallel topRight = new SearchParallel(xMid, xmax, ymin, yMid, terrain);
            SearchParallel bottomLeft = new SearchParallel(xmin, xMid, yMid, ymax, terrain);
            SearchParallel bottomRight = new SearchParallel(xMid, xmax, yMid, ymax, terrain);

            topLeft.fork() ;
            topRight.fork();
            bottomLeft.fork();
            bottomRight.fork();

            int localMin = Integer.MAX_VALUE;
            localMin = Math.min(localMin, topLeft.join());
            localMin = Math.min(localMin, topRight.join());
            localMin = Math.min(localMin, bottomLeft.join());
            localMin = Math.min(localMin, bottomRight.join());

            return localMin ;

            // fork the subtasks to be executed in parall
        }
    }
	
}
