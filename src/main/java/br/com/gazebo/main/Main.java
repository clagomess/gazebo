package br.com.gazebo.main;


import br.com.gazebo.service.Coleta;
import org.apache.commons.cli.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
    public static void main (String[] args){
        CommandLine cmd = consoleOptions(args);

        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        String dat = sdf.format(Calendar.getInstance().getTime());

        String datInicio = (cmd.getOptionValue("datInicio") == null ? "01" + dat : cmd.getOptionValue("datInicio"));
        String datFim = (cmd.getOptionValue("datFim") == null ? "31" + dat : cmd.getOptionValue("datFim"));

        Coleta coleta = new Coleta();
        String rep = coleta.coletar(datInicio, datFim, cmd.getOptionValue("ip"));

        if(rep.length() > 0){
            String filename = cmd.getOptionValue("output") + File.separator;
            filename += "coleta_" + datInicio + "_" + datFim + ".rep";

            try {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
                out.write(rep);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static CommandLine consoleOptions(String[] args){
        Options options = new Options();
        Option option;

        option = new Option("ip", true, "IP CodinREP");
        option.setRequired(true);
        options.addOption(option);

        option = new Option("datInicio", true, "Periodo ddmmyy");
        options.addOption(option);

        option = new Option("datFim", true, "Periodo ddmmyy");
        options.addOption(option);

        option = new Option("output", true, "Diretorio de saida");
        option.setRequired(true);
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("gazebo", options);

            System.exit(1);
        }

        return cmd;
    }
}
