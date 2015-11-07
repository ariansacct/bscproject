/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.snl.mrcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author arian
 */
public class StageFour extends Configured implements Tool {

    static BufferedReader bufferedReader;
    static BufferedReader bufferedReader2;
    static Scanner scanner;
    static Scanner scanner2;
    static Configuration configuration = new Configuration();
    FileSystem fs;
    static URI[] localCacheFiles;
    
    FSDataInputStream in2;
    
    static ArrayList<String> neighboursList = null;

//    public StageFour() throws IOException {
////        this.in2 = fs.open(new Path("/home/arian/myhadoop/NetBeansProjects/bscthesis/input3-converted"));
//        in2 = fs.open(new Path(localCacheFiles[0].toString()));
//        bufferedReader2 = new BufferedReader(new InputStreamReader(in2));
//        scanner2 = new Scanner(bufferedReader2);
////        configuration = new Configuration();
//        fs = FileSystem.get(configuration);
//    }

//    public static class StageFourMapper extends Mapper<DoubleWritable, Text, Text, Edge> {
    public static class StageFourMapper extends Mapper<Text, Text, Text, Edge> {

//        public void map(DoubleWritable betweenness, Text vertices, Context context) throws FileNotFoundException, IOException {
        public void map(Text betw, Text verti, Context context) throws FileNotFoundException, IOException, InterruptedException {

            //  TAVAJJOH: KEY VA VALUEYE VOODROODI ALAKI HASTAND VA MA DATA RA AZ FILE MIGIRIM
            if (scanner.hasNextLine()) {
//                System.out.println("see if we have access   " + scanner.nextLine());
                String nextLine = scanner.nextLine();
                String[] tokens = nextLine.split("\\s+");
                double betweenness = Double.valueOf(tokens[0]);
                String[] vertices = tokens[1].split(",");
                int tail = Integer.valueOf(vertices[0]);
                int head = Integer.valueOf(vertices[1]);
                
                RandomAccessFile file = new RandomAccessFile("/home/arian/myhadoop/NetBeansProjects/bscthesis2/input3-converted", "rw");
                int startPos = (tail + 1) * 50;
                file.seek(startPos);
                String aLine = file.readLine();
                String aLineTrimmed = aLine.trim();
                String[] neighbours = aLineTrimmed.split("\\s+");
                for(String str : neighbours)
                    System.out.println("HOOOO   "+str+"xx");
//                ArrayList<String> neighboursList = new ArrayList<String>(Arrays.asList(neighbours));
                neighboursList = new ArrayList<String>(Arrays.asList(neighbours));
                for (int i = 0; i < neighboursList.size(); i++) {
                    System.out.println("size chande "+neighboursList.size());
                    System.out.println("meifhfn "+neighboursList.get(i)+"salamaa");
                    if (Integer.parseInt(neighboursList.get(i)) == head)
                        neighboursList.remove(i);
                }
                String newAdjList = "";
                for (int i = 0; i < neighboursList.size(); i++) {
                    newAdjList = newAdjList + neighboursList.get(i);
                    if (i != neighboursList.size() - 1)
                        newAdjList = newAdjList + " ";
                }
//                String paddedAdjList = StringUtils.leftPad(newAdjList, 49);
                String paddedAdjList2 = "";
                for (int i = 0; i < 49-newAdjList.length(); i++)
                    paddedAdjList2 = paddedAdjList2 + " ";
                paddedAdjList2 = paddedAdjList2 + newAdjList;
                
                
                
//                file.seek(startPos);
////                file.writeUTF(paddedAdjList);
//                file.writeUTF(paddedAdjList2);
//                file.close();
                
                FileChannel fileChannel = file.getChannel();
		ByteBuffer buffer=fileChannel.map(FileChannel.MapMode.READ_WRITE, startPos, 50);
                buffer.order(ByteOrder.nativeOrder());
                ByteBuffer src = ByteBuffer.allocate(50);
                buffer.put(paddedAdjList2.getBytes());
		fileChannel.close();
                file.close();
                
                
                
                
                
                
                
                
                
                
                RandomAccessFile raf = new RandomAccessFile("/home/arian/NetBeansProjects/bscthesis2/input/stageone/spts.txt", "rw");
                int rafStartPos = tail * 500;
                raf.seek(rafStartPos);
                String line = raf.readLine();
                System.out.println("rafstartpos "+rafStartPos);
                System.out.println("hey hey hey:    "+line);
                String trimmedLine = line.trim();
                String[] oldRecordElements = trimmedLine.split("\\s+");
                String oldRecordValue = oldRecordElements[1];
                String[] oldValueElements = oldRecordValue.split(",");
                String newValue = tail + "," + tail + ",0,a,1.0,[],[";
                
                for (int i = 1; i < neighboursList.size(); i++) {
                    newValue = newValue + neighboursList.get(i);
                    if (i != neighboursList.size() - 1)
                        newValue = newValue + ";";
                }
                newValue = newValue + "]";
                
                String newRecord = tail + "," + tail + '\t' + newValue;
                
                //  padding
                String paddedNewRecord = "";
                for (int i = 0; i < 499 - newRecord.length(); i++)
                    paddedNewRecord = paddedNewRecord + " ";
                paddedNewRecord = paddedNewRecord + newRecord;
                
                //  writing to file
                FileChannel channel = raf.getChannel();
                ByteBuffer buffer2 = channel.map(FileChannel.MapMode.READ_WRITE, rafStartPos, 500);
                buffer2.order(ByteOrder.nativeOrder());
                buffer2.put(paddedNewRecord.getBytes());
                channel.close();
                raf.close();

            }

            System.out.println("khatte akhare mape khali");
        }
    }

