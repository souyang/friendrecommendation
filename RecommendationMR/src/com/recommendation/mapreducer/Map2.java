package com.recommendation.mapreducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.recommendation.dto.TextIntPair;

import java.io.IOException;

/* Use the first ID as key to map each line to a key value pair */
public class Map2 extends Mapper<LongWritable, Text, Text, TextIntPair> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] data = line.split("\t");
      
        Text user_id = new Text(data[0]);
        Text friend_id = new Text(data[1]);
        Integer commonFriendCount = Integer.parseInt(data[2]);
        context.write(user_id, new TextIntPair(friend_id, new IntWritable(commonFriendCount)));
    }
}