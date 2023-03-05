package datetool.date;

/**
 * 工具类异常
 *
 * @author xiaoleilu
 */
public class DateException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public DateException(Throwable e) {
        super(getMessage(e), e);
    }

    private static String getMessage(Throwable e) {
        String message;
        if (null == e) {
            message = "null";
        } else {
            message = DateUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        }
        return message;
    }

    public DateException(String message) {
        super(message);
    }

    public DateException(String messageTemplate, Object... params) {
        super(DateUtil.format(messageTemplate, params));
    }

    public DateException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
