package io.dotinc.perflog.util;


import org.springframework.util.StopWatch;

import java.text.NumberFormat;

/**
 * @author vladbulimac on 11.04.2022.
 */

public class ExecutionStopWatch extends StopWatch {
    private boolean keepTaskList = true;

    public ExecutionStopWatch(String id) {
        super(id);
    }

    @Override
    public void setKeepTaskList(boolean keepTaskList) {
        super.setKeepTaskList(keepTaskList);
        this.keepTaskList = keepTaskList;
    }

    /**
     * Get a short description of the total running time.
     */
    public String shortSummary() {
        return "StopWatch '" + getId() + "': running time = " + getTotalTimeMillis() + " ms";
    }

    /**
     * Generate a string with a table describing all tasks performed.
     * <p>For custom reporting, call {@link #getTaskInfo()} and use the task info
     * directly.
     */
    public String prettyPrint() {
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append('\n');
        if (!keepTaskList) {
            sb.append("No task info kept");
        }
        else {
            sb.append("---------------------------------------------\n");
            sb.append("ms         %     Task name\n");
            sb.append("---------------------------------------------\n");
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(9);
            nf.setGroupingUsed(false);
            NumberFormat pf = NumberFormat.getPercentInstance();
            pf.setMinimumIntegerDigits(3);
            pf.setGroupingUsed(false);
            for (TaskInfo task : getTaskInfo()) {
                sb.append(nf.format(task.getTimeMillis())).append("  ");
                sb.append(pf.format((double) task.getTimeMillis() / getTotalTimeMillis())).append("  ");
                sb.append(task.getTaskName()).append("\n");
            }
        }
        return sb.toString();
    }
}
