package codexe.han.nio.doc;

/**
 * 位置 position
 * 缓冲区可读写的起始位置
 *
 * 容量 capacity
 * 缓冲区的最大容量
 *
 * 限度 limit
 * 缓冲区读写的末尾位置
 *
 * 标记 mark
 *
 * clear()
 * position->0 limit->capacity
 *
 * flip() 翻动
 * position->0 limit->当前位置
 * 适用于刚写完，可以开始读了
 *v
 * rewind() 倒带
 * position->0 limit->limit不改变
 *
 * remaining()
 * 返回缓冲区position与limit之间的元素
 *
 *
 * 缓冲区的分配
 * allocate()
 * 创建的缓冲区基于java数组实现，操作数据会反映到缓冲区，反之亦然。
 * allocateDirect()
 * 不会创建数组，而是直接进行内存访问。
 *
 * 直接缓冲区在一些情况下会很快，尤其是缓冲区很大的时候。不过创建缓冲区的 代价比简介缓冲区更高，所以只能在缓冲区可能只持续较短时间是才分配直接缓冲区。
 *
 */
public class buffer {

}
