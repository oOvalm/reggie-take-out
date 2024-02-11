package com.ovalm.reggie.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.stereotype.Component;

/**
 * vtxeezvgalczjcdb
 */

@Component
public class POP3Util {

    @Value("${ovalm.email.from}")
    private String srcEmail;

    @Value("${ovalm.email.name}")
    private String name;

    @Value("${ovalm.email.code}")
    private String authCode;

    @Value("${ovalm.email.stmp}")
    private String stmp;


    public void sendMessage(String dest, int param) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(stmp);   // 邮箱stmp地址
        email.setCharset("utf-8");  // 邮件字符集
        email.addTo(dest);          // 收件人
        email.setFrom(srcEmail, name);
        email.setAuthentication(srcEmail, authCode);
        email.setSubject("验证码");
        email.setTextMsg("【oval_m】您的验证码为" + param + ", 请勿泄露和转发");
        email.send();
    }
}
