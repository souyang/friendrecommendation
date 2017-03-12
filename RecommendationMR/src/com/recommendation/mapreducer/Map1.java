package com.recommendation.mapreducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.recommendation.dto.TextPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/** 
 * 
 *
 * 
 * **/
/* for each line of input, generates user pairs that have common friend, or are already friends */
public class Map1 extends Mapper<LongWritable, Text, TextPair, IntWritable> {

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //for each line, separate the line to the user, <comma separated list of friends>
    	String line = value.toString();
        int index = line.indexOf('\t');
        if(index == -1)
            return;
        String user_id = line.substring(0, index);
        List<String> friends_id = new ArrayList<>();
        //use stringtokenizer to separate the comma separated list
        StringTokenizer tokenizer = new StringTokenizer(line.substring(index+1), ",");
        while(tokenizer.hasMoreTokens()) {
            friends_id.add(tokenizer.nextToken());
        }

        int length = friends_id.size(), i,j;
        //user should be the friend for each person in the friend list
        for(i = 0; i < length; i++) {
           context.write(new TextPair(user_id , friends_id.get(i)), new IntWritable(0));
        }
        //For each friend in the friend list, compare them twice to determine the common friend
        for(i = 0; i < length; i++) {
            for(j = 0; j < length; j ++) {
                if(j == i) //skip the same person
                    continue;
                context.write(new TextPair(friends_id.get(i) , friends_id.get(j)), new IntWritable(1));
            }
        }
    }
}