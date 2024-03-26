package network;


class MAC {
    String macAddress;
    public MAC() {
        macAddress = "00:00:00:00:00:00";
    }
    public MAC(String mac) {
        // Check format of mac address
        if (mac.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            macAddress = mac;
        } else {
            macAddress = "00:00:00:00:00:00";
            System.out.println("Invalid MAC address format. Setting to default value.");
            System.out.println("To Change MAC address, use the setMAC method.");
            System.out.println("Format: 00:00:00:00:00:00");
        }
        macAddress = mac;
    }
    public void setMAC(String mac) {
        // Check format of mac address
        if (mac.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            macAddress = mac;
        } else {
            System.out.println("Invalid MAC address format. MAC address not changed.");
            System.out.println("Format: 00:00:00:00:00:00");
        }
    }
    public String getMAC() {
        return macAddress;
    }

    public String toString() {
        return macAddress;
    }

}
