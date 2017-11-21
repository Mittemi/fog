package at.sintrum.fog.simulation.taskengine.log;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael Mittermayr on 04.09.2017.
 */
public class ExecutionLogging {

    private final List<LogEntry> logs;

    public ExecutionLogging() {
        logs = Collections.synchronizedList(new LinkedList<>());
    }

    public static class LogEntry {
        private String message;
        private String instanceId;
        private DateTime dateTime;

        public void setMessage(String message) {
            this.message = message;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

        public void setDateTime(DateTime dateTime) {
            this.dateTime = dateTime;
        }

        public LogEntry() {
        }

        public LogEntry(String message, String instanceId, DateTime dateTime) {
            this.message = message;
            this.instanceId = instanceId;
            this.dateTime = dateTime;
        }

        public LogEntry(String instanceId, String message) {
            this(instanceId, message, new DateTime());
        }

        public String getMessage() {
            return message;
        }

        public String getInstanceId() {
            return instanceId;
        }

        public DateTime getDateTime() {
            return dateTime;
        }
    }

    public void addMessage(String instanceId, String message) {
        logs.add(new LogEntry(instanceId, message));
    }

    public List<LogEntry> getLogs() {
        return logs;
    }
}
