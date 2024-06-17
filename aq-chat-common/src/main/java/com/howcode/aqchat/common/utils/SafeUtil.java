package com.howcode.aqchat.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/17 18:18
 */
public class SafeUtil {

    private static final Safelist whitelist = Safelist.basicWithImages();
    /*
     * 配置过滤化参数,不对代码进行格式化
     */
    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);

    static {
        /*
         * 富文本编辑时一些样式是使用style来进行实现的 比如红色字体 所以需要给所有标签添加style属性
         */
        whitelist.addAttributes(":all", "style");
    }

    public static String clean(String input) {
        return Jsoup.clean(input, "", whitelist, outputSettings);
    }
}
