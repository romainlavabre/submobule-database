package org.romainlavabre.database;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface LockType {
    Integer PESSIMISTIC_READ  = 4;
    Integer PESSIMISTIC_WRITE = 5;
    Integer OPTIMISTIC        = 1;
}
