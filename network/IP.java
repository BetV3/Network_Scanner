package network;

class IP {
    MAC address;
    String ip;
    public IP() {
        address = new MAC();
        ip = "0.0.0.0";
    }
    public IP(String ip) {
        address = new MAC();
        // Check format of ip address
        if (ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            this.ip = ip;
        } else {
            this.ip = "0.0.0.0";
        }
    }
    public IP(String ip, MAC mac) {
        this.address = mac;
        // Check format of ip address
        if (ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            this.ip = ip;
        } else {
            this.ip = "0.0.0.0";
        }

    }
    public void setIP(String ip) {
        // Check format of ip address
        if (ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            this.ip = ip;
        } else {
            System.out.println("Invalid IP address format. IP address not changed.");
            System.out.println("Format: 0.0.0.0");
        }
    }
    public String getIP() {
        return ip;
    }
    public byte[] getAddress() {
        String[] octets = ip.split("\\.");
        byte[] address = new byte[4];
        for (int i = 0; i < 4; i++) {
            address[i] = (byte) Integer.parseInt(octets[i]);
        }
        return address;
    }
    public String getMAC() {
        return address.getMAC();
    }
    public void setMAC(String mac) {
        address.setMAC(mac);
    }
    public MAC getMACObject() {
        return address;
    }
    public int hashCode() {
        return ip.hashCode();
    }
    public String toString() {
        return ip + " " + address.toString();
    }

}
