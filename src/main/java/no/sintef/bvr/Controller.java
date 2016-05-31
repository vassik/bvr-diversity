package no.sintef.bvr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import no.sintef.bvr.sampler.Sample;
import no.sintef.bvr.sampler.Sampler;
import no.sintef.bvr.sampler.diversity.DiversitySampler;
import no.sintef.bvr.sampler.diversity.EvolutionListener;

public class Controller {

    private final Console display;

    public Controller() {
        this(new Console(System.out));
    }

    public Controller(Console display) {
        this.display = display;
    }

    public void execute(String arguments[]) {
        try {
            display.opening();

            String pathToProductLine = SOURCE_FILE;
            int maxEpoch = 10000;

            if (arguments.length == 2) {
                pathToProductLine = arguments[0];
                maxEpoch = Integer.parseInt(arguments[1]);
            }

            InputStream inputFile = new FileInputStream(pathToProductLine);
            int sampleSize = 3;

            ProductLineReader read = new ProductLineReader();
            ProductLine productLine = read.from(inputFile);
            display.productLineLoaded(productLine);

            Sampler sampler = new DiversitySampler(sampleSize, DESIRED_DIVERSITY, maxEpoch, new MonoThreadEvolutionReporter(display));
            Sample result = sampler.sample(productLine);
            display.show(result);

        } catch (IOException ex) {
            display.unknownFile(SOURCE_FILE);

        }
    }

    private static final double DESIRED_DIVERSITY = 1D;
    private static final String SOURCE_FILE = "sample_pl.txt";

    public static void main(String arguments[]) {
        Controller controller = new Controller();
        controller.execute(arguments);
    }

}

class MonoThreadEvolutionReporter extends EvolutionListener {
    
    
    private final Console display;

    public MonoThreadEvolutionReporter(Console display) {
        this.display = display;
    }
    
    @Override
    public void epoch(int epoch, int MAX_EPOCH, double fitness) {
        display.progress(epoch, MAX_EPOCH, fitness);
    }
    
}

class MultiThreadEvolutionReporter extends EvolutionListener {

    private final Console display;
    private final BlockingQueue<Runnable> tasks;
    private final Processor processor;
    private final Thread thread;
    
    public MultiThreadEvolutionReporter(Console display) {
        this.display = display;
        this.tasks = new ArrayBlockingQueue<>(1000);
        this.processor = new Processor(tasks);
        this.thread = new Thread(processor);
        this.thread.start();
    }

    @Override
    public void epoch(final int epoch, final int MAX_EPOCH, final double fitness) {
        tasks.add(new Runnable() {
            @Override
            public void run() {
                display.progress(epoch, MAX_EPOCH, fitness);
            }
        }
        );
    }

    @Override
    public void complete() {
        tasks.add(new Runnable() {

            @Override
            public void run() {
                processor.stop();
                try {
                    thread.join(5000L);
                
                } catch (InterruptedException ex) {
                    // TODO do something
                }
            }
        });
    }
    
    

    static class Processor implements Runnable {

        private final BlockingQueue<Runnable> tasks;
        private volatile boolean stopped;

        public Processor(BlockingQueue<Runnable> tasks) {
            this.tasks = tasks;
            this.stopped = false;
        }
             
        public void stop() {
            this.stopped = true;
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    Runnable nextTask = tasks.take();
                    nextTask.run();
                    if (stopped) break;
                    
                } catch (InterruptedException ex) {
                    
                }
            }
        }

    }

}

class Console {

    public static final String TOOL_NAME = "BVR Diversity Sampler -- %s\n";
    public static final String ERROR__UNKNOWN_FILE = "Error: Unable to load file '%s'";
    public static final String PRODUCT_LINE_OVERVIEW = "Product line (%d features ; %d constraints)\n";
    public static final String RESULTS = "\nResults:\n";
    public static final String PROGRESS = "\rEpoch %d/%d ~ fitness: %.2f";

    private final PrintStream output;

    public Console(OutputStream output) {
        this.output = new PrintStream(output);
    }

    void opening() {
        final String currentVersion = Console.class.getPackage().getImplementationVersion();
        show(TOOL_NAME, currentVersion);
    }

    void unknownFile(String SOURCE_FILE) {
        show(ERROR__UNKNOWN_FILE, SOURCE_FILE);
    }

    void productLineLoaded(ProductLine productLine) {
        show(PRODUCT_LINE_OVERVIEW, productLine.featureCount(), productLine.constraints().size());
    }

    void show(Sample result) {
        show(RESULTS + result);
    }

    void progress(int epoch, int MAX_EPOCH, double fitness) {
        show(PROGRESS, epoch, MAX_EPOCH, fitness);
    }

    private void show(String pattern, Object... values) {
        output.printf(pattern, values);
    }

}
