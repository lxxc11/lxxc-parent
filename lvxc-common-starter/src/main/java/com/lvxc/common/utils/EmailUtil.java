package com.lvxc.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.lvxc.common.dto.EmailContact;
import com.lvxc.common.dto.EmailDto;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @Author caoyq
 * @Date 2023/9/14 17:53
 * @PackageName:com.lvxc.common.utils
 * @ClassName: EmailUtil
 * @Version 1.0
 */
@Slf4j
public class EmailUtil {
    private static String username;
    private static String password;
    private static String host;
    private static String port;
    private static String from;
    private static boolean isLocal;
    private static String debug;

    public static void setUsername(String username) {
        EmailUtil.username = username;
    }

    public static void setPassword(String password) {
        EmailUtil.password = password;
    }

    public static void setHost(String host) {
        EmailUtil.host = host;
    }

    public static void setPort(String port) {
        EmailUtil.port = port;
    }

    public static void setFrom(String from) {
        EmailUtil.from = from;
    }

    public static void setIsLocal(boolean isLocal) {
        EmailUtil.isLocal = isLocal;
    }

    public static void setDebug(String debug) {
        EmailUtil.debug = debug;
    }

    /**
     * 发送邮件
     * @param emailDto
     */
    public static void send(EmailDto emailDto) {
        // 这个是整个邮件的包裹对象
        // 附件和正文一系列都在整个里面
        Multipart part = new MimeMultipart();


        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");//开启协议
        props.setProperty("mail.smtp.host", host);//主机名
        if (isLocal) {
            props.setProperty("mail.smtp.port", port);//端口
            props.setProperty("mail.smtp.socketFactory.port", port);
        }
        props.setProperty("mail.smtp.auth", "true");//是否开启权限控制
        props.setProperty("mail.debug", debug);//true表示在发送邮件时会在控制台上打印发送时的信息

        //Session表示程序到邮件服务器之间的一次会话
        Session session = Session.getInstance(props);
        Message msg = new MimeMessage(session);//邮件对象
        try {
            msg.setFrom(new InternetAddress(from));//发送人
            //收件人
            if (CollectionUtil.isNotEmpty(emailDto.getTo())) {
                InternetAddress to[] = new InternetAddress[emailDto.getTo().size()];
                for (int i = 0; i < emailDto.getTo().size(); i++) {
                    EmailContact emailContact = emailDto.getTo().get(i);
                    to[i] = new InternetAddress(emailContact.getAddress(), emailContact.getName());
                }
                msg.setRecipients(Message.RecipientType.TO, to);//收件人
            } else {
                throw new Exception("请输入收件人地址!");
            }
            // 抄送人
            if (CollectionUtil.isNotEmpty(emailDto.getCc())) {
                InternetAddress cc[] = new InternetAddress[emailDto.getCc().size()];
                for (int i = 0; i < emailDto.getCc().size(); i++) {
                    EmailContact emailContact = emailDto.getCc().get(i);
                    cc[i] = new InternetAddress(emailContact.getAddress(), emailContact.getName());
                }
                msg.setRecipients(Message.RecipientType.CC, cc);//抄送人
            }
            // 密送人 一般用不到
            if (CollectionUtil.isNotEmpty(emailDto.getBcc())) {
                InternetAddress bcc[] = new InternetAddress[emailDto.getBcc().size()];
                for (int i = 0; i < emailDto.getBcc().size(); i++) {
                    EmailContact emailContact = emailDto.getBcc().get(i);
                    bcc[i] = new InternetAddress(emailContact.getAddress(), emailContact.getName());
                }
                msg.setRecipients(Message.RecipientType.BCC, bcc);//抄送人
            }
            msg.setSubject(emailDto.getSubject());//主题
            //设置正文
            BodyPart textPart = new MimeBodyPart();
            //设置正文内容并标明含有html标签
            textPart.setContent(emailDto.getContent(), "text/html;charset=utf-8");

            // 这种是读取本地附件的
            if (CollectionUtil.isNotEmpty(emailDto.getLocalUrl())) {
                for (String localUrl : emailDto.getLocalUrl()) {
                    //设置附件
                    BodyPart localFilePart = new MimeBodyPart();
                    Path path = Paths.get(localUrl);
                    localFilePart.setFileName(path.getFileName().toString());

                    localFilePart.setDataHandler(
                            new DataHandler(
                                    new ByteArrayDataSource(
                                            Files.readAllBytes(path), "application/octet-stream")));
                    part.addBodyPart(localFilePart);
                }
            }
            // 网络路径
            if (CollectionUtil.isNotEmpty(emailDto.getNetUrl())) {
                for (String netUrl : emailDto.getNetUrl()) {
                    BodyPart netFilePart = new MimeBodyPart();
                    // 通过网络路径获取到文件
                    URL url = new URL(netUrl);
                    netFilePart.setFileName(url.getFile().substring(url.getFile().lastIndexOf("/")+1));
                    DataHandler handler = new DataHandler(url);
                    netFilePart.setDataHandler(handler);
                    part.addBodyPart(netFilePart);
                }
            }
            //整合正文和附件
            part.addBodyPart(textPart);


            msg.setContent(part);
            Transport tran = session.getTransport();
            tran.connect(username, password);//此处不是QQ的密码，是开启POP3/SMTP时生成的authorization code
            tran.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
