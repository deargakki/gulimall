package com.atguigu.gulimall.search.thread;

import java.util.concurrent.*;

public class ThreadTest {
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main线程开始执行");
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
//            System.out.println("线程正在运行，编号为："+Thread.currentThread().getId());
//            int i = 10/0;
//            System.out.println("线程"+Thread.currentThread().getId()+"执行结束");
//            return i;
//        },executorService).whenComplete((i,exp)->{
//            System.out.println("当前结果为："+i+"，当前异常为："+exp);
//        }).exceptionally((exp)->{
//            System.out.println("当前的异常是："+exp);
//            return -1;
//        });

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()->{
//            System.out.println("线程正在运行，编号为："+Thread.currentThread().getId());
//            int i = 10/2;
//            System.out.println("线程"+Thread.currentThread().getId()+"执行结束");
//            return i;
//        },executorService).handle((res,exp)->{
//            if(res!=null)
//                return res*2;
//            else
//                return 0;
//        });
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("线程正在运行，编号为：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("线程" + Thread.currentThread().getId() + "执行结束");
            return i;
        }, executorService).thenAcceptAsync(res->{
            System.out.println("线程2启动了，编号是"+Thread.currentThread().getId()+",res是"+res);
        },executorService);


//        System.out.println("result is "+future.get());
//        Thread01 thread01 = new Thread01();

//        thread01.run();
//        Runnable01 runnable01 = new Runnable01();
//        runnable01.run();
//        FutureTask<Integer> integerFutureTask = new FutureTask<>(new Callable01());
//        new Thread(integerFutureTask).start();
        System.out.println("main线程执行结束");
    }


    //继承Thread类
    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("线程正在运行，编号为："+Thread.currentThread().getId());

            System.out.println("线程"+Thread.currentThread().getId()+"执行结束");
        }
    }

    //实现Runnable接口
    public static class Runnable01 implements Runnable{

        @Override
        public void run() {
            System.out.println("线程正在运行，编号为："+Thread.currentThread().getId());

            System.out.println("线程"+Thread.currentThread().getId()+"执行结束");
        }
    }

    //Callable+FutureTask
    public static class Callable01 implements Callable<Integer>{

        @Override
        public Integer call() throws Exception {
            System.out.println("线程正在运行，编号为："+Thread.currentThread().getId());

            System.out.println("线程"+Thread.currentThread().getId()+"执行结束");
            return 1;
        }
    }

    //线程池
}
