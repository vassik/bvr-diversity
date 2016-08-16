package no.sintef.bvr;

import no.sintef.bvr.spl.FeatureSet;
import no.sintef.bvr.spl.ProductLine;
import no.sintef.bvr.spl.ProductSet;
import no.sintef.bvr.spl.Product;
import no.sintef.bvr.spl.Feature;
import no.sintef.bvr.spl.ProductLineReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import no.sintef.bvr.metrics.Coverage;
import no.sintef.bvr.metrics.DistanceMatrix;
import no.sintef.bvr.metrics.Diversity;
import no.sintef.bvr.sampler.Sampler;
import no.sintef.bvr.sampler.diversity.DiversitySampler;
import no.sintef.bvr.sampler.diversity.evolution.EvolutionListener;

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
            int sampleSize = 3;

            if (arguments.length == 3) {
                pathToProductLine = arguments[0];
                sampleSize = Integer.parseInt(arguments[1]);
                maxEpoch = Integer.parseInt(arguments[2]);
            }

            ProductLine productLine = loadProductLine(pathToProductLine);
            display.productLineLoaded(productLine);

            Sampler sampler = new DiversitySampler(productLine, DESIRED_DIVERSITY, maxEpoch, new SingleThreadedEvolutionReporter(display));
            ProductSet result = sampler.sample(sampleSize);

            Diversity diversity = new Diversity();
            Coverage coverage = new Coverage(productLine.features());
            display.show(productLine.features(), result, coverage.of(result), diversity.of(result));

            storeDistanceMatrix(result);
 
        } catch (IOException ex) {
            display.unknownFile(SOURCE_FILE);

        }
    }

    private ProductLine loadProductLine(String pathToProductLine) throws IOException, FileNotFoundException {
        InputStream inputFile = new FileInputStream(pathToProductLine);
        ProductLineReader read = new ProductLineReader();
        ProductLine productLine = read.from(inputFile);
        return productLine;
    }

    private void storeDistanceMatrix(ProductSet result) throws FileNotFoundException {
        DistanceMatrix distanceMatrix = new DistanceMatrix();
        double[][] matrix = distanceMatrix.of(result);
        CsvDistanceMatrixFormatter csv = new CsvDistanceMatrixFormatter(new FileOutputStream("distance_matrix.csv"));
        csv.write(matrix);
    }

    private static final double DESIRED_DIVERSITY = 1D;
    private static final String SOURCE_FILE = "sample_pl.txt";

    public static void main(String arguments[]) {
        Controller controller = new Controller();
        controller.execute(arguments);
    }

}

class SingleThreadedEvolutionReporter extends EvolutionListener {

    private final Console display;

    public SingleThreadedEvolutionReporter(Console display) {
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
        try {
            tasks.put(new Runnable() {
                @Override
                public void run() {
                    display.progress(epoch, MAX_EPOCH, fitness);
                }
            }
            );
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void complete() {
        try {
            tasks.put(new Runnable() {
                
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
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
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
                    if (stopped) {
                        break;
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

    }

}

class Console {

    public static final String TOOL_NAME = "BVR Diversity Sampler -- %s\n";
    public static final String ERROR__UNKNOWN_FILE = "Error: Unable to load file '%s'";
    public static final String PRODUCT_LINE_OVERVIEW = "Product line (%d features ; %d constraints)\n";
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
        show(PRODUCT_LINE_OVERVIEW, productLine.features().count(), productLine.products().size());
    }

    void show(FeatureSet features, ProductSet sample, double coverage, double diversity) {
        show("\nResults (Cov=%.3f %%; FAD=%.3f):\n", coverage, diversity);
        showTableHeader(features, sample);
        showTableBody(features, sample);
    }

    private void showTableHeader(FeatureSet features, ProductSet sample) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(String.format(TABLE_COLUMN_WIDTH, "P/F"));
        for (Feature eachFeature : features) {
            buffer.append(String.format(TABLE_COLUMN_WIDTH, eachFeature.name()));
        }
        buffer.append("\n");
        show(buffer.toString());
    }

    private static final String TABLE_COLUMN_WIDTH = "%4s ";

    private void showTableBody(FeatureSet features, ProductSet products) {
        for (int index = 0; index < products.size(); index++) {
            show(" P%02d", index);
            for (Feature eachFeature : features) {
                Product eachProduct = products.withKey(index);
                if (eachProduct.offers(eachFeature)) {
                    show(TABLE_COLUMN_WIDTH, "X");
                } else {
                    show(TABLE_COLUMN_WIDTH, "~");
                }
            }
            show("\n");
        }
    }

    void progress(int epoch, int MAX_EPOCH, double fitness) {
        show(PROGRESS, epoch, MAX_EPOCH, fitness);
    }

    private void show(String pattern, Object... values) {
        output.printf(pattern, values);
    }

}
