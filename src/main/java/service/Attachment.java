package service;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
@Controller
public class Attachment extends Base {
    @Mapping("/file/{type}/{id}/{name}")
    public void file(String type, String id, String name) {
        log(type, id, name);
    }
}