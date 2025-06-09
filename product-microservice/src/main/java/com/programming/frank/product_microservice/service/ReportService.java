package com.programming.frank.product_microservice.service;

import com.programming.frank.product_microservice.dto.Report;
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
    Report getReportProduct(Map<String, Object> params) throws JRException, IOException, SQLException;

}