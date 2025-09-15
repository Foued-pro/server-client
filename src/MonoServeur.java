import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.net.InetAddress;
public class MonoServeur {

    public static void main(String[] args) throws IOException {
    //AFFICHER IP ET CHOIX
        System.out.println(Outils.getSystemIP());
        Scanner sc = new Scanner(System.in);
        System.out.print("Entre l'ip :");
        String ip = "10.0.0.108";  //sc.nextLine();

        final int port=4000;
        boolean deconnexionClientDemandee = false ;
        char [ ] bufferEntree = new char [65535];
        String messageRecu = null;
        String reponse = null;
        //créer une instance de la classe SocketServer en précisant le port en paramètre

        ServerSocket monServerDeSocket = new ServerSocket(4000,1,InetAddress.getByName(ip)); // socket d'ecoute

        //définir une boucle sans fin contenant les actions ci-dessous
        while (true) {
            //appelle de la méthode accept() qui renvoie une socket lors d'une nouvelle connexion
            Socket socketDuClient = monServerDeSocket.accept();
            System.out.println("Connexion avec : "+socketDuClient.getInetAddress());

            //obtenir un flux en entrée et en sortie à partir de la socket
            BufferedReader fluxEntree = new BufferedReader(new InputStreamReader(socketDuClient.getInputStream()));
            PrintStream fluxSortie = new PrintStream(socketDuClient.getOutputStream());

            //écrire les traitements à réaliser
            while (!deconnexionClientDemandee && socketDuClient.isConnected()){
                System.out.println("attente...");
                fluxSortie.println("Entrez une phase qui sera mise en majuscule par le serveur (exit pour finir)");
                //reception
                int NbLus = fluxEntree.read(bufferEntree);
                messageRecu= new String(bufferEntree, 0,NbLus);
                if (messageRecu.length() != 0 )
                {
                    System.out.println("\t\t Message reçu : " + messageRecu );
                }
                //TRAITEMENT
                System.out.println("\t\t Traitement Requete ... ");
                messageRecu = messageRecu.toLowerCase().trim();
                String messageRecuEcho = "";
                if (messageRecu.length() >=4) {
                    messageRecuEcho = messageRecu.substring(0,4);
                }
                switch (messageRecu){
                    case "fin":
                        reponse = "JE VOUS DECONNECTE !!!";
                        deconnexionClientDemandee = true;
                        break;
                    case "hello":
                        reponse = "Bienvenue !!!";
                        System.out.println("\t\t Message reçu : [" + messageRecu + "]");
                        break;
                    case "time":
                        reponse = "On est le :" + String.valueOf(LocalDate.now()) + " et il est "+String.valueOf(LocalTime.now());
                        break;
                    case "me":
                    case "whoami?":
                        reponse = "votre " + socketDuClient.getInetAddress().getHostAddress() + ":" + socketDuClient.getPort();
                        break;
                    case "you":
                        reponse = "mon port est " + monServerDeSocket.getInetAddress() + ":" + monServerDeSocket.getLocalPort();
                        break;
                    case "WHOAREYOU?":
                        reponse = "mon port est " + monServerDeSocket.getInetAddress() + ":" + monServerDeSocket.getLocalPort();
                        break;
                }
                if(messageRecuEcho.equalsIgnoreCase("echo")){
                    reponse = messageRecu.substring(5, messageRecu.length());
                }
                fluxSortie.println(reponse);
                System.out.println("\t\t Message emis : "+reponse);
            }

            //client vient de se deconnecter !!!
            socketDuClient.close();
            //faut réarmé pour le client suivant
            deconnexionClientDemandee = false;
        }
    }
}