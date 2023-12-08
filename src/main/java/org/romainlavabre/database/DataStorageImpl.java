package org.romainlavabre.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.romainlavabre.event.EventDispatcher;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DataStorageImpl implements DataStorageHandler {

    protected static final Map< Integer, LockModeType > LOCK_TYPE = Map.of(
            LockType.PESSIMISTIC_READ, LockModeType.PESSIMISTIC_READ,
            LockType.PESSIMISTIC_WRITE, LockModeType.PESSIMISTIC_WRITE,
            LockType.OPTIMISTIC, LockModeType.OPTIMISTIC
    );
    protected final        EntityManager                entityManager;
    protected final        EventDispatcher              eventDispatcher;
    protected final        StampManagedEntity           stampManagedEntity;
    protected final        ApplicationEventPublisher    applicationEventPublisher;


    public DataStorageImpl(
            final EntityManager entityManager,
            EventDispatcher eventDispatcher,
            final StampManagedEntity stampManagedEntity,
            ApplicationEventPublisher applicationEventPublisher ) {
        this.entityManager             = entityManager;
        this.eventDispatcher           = eventDispatcher;
        this.stampManagedEntity        = stampManagedEntity;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void lock( final Object entity, final int type ) {
        this.entityManager.lock( entity, DataStorageImpl.LOCK_TYPE.get( type ) );
    }


    @Override
    public void persist( final Object entity ) {
        this.stampManagedEntity.buffer( entity );
    }


    @Override
    public void remove( final Object entity ) {
        this.stampManagedEntity.remove( entity );
    }


    @Override
    public void save() {
        this.stampManagedEntity.collectBuffer( this.entityManager );

        this.entityManager.flush();

        this.eventDispatcher.trigger( DataStorageConfigurer.get().getOnTransactionSuccess(), new HashMap<>() );

        this.stampManagedEntity.collectBuffer( this.entityManager );

        applicationEventPublisher.publishEvent( new ApplicationEvent( new Object() ) {
            @Override
            public Object getSource() {
                return null;
            }


            @Override
            public String toString() {
                return "TRANSACTION_COMMITED_EVENT";
            }
        } );
    }
}
