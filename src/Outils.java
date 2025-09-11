import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

public class Outils {

//    public static ArrayList<Ipv4> getSystemIP() {
//        ArrayList<Ipv4> nomInetAddress = new ArrayList<>();
//        NetworkInterface networkInterface;
//        Enumeration<NetworkInterface> interfaces;
//        try {
//            // Liste toutes les interfaces réseau disponibles
//            interfaces = NetworkInterface.getNetworkInterfaces();
//            // Parcours des interfaces réseau
//            while (interfaces.hasMoreElements()) {
//                networkInterface = interfaces.nextElement();
//                // Ignore les interfaces inactives, loopback, ou virtuelles
//                if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isVirtual()) {
//                    // Récupère les adresses IP associées à  ces interfaces
//                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
//                    while (inetAddresses.hasMoreElements()) {
//                        InetAddress inetAddress = inetAddresses.nextElement();
//                        // Filtre les adresses IPv4 et évite les adresses locales
//                        if (inetAddress.getHostAddress().contains(".") && !inetAddress.isLoopbackAddress()) {
//                            nomInetAddress.add(new Ipv4(networkInterface.getDisplayName(),inetAddress.getHostName(),inetAddress.getHostAddress()));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return nomInetAddress;
//    }

    public static ArrayList<Ipv4> getSystemIP() throws SocketException {
        ArrayList<Ipv4> nomInetAddress = new ArrayList<>();
        // Conversion Enumeration -> Arraylist
        ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        // Parcours des interfaces réseau
        interfaces.forEach(networkInterface -> {
            try {
                if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isVirtual()) {
                    ArrayList<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                    inetAddresses.forEach(inetAddress -> {
                        if (inetAddress.getHostAddress().contains(".") && !inetAddress.isLoopbackAddress()) {
                            nomInetAddress.add(new Ipv4(networkInterface.getDisplayName(), inetAddress.getHostName(), inetAddress.getHostAddress()));
                        }
                    });
                }
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
        });
        return nomInetAddress;
    }
}
