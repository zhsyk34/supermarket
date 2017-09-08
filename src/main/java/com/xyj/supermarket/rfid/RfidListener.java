package com.xyj.supermarket.rfid;

import com.clou.uhf.G3Lib.CLReader;
import com.xyj.supermarket.config.AbstractDaemonService;
import com.xyj.supermarket.config.Config;
import com.xyj.supermarket.util.LoggerUtils;
import com.xyj.supermarket.util.ThreadUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import static com.xyj.supermarket.config.Config.RFID_SN;

@RequiredArgsConstructor
public class RfidListener extends AbstractDaemonService {

    private final Logger logger = LoggerUtils.getLogger(this.getClass());

    private final AbstractDaemonService handler;

    @Override
    public void run() {
        logger.info("开始监控RFID[{}]连接状态", RFID_SN);
        super.setStartup(true);
        try {
            while (!Thread.currentThread().isInterrupted()) {
                //应用处理器软件版本|读写器名称|读写器上电时间
                String status = CLReader._Config.GetReaderInformation(RFID_SN);
                ThreadUtils.await(Config.RFID_SYNC);

                if (StringUtils.isEmpty(status) || !status.startsWith("V")) {
                    Thread.currentThread().interrupt();

                    CLReader.CloseConn(RFID_SN);
                    logger.error("RFID_SN[{}]已断开,关闭连接并尝试重连", RFID_SN);

                    handler.startup();
                } else {
                    logger.info("RFID_SN[{}]当前在线", RFID_SN);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
