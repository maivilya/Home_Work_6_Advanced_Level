import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {


    public static void main(String[] args) {
        new EchoServer().start();
    }


    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;
    private Object start;

    private void start() {
        scanner = new Scanner(System.in);
        try {
            openConnection();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void openConnection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8189);
        System.out.println("- - - Ждем подключение клиента - - -");
        Socket socket = serverSocket.accept();
        System.out.println("- - - Клиент подключился - - -");
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (true) {
                    String serverMessage = in.readUTF();
                    System.out.println("Сообщение от клиента: " + serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    String text = scanner.nextLine();
                    sendMessage(text);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }).start();
    }

    private void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

}
