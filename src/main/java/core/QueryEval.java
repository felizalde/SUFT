package core;



import searcher.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class QueryEval {


    private static int HIGH = 2;
    private static int PARTIAL = 1;
    private static int LOW = 0;


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

    public String toString(){
        return this.query;
    }

    public double precision(Response response){
        return ((double)totalRel(response)/response.getItemsCount());
    }

    public double recall(Response response){
        return ((double)totalRel(response)/(totalHigh + totalPartial));
    }

    private int totalRel(Response response){
        Iterator<UThread> it = response.getItems().iterator();
        int acc = 0;
        while (it.hasNext()){
            String id = it.next().getID();
            if (isRelevant(id)){
              acc++;
            }
          }
        return acc;
      }

    private boolean isRelevant(String id){
        int current = LOW;
        if (this.relevances.containsKey(id)){
            current = this.relevances.get(id);
        }
        if((current==HIGH)||(current==PARTIAL)){
            return true;
        }
        return false;
      }

    public List<PRData> datacharts(Response response){
      List<PRData> out = new ArrayList<PRData>();
      double totalRec = 0;
      double relRecuperados = 0;
      Iterator<UThread> it = response.getItems().iterator();
      while (it.hasNext()){
          totalRec++;
          String id = it.next().getID();
          if (isRelevant(id)){
              relRecuperados++;
          }

          double p = (relRecuperados/totalRec);
          double r = (relRecuperados/(totalPartial+totalHigh));
          out.add(new PRData(p, r));
      }
      return out;
  }

}
