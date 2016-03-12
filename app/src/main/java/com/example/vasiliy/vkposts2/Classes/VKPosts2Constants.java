package com.example.vasiliy.vkposts2.Classes;

public class VKPosts2Constants {
    public static final int DO_POST_ON_WALL= 0;
    public static final int DO_POST_IN_COMMENT= 1;

    public static final int SUCCESFULL_RESPONSE = 0;
    public static final int RESPONSE_WITH_CAPTCHA = 1;
    public static final int RESPONSE_WITH_CLOSE_WALL = 3;
    public static final int ERROR_RESPONSE = 4;

    public static final int VK_ERROR_CODE_TOO_MACH_REQUEST_PER_SECOND = 6;
    public static final int VK_ERROR_CODE_CAPTCHA = 14;
    public static final int VK_ERROR_CODE_CLOSE_WALL = 15;

    public static final String VK_ERROR_MSG_GROUP_IS_BLOCKED = "Access denied: group is blocked";


    public static final int VK_RANDOM_TIME_RANGE = 300;
    public static final int VK_TIME_BETWEEN_DO_POST = 900;
    public static final int VK_TIME_BETWEEN_ADD_FRIEND = 205;

    public static final int INPUT_CAPTCHA_ACTIVITY_RESULT_CODE = 1;
    public static final int EDIT_LIST_ACTIVITY_RESULT_CODE = 2;


    public static final int TWO_TIMES_DO_POST = 2;
    public static final int THREE_TIMES_DO_POST = 3;
}
