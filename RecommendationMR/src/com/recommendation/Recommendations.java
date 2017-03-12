package com.recommendation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.recommendation.dto.TextIntPair;
import com.recommendation.dto.TextPair;
import com.recommendation.mapreducer.Map1;
import com.recommendation.mapreducer.Map2;
import com.recommendation.mapreducer.Reduce1;
import com.recommendation.mapreducer.Reduce2;

public class Recommendations extends Configured implements Tool{
    private static final String OUTPUT_PATH = "/test/intermediate_output";
    public static void main(String[] args) throws Exception{
    	int exitCode = ToolRunner.run(new Recommendations(), args);
    	System.exit(exitCode);
    }
    public int run(String[] args) throws Exception {
    	if(args.length != 2) {
    		System.err.printf("Usage: %s neds two arguments, input and output files\n", getClass().getSimpleName());
    		return -1;
    	}
        // --------- first MapReduce Job -------------------------
        // For each user, calculates the number of common friends
        // with other users
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        //Hadoop expects the output path does not exist
        if(fs.exists(new Path(OUTPUT_PATH))){
            fs.delete(new Path(OUTPUT_PATH),true);
        }
        Job job = Job.getInstance();
        job.setJobName("FriendRecom-MR1");
        //Job job = new Job(conf, "FriendRecom-MR1");
        job.setJarByClass(Recommendations.class);
        job.setOutputKeyClass(TextPair.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapOutputKeyClass(TextPair.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setMapperClass(Map1.class);
        job.setReducerClass(Reduce1.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

        job.waitForCompletion(true);
        
        // -------- Second MapReduce Job -------
        // For each user, find top recommendations
        //Configuration conf2 = new Configuration();

        //Job job2 = new Job(conf2, "FriendRecom-MR2");
        Job job2 = Job.getInstance();
        job2.setJobName("FriendRecom-MR2");
        job2.setJarByClass(Recommendations.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(TextIntPair.class);

        job2.setMapperClass(Map2.class);
        job2.setReducerClass(Reduce2.class);

        FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]));

        int returnValue = job2.waitForCompletion(true) ? 0 : 1;
        if(job2.isSuccessful()) {
        	System.out.println("Job was successful");
        } else if (!job2.isSuccessful()) {
        	System.out.println("Job was not successful");
        }
        return returnValue;
    }

}