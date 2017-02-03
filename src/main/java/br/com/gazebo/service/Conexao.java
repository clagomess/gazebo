package br.com.gazebo.service;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class Conexao {
    private String ip;

    Conexao(String ip){
        this.ip = ip;
    }

    byte[] pacoteEnvio(byte[] dado) {
        byte[] pacote = new byte[dado.length + 8];

        int num1 = dado.length + 8;
        int num2 = num1 % 100;
        int num3 = num2 % 10;

        pacote[0] = (byte) 80;
        pacote[1] = (byte) 71;
        pacote[2] = (byte) 82;
        pacote[3] = (byte) 69;
        pacote[4] = (byte) 80;
        pacote[5] = (byte) (num1 / 100 + 48);
        pacote[6] = (byte) (num2 / 10 + 48);
        pacote[7] = (byte) (num3 + 48);

        for(int i = 0; i < dado.length; i++){
            pacote[i + 8] = dado[i];
        }

        return pacote;
    }

    byte[] pacoteReceber(byte[] dado){
        byte[] toReturn = new byte[]{};

        if(dado[0] != 82 || dado[1] != 69 || dado[2] != 80){
            System.out.println("pau");
        }else{
            int tamanho = (dado[3] - 48) * 100 + (dado[4] - 48) * 10 + (dado[5] - 48);

            toReturn = new byte[tamanho];

            for (int i = 0; i < tamanho; i ++){
                toReturn[i] = dado[i + 6];
            }
        }

        return toReturn;
    }

    byte[] enviar(byte[] pacote){
        byte[] buffer = new byte[]{};

        try {
            Socket socket = new Socket(ip, 1365);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            out.write(pacote);
            out.flush();

            while (true) {
                if (in.available() > 0) {
                    buffer = new byte[in.available()];

                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] = in.readByte();
                    }

                    socket.close();

                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return buffer;
    }
}
