package thread;

import org.example.MyVariantInfoIterator;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class IterateList implements Runnable{
    private List<String> workload;
    Map<String, String> pathologies;

    public IterateList(List<String> workload, Map<String, String> pathologies) {
        this.workload = workload;
        this.pathologies = pathologies;
    }

    @Override
    public void run() {
//        try {
//            MyVariantInfoIterator.getMyVariantInfo(workload, pathologies);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
