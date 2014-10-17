package ru.dev_server.client.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.sms.SmsSender;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**.*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class ReportTest {
    private static final Logger LOG= LoggerFactory.getLogger(ReportTest.class);

    @Resource
    private DynamicColumnDAO dynamicColumnDAO;

    @Resource
    CompanyDAO companyDAO;

    @Resource
    private ClientDAO clientDAO;

    @Resource private SmsSender smsSender;

    @Test      @Transactional
    public void tetGetMeetingDynamicColumnReport(){

        LOG.warn("start tetGetMeetingDynamicColumnReport");

        Company company = companyDAO.get(32768L);
        Calendar now = Calendar.getInstance();
        Calendar day7 = Calendar.getInstance();
        day7.add(Calendar.WEEK_OF_YEAR,-1);

        Double summ = dynamicColumnDAO.getMeetingReport(company, "Оплата", day7.getTime(), now.getTime());
        List<Object[]> report = dynamicColumnDAO.getMeetingDaylyReport(company, "Оплата", day7.getTime(), now.getTime());
        LOG.warn("" + summ);
    }


}
