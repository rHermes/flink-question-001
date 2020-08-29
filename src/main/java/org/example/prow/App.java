package org.example.prow;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.prow.wikimedia.Event;

import java.time.Duration;

public class App {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        final String fileNameInput = "file:///home/rhermes/madsci/thesis/data/mediawiki_history/2020-07.enwiki.2016-04.sorted.tsv";
        final DataStream<String> linesIn = env.readTextFile(fileNameInput);


        final SingleOutputStreamOperator<Event> jj = linesIn.map(value -> new Event(value));

        final WatermarkStrategy<Event> mew = WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofHours(1)).withTimestampAssigner((element, recordTimestamp) -> element.eventTimestamp.toEpochSecond() * 1000);

        final DataStream<Event> props = jj.assignTimestampsAndWatermarks(mew);

        final KeyedStream<Event, String> praps = props.keyBy(e -> e.eventEntity.toString());

        praps.window(TumblingEventTimeWindows.of(Time.hours(1))).sum("something").print("JAJ!");

        env.execute("FlinkWikipediaHistoryTopEditors");
    }
}
