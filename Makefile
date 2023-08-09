JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
.SUFFIXES: .java .class
SRCDIR=src/MonteCarloMini
BINDIR=bin

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= Directions.class \
TerrainArea.class \
Search.class \
MonteCarloMinimization.class \
SearchParallel.class \
MonteCarloMinimizationParallel.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/MonteCarloMini/*.class

serial: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.MonteCarloMinimization 200 200 -2 2 -2 2 0.5
	
	
parallel: $(CLASS_FILES)
	$(JAVA) -cp bin MonteCarloMini.MonteCarloMinimizationParallel 200 200 -2 2 -2 2 0.5
# Command Line Inputs rows columns xmin xmax ymin ymax density