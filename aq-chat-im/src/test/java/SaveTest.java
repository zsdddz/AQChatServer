import com.howcode.aqchat.AQChatApplication;
import com.howcode.aqchat.common.model.MessageDto;
import com.howcode.aqchat.service.dao.po.AqMessage;
import com.howcode.aqchat.service.service.IAQMessageService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

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
        MessageDto aqMessage = new MessageDto();
        aqMessage.setMessageId(1L);
        aqMessage.setRoomId("1");
        aqMessage.setSenderId("1");
        aqMessage.setMessageType(1);
        aqMessage.setMessageContent("test");
        aqMessage.setCreateTime(new Date());
        messageService.saveMessage(aqMessage);
    }
}
