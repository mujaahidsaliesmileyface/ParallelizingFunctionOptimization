package MonteCarloMini;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class MonteCarloMinimizationParallel {

    public static void main(String[] args) {

        int rows, columns; //grid size
        double xmin, xmax, ymin, ymax; //x and y terrain limits
        TerrainArea terrain; //object to store the heights and grid points visited by searches
        double searches_density; // Density - number of Monte Carlo  searches per grid position - usually less than 1!

        int num_searches; // Number of searches
        Search[] searches; // Array of searches
        Random rand = new Random(); //the random number generator

        if (args.length != 7) {
            System.out.println("Incorrect number of command line arguments provided.");
            System.exit(0);
        }
        /* Read argument values */
        rows = Integer.parseInt(args[0]);
        columns = Integer.parseInt(args[1]);
        xmin = Double.parseDouble(args[2]);
        xmax = Double.parseDouble(args[3]);
        ymin = Double.parseDouble(args[4]);
        ymax = Double.parseDouble(args[5]);
        searches_density = Double.parseDouble(args[6]);

        // Initialize
        terrain = new TerrainArea(rows, columns, xmin, xmax, ymin, ymax);
        num_searches = (int) (rows * columns * searches_density);
        searches = new Search[num_searches];
        for (int i = 0; i < num_searches; i++)
            searches[i] = new Search(i + 1, rand.nextInt(rows), rand.nextInt(columns), terrain);

        // Create a ForkJoinPool with the default number of threads
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Perform the parallel searches using SearchParallel
        SearchParallel searchParallel = new SearchParallel(0, num_searches, terrain);
        int min = forkJoinPool.invoke(searchParallel);

        // Print the global minimum
        System.out.printf("Global minimum: %d%n", min);
    }
}
