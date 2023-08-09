# ParallelizingFunctionOptimization
Parallelizing Monte Carlo Function Optimisation

## Installation

To compile and run the project, you'll need to have Java installed on your system. If you are using a macOS environment, it should be installed. If you are on a windows environment, you can follow these steps:

1. Download Java: Visit the official Java website (https://www.oracle.com/java/technologies/javase-downloads.html) or the AdoptOpenJDK website (https://adoptopenjdk.net/) to download the Java Development Kit (JDK).
2. Install JDK: Run the downloaded installer and follow the on-screen instructions to install the JDK.
3. Verify installation in command prompt as follows : 

```bash 
    java -version 
```

To get the program and run it, you can follow these instructions: 


```bash 
# You'll first need to clone the git  repository
    git clone https://github.com/mujaahidsaliesmileyface.ParallelizingFunctionOptimization
# You then need to navigate to the correct directory using 
    cd ParallelAssignment2023
# You can then compile and run as needed. 
    javac -d bin src/MonteCarloMini/*.java
```

A makefile is included in the directory. Use it as follows 
```bash 
# serial version
    make run serial
# parallel version
    make run parallel
```
You can change the paramemters within the makefile, as seen below: 
```bash 
    serial: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.MonteCarloMinimization 200 200 -2 2 -2 2 0.5
	
	
parallel: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.MonteCarloMinimizationParallel 200 200 -2 2 -2 2 0.5
# Command Line Inputs rows columns xmin xmax ymin ymax density
```