package service;

import org.anyline.entity.DataSet;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

@Controller
@Mapping("*/member")
public class Member extends Base {
    @Mapping("save")
    public String save() {
        //DataSet ds = s().querys("Member");
        //return ds;
        log("Member save11");
        return getServerPort();
    }
}