package com.demo.bpm.jee.util;

import java.util.concurrent.locks.ReentrantLock;

import org.drools.core.command.impl.AbstractInterceptor;
import org.drools.persistence.OrderedTransactionSynchronization;
import org.drools.persistence.TransactionManager;
import org.drools.persistence.TransactionManagerHelper;
import org.kie.api.command.Command;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.EnvironmentName;

public class TransactionLockInterceptor extends AbstractInterceptor {

    private ReentrantLock lock = new ReentrantLock();
    private Environment environment;

    public TransactionLockInterceptor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <T> T execute(Command<T> command) {
        boolean locked = false;
        if (!lock.isHeldByCurrentThread()) {
            lock.lock();
            locked = true;
        }
        try {
            return executeNext(command);
        } finally {
            if (locked) {
                release((TransactionManager) environment.get(EnvironmentName.TRANSACTION_MANAGER));
            }
        }
    }

    protected void release(TransactionManager txm) {
        try {
            TransactionManagerHelper.registerTransactionSyncInContainer(txm, new OrderedTransactionSynchronization(100) {

                @Override
                public void beforeCompletion() {
                }

                @Override
                public void afterCompletion(int status) {
                    lock.unlock();
                }
            });
        } catch (Exception e) {
            lock.unlock();
        }
    }

}
