package com.general.service;

import com.general.domain.VerificationCode;
import com.general.domain.vo.EmailVo;

/**
 * @author L
 * @date 2018-12-26
 */
public interface VerificationCodeService {

    /**
     * 发送邮件验证码
     * @param code
     */
    EmailVo sendEmail(VerificationCode code);

    /**
     * 验证
     * @param code
     */
    void validated(VerificationCode code);
}
