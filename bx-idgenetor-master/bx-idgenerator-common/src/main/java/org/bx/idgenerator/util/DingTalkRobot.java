package org.bx.idgenerator.util;



public class DingTalkRobot {

   /* static String CorpId = "dingc2288ff4453d296df5bf40eda33b7ba0";
    static String AgentId = "3531003";
    static String AppKey = "dingea6vhtpsuvttsxwi";
    static String AppSecret = "dI1TS9N9WE_CL3CWDZ8Xq2OXA7tK1-BdlN-nTBoef-j-i_cLo-4F6syQRhOE2RHq";


    public static String getTokenResponse(){
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest req = new OapiGettokenRequest();
        req.setAppkey(AppKey);
        req.setAppsecret(AppSecret);
        req.setHttpMethod("GET");

        String token = null;

        try {
            OapiGettokenResponse rsp = client.execute(req);
            token = rsp.getAccessToken();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return token;
    }

    public static void sendMessage(){
        String token = getTokenResponse();
//        String token = "476dda0f0da93ed48181139e7caa71d5";
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=" + token);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("测试文本消息");
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("132xxxxxxxx"));
// isAtAll类型如果不为Boolean，请升级至最新SDK
        at.setIsAtAll(true);
        request.setAt(at);

        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl("https://www.dingtalk.com/");
        link.setPicUrl("");
        link.setTitle("时代的火车向前开");
        link.setText("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
        request.setLink(link);

        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("杭州天气");
        markdown.setText("#### 杭州天气 @156xxxx8827\n" +
                "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
                "> ![screenshot](https://gw.alicdn.com/tfs/TB1ut3xxbsrBKNjSZFpXXcXhFXa-846-786.png)\n"  +
                "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");
        request.setMarkdown(markdown);
        try {
            OapiRobotSendResponse response = client.execute(request);
            int a = 1;
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        sendMessage();

    }
*/
}
