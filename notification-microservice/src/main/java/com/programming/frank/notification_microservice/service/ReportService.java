package com.programming.frank.notification_microservice.service;


import com.programming.frank.notification_microservice.dto.Report;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface ReportService {

    /**
     * @author <a href="mailto:4softwaredevelopers@gmail.com">Jordy Rodríguezr</a>
     * @date 24 sep. 2021
     * @description
     * @HU_CU_REQ
     * @param params
     * @return
     */
    Report getReportQuotation(Map<String, Object> params) throws JRException, IOException, SQLException;

}