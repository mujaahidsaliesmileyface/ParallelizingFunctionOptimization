#!/bin/bash

# Clear previous CSV file (optional)
rm -f results.csv

# Run the program 5 times and append results to CSV
for i in {1..5}
do
    echo "Running iteration $i"
    java -cp bin MonteCarloMini.MonteCarloMinimizationParallel 200 200 0 10 0 10 0.5 >> results.csv
done

echo "All iterations completed"