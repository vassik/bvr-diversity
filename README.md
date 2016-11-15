# The BVR Diversity Sampler

Given a product-line, the BVR diversity sampler generates a set of products that are very different 
one another. It uses a genetic algorithm (GA) that maximises the diversity between the selected 
products.

The BVR sampler comes with *an experiment* that compares it to random sampling. We describe below 
how to reproduce this experiment, that is:
 1. How to build the JAVA binary files
 1. How to run the experiment
 1. How to visualise the results.

## How to Build the Binaries?
The BVR diversity sampler is a regular Java/Maven application. Here are the steps to build it:

 1. Choose your favourite directory, say `my/workplace` for instance. In a terminal, move to this 
	very directory:
	````
	$> cd my/workplace
	````

 1. Then, fetch the Java source code from Github by entering the following command. Note that `git` 
	should be installed on your machine (See the [Git homepage](https://git-scm.com/)).
	````
	$> git clone https://github.com/SINTEF-9012/bvr-diversity.git
	````

 1. Git automatically creates a directory named `bvr-diversity`. Move into this directory and 
	use Maven to build compile and package the binaries. Note that this requires a Internet connection as Maven will
	fetch online any needed dependency. (See also the [Maven homepage](https://maven.apache.org/).)
	````
	$> cd bvr-diversity
	$> mvn clean install
	````

 1. You are now ready to run the experiment. The binaries are located into the `target` folder, 
	that Maven created. The result is an executable JAR file 
	`target/bvr-diversity.jar`. 	
	
	
## How to Run the Experiment?	
The source code contains an experiment that compares the BVR diversity sampler with random sampling, 
that is the random selection of valid products. The comparison looks at the diversity and the feature 
coverage of the resulting set of products.

 1. You can now run the experiment, by entering the following commands. It generates a CSV file that
	contains the diversity and the coverage measured for both random sampling and diversity sampling.
	````
	$> java -jar target/bvr-diversity.jar
	````
 
 
## How to Visualise the Results?
We provide a R script, to help visualise how the BVR diversity sampler compares to random sampling.
Assuming that R is installed on your machine (see the [R homepage](https://www.r-project.org/)), 
enter the following commands, which produces two PDF files, namely `coverage.pdf` and `diversity.pdf`.

````
$> R CMD BATCH view_results.r
````
 



