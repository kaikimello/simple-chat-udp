package chat.client;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {

    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";



    private String nome;
    private InetAddress ip;
    private InetAddress ipListener;
    private int porta;

    Scanner input = new Scanner(System.in);

    public UDPClient(InetAddress ip, int porta, String nome) {
        this.ip = ip;
        this.porta = porta;
        this.nome = nome;
    }

    public UDPClient(InetAddress ipClient, int porta, String nome, InetAddress ipListener) {
        this.ip = ipClient;
        this.porta = porta;
        this.nome = nome;
        this.ipListener = ipListener;
    }

    public void listener(){
        try {
            DatagramSocket listenerSocket = new DatagramSocket(this.porta);
            System.out.println("Aguardando Contato");

            while (true){
                byte[] receberBufferClient = new byte[1024];
                byte[] enviarBufferClient;
                /*
                * Criar um Datagram Packet, recupera o ip, a porta do cliente remetente e a mensagem
                *
                * */
                DatagramPacket pacoteRecebido = new DatagramPacket(receberBufferClient, receberBufferClient.length);
                listenerSocket.receive(pacoteRecebido);
                InetAddress ipClientRemetente = pacoteRecebido.getAddress();
                int portaUserRemetente = pacoteRecebido.getPort();

                /*
                * Separa a mensagem do nome do Cliente Remetente
                * Exibe a mensagem do enviada pelo cliente remetente
                * */
                String dadoClient = new String(pacoteRecebido.getData(), StandardCharsets.UTF_8);
                String mensagemClient = dadoClient.substring(0, dadoClient.indexOf("&"));
                String nomeCliente = dadoClient.substring(dadoClient.indexOf("&")+1);
                System.out.println(PURPLE_BOLD_BRIGHT + "Mensagem Recebida de "+ ipClientRemetente.getHostAddress() + ":"+ portaUserRemetente + " | " + nomeCliente.trim() + ": " + mensagemClient + TEXT_RESET);

                /*
                * Solicita que o Cliente Listener responda, caso a mensagem seja /bye encerra a
                * comunicação
                * */
                System.out.println(ip.getHostAddress() + " | " + this.nome);
                String dadoListener = input.nextLine();
                if(dadoListener.equalsIgnoreCase("/bye")){
                    System.out.println("Conexão encerrada pelo Cliente Listener");
                    break;
                }

                /*
                * Concatena o nome do Cliente Listener a mensagem, converte para bytes, e envia o datagrama
                * */

                dadoListener = dadoListener + "&" + this.nome;
                enviarBufferClient = dadoListener.getBytes();
                DatagramPacket enviarPacoteListener = new DatagramPacket(enviarBufferClient, enviarBufferClient.length, ipClientRemetente, portaUserRemetente);
                listenerSocket.send(enviarPacoteListener);
            }
            listenerSocket.close();
        }  catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMenssage(){
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            while (true) {
                byte[] bufferEnviarListener;
                byte[] bufferReceberListener = new byte[1024];
                System.out.println(ip.getHostAddress() + " | " + this.nome);
                String dadoCliente = input.nextLine();
                /*
                 * Caso a mensagem digitada seja /bye é finalizada a comunicação
                 */

                if (dadoCliente.equalsIgnoreCase("/bye")){
                    System.out.println("Conexão finalizada pelo Cliente");
                    break;
                }
                /*
                * Concatena o nome do Cliente a mensagem, converte para bytes, e envia o datagrama
                * */
                dadoCliente = dadoCliente + "&" + this.nome;
                bufferEnviarListener = dadoCliente.getBytes();
                DatagramPacket pacoteEnviar = new DatagramPacket(bufferEnviarListener, bufferEnviarListener.length, ipListener, porta);
                clientSocket.send(pacoteEnviar);

                /*
                * Cria um Datagrama Packet, recupera a mensagem, o Ip e o nome
                * Exibe a mensagem do Listener
                * */
                DatagramPacket pacoteRecebido = new DatagramPacket(bufferReceberListener, bufferReceberListener.length);
                clientSocket.receive(pacoteRecebido);
                String dadosRecebidosListener = new String(pacoteRecebido.getData(), StandardCharsets.UTF_8);
                InetAddress ipListener = pacoteRecebido.getAddress();
                int portaUserListener = pacoteRecebido.getPort();
                String mensagemListener = dadosRecebidosListener.substring(0, dadosRecebidosListener.indexOf("&"));
                String nomeListener = dadosRecebidosListener.substring(dadosRecebidosListener.indexOf("&")+1);
                System.out.println(CYAN_BOLD_BRIGHT+ "Mensagem recebida de "+ ipListener.getHostAddress() + ":" + portaUserListener + " | " + nomeListener.trim() + ": " + mensagemListener + TEXT_RESET);
            }
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println();
    }


}
