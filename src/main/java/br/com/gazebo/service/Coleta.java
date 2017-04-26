package br.com.gazebo.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Coleta {
    private static Logger logger = LoggerFactory.getLogger(Coleta.class);

    /**
     * Comando de coleta parcial
     * @param datIni formato: ddmmyy
     * @param datFim formato: ddmmyy
     * @param ip endereço ip do CodinRep
     * @return cmd
     */
    public String coletar(String datIni, String datFim, String ip){
        logger.info("Iniciando conexao com {} e intervalo de {} ate {}", ip, datIni, datFim);
        Coleta coleta = new Coleta();
        Conexao conexao = new Conexao(ip);

        byte[] pctEnvio;
        byte[] pctResposta;
        byte[] resposta;
        StringBuilder rep = new StringBuilder();

        byte[] cmdColeta = coleta.parcial(datIni, datFim);
        pctEnvio = conexao.pacoteEnvio(cmdColeta);
        pctResposta = conexao.enviar(pctEnvio);
        resposta = conexao.pacoteReceber(pctResposta);

        if(resposta.length == 2 && resposta[1] == 91){
            if(resposta[0] == 57 || resposta[0] == 50 || resposta[0] == 49 || resposta[0] == 48){
                pctEnvio = conexao.pacoteEnvio(coleta.primeiraVez());
                pctResposta = conexao.enviar(pctEnvio);
                resposta = conexao.pacoteReceber(pctResposta);

                if(resposta.length > 0){
                    rep.append(new String(resposta).substring(1, resposta.length));
                    logger.info("REP HEADER: ", resposta);

                    while (resposta.length > 0){
                        pctEnvio = conexao.pacoteEnvio(coleta.proximaVez());
                        pctResposta = conexao.enviar(pctEnvio);
                        resposta = conexao.pacoteReceber(pctResposta);

                        if(resposta[0] == 57 && resposta[1] == 93){
                            break;
                        }else{
                            rep.append(new String(resposta).substring(1, resposta.length));
                            logger.info("REP LINE: ", resposta);
                        }
                    }
                }else {
                    logger.warn("ERROU 001");
                }
            }else{
                logger.warn("ERROU 002");
            }
        }else{
            logger.warn("ERROU 003");
        }

        return rep.toString();
    }

    private byte[] parcial(String datIni, String datFim){
        byte[] dado = new byte[14];

        dado[0] = (byte) 57;
        dado[1] = (byte) 49;

        for (int i = 0; i < 6; i++){
            dado[i + 2] = (byte) datIni.charAt(i);
        }

        for (int i = 0; i < 6; i++){
            dado[i + 8] = (byte) datFim.charAt(i);
        }

        return dado;
    }

    private byte[] primeiraVez(){
        return new byte[]{(byte) 90};
    }

    private byte[] proximaVez(){
        return new byte[]{(byte) 44};
    }
}
