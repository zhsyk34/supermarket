package com.xyj.supermarket.rfid;

import com.clou.uhf.G3Lib.CLReader;
import com.clou.uhf.G3Lib.ClouInterface.IAsynchronousMessage;
import com.clou.uhf.G3Lib.Enumeration.eGPO;
import com.clou.uhf.G3Lib.Enumeration.eGPOState;
import com.clou.uhf.G3Lib.Tag6C;
import com.xyj.supermarket.config.AbstractDaemonService;
import com.xyj.supermarket.util.LoggerUtils;
import com.xyj.supermarket.util.ThreadUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

import static com.clou.uhf.G3Lib.Enumeration.eReadType.Inventory;
import static com.xyj.supermarket.config.Config.RFID_ANT;
import static com.xyj.supermarket.config.Config.RFID_SN;

@Service
public class RfidService extends AbstractDaemonService {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());

    @Resource
    private IAsynchronousMessage callback;

    private RfidListener listener = new RfidListener(this);

    @Override
    public void run() {
        boolean connected = CLReader.CreateTcpConn(RFID_SN, callback);
        while (!connected) {
            logger.error("RFID_SN[{}] 连接失败", RFID_SN);

            ThreadUtils.await(500);

            connected = CLReader.CreateTcpConn(RFID_SN, callback);
        }
        logger.info("RFID_SN[{}] 连接成功", RFID_SN);

        CLReader.Stop(RFID_SN);

        int read = Tag6C.GetEPC_TID(RFID_SN, RFID_ANT, Inventory);
        while (read != 0) {
            logger.error("RFID_SN[{}] 开启标签扫描", RFID_SN);

            ThreadUtils.await(500);

            logger.info("正在重试开启 RFID_SN[{}] 标签扫描...", RFID_SN);
            read = Tag6C.GetEPC_TID(RFID_SN, RFID_ANT, Inventory);
        }

        logger.info("RFID_SN[{}] 已开启标签扫描", RFID_SN);

        super.setStartup(true);

        listener.startup();
    }

    private boolean operation(eGPOState state, int maxTimes) throws Exception {
        HashMap<eGPO, eGPOState> map = new HashMap<>();
        map.put(eGPO._1, state);
        int times = 0;

        boolean flag = CLReader._Config.SetReaderGPOState(RFID_SN, map) == 0;
        while (!flag && times < maxTimes) {
            logger.error("操作失败,正在重试...");
            ThreadUtils.await(500);

            flag = CLReader._Config.SetReaderGPOState(RFID_SN, map) == 0;
            times++;
        }
        return flag;
    }

    private boolean open() {
        logger.debug("正在开门...");

        try {
            return operation(eGPOState._High, 3);
        } catch (Exception e) {
            logger.debug("开门出错", e);
            return false;
        }
    }

    private boolean close() {
        logger.debug("正在关门...");

        try {
            return operation(eGPOState.Low, 3);
        } catch (Exception e) {
            logger.debug("关门出错", e);
            return false;
        }
    }

    public void openDoor() {
        logger.debug("正在开门...");

        boolean opened = open();
        logger.info("开门结果:{},重置电平...", opened);

        ThreadUtils.await(1000);

        close();
    }

    public void keepOpen() {
        logger.info("保持开门状态...");
        open();
    }

}