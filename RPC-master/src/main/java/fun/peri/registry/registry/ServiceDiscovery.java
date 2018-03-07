package fun.peri.registry.registry;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 本类用于client发现server节点的变化 ，实现负载均衡
 */
public class ServiceDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDiscovery.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<String>();

    private String registryAddress;

    /**
     * zk链接
     *
     * @param registryAddress
     */
    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
        ZooKeeper zooKeeper = connectServer();
        if (zooKeeper != null) {
            watchNode(zooKeeper);
        }
    }

    /**
     * 发现新节点
     *
     * @return
     */
    public String discover() {
        String data = null;
        int size = dataList.size();
        // 存在新节点，使用即可
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
                LOGGER.debug("using only data: {}", data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("using random data: {}", data);
            }
        }
        return data;
    }

    /**
     * 链接
     *
     * @return
     */
    private ZooKeeper connectServer() {
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT,
                    event -> {
                        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                            latch.countDown();
                        }
                    });
            latch.await();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return zooKeeper;
    }

    /**
     * 监听
     *
     * @param zooKeeper
     */
    private void watchNode(final ZooKeeper zooKeeper) {
        try {
            // 获取所有子节点
            List<String> nodeList = zooKeeper.getChildren(Constant.ZK_REGISTRY_PATH,
                    event -> {
                        // 节点改变
                        if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                            watchNode(zooKeeper);
                        }
                    });
            List<String> dataList = new ArrayList<String>();
            // 循环子节点
            for (String node : nodeList) {
                // 获取节点中的服务器地址
                byte[] bytes = zooKeeper.getData(Constant.ZK_REGISTRY_PATH + "/"
                        + node, false, null);
                // 存储到list中
                dataList.add(new String(bytes));
            }
            LOGGER.debug("node data: {}", dataList);
            // 将节点信息记录在成员变量
            this.dataList = dataList;
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }
}