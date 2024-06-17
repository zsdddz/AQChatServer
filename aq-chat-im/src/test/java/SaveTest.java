import com.howcode.aqchat.AQChatApplication;
import com.howcode.aqchat.common.utils.SafeUtil;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @Author: ZhangWeinan
 * @Description:
 * @date 2024-04-27 0:05
 */
@SpringBootTest(classes = AQChatApplication.class)
public class SaveTest {

    @Resource
    private IAQMessageService messageService;

    @Test
    public void testSave() {
//        MessageDto aqMessage = new MessageDto();
//        aqMessage.setMessageId("1");
//        aqMessage.setRoomId("1");
//        aqMessage.setSenderId("1");
//        aqMessage.setMessageType(1);
//        aqMessage.setMessageContent("test");
//        aqMessage.setCreateTime(new Date());
//        messageService.saveMessage(aqMessage);
    }

    public static void main(String[] args) {
        // 模拟从用户输入中获取的富文本内容
        String userInput = "<img src=x oneRRoR=\"alert(&quot;is xss&quot;)\">";
        // 使用Jsoup库进行输入过滤和验证
        String sanitizedInput = SafeUtil.clean(userInput);
        // 输出经过转义和过滤的富文本内容
        System.out.println("Sanitized Input: " + sanitizedInput);
    }

}
