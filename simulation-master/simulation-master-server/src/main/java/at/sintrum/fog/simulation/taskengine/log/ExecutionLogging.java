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

    public class LogEntry {
        private final String message;
        private final String instanceId;
        private final DateTime dateTime;

        public LogEntry(String instanceId, String message) {
            this.instanceId = instanceId;
            this.message = message;
            dateTime = new DateTime();
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