    public static class StageFourReducer extends Reducer<Text, Edge, Text, Text> {

        public void reduce(Text key, Iterable<Edge> values, Context context) throws IOException, InterruptedException {
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
            System.out.println("hich kari nemikonim INJAA");
        }
    }

    public int run(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException {
        System.out.println("enter fuckin run");
        Job job = new Job();
        String input = args[0];
        String output = args[1];
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        job.setJarByClass(StageFour.class);
        job.setJobName("Stage four");
        job.setMapperClass(StageFourMapper.class);
        job.setReducerClass(StageFourReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Edge.class);

        Path inFile = new Path("/home/arian/NetBeansProjects/bscthesis2/topkedgebetweenness");
        Path outFile = new Path("/home/arian/myhadoop/NetBeansProjects/bscthesis2/topkedgebetweenness");
//        fs = FileSystem.get(new Configuration());
        fs= FileSystem.get(configuration);
        FSDataInputStream in = fs.open(inFile);
        FSDataOutputStream out = fs.create(outFile);
        System.out.println("fs is ok");
        int bytesRead = 0;
        byte buffer[] = new byte[256];
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();
        
        System.out.println("copy is ok");

        DistributedCache.addCacheFile(new URI("/home/arian/myhadoop/NetBeansProjects/bscthesis2/input3-converted"), configuration);
        DistributedCache.addCacheFile(new URI("/home/arian/myhadoop/NetBeansProjects/bscthesis2/topkedgebetweenness"), configuration);
//        URI[] localCacheFilesx = DistributedCache.getCacheFiles(configuration);
//        if (localCacheFilesx == null) {
//            System.out.println("NULLE BI PEDARsssssssss");
//        }
//        if (localCacheFilesx != null) {
//            System.out.println("There's something in the cache now.");
//        }

        System.out.println("salam lllaaa");

//        bufferedReader = null;
//        bufferedReader2 = null;
//        scanner = null;
//        scanner2 = null;
        localCacheFiles = DistributedCache.getCacheFiles(configuration);
        if (localCacheFiles == null) {
            System.out.println("NULLE");
        }
        if (localCacheFiles != null) {
            System.out.println("There's something in the cache. an    " + localCacheFiles[1].toString());
//                fileReader = new FileReader(localCacheFiles[1].toString());
            fs = FileSystem.get(configuration);
            in = fs.open(new Path(localCacheFiles[1].toString()));
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            scanner = new Scanner(bufferedReader);
            if (! scanner.hasNextLine())
                System.out.println("ay ay AY AY SCANNER nextline nadare!!!!!!!!!!!!");
        }

//        System.out.println("ssssalam sssssxxxxxx23ssa     " + scanner.nextLine());
        job.waitForCompletion(true);
        return 0;
    }

    public static void main(String[] args) throws Exception {
//        int res = ToolRunner.run(new Configuration(), new StageFour(), args);
        System.out.println("salam aziam");
        if (configuration == null)
            System.out.println("is null snsb");
        int res = ToolRunner.run(configuration, new StageFour(), args);
        if (args.length != 2) {
            System.err.println("Usage: StageFour!!! <input path> <output path>");
        }
        System.exit(res);
    }
}
