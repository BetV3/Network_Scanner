package network;

import java.util.ArrayList;
import java.util.HashMap;
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
        HashMap <IP, List<Integer>> portMap = new HashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        List<Future<String>> futures = new ArrayList<>();
        List<Future<Integer>> portFutures = new ArrayList<>();
        String ipAddr = Utilities.getAddress();
        System.out.println("Your IP address is: " + ipAddr);
        System.out.println("Enter network mask (default: 24): ");
        if (in.nextLine() != "\n") {
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



        // Scanning for open IP addresses
        for (long i = network + 1; i < broadcast; i++) {
            String currIP = Utilities.longToIp(i);
            futures.add(executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (isHostReachable(currIP)) {
                        String mac = getMACFromIP(currIP);
                        IP ip = new IP(currIP, new MAC(mac));
                        if (!portMap.containsKey(ip)) {
                            List<Integer> ports = new ArrayList<>();
                            portMap.put(ip, ports);
                        }
                        return currIP + " is reachable. MAC address: " + mac;
                    }
                    return null;
                }
            }));
        }      
        // Waiting for the tasks to finish scanning before prompting for port scanning
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
        // Prompting for port scanning
        System.out.println("Would you like to port scan the network? (y/n)");
        String response = in.next();
        if (response.equals("y")) {
            System.out.println("Enter port range (default: 1-1024): ");
            int startPort = 1;
            int endPort = 1024;
            if (in.hasNextInt()) {
                startPort = in.nextInt();
                if (in.hasNextInt()) {
                    endPort = in.nextInt();
                }
            }
            for (IP curIP : portMap.keySet() ) {
                final int start = startPort;
                final int end = endPort;
                futures.add(executor.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String result = curIP.getIP() + ":\n";
                        for (int port = start; port <= end; port++) {
                            final int curPort = port;
                            portFutures.add(executor.submit(new Callable<Integer>() {
                                @Override
                                public Integer call() throws Exception {
                                    if (Utilities.isPortOpen(curIP.getIP(), curPort)) {
                                        portMap.get(curIP).add(curPort);
                                        //System.out.println("Port " + curPort + " is open on " + curIP.getIP());
                                        return curPort;
                                    }
                                    return null;
                                }
                            }));
                        }
                        return result;
                    }
                }));
            }
        }
        else {
            System.out.println("Exiting.");
            System.exit(0);
        }
        // Waiting to see if port scanning is finished

        // waiting to check if ip is finished being scanned
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
        for (Future<Integer> future : portFutures) {
            try {
                Integer result = future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }         
        executor.shutdown();
        for (IP nIp : portMap.keySet()) {
            System.out.println(nIp.getIP() + ": " + portMap.get(nIp));
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