# Define the number of runs
NUM_RUNS = 5

# Define the CSV file name
CSV_FILE = results.csv

JAVAC = /usr/bin/javac
JAVA = /usr/bin/java
.SUFFIXES: .java .class
SRCDIR = src/MonteCarloMini
BINDIR = bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES = Directions.class \
TerrainArea.class \
Search.class \
MonteCarloMinimization.class \
SearchParallel.class \
MonteCarloMinimizationParallel.class

CLASS_FILES = $(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/MonteCarloMini/*.class

run_serial:
	@echo "Run serial version $(NUM_RUNS) times and output to $(CSV_FILE)"
	@for i in `seq $(NUM_RUNS)`; do \
		$(JAVA) -Xmx6g -cp bin MonteCarloMini.MonteCarloMinimization 250 250 -250 250 -250 250 0.5 >> $(CSV_FILE); \
	done

run_parallel:
	@echo "Run parallel version $(NUM_RUNS) times and output to $(CSV_FILE)"
	@for i in `seq $(NUM_RUNS)`; do \
		$(JAVA) -Xmx6g -cp bin MonteCarloMini.MonteCarloMinimizationParallel 250 250 -250 250 -250 250 0.5 >> $(CSV_FILE); \
	done
