package service;

import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.core.util.LogUtil;

@Mapping("api")
public class Base {
    protected AnylineService s(){
        return ServiceProxy.service();
    }
    protected String env(String key){
        return Solon.cfg().get(key);
    }
    protected String getAppName(){
        return env("solon.app.name");
    }
    protected String getServerPort(){
        return env("server.port");
    }
    protected void log(String... str){
        LogUtil.global().info(String.join(" ", str));
    }
}
