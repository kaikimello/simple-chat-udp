package chat.client;

import java.net.*;
import java.util.*;


public class ChatMain {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        try {
            int porta;
            String nome;
            System.out.println("1 - Cliente Listener");
            System.out.println("2 - Cliente");
            int opcaoCliente = entrada.nextInt();
            System.out.println();
            boolean flagListener = opcaoCliente == 1;

            InetAddress ip;

            do {
                System.out.println("Informe seu nome");
                nome = entrada.next();
            } while (nome == null);

            UDPClient client;

            if (flagListener){
                do {
                    System.out.println("A porta informada deve estar entre:");
                    System.out.println("4000 e 6000");
                    System.out.println("Informe a porta que deseja ouvir");
                    porta = entrada.nextInt();
                } while((porta < 4000 || porta > 6000));

                ip = InetAddress.getByName(pegarIpMaquina()[0]);

                client = new UDPClient(ip, porta, nome);
                client.listener();
            }else {
                do {
                    System.out.println("A porta informada deve estar entre:");
                    System.out.println("4000 e 6000");
                    System.out.println("Informe a porta que deseja se comunicar");
                    porta = entrada.nextInt();
                } while((porta < 4000 || porta > 6000));

                String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
                String ipInformado;
                do {
                    System.out.println("Informe o IP que deseja se comunicar!");
                     ipInformado = entrada.next();
                } while (!(ipInformado.matches(PATTERN)));

                String[] ipArray = ipInformado.split("\\.");
                byte[] enderecoIPListener = {(byte) Integer.parseInt(ipArray[0]), (byte) Integer.parseInt(ipArray[1]),
                        (byte) Integer.parseInt(ipArray[2]), (byte) Integer.parseInt(ipArray[3])};

                InetAddress ipListener = InetAddress.getByAddress(enderecoIPListener);
                ip = InetAddress.getByName(pegarIpMaquina()[0]);
                client = new UDPClient(ip, porta, nome, ipListener);
                client.sendMenssage();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    private static String[] pegarIpMaquina() {
        Set<String> enderecosHost= new HashSet<>();
        try {
            for (NetworkInterface networkInterface: Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!networkInterface.isLoopback() && networkInterface.isUp() && networkInterface.getHardwareAddress() != null){
                    for (InterfaceAddress interfaceAddress: networkInterface.getInterfaceAddresses()) {
                        if (interfaceAddress.getBroadcast() != null){
                            enderecosHost.add(interfaceAddress.getAddress().getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return enderecosHost.toArray(new String[0]);
    }
}
