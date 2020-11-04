package com.yjfei.antibot.service;


import com.yjfei.antibot.bean.StrategyBean;
import com.yjfei.antibot.data.DataModel;
import com.yjfei.antibot.engine.Context;
import com.yjfei.antibot.engine.model.DataSet;
import com.yjfei.antibot.engine.rule.Rule;
import com.yjfei.antibot.engine.rule.Strategy;
import com.yjfei.antibot.engine.rule.StrategyResult;
import com.yjfei.antibot.dto.RiskResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@SuppressWarnings("all")
public class RiskDetectService {

    private final Map<String, StrategyWrapper> cache;

    private final ExecutorService executor;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private DataModelService dataModelService;

    @Autowired
    private RiskLogService riskLogService;

    public RiskDetectService() {
        this.cache = new ConcurrentHashMap<>();
        this.executor = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                r -> new Thread(r, "reload-thread"),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }


    public RiskResultDTO detect(String strategyCode, DataSet dataSet) {

        StopWatch stopWatch = StopWatch.createStarted();

        Context context = new Context(strategyCode, dataSet);

        RiskResultDTO riskResult = new RiskResultDTO();
        try{

            Strategy strategy = getStrategyByCode(strategyCode);

            StrategyResult strategyResult = strategy.calculate(context);

            riskResult.setRiskType(strategyResult.getType());
            riskResult.setScore(strategyResult.getScore());

            stopWatch.stop();
            riskLogService.log(strategyCode,dataSet.get("host"),strategyResult,stopWatch.getTime());
        }catch (Exception e){
            log.error(strategyCode + "strategy calculate error.",e);
            throw e;
        }

        return riskResult;
    }


    public Strategy getStrategyByCode(String strategyCode){
        StrategyWrapper strategyWrapper = getStrategyWrapper(strategyCode);

        return strategyWrapper.strategy;
    }

    public DataModel getDataModelByCode(String strategyCode){
        StrategyWrapper strategyWrapper = getStrategyWrapper(strategyCode);

        return strategyWrapper.dataModel;
    }

    private StrategyWrapper getStrategyWrapper(String strategyCode){
        if (!cache.containsKey(strategyCode)){
            loadStrategy(strategyCode);
        }else {
            StrategyWrapper strategyWrapper = cache.get(strategyCode);
            if (strategyWrapper.isExpired() && strategyWrapper.loading.compareAndSet(false,true)){
                this.executor.submit(new ReloadTask(strategyCode));
            }
        }
        return cache.get(strategyCode);
    }

    private void loadStrategy(String strategyCode){

        try {
            StrategyBean strategyBean = strategyService.getStrategyByCode(strategyCode);

            List<Rule> ruleList = ruleService.getRuleByStrategyId(strategyBean.getId());

            Strategy strategy = new Strategy(strategyBean.getId(),ruleList,strategyBean.getMid(),strategyBean.getHigh());

            DataModel dataModel = dataModelService.getDataModelById(strategyBean.getModelId());

            StrategyWrapper strategyWrapper = new StrategyWrapper(strategy,dataModel);

            cache.put(strategyCode,strategyWrapper);

        }catch (Throwable t){
            log.error("load strategy error,strategycode="+strategyCode,t);
        }
    }


    private class StrategyWrapper {
        private static final long EXPIRED_PERIOD = 1000 * 60 * 10L;

        private final Strategy strategy;

        private final DataModel dataModel;

        private final AtomicBoolean loading;

        private final long loadedTime;

        public StrategyWrapper(Strategy strategy,DataModel dataModel) {
            this.strategy = strategy;
            this.dataModel = dataModel;
            this.loading = new AtomicBoolean(false);
            this.loadedTime = System.currentTimeMillis();
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - loadedTime > EXPIRED_PERIOD;
        }
    }

    private class ReloadTask implements Runnable {

        private String code;

        public ReloadTask(String code) {
            this.code = code;
        }

        @Override
        public void run() {
            StrategyWrapper strategyWrapper = cache.get(code);
            if (strategyWrapper.isExpired()){
                loadStrategy(code);
            }
        }
    }

}
