package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.DetectLogBean;
import com.yjfei.antibot.bean.DetectLogItemBean;
import com.yjfei.antibot.common.RingBuffer;
import com.yjfei.antibot.dao.DetectLogItemMapper;
import com.yjfei.antibot.dao.DetectLogMapper;
import com.yjfei.antibot.engine.rule.RuleResult;
import com.yjfei.antibot.engine.rule.StrategyResult;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@SuppressWarnings("all")
public class RiskLogService {

    @Autowired
    private DetectLogMapper detectLogMapper;

    @Autowired
    private DetectLogItemMapper detectLogItemMapper;

    private Config config = ConfigService.getAppConfig();

    private PersistThread persistThread;


    public RiskLogService(){
        this.persistThread = new PersistThread(this);
        this.persistThread.start();
    }

    public void log(String strategyCode, String host, StrategyResult strategyResult, long cost) {

        if (!config.getBooleanProperty("risk.result.log.enable", true)) {
            return;
        }

        DetectLogBean detectLogBean = new DetectLogBean();
        detectLogBean.setCost(cost);
        detectLogBean.setHost(host);
        detectLogBean.setRiskLevel(strategyResult.getType().getCode());
        detectLogBean.setStrategyCode(strategyCode);
        detectLogBean.setScore(strategyResult.getScore());
        detectLogBean.setStrategyId(strategyResult.getStrategyId());


        List<DetectLogItemBean> detectLogItemBeans = Lists.newArrayList();
        for (RuleResult ruleResult : strategyResult.getRuleResultList()) {
            DetectLogItemBean detectLogItemBean = new DetectLogItemBean();
            detectLogItemBean.setRuleId(ruleResult.getRuleId());
            detectLogItemBean.setStrategyId(strategyResult.getStrategyId());
            detectLogItemBean.setScore(ruleResult.getScore());
            detectLogItemBeans.add(detectLogItemBean);
        }

        persistThread.addLog(new LogData(detectLogBean,detectLogItemBeans));

    }

    @Transactional
    public void savelog(DetectLogBean detectLogBean,List<DetectLogItemBean> detectLogItemBeans){
        detectLogMapper.insertSelective(detectLogBean);
        for (DetectLogItemBean detectLogItemBean:detectLogItemBeans) {
            detectLogItemMapper.insertSelective(detectLogItemBean);
        }
    }

    public Page<DetectLogBean> list(Long strategyId, Integer level, Date effectDate, Date expireDate, Integer page, Integer pageSize) {
        Page<DetectLogBean> pageResult = PageHelper.startPage(page, pageSize).setOrderBy("update_time desc")
                .doSelectPage(() -> detectLogMapper.queryDetectLog(strategyId,level,effectDate,expireDate));

        return pageResult;
    }

    public Page<DetectLogItemBean> listitem(Long strategyId,Long ruleId,Date effectDate, Date expireDate,Integer page, Integer pageSize) {
        Page<DetectLogItemBean> pageResult = PageHelper.startPage(page,pageSize).setOrderBy("update_time desc")
                .doSelectPage(()->detectLogItemMapper.queryDetectLogItem(strategyId, ruleId, effectDate, expireDate));

        return pageResult;
    }

    @Data
    public static class LogData{
        private DetectLogBean log;
        private List<DetectLogItemBean> logItems;

        public LogData(DetectLogBean log,List<DetectLogItemBean> logItems){
            this.log = log;
            this.logItems = logItems;
        }
    }

    public static class PersistThread extends Thread{

        private boolean running = true;
        private RingBuffer<LogData> cache = new RingBuffer<LogData>(2048);
        private RiskLogService logService;

        public PersistThread(RiskLogService logService){
            this.logService = logService;
        }

        public void start(){
            this.running = running;
            super.start();
        }

        public void end(){
            this.running = false;
        }

        public void addLog(LogData logData){
            this.cache.add(logData);
        }

        @Override
        public void run() {
            while (this.running){
                try{
                    LogData logData = cache.get();
                    if (logData != null) {
                        logService.savelog(logData.log,logData.logItems);
                    }else {
                        Thread.sleep(200);
                    }
                }catch (Throwable t){
                    log.error("log data error.",t);
                }
            }
        }
    }
}
