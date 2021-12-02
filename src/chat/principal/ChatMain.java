package chat.principal;

import chat.client.UDPClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatMain {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
//teste commit
        try {
            int porta = -1;
            String nome;
            System.out.println("1 - Cliente Listener");
            System.out.println("2 - Cliente");
            int opcaoCliente = entrada.nextInt();
            System.out.println();
            boolean flagListener = opcaoCliente == 1;
            InetAddress ip = InetAddress.getByName("localhost");
            do {
                System.out.println("Informe seu nome");
                nome = entrada.next();
            } while (nome == null);
            if (flagListener){
                do {
                    System.out.println("A porta informada deve estar entre:");
                    System.out.println("4000 e 6000");
                    System.out.println("Informe a porta que deseja ouvir");
                    porta = entrada.nextInt();
                } while((porta < 4000 || porta > 6000));

                UDPClient clientListener = new UDPClient(ip, porta, nome);
                clientListener.listener();
            }else {
                do {
                    System.out.println("A porta informada deve estar entre:");
                    System.out.println("4000 e 6000");
                    System.out.println("Informe a porta que deseja se comunicar");
                    porta = entrada.nextInt();
                } while((porta < 4000 || porta > 6000));

                UDPClient client = new UDPClient(ip, porta, nome);
                client.sendMenssage();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
