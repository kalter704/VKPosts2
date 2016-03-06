package com.example.vasiliy.vkposts2;

import android.test.suitebuilder.annotation.SmallTest;

import com.example.vasiliy.vkposts2.Activitys.PRActivity;
import com.example.vasiliy.vkposts2.Classes.VKPosts2Constants;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@SmallTest
public class MainActivityTest {

    private PRActivity PRActivity;

    private String nullString = null;
    private String succesfullresponse = "{\"response\":{\"post_id\":1235721}}";
    private String responseWithCaptcha = "{\"error\":{\"error_code\":14,\"error_msg\":\"Captcha needed\",\"request_params\":[{\"key\":\"oauth\",\"value\":\"1\"},{\"key\":\"method\",\"value\":\"wall.post\"},{\"key\":\"owner_id\",\"value\":\"-34985835\"},{\"key\":\"friends_only\",\"value\":\"0\"},{\"key\":\"from_group\",\"value\":\"0\"},{\"key\":\"message\",\"value\":\"Hello!!!\"},{\"key\":\"v\",\"value\":\"5.45\"}],\"captcha_sid\":\"870095733291\",\"captcha_img\":\"http:\\/\\/api.vk.com\\/captcha.php?sid=870095733291\"}}";
    private String responseWithDoPostInComment = "{\"error\":{\"error_code\":15,\"error_msg\":\"Access denied: user should be group editor\",\"request_params\":[{\"key\":\"oauth\",\"value\":\"1\"},{\"key\":\"method\",\"value\":\"wall.post\"},{\"key\":\"owner_id\",\"value\":\"-13295252\"},{\"key\":\"friends_only\",\"value\":\"0\"},{\"key\":\"from_group\",\"value\":\"0\"},{\"key\":\"message\",\"value\":\"Hello!!!\"},{\"key\":\"v\",\"value\":\"5.45\"}]}}";

    private String stringWithId = "{\"response\":{\"count\":5,\"items\":[{\"id\":66896044,\"from_id\":-13295252,\"owner_id\":-13295252,\"date\":1456682467,\"post_type\":\"post\",\"text\":\"https:\\/\\/vk.com\\/lvovdanila Добавляйтесь в друзья. Буду рад новым знакомствам \uD83D\uDD25\",\"attachments\":[{\"type\":\"photo\",\"photo\":{\"id\":406632652,\"album_id\":-7,\"owner_id\":-13295252,\"user_id\":100,\"photo_75\":\"http:\\/\\/cs543104.vk.me\\/v543104847\\/152f8\\/m-unYMFQXhk.jpg\",\"photo_130\":\"http:\\/\\/cs543104.vk.me\\/v543104847\\/152f9\\/ou25VQNi_x4.jpg\",\"photo_604\":\"http:\\/\\/cs543104.vk.me\\/v543104847\\/152fa\\/p83BzMu_64s.jpg\",\"photo_807\":\"http:\\/\\/cs543104.vk.me\\/v543104847\\/152fb\\/fFxDDnztAaA.jpg\",\"photo_1280\":\"http:\\/\\/cs543104.vk.me\\/v543104847\\/152fc\\/-AmQj-pKTIU.jpg\",\"width\":960,\"height\":960,\"text\":\"\",\"date\":1456595921,\"post_id\":66779707,\"access_key\":\"7c966ccd303a06406b\"}}],\"post_source\":{\"type\":\"api\",\"platform\":\"android\"},\"comments\":{\"count\":8671,\"can_post\":1},\"likes\":{\"count\":47,\"user_likes\":0,\"can_like\":1,\"can_publish\":1},\"reposts\":{\"count\":0,\"user_reposted\":0}}]}}";

    private String stringWithCaptcha = "{\"error\":{\"error_code\":14,\"error_msg\":\"Captcha needed\",\"request_params\":[{\"key\":\"oauth\",\"value\":\"1\"},{\"key\":\"method\",\"value\":\"wall.addComment\"},{\"key\":\"owner_id\",\"value\":\"-13295252\"},{\"key\":\"post_id\",\"value\":\"66896044\"},{\"key\":\"from_group\",\"value\":\"0\"},{\"key\":\"text\",\"value\":\"Добавь меня!!!)))\"},{\"key\":\"v\",\"value\":\"5.45\"}],\"captcha_sid\":\"607609401370\",\"captcha_img\":\"http:\\/\\/api.vk.com\\/captcha.php?sid=607609401370&s=1\"}}";

    @Before
    public void beforeTests() throws Exception {
        PRActivity = new PRActivity();
    }

    @Test
    public void testWhatIsResponse() throws Exception {
        Assert.assertEquals(PRActivity.whatIsResponse(nullString), VKPosts2Constants.ERROR_RESPONSE);
        Assert.assertEquals(PRActivity.whatIsResponse(succesfullresponse), VKPosts2Constants.SUCCESFULL_RESPONSE);
        Assert.assertEquals(PRActivity.whatIsResponse(responseWithCaptcha), VKPosts2Constants.RESPONSE_WITH_CAPTCHA);
        Assert.assertEquals(PRActivity.whatIsResponse(responseWithDoPostInComment), VKPosts2Constants.RESPONSE_WITH_CLOSE_WALL);
    }

    @Test
    public void testGetPostId() throws Exception {
        Assert.assertEquals(PRActivity.getPostId(nullString), null);
        Assert.assertEquals(PRActivity.getPostId(stringWithId), "66896044");
    }

    @Test
    public void testGetCaptcha() throws Exception {
        Map<String, String> capMap = PRActivity.getCaptcha(stringWithCaptcha);
        Assert.assertEquals(capMap.get("captchaId"), "607609401370");
        Assert.assertEquals(capMap.get("captchaUrl"), "http://api.vk.com/captcha.php?sid=607609401370&s=1");
    }
}