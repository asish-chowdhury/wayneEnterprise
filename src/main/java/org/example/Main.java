package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class WayneEnterprise{
    int TargetRevenue;
    int revenue = 0;
    int total_transaction;
    int successful_transaction;
    WayneEnterprise(int target){
        this.TargetRevenue = target;
    }
    public void profit(){
        synchronized (this){
            revenue += 1000;
            total_transaction += 1;
            successful_transaction += 1;
        }
    }
    public void loss(){
        synchronized (this){
            revenue -= 250;
            total_transaction += 1;
        }
    }
    public boolean isTargetDone(){
        return revenue >= TargetRevenue;
    }
}

public class Main {
    public static void main(String[] args) {
        int NO_OF_CUSTOMER = 7;
        int NO_OF_SHIPPER = 5;
        WayneEnterprise wayneEnterprise = new WayneEnterprise(1000000);

        BlockingQueue<Order> orderForAtlanta = new LinkedBlockingDeque<>();
        BlockingQueue<Order> orderForGotham = new LinkedBlockingDeque<>();
        List<Thread> threads = new ArrayList<>();
        for(int i = 0; i < NO_OF_CUSTOMER; i++){
            Thread t = new Thread(new Customer(orderForAtlanta, orderForGotham, wayneEnterprise));
            threads.add(t);
            t.start();
        }
        for(int i = 0; i < NO_OF_SHIPPER; i++){
            Thread t = new Thread(new Ship(orderForAtlanta, orderForGotham, wayneEnterprise));
            threads.add(t);
            t.start();
        }
        for(Thread t: threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Total Number of Orders: " + wayneEnterprise.total_transaction);
        System.out.println("Successful Transactions: " + wayneEnterprise.successful_transaction);
        System.out.println("Revenue: " + wayneEnterprise.revenue);
    }
}