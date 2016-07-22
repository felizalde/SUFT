package core;


import javafx.util.Pair;
import searcher.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QueryEval {


    private static int HIGH = 2;
    private static int PARTIAL = 1;
    private static int LOW = 0;

    //HIGH and PARTIAL importance..
    private static double P_HIGH = 1;
    private static double P_PARTIAL = 1;

    private double totalLow = 0;
    private double totalPartial = 0;
    private double totalHigh = 0;

    private String query;
    private HashMap<String, Integer> relevances;



    public QueryEval(String query){
        this.query = query;
        this.relevances = new HashMap<String, Integer>();
    }

    public void put(String key, int rel){
        this.relevances.put(key, rel);
        update(rel);

    }

    private void update(int rel){

        if(rel==HIGH){
            this.totalHigh++;
            return;
        }
        if(rel==PARTIAL){
            this.totalPartial++;
            return;
        }
        if(rel==LOW){
            this.totalLow++;
            return;
        }


    }

    public double fmeasure(Response response){
        double r = recall(response);
        double p = precision(response);
        return (2/((1/r)+(1/p)));
    }

    public double fallout(Response response){
        return totalNotRelevant(response)/totalLow;
    }

    public int getQueryHits(){
        return this.relevances.size();
    }

    public String toString(){
        return this.query;
    }

    private double relevance(Response response){
        int current;
        double partial = 0;
        double high = 0;
        Iterator<UThread> it = response.getItems().iterator();
        while (it.hasNext()){
            current = LOW;
            String id = it.next().getID();

            if (this.relevances.get(id) != null){
                current = this.relevances.get(id);
            }


            if(current==HIGH)high++;
            if(current==PARTIAL)partial++;

        }

        return ((P_HIGH*high)+(P_PARTIAL*partial));

    }

    private double precision(Response response){
        return (relevance(response)/response.getItemsCount());
    }

    private double recall(Response response){
        return (relevance(response)/((P_HIGH*totalHigh)+(P_PARTIAL*totalPartial)));
    }

    private int totalNotRelevant(Response response) {
        int recu = 0;
        int current = 0;
        Iterator<UThread> it = response.getItems().iterator();
        while (it.hasNext()) {
            current = LOW;
            String id = it.next().getID();

            if (this.relevances.get(id) != null) {
                current = this.relevances.get(id);
            }
            if (current == LOW) recu++;
        }
        return recu;
    }


    public List<Pair<Number, Number>> datacharts(Response response){
        List<Pair<Number,Number>> out = new ArrayList<Pair<Number, Number>>();
        double totalRec = 0;
        double relRecuperados = 0;
        int current = LOW;
        Iterator<UThread> it = response.getItems().iterator();
        while (it.hasNext()){
            totalRec++;
            current = LOW;
            String id = it.next().getID();
            if (this.relevances.get(id) != null){
                current = this.relevances.get(id);
            }
            if((current==HIGH)||(current==PARTIAL)){
                relRecuperados++;
            }

            double p = (relRecuperados/totalRec);
            double r = (relRecuperados/(totalHigh+totalPartial));

            out.add(new Pair(p, r));
        }
        return out;
    }

    /**
     *   PRECISION:
     *   P : ((0.8*#REL.REC)+(0.2*#PAR.REC))/#TOTALREC
     *   RECALL:
     *   R : ((0.8*#REL.REC)+(0.2*#PAR.REC))/((0.8*#REL.T)+(0.2*#PAR.T)
     *
     *   Calcular precision y recall en cada momento.
     *
     */

}
