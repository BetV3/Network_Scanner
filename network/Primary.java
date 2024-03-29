package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.*;

public class Primary {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int mask = -1;
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<String>> futures = new ArrayList<>();
        String ipAddr = Utilities.getAddress();
        System.out.println("Your IP address is: " + ipAddr);
        System.out.println("Enter network mask (default: 24): ");
        if (in.hasNextInt()) {
            mask = in.nextInt();
            if (mask < 0 || mask > 32) {
                System.out.println("Invalid mask. Using default mask: 24");
                mask = 24;
            }
        }
        else {
            System.out.println("No mask entered. Using default mask: 24");
            mask = 24;
        }
        long startTime = System.nanoTime();
        long ip = Utilities.ipToLong(new IP(ipAddr));
        long network = ip & (-1L << (32 - mask));
        long broadcast = network | ((1L << (32 - mask)) - 1);
        System.out.println(ip);
        System.out.println(network);
        System.out.println(broadcast);

        for (long i = network + 1; i < broadcast; i++) {
            String currIP = Utilities.longToIp(i);
            futures.add(executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (isHostReachable(currIP)) {
                        String mac = getMACFromIP(currIP);
                        return currIP + " is reachable. MAC address: " + mac;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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