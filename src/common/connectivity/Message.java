package common.connectivity;

import java.io.Serializable;

/** Represents a Message used for communication client <-> server.
 *
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The message data (e.g. User instance, List of Products etc..)
     */
    private Object data;

    /**
     * Describes the task the client is asking the server to perform
     */
    private MessageFromClient task;

    /**
     * Describes the result of the task completed by the server
     */
    private MessageFromServer answer;



    public Message() {
        super();
        this.data = null;
        this.task = null;
        this.answer = null;
    }

    /**
     * @param data
     * @param answer
     */
    public Message(Object data, MessageFromServer answer) {
        super();
        this.data = data;
        this.answer = answer;
    }

    /**
     * @param data
     * @param task
     */
    public Message(Object data, MessageFromClient task) {
        super();
        this.data = data;
        this.task = task;
    }

    /**
     * @return
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return
     */
    public MessageFromClient getTask(){
        return task;
    }

    /**
     * @param task
     */
    public void setTask(MessageFromClient task) {
        this.task = task;
    }

    /**
     * @return
     */
    public MessageFromServer getAnswer() {
        return answer;
    }

    /**
     * @param ExecutionStatus
     */
    public void setAnswer(MessageFromServer ExecutionStatus) {
        this.answer = ExecutionStatus;
    }

    /**
     * @return this objects fields as strings
     */
    @Override
    public String toString() {

        if (task == null){
            return "Message:\n\tData: " + data + "\n\tAnswer: " + answer;
        }
        return "Message:\n\tData: " + data + "\n\ttask: " + task;


        //return "Message [data=" + data + ", task=" + task + ", answer=" + answer + "]";
    }

}
