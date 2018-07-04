package com.nupday.bo;
import java.time.LocalDate;
import java.time.Period;

import org.apache.commons.lang3.StringUtils;

public class MemorialDayBo {
    private Integer id;

    private String description;

    private LocalDate time;

    private Boolean open;

    private Boolean deletable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeleteable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Long getDays() {
        if (this.time == null) {
            return 0L;
        }
        LocalDate now = LocalDate.now();
        return now.toEpochDay() - time.toEpochDay();
    }

    public String getDetailDays() {
        if (this.time == null) {
            return "";
        }
        LocalDate now = LocalDate.now();
        Period period = Period.between(time, now);
        StringBuilder builder = new StringBuilder();
        if (period.getYears() > 0) {
            builder.append(period.getYears()).append("年");
        }
        if (period.getMonths() > 0) {
            if (StringUtils.isNotBlank(builder)) {
                builder.append("零");
            }
            builder.append(period.getMonths()).append("个月");
        }
        if (period.getDays() > 0) {
            if (StringUtils.isNotBlank(builder)) {
                builder.append("零");
            }
            builder.append(period.getDays()).append("天");
        }
        return builder.toString();
    }
}
