package ift604.common.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gxt.common.Act1;
import gxt.common.Challenge;

/**
 * Created by taig1501 on 15-10-14.
 */
public class  ActService<Ta> {
    protected Ta thing;
    protected ExecutorService pool;

    public ActService(Ta thing) {
        this.thing = thing;
        this.pool = Executors.newFixedThreadPool(1);
    }

    public Challenge addAct(final Act1<Ta> f) {
        System.out.println("executing...");
        pool.execute(new Runnable() {
            @Override
            public void run() {
                f.func(thing);
            }
        });
        System.out.println("executed.");
        return Challenge.Success("act added");
    }

    public void start() {
    }
}
