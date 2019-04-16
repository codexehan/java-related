package codexe.han.concurrency.tools;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class VariousLock {
    /**
     * 乐观锁 CAS
     * 悲观锁 synchronized  reentrentlock
     * 自旋锁 如果持有锁的线程能在很短的时间内释放锁资源，那么等待竞争锁的线程就不需要做"内核态"和"用户态"之间的切换进入阻塞挂起状态，他们只需要等一等（自旋）。从而避免用户线程和内核的切换的销号
     *
     */

    /**
     * kernel space 和 user pace
     *      * 对于32位操作系统而言，寻址空间（虚拟地址空间，或线性地址空间）为4G(2的32次方)。也就是说一个进程最大的地址空间为4G.
     *      * 其中最高1G字节（0xC0000000 - 0xFFFFFFF）有内核使用，称为内核空间，较低3G字节，由各个进程使用，称为用户空间
     *      * 每个进程的4G地址空间，最高1G都是一样的，及内核空间。换句话说，最高1G的内核空间是被所有进程共享的！
     * 为什么要区分内核空间和用户空间
     *      CPU指令中，有些指令是非常危险的，禁止用户使用。Linux系统指令分为Ring0和Ring3两个级别。Ring3是用户态，Ring0是内核态
     * 什么是内核态 用户态
     *      进程运行在内核空间就是内核态，运行在用户空间就是用户态
     * 从用户空间进入内核空间
     *      比如写磁盘文件，分配回收内存，从网络接口读写数据，都是内和空间下完成的，我们的应用程序只能通过内核提供的接口来完成这样的操作。
     *      比如要应用程序要读取磁盘上的一个文件，他可以像内核发起一个系统调用告诉内核：读取磁盘文件。其实就是通过一个特殊指令让进程从用户态进入到内核态。在内核空间中，CPU先把数据读取到内核空间中，然后再把数据拷贝到用户空间并从内核态切换到用户态。
     * 用户态进入内核态的方式：
     *      系统调用
     *
     *      软中断
     *
     *      硬件中断
     *          时钟，外设
     * kafka读写速度快的一个原因是，使用linux sendFile指令（java中的FileChannel中的transferTo）直接在内核态将文件读取到read buffer,然后读取到NIC 进行socket传输。
     * 省去了由内核态转换为用户态，用户态再到内核态的过程，实现了zero copy
     *
     */


    /**
     * Synchronized
     * 作用于方法，锁住的是对象实例 this
     * 作用于静态方法，锁住的是Class 实例，因为Class相关数据是存储在永久带PermGen，永久带是全局共享的，因此静态方法锁相当于类的一个全局锁，会锁住所有调用该方法的线程
     * 作用于对象实例，锁住的是所有以该对象为锁的代码块。
     *
     * 核心组件
     * Wait Set: 调用wait方法被阻塞的线程放在这里
     * Contention List: 竞争队列， 所有请求锁的线程首先被放在这个竞争队列中
     * Entry List: Contention List有资格成为候选资源的线程被移动到entry list中
     * OnDeck: 任意时刻，最多只有一个线程正在竞争锁资源，该线程被称为OnDeck
     *
     * contention list->entry list->OnDeck->owner->wait set->entry list
     */

    public void useSemaphore(){
        Semaphore semp = new Semaphore(5);
        try{
            semp.acquire();//可相应中断，与ReentrantLock.lockInterruptibly()效果一直
            try{
                System.out.println("get one semaphore");
            }finally {
                semp.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



}
