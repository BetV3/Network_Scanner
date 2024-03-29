package network;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;

public class Utilities {
    public static String getAddress() {
        Scanner in = new Scanner(System.in);
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                // Check if the interface is up and not a loopback interface
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    // Check if the interface has IP addresses associated with it
                    List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                    for (InetAddress address : addresses) {
                        // Prefer interfaces with IPv4 addresses
                        System.out.println("Interface with ip address: " + address.getHostAddress());
                        System.out.println("Is this the correct interface? (y/n)");
                        String response = in.nextLine();
                        if (response.equals("y")) {
                            return address.getHostAddress();
                        }
                        else if(response.equals("n")) {
                            continue;
                        }
                        else {
                            System.out.println("No nic found. Exiting.");
                            System.exit(1);
                        
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static long ipToLong(IP address){
        byte[] octets = address.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }
    public static String longToIp(long ip) {
        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                sb.insert(0, '.');
            }
            ip >>= 8;
        }
        return sb.toString();
    }
}   
