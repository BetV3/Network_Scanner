package network;
import java.util.regex.*;

public class Primary{
    public static void main(String[] args) {
        MAC mac = new MAC("00:00:00:00:00:00");
        IP ip = new IP("1.1.1.1", mac);
        System.out.println(ip.toString());
        String ipSubnet = "10.0.1.";
        for (int i = 1; i < 255; i++) {
            String ipAddr = ipSubnet + i;
            System.out.println("Checking " + ipAddr);
            if (isHostReachable(ipAddr)) {
                System.out.println(ipAddr + " is reachable.");
                String input = getMACFromIP(ipAddr);
                System.out.println("MAC address: " + input);
            }
        }
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