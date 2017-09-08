package com.xyj.supermarket.rfid;

import com.clou.uhf.G3Lib.ClouInterface.IAsynchronousMessage;
import com.clou.uhf.G3Lib.Protocol.Tag_Model;

public class IAsynchronousMessageAdapter implements IAsynchronousMessage {
    @Override
    public void WriteDebugMsg(String s) {

    }

    @Override
    public void WriteLog(String s) {

    }

    @Override
    public void PortConnecting(String s) {

    }

    @Override
    public void PortClosing(String s) {

    }

    @Override
    public void OutPutTags(Tag_Model tag_model) {

    }

    @Override
    public void OutPutTagsOver() {

    }

    /**
     * @param gpiIndex    GPI口的下标
     * @param gpiState    0 为低电平,1 为高电平
     * @param startOrStop 0 为触发开始,1 为触发停止
     */
    @Override
    public void GPIControlMsg(int gpiIndex, int gpiState, int startOrStop) {

    }
}
