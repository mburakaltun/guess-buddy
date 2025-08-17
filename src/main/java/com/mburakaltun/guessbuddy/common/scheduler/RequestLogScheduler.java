package com.mburakaltun.guessbuddy.common.scheduler;

import com.mburakaltun.guessbuddy.common.model.entity.RequestLogEntity;
import com.mburakaltun.guessbuddy.common.model.entity.RequestLogHistoryEntity;
import com.mburakaltun.guessbuddy.common.repository.RequestLogHistoryRepository;
import com.mburakaltun.guessbuddy.common.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestLogScheduler {

    private final RequestLogRepository requestLogRepository;
    private final RequestLogHistoryRepository requestLogHistoryRepository;

    private static final int BATCH_SIZE = 1000;

    @Scheduled(cron = "59 59 23 * * *")
    @Transactional
    public void moveLogsToHistory() {
        log.info("Starting scheduled job to move request logs to history table");

        try {
            long startTime = System.currentTimeMillis();
            int totalProcessed = 0;

            List<RequestLogEntity> allLogs = requestLogRepository.findAll();
            List<RequestLogHistoryEntity> historyEntities = new ArrayList<>();

            for (RequestLogEntity requestLog : allLogs) {
                RequestLogHistoryEntity historyEntity = convertToHistoryEntity(requestLog);
                historyEntities.add(historyEntity);

                if (historyEntities.size() >= BATCH_SIZE) {
                    requestLogHistoryRepository.saveAll(historyEntities);
                    totalProcessed += historyEntities.size();
                    historyEntities.clear();
                    log.debug("Processed batch of {} logs", BATCH_SIZE);
                }
            }

            if (!historyEntities.isEmpty()) {
                requestLogHistoryRepository.saveAll(historyEntities);
                totalProcessed += historyEntities.size();
            }

            requestLogRepository.deleteAll(allLogs);

            long duration = System.currentTimeMillis() - startTime;
            log.info("Completed moving {} request logs to history table in {} ms", totalProcessed, duration);
        } catch (Exception e) {
            log.error("Error moving request logs to history table", e);
        }
    }

    private RequestLogHistoryEntity convertToHistoryEntity(RequestLogEntity source) {
        return RequestLogHistoryEntity.builder()
                .responseTime(source.getResponseTime())
                .responseStatus(source.getResponseStatus())
                .requestMethod(source.getRequestMethod())
                .requestUrl(source.getRequestUrl())
                .requestHeaders(source.getRequestHeaders())
                .requestPayload(source.getRequestPayload())
                .responsePayload(source.getResponsePayload())
                .requestIp(source.getRequestIp())
                .build();
    }
}
