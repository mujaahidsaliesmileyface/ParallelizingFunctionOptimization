package MonteCarloMini;

import java.util.concurrent.RecursiveTask; 
import java.util.Arrays;


public class SearchParallel extends RecursiveTask<Integer>
{
    
    static final int SEQUENTIAL_CUTOFF = 550; // Will probably play around with 
    
    private Search[] searches ;
    private int min;
    // Initialize with an invalid value
    int minRow = -1; 
    int minCol = -1;


    // constructor 
    public SearchParallel(Search[] searches, int min, int minRow, int minCol) 
    {
        this.searches = searches;
        this.min = min;
        this.minRow = minRow;
        this.minCol = minCol;
    }

    public SearchParallel(Search[] searches)
    {
        this.searches = searches ;
    } 

    protected Integer compute() // need implementation of compute for RecursiveTask must return the min
    {
        if ( searches.length <= SEQUENTIAL_CUTOFF) // inside here we would have the implementation of the search task
        {
            int minLocal = Integer.MAX_VALUE;
            int localMinRow = -1;
            int localMinCol = -1;


            for (int i = 0; i < searches.length; i++) {
                int localMin = searches[i].find_valleys();
                if (!searches[i].isStopped() && localMin < minLocal) {
                    minLocal = localMin;
                    localMinRow = searches[i].getPos_row(); // Store the row of the minimum
                    localMinCol = searches[i].getPos_col(); // Store the column of the minimum
                }
            }
                if (minLocal < min) {
                    min = minLocal;
                    minRow = localMinRow;
                    minCol = localMinCol;
                }
            return minLocal;
        }
        else // inside the else we need to split and keep splitting the threads.
        {
            int mid = searches.length / 2;

            
            SearchParallel leftSearch = new SearchParallel(Arrays.copyOfRange(searches, 0, mid));
            SearchParallel rightSearch = new SearchParallel(Arrays.copyOfRange(searches, mid, searches.length));

           
            leftSearch.fork();
            int rightResult = rightSearch.compute();

           
            int leftResult = leftSearch.join();

            
            return Math.min(leftResult, rightResult);
        }
    }
	
}
