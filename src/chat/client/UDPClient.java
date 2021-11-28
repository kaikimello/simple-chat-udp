package chat.client;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient {

    private String nome;
    private InetAddress ip;
    private int porta;
    private byte[] receberBuffer = new byte[1024];
    private byte[] enviarBuffer = new byte[1024];

    Scanner input = new Scanner(System.in);

    public UDPClient(String nome) {
        this.nome = nome;
    }

    public UDPClient(InetAddress ip, int porta, String nome) {
        this.ip = ip;
        this.porta = porta;
        this.nome = nome;
    }

    public void listener(){
        try {
            DatagramSocket listenerSocket = new DatagramSocket(porta);
            System.out.println("Aguardando Contato");

            while (true){
                System.out.println(ip + ":" + porta + " | " + this.nome);
                DatagramPacket pacoteRecebido = new DatagramPacket(receberBuffer, receberBuffer.length);
                listenerSocket.receive(pacoteRecebido);
                InetAddress ipRemetente = pacoteRecebido.getAddress();
                int portaUserRemetente = pacoteRecebido.getPort();
                System.out.println("A porta do remetente é: "+ portaUserRemetente);
                String dadoClient = new String(pacoteRecebido.getData());
                System.out.println("\nCliente: "+ dadoClient);
                System.out.println("Cliente Listener: ");
                String dadoListener = input.nextLine();
                if(dadoListener.equalsIgnoreCase("/bye")){
                    System.out.println("Conexão encerrada pelo Cliente Listener");
                    break;
                }
                enviarBuffer = dadoListener.getBytes();
                DatagramPacket enviarPacoteListener = new DatagramPacket(enviarBuffer, enviarBuffer.length, ipRemetente, portaUserRemetente);
                listenerSocket.send(enviarPacoteListener);
            }
            listenerSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMenssage(){
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            while (true) {
                System.out.println(ip + ":" + porta + " | " + this.nome);
                String dadoCliente = input.nextLine();
                if (dadoCliente.equalsIgnoreCase("/bye")){
                    System.out.println("Conexão finalizada pelo Cliente");
                    break;
                }
                enviarBuffer = dadoCliente.getBytes();
                DatagramPacket pacoteEnviar = new DatagramPacket(enviarBuffer, enviarBuffer.length, ip, porta);
                clientSocket.send(pacoteEnviar);
                DatagramPacket pacoteRecebido = new DatagramPacket(receberBuffer, receberBuffer.length);
                clientSocket.receive(pacoteRecebido);
                String dadosRecebidosListener = new String(pacoteRecebido.getData());
                System.out.println("\nListener: " + dadosRecebidosListener);
            }
            clientSocket.close();
        } catch (UnknownHostException ex1) {
            ex1.printStackTrace();
        } catch (SocketException ex2) {
            ex2.printStackTrace();
        } catch (IOException ex3) {
            ex3.printStackTrace();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        try {
            System.out.println("Informe o IP: ");
            String ipInformado = entrada.next();
            InetAddress ip = InetAddress.getByName(ipInformado);
            System.out.println("Informe a porta");
            int porta = -1;
            String nome;

            do {
                System.out.println("A porta informada deve estar entre:");
                System.out.println("4000 e 6000");
                porta = entrada.nextInt();
                System.out.println("Informe seu nome");
                nome = entrada.next();
            }while((porta < 4000 || porta > 6000) && nome != null);
            System.out.println("1 - Cliente Listener");
            System.out.println("2 - Cliente");
            int opcaoCliente = entrada.nextInt();
            System.out.println();
            boolean flagListener = opcaoCliente == 1;
            if (flagListener){
                UDPClient clientListener = new UDPClient(ip, porta, nome);
                clientListener.listener();
            }else {
                UDPClient client = new UDPClient(ip, porta, nome);
                client.sendMenssage();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
