//package thread;
//
//import org.example.MyVariantInfoIterator;
//
//import java.io.FileNotFoundException;
//import java.util.*;
//import java.util.concurrent.ForkJoinTask;
//import java.util.concurrent.RecursiveTask;
//
//public class CustomRecursiveTask extends RecursiveTask<Collection<CustomRecursiveTask>> {
//    private final List<String> workload;
//    Map<String, String> pathologies;
//
//    private static final int THRESHOLD = 20;
//
//    public CustomRecursiveTask(List<String> workload, Map<String, String> pathologies) {
//        this.workload = workload;
//        this.pathologies = pathologies;
//    }
//
//    @Override
//    protected Collection<CustomRecursiveTask> compute() {
//        if (workload.size() > THRESHOLD) {
//            return ForkJoinTask.invokeAll(createSubtasks());
////                    .stream()
////                    .mapToInt(ForkJoinTask::join)
////                    .sum();
//        } else {
//            try {
//                processing(workload, this.pathologies);
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private Collection<CustomRecursiveTask> createSubtasks() {
//        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
//        dividedTasks.add(new CustomRecursiveTask(
//                Arrays.copyOfRange(workload, 0, workload.size() / 2), workload));
//        dividedTasks.add(new CustomRecursiveTask(
//                Arrays.copyOfRange(workload, workload.length / 2, workload.length), workload));
//        return dividedTasks;
//    }
//
//    private void processing(List<String> work, Map<String, String> pathologies) throws FileNotFoundException {
////        return Arrays.stream(arr)
////                .filter(a -> a > 10 && a < 27)
////                .map(a -> a * 10)
////                .sum();
//        MyVariantInfoIterator.getMyVariantInfo(work, this.pathologies);
//    }
//}
