import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Split {
    private File file;

    public Split(File file) {
        this.file = file;
    }

    // Processes the given portion of the file.
    // Called simultaneously from several threads.
    // Use your custom return type as needed, I used String just to give an example.
    public String processPart(long start, long end)
        throws Exception
    {
        InputStream is = new FileInputStream(file);
        is.skip(start);
        // do a computation using the input stream,
        // checking that we don't read more than (end-start) bytes
        System.out.println("Computing the part from " + start + " to " + end);
        Thread.sleep(1000);
        Scanner scanner = new Scanner(is);
        for(int i=(int) start;i<end;i++){
        	if(scanner.hasNextLine())
        			System.out.println(scanner.nextLine());
        }
        scanner.close();
        System.out.println("Finished the part from " + start + " to " + end);

        is.close();
        return "";
    }

    // Creates a task that will process the given portion of the file,
    // when executed.
    public Callable<String> processPartTask(final long start, final long end) {
        return new Callable<String>() {
            public String call()
                throws Exception
            {
                return processPart(start, end);
            }
        };
    }

    // Splits the computation into chunks of the given size,
    // creates appropriate tasks and runs them using a 
    // given number of threads.
    public void processAll(int noOfThreads, int chunkSize)
        throws Exception
    {
        int count = (int)((file.length() + chunkSize - 1) / chunkSize);
        System.out.println(file.length()+ " " + count);
        java.util.List<Callable<String>> tasks = new ArrayList<Callable<String>>(count);
        for(int i = 0; i < count; i++)
            tasks.add(processPartTask(i * chunkSize, Math.min(file.length(), (i+1) * chunkSize)));
        ExecutorService es = Executors.newFixedThreadPool(noOfThreads);

        java.util.List<Future<String>> results = es.invokeAll(tasks);
        es.shutdown();

        // use the results for something
        for(Future<String> result : results)
            System.out.println(result.get());
    }

    public static void main(String argv[])
        throws Exception
    {
    	String s1 = "C:\\Users\\mosrivastava\\Documents\\testfile.txt";
    	File f = new File(s1);
        Split s = new Split(f);
        s.processAll(8, 1);
    }
}