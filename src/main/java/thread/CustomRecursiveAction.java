package thread;

import org.example.MyVariantInfoIterator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CustomRecursiveAction extends RecursiveAction {

    private final List<String> workload;
    Map<String, String> pathologies;

    private static final int THRESHOLD = 100;

    private static Logger logger =
            Logger.getAnonymousLogger();

    public CustomRecursiveAction(List<String> workload, Map<String, String> pathologies) {
        this.workload = workload;
        this.pathologies = pathologies;
        compute();
    }

    @Override
    protected void compute() {
        if (workload.size() > THRESHOLD) {
//                for (CustomRecursiveAction subtask : createSubtasks()) {
//                    System.out.println(subtask.toString());
//                }

            ForkJoinTask.invokeAll(createSubtasks());

        } else {
            try {
                processing(workload);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<CustomRecursiveAction> createSubtasks() {
        List<CustomRecursiveAction> subtasks = new ArrayList<>();

        List<String> partOne = workload.subList(0, workload.size() / 2);
        List<String> partTwo = workload.subList(workload.size() / 2, workload.size());

        subtasks.add(new CustomRecursiveAction(partOne, pathologies));
        subtasks.add(new CustomRecursiveAction(partTwo, pathologies));

        return subtasks;
    }

    private void processing(List<String> work) throws FileNotFoundException {
//        MyVariantInfoIterator.getMyVariantInfo(work, this.pathologies);
//        logger.info("This result - (" + result + ") - was processed by "
//                + Thread.currentThread().getName());
    }

    public static <T> Stream<List<T>> batches(List<T> source, int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        int size = source.size();
        if (size <= 0)
            return Stream.empty();
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }
}
