package com.programming.frank.client_microservice.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;


import com.programming.frank.client_microservice.dto.Report;
import net.sf.jasperreports.engine.JRException;

public interface ReportService {

    /**
     * @author <a href="mailto:4softwaredevelopers@gmail.com">Jordy Rodríguezr</a>
     * @date 24 sep. 2021
     * @description
     * @HU_CU_REQ
     * @param params
     * @return
     */
    Report getReportClient(Map<String, Object> params) throws JRException, IOException, SQLException;

}