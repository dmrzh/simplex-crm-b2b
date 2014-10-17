package ru.dev_server.client.dao;

import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.model.SmppSettings;
import ru.dev_server.client.sms.SmsSender;

import javax.annotation.Resource;

/**.*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class SmppSettingsTest {
    @Resource
    private SmppSettingsDAO smppSettingsDAO;

    @Resource
    private SmsSender smsSender;

    @Test   @Transactional
    public void createSmppSettings() {
        SmppSettings settings = new SmppSettings();
        settings.setHost("78.46.32.24");
        settings.setPort(2775);
        settings.setLogin("471");
        settings.setPassword("gV10J7am");
        settings.setSourceAddrTon(TypeOfNumber.UNKNOWN);
        settings.setSourceAddrNpi(NumberingPlanIndicator.UNKNOWN);
        settings.setDestAddrTon(TypeOfNumber.INTERNATIONAL);
        settings.setDestAddrNpi(NumberingPlanIndicator.ISDN);
        settings.setPriorityFlag(0);
        settings.setServiceType(null);
        settings.setMessageClass(null);
        smppSettingsDAO.saveOrUpdate(settings);

        settings = new SmppSettings();
        settings.setHost("smpp3.websms.ru");
        settings.setPort(2222);
        settings.setLogin("dmrzh2");
        settings.setPassword("uw0cho0E");
        settings.setSourceAddrTon(TypeOfNumber.ALPHANUMERIC);
        settings.setSourceAddrNpi(NumberingPlanIndicator.UNKNOWN);
        settings.setDestAddrTon(TypeOfNumber.INTERNATIONAL);
        settings.setDestAddrNpi(NumberingPlanIndicator.ISDN);
        settings.setPriorityFlag(1);
        settings.setServiceType("CMT");
        settings.setMessageClass(MessageClass.CLASS1);
        smppSettingsDAO.saveOrUpdate(settings);
    }

    @Test
    @Transactional
    public void testSmsSender_notifyClient() throws Exception{
        String msg10="qwe123йцу\n";
        String msg="";
        for(int i=0;i<10;i++){
            msg=msg+msg10;
        }

        String ipsum="Проснувшись однажды утром после беспокойного сна, Грегор Замза обнаружил, что он у себя в постели превратился в страшное насекомое.\n" +
                "\n" +
                "Лежа на панцирнотвердой спине, он видел, стоило ему приподнять голову, свой коричневый, выпуклый, разделенный дугообразными чешуйками живот, на верхушке которого еле держалось готовое вот-вот окончательно сползти одеяло.\n" +
                "\n" +
                "Его многочисленные, убого тонкие по сравнению с остальным телом ножки беспомощно копошились у него перед глазами.\n" +
                "\n" +
                "«Что со мной случилось? » – подумал он.\n"
                ;  String loren=
                //+
                "\n" +
                "Это не было сном.\n" +
                "\n" +
                "Его комната, настоящая, разве что слишком маленькая, но обычная комната, мирно покоилась в своих четырех хорошо знакомых стенах.\n" +
                "\n" +
                "Над столом, где были разложены распакованные образцы сукон – Замза был коммивояжером, – висел портрет, который он недавно вырезал из иллюстрированного журнала и вставил в красивую золоченую рамку.\n" +
                "\n" +
                "На портрете была изображена дама в меховой шляпе и боа, она сидела очень прямо и протягивала зрителю тяжелую меховую муфту, в которой целиком исчезала ее рука.\n" +
                "\n" +
                "Затем взгляд Грегора устрем"
//                        +
//                        "ился в окно, и пасмурная погода – слышно было, как по жести подоконника стучат капли дождя – привела его и вовсе в грустное настроение.\n" +
//                "\n" +
//                "«Хорошо бы еще немного поспать и забыть всю эту чепуху», – подумал он, но это было совершенно неосуществимо, он привык спать на правом боку, а в теперешнем своем "
                ;
        String nadyaPhone = "79263578060";
        String dimaPhone="79264265555";
//        smsSender.notifyClient("message", nadyaPhone,loren);
//
//        Thread.sleep(1000*100);

    }
}
