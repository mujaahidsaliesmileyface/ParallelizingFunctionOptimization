package MonteCarloMini;

import java.util.concurrent.RecursiveTask; 
import java.util.Arrays;


public class SearchParallel extends RecursiveTask<Integer>
{
    
    static final int SEQUENTIAL_CUTOFF = 550; // Changed as needed. Landed on this value because it works the best in my case. 
    
    private Search[] searches ; // created an array of searches to use to split recursively.
    private int min;
    // Initialize with an invalid value
    int minRow = -1; 
    int minCol = -1;


    // constructors
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
            int minLocal = Integer.MAX_VALUE; // sets to maximum possible integer so smaller is always returned.
            int localMinRow = -1;
            int localMinCol = -1;


            for (int i = 0; i < searches.length; i++) { // sequential algo
                int localMin = searches[i].find_valleys();
                if (!searches[i].isStopped() && localMin < minLocal) {
                    minLocal = localMin;
                    localMinRow = searches[i].getPos_row(); // Store the row of the minimum
                    localMinCol = searches[i].getPos_col(); // Store the column of the minimum
                }
            }
                if (minLocal < min) { // returning the co ords.
                    min = minLocal;
                    minRow = localMinRow;
                    minCol = localMinCol;
                }
            return minLocal; // return minimum
        }
        else // inside the else we need to split and keep splitting the threads.
        {
            // Split the search tasks array into two halves to distribute the work among threads
            int mid = searches.length / 2;

            // Create new instances of SearchParallel for the left and right half of array
            SearchParallel leftSearch = new SearchParallel(Arrays.copyOfRange(searches, 0, mid));
            SearchParallel rightSearch = new SearchParallel(Arrays.copyOfRange(searches, mid, searches.length));

            // Start the parallel execution for the leftSearch task
            leftSearch.fork();

            // Calculate the result of the rightSearch task
            int rightResult = rightSearch.compute();

            // Wait for the leftSearch task to complete and get its result
            int leftResult = leftSearch.join();

            // Return the minimum of the results obtained from leftSearch and rightSearch
            return Math.min(leftResult, rightResult);

        }
    }
	
}
