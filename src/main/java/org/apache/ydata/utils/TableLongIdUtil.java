package org.apache.ydata.utils;

/**
 * Generate 64bits uid (long), default allocated as below:<br>
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |     delta seconds    | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              24bits         11bits
 * }</pre>
 *
 * You can also specified the bits by Spring property setting.
 * <li>snowflake.timeBits: default as 32
 * <li>snowflake.workerBits: default as 8
 * <li>snowflake.dbBits: default as 8
 * <li>snowflake.seqBits: default as 15
 * <li>snowflake.baseDate: Epoch date string format 'yyyy-MM-dd'. Default as '2018-07-01'<p>
 *
 * <b>Note that:</b> The total bits must be 64 -1
 *
 * @author yutianbao@baidu, john.huang
 */
public class TableLongIdUtil {
    /** 初始偏移时间戳 */
    private static final long OFFSET = 1546300800L;

    /** 机器id所占位数 (5bit, 支持最大机器数 2^5 = 32)*/
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATACENTER_ID_BITS = 5L;
    /** 自增序列所占位数 (16bit, 支持最大每秒生成 2^16 = ‭65536‬) */
    private static final long SEQUENCE_ID_BITS = 16L;
    /** 机器id偏移位数 */
    private static final long WORKER_SHIFT_BITS = SEQUENCE_ID_BITS;
    /** 自增序列偏移位数 */
    private static final long OFFSET_SHIFT_BITS = SEQUENCE_ID_BITS + WORKER_ID_BITS;
    /** 机器标识最大值 (2^5 / 2 - 1 = 15) */
    private static final long WORKER_ID_MAX = -1L ^ (-1L << WORKER_ID_BITS);
    private static final long DATACENTER_ID_MAX = -1L ^ (-1L << DATACENTER_ID_BITS);

    private static final long BACK_WORKER_ID_BEGIN = (1 << WORKER_ID_BITS) >> 1;
    /** 自增序列最大值 (2^16 - 1 = ‭65535) */
    private static final long SEQUENCE_MAX = (1 << SEQUENCE_ID_BITS) - 1;
    /** 发生时间回拨时容忍的最大回拨时间 (秒) */
    private static final long BACK_TIME_MAX = 1L;

    /** 上次生成ID的时间戳 (秒) */
    private static long lastTimestamp = 0L;
    /** 当前秒内序列 (2^16)*/
    private static long sequence = 0L;
    /** 备份机器上次生成ID的时间戳 (秒) */
    private static long lastTimestampBak = 0L;
    /** 备份机器当前秒内序列 (2^16)*/
    private static long sequenceBak = 0L;

    private long WORKER_ID;
    private long DATACENTER_ID;


    public TableLongIdUtil(long actualWorkId, long datacenterId) {
        if (actualWorkId > WORKER_ID_MAX || actualWorkId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", WORKER_ID_MAX));
        }
        if (datacenterId > DATACENTER_ID_MAX || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", DATACENTER_ID_MAX));
        }
        this.WORKER_ID = actualWorkId;
        this.DATACENTER_ID = datacenterId;
    }

    public TableLongIdUtil() {
        this.WORKER_ID = 0l;
        this.DATACENTER_ID = 0l;
    }

    /**
     * 获取自增序列
     *
     * @return long
     */
    public long nextId() {
        return nextId(SystemClock.now() / 1000);
    }

    /**
     * 主机器自增序列
     *
     * @param timestamp 当前Unix时间戳
     * @return long
     */
    private long nextId(long timestamp) {
        // 时钟回拨检查
        if (timestamp < lastTimestamp) {
            // 发生时钟回拨
            return nextIdBackup(timestamp);
        }

        // 开始下一秒
        if (timestamp != lastTimestamp) {
            lastTimestamp = timestamp;
            sequence = 0L;
        }
        if (0L == (++sequence & SEQUENCE_MAX)) {
            // 秒内序列用尽
//            log.warn("秒内[{}]序列用尽, 启用备份机器ID序列", timestamp);
            sequence--;
            return nextIdBackup(timestamp);
        }

        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | (WORKER_ID << WORKER_SHIFT_BITS) | sequence;
    }

    /**
     * 备份机器自增序列
     * @param timestamp timestamp 当前Unix时间戳
     * @return long
     */
    private long nextIdBackup(long timestamp) {
        if (timestamp < lastTimestampBak) {
            if (lastTimestampBak - SystemClock.now() / 1000 <= BACK_TIME_MAX) {
                timestamp = lastTimestampBak;
            } else {
                throw new RuntimeException(String.format("时钟回拨: now: [%d] last: [%d]", timestamp, lastTimestampBak));
            }
        }

        if (timestamp != lastTimestampBak) {
            lastTimestampBak = timestamp;
            sequenceBak = 0L;
        }

        if (0L == (++sequenceBak & SEQUENCE_MAX)) {
            // 秒内序列用尽
//            logger.warn("秒内[{}]序列用尽, 备份机器ID借取下一秒序列", timestamp);
            return nextIdBackup(timestamp + 1);
        }

        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | ((WORKER_ID ^ BACK_WORKER_ID_BEGIN) << WORKER_SHIFT_BITS) | sequenceBak;
    }

}
