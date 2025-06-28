package com.example.searchserver.domain.queue

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/*
 * 비동기 처리를 위한 Thread-Safe한 블로킹 큐
 * Producer-Consumer 패턴을 구현하며, 다음 두 가지 조건에 따라 Consumer를 활성화 시킨다.
 * 1. 큐에 아이템이 `batchSize` 이상 쌓였을 때 즉시 처리
 * 2. 아이템이 `batchSize` 미만이더라도, `timeoutMillis` 시간이 경과했을 때
 */
class BlockingLogQueue<T>(
    capacity: Int = Int.MAX_VALUE,
    private val batchSize: Int = 1000,
    private val timeoutMillis: Long = 5000L
) : Producer<T>, Consumer<T> {

    private val queue: BlockingQueue<T> = LinkedBlockingQueue<T>(capacity)
    private val lock: Lock = ReentrantLock()
    private val condition = lock.newCondition()

    override fun produce(data: T) {
        data?.let {
            lock.withLock {
                queue.offer(it)

                if (queue.size >= batchSize) {
                    condition.signal()
                }
            }
        }
    }

    override fun consume(): List<T> {
        lock.withLock {
            while (queue.size < batchSize && !Thread.currentThread().isInterrupted) {
                try {
                    val isLive = condition.await(timeoutMillis, TimeUnit.MILLISECONDS)

                    if (!isLive && !queue.isEmpty()) {
                        break
                    }
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }
            }

            val result = mutableListOf<T>()
            queue.drainTo(result, batchSize)

            return result
        }
    }

}