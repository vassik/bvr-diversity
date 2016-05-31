package no.sintef.bvr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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

            InputStream inputFile = new FileInputStream(SOURCE_FILE);
            int sampleSize = 3;

            ProductLineReader read = new ProductLineReader();
            ProductLine productLine = read.from(inputFile);
            display.productLineLoaded(productLine);

            Sampler sampler = new DiversitySampler(sampleSize, DESIRED_DIVERSITY, new EvolutionReporter(display));
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


class EvolutionReporter extends EvolutionListener {
    
    private final Console display;

    public EvolutionReporter(Console display) {
        this.display = display;
    }

    @Override
    public void epoch(int epoch, int MAX_EPOCH, double fitness) {
        display.progress(epoch, MAX_EPOCH, fitness);
    }
    
    
}

class Console {

    private final PrintStream output;
    
    public Console(OutputStream output) {
        this.output = new PrintStream(output);
    }

    void opening() {
        output.println("BVR Diversity Sampler");
    }

    void unknownFile(String SOURCE_FILE) {
        output.println(String.format("Error: Unable to load file '%s'", SOURCE_FILE));
    }

    void productLineLoaded(ProductLine productLine) {
        output.println(String.format("Product line (%d features ; %d constraints)", productLine.featureCount(), productLine.constraints().size()));
    }

    void show(Sample result) {
        output.println("\nResults:\n" + result);
    }

    void progress(int epoch, int MAX_EPOCH, double fitness) {
        output.println(String.format("Epoch %d/%d ~ fitness: %.2f", epoch, MAX_EPOCH, fitness));
    }

}
