/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.ac.ut.snl.mrcd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.Writable;

/**
 *
 * @author arian
 */
public class ShortestPathTuple implements Writable {
    int targetId;
    int sourceId;
    int distance;
    char status;
    double weight;
    List<Integer> pathInfo;
    List<Integer> adjList;

    public int getTargetId() {
        return targetId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getDistance() {
        return distance;
    }

    public char getStatus() {
        return status;
    }

    public double getWeight() {
        return weight;
    }

    public List<Integer> getPathInfo() {
        return pathInfo;
    }

    public List<Integer> getAdjList() {
        return adjList;
    }
    
    public ShortestPathTuple(int id, List<Integer> adjList) {
        this.targetId = id;
        this.sourceId = id;
        this.distance = 0;
        this.status = 'a';
        this.weight = 1.;
        this.pathInfo = null;
        this.adjList = adjList;
    }
    
    public ShortestPathTuple(int targetId, int sourceId, int distance, char status, double weight,
            List<Integer> pathInfo, List<Integer> adjList) {
        this.targetId = targetId;
        this.sourceId = sourceId;
        this.distance = distance;
        this.status = status;
        this.weight = weight;
        this.pathInfo = pathInfo;
        this.adjList = adjList;
    }
    
    public ShortestPathTuple(ShortestPathTuple shortestPathTuple, int newTargetId) {
        status = 'a';
        adjList = null;
        targetId = newTargetId;
        sourceId = shortestPathTuple.sourceId;
        distance = shortestPathTuple.distance;
        weight = shortestPathTuple.weight;
        pathInfo = shortestPathTuple.pathInfo;
        
    }
    
    public ShortestPathTuple() {
        
    }

    public String writeAdjList() {
        String result = "[";
        int counter = 0;
        for (Integer i : adjList) {
            result = result + i;
            counter++;
            if (counter != adjList.size())
                result = result + ";";
        }
        result = result + "]";
        return result;
    }

    public void write(DataOutput d) throws IOException {
        String outputRecord="";
        outputRecord+=targetId+",";
        outputRecord+=sourceId+",";
        outputRecord+=distance+",";
        outputRecord+=status+",";
        outputRecord+=weight+",";
        outputRecord+="[";
        if(pathInfo!=null){
            for (int i=0;i<pathInfo.size()-1;i++)
                outputRecord+=pathInfo.get(i)+";";
            if(pathInfo.size()>0)
                outputRecord+=pathInfo.get(pathInfo.size()-1);
        }
        outputRecord+="],";
        
        
        outputRecord+="[";
        if(adjList!=null){
            for (int i=0;i<adjList.size()-1;i++)
                outputRecord+=adjList.get(i)+";";
            if(adjList.size()>0)
                outputRecord+=adjList.get(adjList.size()-1);
        }
        outputRecord+="]";
        
//        d.writeInt(targetId);
//        d.writeInt(sourceId);
//        d.writeInt(distance);
//        d.writeChar(status);
//        d.writeDouble(weight);
//        d.writeInt(pathInfo.size());
//        for (Integer vertex : pathInfo)
//            d.writeInt(vertex);
//        d.writeInt(adjList.size());
//        for (Integer vertex : adjList)
//            d.writeInt(vertex);
        d.writeUTF(outputRecord);
    }

    public void readFields(DataInput di) throws IOException {
        String inputRecord=di.readUTF();
        String []records=inputRecord.split(",");
        targetId=Integer.parseInt(records[0]);
        sourceId=Integer.parseInt(records[1]);
        distance=Integer.parseInt(records[2]);
        status=records[3].charAt(0);
        weight=Double.parseDouble(records[4]);
        String pathString=records[5].substring(1,records[5].length()-1);
        String []pathArray=pathString.split(";");
        pathInfo=new ArrayList<Integer>();
        if(pathString.length()>0)
            for(String li:pathArray)
                pathInfo.add(Integer.parseInt(li));
        
        
        
        String adjString=records[6].substring(1,records[6].length()-1);
        String []adjArray=adjString.split(";");
        adjList=new ArrayList<Integer>();
        if(adjString.length()>0)
            for(String li:adjArray)
                adjList.add(Integer.parseInt(li));
//         targetId = di.readInt();
//         sourceId = di.readInt();
//         distance = di.readInt();
//         status = di.readChar();
//         weight = di.readDouble();
//         int pathInfoSize = di.readInt();
//         List<Integer> pathInfo = new ArrayList<Integer>();
//         for (int i = 0; i < pathInfoSize; i++)
//             pathInfo.add(di.readInt());
//         int adjListSize = di.readInt();
//         List<Integer> adjList = new ArrayList<Integer>();
//         for (int i = 0; i < adjListSize; i++)
//             adjList.add(di.readInt());
    }
    
    public static ShortestPathTuple read(DataInput in) throws IOException {
        ShortestPathTuple spt = new ShortestPathTuple();
        spt.readFields(in);
        return spt;
    }
    
    public String toString() {
//        return "ShortestPathTuple object with source ID="+sourceId+", target ID="+targetId+
//                ", distance="+distance+", and weight="+weight;
        for(Integer li:adjList)
            System.out.print(li+",");
        String result = new String();
        result += this.targetId+","+this.sourceId+","+this.distance
                +","+this.status+","+this.weight+","+this.writePathInfo()+","+this.writeAdjList();
        return result;
    }

    private String writePathInfo() {
        String result = "[";
        int counter = 0;
        if (pathInfo != null)
            for (Integer i : pathInfo) {
            result = result + i;
            counter++;
            if (counter != pathInfo.size())
                result = result + ";";
        }
        result = result + "]";
        return result;
    }
}
