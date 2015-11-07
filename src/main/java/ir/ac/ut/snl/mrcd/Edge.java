/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.ac.ut.snl.mrcd;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.WritableComparable;

/**
 *
 * @author arian
 */
public class Edge implements WritableComparable<Edge> {
    int tail;
    int head;
    
    public Edge(int t, int h) {
        tail = t;
        head = h;
    }
    
    public Edge() {
        
    }
    
    public void write(DataOutput d) throws IOException {
        String outputRecord=tail+","+head;
        d.writeUTF(outputRecord);
    }
    
    public void readFields(DataInput di) throws IOException {
        String inputRecord=di.readUTF();
        String []elements=inputRecord.split(",");
        tail = Integer.parseInt(elements[0]);
        head = Integer.parseInt(elements[1]);
    }
    
    public static Edge read(DataInput in) throws IOException {
        Edge edge = new Edge();
        edge.readFields(in);
        return edge;
    }
    
    @Override
    public String toString() {
        return tail + "," + head;
    }

    public int compareTo(Edge e) {
        if (this.tail < e.tail)
            return -1;
        else if (this.tail > e.tail)
            return 1;
        else if (this.head < e.head)
            return 0;
        else return 0;
    }
}
