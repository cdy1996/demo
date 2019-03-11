package com.cdy.demo.java.threadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FormualTask extends RecursiveTask<List<User>> {

    User user;
    Project project;

    public FormualTask(User user) {
        this.user = user;
    }
    public FormualTask(Project project) {
        this.project = project;
    }


    public static void main(String[] args) {
        List<FormualTask> collect = IntStream.range(1, 10)
                .mapToObj(FormualTask::generateUser)
                .map(e -> new FormualTask(e))
                .collect(Collectors.toList());

    }

    private static User generateUser(int i) {
        Project p1 = new Project("p"+i, new Stock("s"+i), new Stock("s"+i+i));
        Project p2 = new Project("p"+i+i, new Stock("s"+i+i+i), new Stock("s"+i+i+i+i));
        return new User("cdy"+i, p1, p2);
    }


    @Override
    protected List<User> compute() {
//invokeAll()
//        for (Project project : user.projcetList) {
//
//        }
//
return null;
    }
}

class User{
    String userName;
    List<Project> projcetList;

    public User(String userName, Project ... projects) {
        this.userName = userName;
        projcetList = new ArrayList<>();
        projcetList.addAll(Arrays.asList(projects));
    }
}


class Project{
    String projectName;
    List<Stock> stockList;

    public Project(String projectName, Stock ... stocks) {
        this.projectName = projectName;
        stockList = new ArrayList<>();
        stockList.addAll(Arrays.asList(stocks));
    }
}

class Stock{
    String stockName;

    public Stock(String stockName) {
        this.stockName = stockName;
    }
}