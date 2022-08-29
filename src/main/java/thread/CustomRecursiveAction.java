package thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.MyVariantInfoIterator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class CustomRecursiveAction extends RecursiveAction {

    private final List<String> workload;
    private static final int THRESHOLD = 10;

    private static Logger logger =
            Logger.getAnonymousLogger();

    public CustomRecursiveAction(List<String> workload) throws JsonProcessingException {
        this.workload = workload;
        compute();
    }

    @Override
    protected void compute() {
        if (workload.size() > THRESHOLD) {
            try {
                ForkJoinTask.invokeAll(createSubtasks());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                processing(workload);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private List<CustomRecursiveAction> createSubtasks() throws JsonProcessingException {
        List<CustomRecursiveAction> subtasks = new ArrayList<>();

        List<String> partOne = workload.subList(0, workload.size() / 2);
        List<String> partTwo = workload.subList(workload.size() / 2, workload.size());

        subtasks.add(new CustomRecursiveAction(partOne));
        subtasks.add(new CustomRecursiveAction(partTwo));

        return subtasks;
    }

    private void processing(List<String> work) throws FileNotFoundException {

//        String result = work.toString();
        MyVariantInfoIterator.getMyVariantInfo(work);
//        logger.info("This result - (" + result + ") - was processed by "
//                + Thread.currentThread().getName());
    }
}
