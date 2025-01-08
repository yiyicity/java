package service;
import org.anyline.util.ConfigTable;
import org.noear.solon.Solon;
public class App {
    public static void main(String[] args) {
        init();
        Solon.start(App.class, args ,app -> {
            app.enableWebSocket(true);
        });
    }

    public static void init(){
        ConfigTable.IS_UPPER_KEY = false;
        ConfigTable.DEFAULT_PRIMARY_KEY="id";
        ConfigTable.IS_CACHE_DISABLED =false;
        ConfigTable.IS_AUTO_CHECK_METADATA = true;
        ConfigTable.IS_UPDATE_EMPTY_COLUMN=true;
        ConfigTable.IS_UPDATE_NULL_COLUMN=true;
        ConfigTable.IS_INSERT_EMPTY_COLUMN =true;
        ConfigTable.IS_INSERT_NULL_COLUMN = true;
        ConfigTable.IS_RETURN_EMPTY_STRING_REPLACE_NULL = true;
        ConfigTable.IS_RETURN_EMPTY_INSTANCE_REPLACE_NULL = true;
        ConfigTable.IS_SQL_DELIMITER_OPEN=true;
        ConfigTable.IS_SQL_DELIMITER_PLACEHOLDER_OPEN = true;
        boolean SQL = true;
        ConfigTable.IS_LOG_SQL = SQL;
        ConfigTable.IS_LOG_SQL_WARN=SQL;
        ConfigTable.IS_LOG_SQL_TIME = SQL;
        ConfigTable.IS_LOG_SLOW_SQL = SQL;
        ConfigTable.IS_LOG_SQL_PARAM = SQL;
        ConfigTable.IS_LOG_SQL_PARAM_WHEN_ERROR = SQL;
        ConfigTable.IS_LOG_ADAPTER_MATCH = SQL;
        ConfigTable.IS_SQL_LOG_PLACEHOLDER=SQL;
    }

}