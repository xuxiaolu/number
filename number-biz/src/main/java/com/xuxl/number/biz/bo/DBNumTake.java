package com.xuxl.number.biz.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class DBNumTake extends AbstractNumTake {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBNumTake.class);

    private LinkedBlockingQueue<Long> queue;

    private NumFillExecutor numFillExecutor;

    private int bufferSize;

    private volatile boolean isRunning = true;

    private AtomicBoolean isFilling = new AtomicBoolean(false);

    public DBNumTake(String bizCode, String subBizCode, int bufferSize, NumFillExecutor numFillExecutor) {
        super(bizCode, subBizCode);
        this.bufferSize = bufferSize;
        this.numFillExecutor = numFillExecutor;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void init0() {
        Thread thread = new Thread(new NumFillThread(), getBizCode() + "_" + getSubBizCode() + "_" + "Thread");
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void destroy() {
        isRunning = false;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public long take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            LOGGER.error("take num error,bizCode:{},subBizCode:{}", getBizCode(), getSubBizCode(), e);
            Thread.currentThread().interrupt();
        }
        return 0L;
    }

    private class NumFillThread implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                if (queue.size() <= bufferSize) {
                    if (isFilling.compareAndSet(false, true)) {
                        try {
                            int fillSize = bufferSize * 10;
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("fill num,bizCode:{},subBizCode:{},size:{}", getBizCode(), getSubBizCode(), fillSize);
                            }
                            long num = numFillExecutor.execute(getBizCode(), getSubBizCode(), fillSize);
                            try {
                                for (int i = fillSize - 1; i >= 0; i--) {
                                    long value = num - i;
                                    queue.put(value);
                                }
                            } catch (InterruptedException e) {
                                LOGGER.error("put num error,bizCode:{},subBizCode:{}", getBizCode(), getSubBizCode(), e);
                            }
                        } finally {
                            isFilling.set(false);
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    LOGGER.error("sleeping error", e);
                }
            }
        }
    }

    interface NumFillExecutor {

        long execute(String bizCode, String subBizCode, int fillSize);
    }
}
