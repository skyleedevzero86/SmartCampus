package com.sleekydz86.alerm.batch.infrastructure.mail;

import com.sleekydz86.alerm.batch.domain.mail.MailStorage;
import com.sleekydz86.alerm.batch.domain.mail.MailStorageRepository;
import com.sleekydz86.alerm.batch.domain.mail.vo.MailStatus;
import com.sleekydz86.alerm.batch.infrastructure.mail.mapper.MailStorageMapper;
import com.sleekydz86.alerm.global.exception.DatabaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MailStorageRepositoryImpl implements MailStorageRepository {

    private final MailStorageMapper mailStorageMapper;

    @Override
    public void save(final MailStorage mailStorage) {
        try {
            Map<String, Object> params = new HashMap<>();
            String operation = (mailStorage.getId() != null) ? "U" : "C";
            params.put("operation", operation);
            params.put("id", mailStorage.getId());
            params.put("memberId", mailStorage.getReceiverId());
            params.put("email", mailStorage.getReceiverEmail());
            params.put("nickname", mailStorage.getReceiverNickname());
            params.put("mailStatus", mailStorage.getMailStatus().name());
            params.put("resultMessage", "");
            params.put("affectedRows", 0);
            
            mailStorageMapper.executeMailStorageCUD(params);
            
            validateProcedureResult(params, operation);
        } catch (Exception e) {
            log.error("메일 저장소 저장 실패 - 회원ID: {}, 이메일: {}", 
                    mailStorage.getReceiverId(), mailStorage.getReceiverEmail(), e);
            throw new DatabaseException("메일 저장소 저장 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public List<MailStorage> findAllByNotDone() {
        try {
            return mailStorageMapper.findAllByMailStatus(MailStatus.FAIL);
        } catch (Exception e) {
            log.error("실패한 메일 조회 중 오류 발생", e);
            throw new DatabaseException("실패한 메일 조회 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void deleteAllByDoneMails() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "D");
            params.put("id", null);
            params.put("memberId", null);
            params.put("email", null);
            params.put("nickname", null);
            params.put("mailStatus", MailStatus.DONE.name());
            params.put("resultMessage", "");
            params.put("affectedRows", 0);
            
            mailStorageMapper.executeMailStorageCUD(params);
            
            validateProcedureResult(params, "D");
        } catch (Exception e) {
            log.error("완료된 메일 삭제 중 오류 발생", e);
            throw new DatabaseException("완료된 메일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateProcedureResult(Map<String, Object> params, String operation) {
        Integer affectedRows = (Integer) params.get("affectedRows");
        String resultMessage = (String) params.get("resultMessage");
        
        if (affectedRows == null || affectedRows <= 0) {
            String errorMessage = resultMessage != null && !resultMessage.isEmpty() 
                    ? resultMessage 
                    : String.format("프로시저 실행 실패 (operation: %s)", operation);
            throw new DatabaseException(errorMessage);
        }
    }
}
