package com.monster.disciple.subscribe;

import com.monster.disciple.exception.BusinessException;

public interface IDiscipleException {

    /**
     * 网络 异常
     */
    void onBadNetError(Throwable t);

    /**
     * http 异常
     */
    void onServerError(Throwable t);

    /**
     * 业务 异常
     */
    void onBusinessError(BusinessException ex);

    /**
     * 其他 异常
     */
    void onOtherError(Throwable t);
}
