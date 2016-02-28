package com.example.vasiliy.vkposts2;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@SmallTest
public class MainActivityTest {

    private MainActivity mainActivity;

    private String succesfullresponse = "{\"response\":{\"post_id\":1235721}}";
    private String responseWithCaptcha = "{\"error\":{\"error_code\":14,\"error_msg\":\"Captcha needed\",\"request_params\":[{\"key\":\"oauth\",\"value\":\"1\"},{\"key\":\"method\",\"value\":\"wall.post\"},{\"key\":\"owner_id\",\"value\":\"-34985835\"},{\"key\":\"friends_only\",\"value\":\"0\"},{\"key\":\"from_group\",\"value\":\"0\"},{\"key\":\"message\",\"value\":\"Hello!!!\"},{\"key\":\"v\",\"value\":\"5.45\"}],\"captcha_sid\":\"870095733291\",\"captcha_img\":\"http:\\/\\/api.vk.com\\/captcha.php?sid=870095733291\"}}";
    private String responseWithDoPostInComment = "{\"error\":{\"error_code\":15,\"error_msg\":\"Access denied: user should be group editor\",\"request_params\":[{\"key\":\"oauth\",\"value\":\"1\"},{\"key\":\"method\",\"value\":\"wall.post\"},{\"key\":\"owner_id\",\"value\":\"-13295252\"},{\"key\":\"friends_only\",\"value\":\"0\"},{\"key\":\"from_group\",\"value\":\"0\"},{\"key\":\"message\",\"value\":\"Hello!!!\"},{\"key\":\"v\",\"value\":\"5.45\"}]}}";


    @Before
    public void beforeTests() throws Exception {
        mainActivity = new MainActivity();
    }

    @Test
    public void testWhatIsResponse() throws Exception {
        Assert.assertEquals(mainActivity.whatIsResponse(succesfullresponse), VKPosts2Constants.SUCCESFULL_RESPONSE);
        Assert.assertEquals(mainActivity.whatIsResponse(responseWithCaptcha), VKPosts2Constants.RESPONSE_WITH_CAPTCHA);
        Assert.assertEquals(mainActivity.whatIsResponse(responseWithDoPostInComment), VKPosts2Constants.RESPONSE_WITH_CLOSE_WALL);
    }
}