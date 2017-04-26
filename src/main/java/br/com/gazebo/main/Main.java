package br.com.gazebo.main;


import br.com.gazebo.service.Coleta;
import org.apache.commons.cli.*;
import org.apache.log4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
    private Main(){}

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main (String[] args){
        // LOGGER
        PatternLayout patternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        ConsoleAppender consoleAppender = new ConsoleAppender(patternLayout);
        BasicConfigurator.configure(consoleAppender);
        LogManager.getRootLogger().setLevel(Level.INFO);

        // CMD LINE
        CommandLine cmd = consoleOptions(args);
        if(cmd == null){
            System.exit(1);
        }

        // PROGRAMA
        SimpleDateFormat sdf = new SimpleDateFormat("MMyy");
        String dat = sdf.format(Calendar.getInstance().getTime());

        String datInicio = (cmd.getOptionValue("datInicio") == null ? "01" + dat : cmd.getOptionValue("datInicio"));
        String datFim = (cmd.getOptionValue("datFim") == null ? "31" + dat : cmd.getOptionValue("datFim"));

        Coleta coleta = new Coleta();
        String rep = coleta.coletar(datInicio, datFim, cmd.getOptionValue("ip"));

        if(rep.length() > 0){
            String filename = cmd.getOptionValue("output") + File.separator;
            filename += "coleta_" + datInicio + "_" + datFim + ".rep";

            try (FileOutputStream fos = new FileOutputStream(filename)){
                OutputStreamWriter out = new OutputStreamWriter(fos);
                out.write(rep);
                out.flush();
            } catch (Exception e) {
                logger.warn(Main.class.getName(), e);
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
        }

        return cmd;
    }
}
