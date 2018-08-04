package com.nupday.bo;
import java.time.LocalDateTime;

/**
 * VisitorBo
 * @author Neil Wan
 * @create 18-8-4
 */
public class VisitorBo {
    private Integer id;

    private String code;

    private Boolean forever;

    private LocalDateTime from;

    private LocalDateTime to;

    private Integer loginCount;

    private Integer maxCount;

    private Boolean isTimeout;

    private Boolean isCountout;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getForever() {
        return forever;
    }

    public void setForever(Boolean forever) {
        this.forever = forever;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Boolean getIsTimeout() {
        return isTimeout;
    }

    public void setIsTimeout(Boolean timeout) {
        isTimeout = timeout;
    }

    public Boolean getIsCountout() {
        return isCountout;
    }

    public void setIsCountout(Boolean countout) {
        isCountout = countout;
    }
}
