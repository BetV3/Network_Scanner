package network;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.*;

public class Primary{
    public static void main(String[] args) {
    long startTime = System.nanoTime();
    ExecutorService executor = Executors.newFixedThreadPool(100);
    List<Future<String>> futures = new ArrayList<>();

    String ipSubnet = "10.0.1.";
    for (int i = 1; i < 255; i++) {
        String ipAddr = ipSubnet + i;
        futures.add(executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (isHostReachable(ipAddr)) {
                    String mac = getMACFromIP(ipAddr);
                    return ipAddr + " is reachable. MAC address: " + mac;
                }
                return null;
            }
        }));
    }

    executor.shutdown();
    for (Future<String> future : futures) {
        try {
            String result = future.get();
            if (result != null) {
                System.out.println(result);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    long endTime = System.nanoTime();
    System.out.println("Time taken: " + (endTime - startTime) / 1000000000 + " sec");
    }
    public static boolean isHostReachable(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 " + ipAddress);
            int returnVal = process.waitFor();
            return returnVal == 0;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Get MAC address from IP using Process
    public static String getMACFromIP(String ipAddress) {
        String regex = "([0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2})";
        String input = "";
        try {
            Process process = Runtime.getRuntime().exec("arp -a " + ipAddress);
            int returnVal = process.waitFor();
            if (returnVal == 0) {
                java.io.InputStream is = process.getInputStream();
                java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
                input = s.hasNext() ? s.next() : "";
                s.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "MAC address not found.";
        }
    }

}