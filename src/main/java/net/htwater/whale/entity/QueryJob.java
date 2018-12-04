package net.htwater.whale.entity;

import lombok.Data;

@Data
public class QueryJob {
    private String JOB_NAME;
    private String JOB_Group;
    private String JOB_CLASS_NAME;
    private String TRIGGER_NAME;
    private String TRIGGER_GROUP;
    private String CRON_EXPRESSION;
}
