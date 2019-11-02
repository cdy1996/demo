package com.cdy.demo.framework.rx;

import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class MergeTest {

    static class Station{
        Long id;
        String name;

        public Station(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Station{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    static class StationLine{
        List<Station> stationList;

        public StationLine(Station ...stations) {
            stationList = Arrays.asList(stations);
        }

        @Override
        public String toString() {
            return "StationLine{" +
                    "stationList=" + Arrays.toString(stationList.toArray()) +
                    '}';
        }
    }
    static class Model{
        Station station;
        StationLine stationLine;

        public Model(Station station) {
            this.station = station;
        }

        public Model(StationLine stationLine) {
            this.stationLine = stationLine;
        }

        public Model(Station station, StationLine stationLine) {
            this.station = station;
            this.stationLine = stationLine;
        }

        public Model(Model model, Model model1) {
            this(model.station, model1.stationLine);
        }

        @Override
        public String toString() {
            return "Model{" +
                    "station=" + station +
                    ", stationLine=" + stationLine +
                    '}';
        }
    }

    static Observable<Station> getStationById(Long id){
        return Observable.just(
                new Station(1L, "同顺街"),new Station(2L, "同顺街"))
                .doOnSubscribe(()-> System.out.println("查库"));
    }

    static Observable<StationLine> getLine(Station station) {
        return Observable.just(
                new StationLine(station, new Station(2L, "五常街")))
                .doOnSubscribe(()-> System.out.println("查库2"));
    }

    public static void main(String[] args) {

        Observable<Station> station = getStationById(1L);
        System.out.println("----");
        Observable<StationLine> stationLine = station.flatMap(MergeTest::getLine); //重复查库
        Observable.merge(station.map(Model::new), stationLine.map(Model::new))
                .subscribe(System.out::println);

//        station.cache()
        System.out.println("----");
        station.publish(selector->
                Observable.merge(selector.map(Model::new),
                        selector.flatMap(MergeTest::getLine).map(Model::new)))
                .subscribe(System.out::println);

        System.out.println("---");
        station.publish(selector->
                Observable.merge(selector.map(Model::new),
                        getLine(null).takeUntil(selector)))
                .subscribe(System.out::println);

    }
}
