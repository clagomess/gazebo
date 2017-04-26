package br.com.gazebo.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseRep {
    ParseRep(){}

    public static Map<String, Map<String, Map<String, List<String>>>> parse(String rep){
        Map<String, Map<String, Map<String, List<String>>>> buffer = new HashMap<>();

        String[] reps = rep.split("\n");

        for (String line : reps){
            if(line.length() > 34){
                continue;
            }

            // -- PIS
            final String pis = line.substring(22, 34);

            if(!buffer.containsKey(pis)){
                buffer.put(pis, new HashMap<>());
            }

            // -- Data
            final String data = line.substring(10, 18);

            if(!buffer.get(pis).containsKey(data)){
                buffer.get(pis).put(data, new HashMap<>());
            }

            // -- Hora Batida
            final String hora = line.substring(18, 22);

            if(!buffer.get(pis).get(data).containsKey("horarios")){
                buffer.get(pis).get(data).put("horarios", new ArrayList<>());
            }

            buffer.get(pis).get(data).get("horarios").add(hora);
        }

        return buffer;
    }
}
