package com.programming.frank.client_microservice.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.programming.frank.client_microservice.common.JasperReportManager;
import com.programming.frank.client_microservice.dto.Report;
import com.programming.frank.client_microservice.enums.TypeReport;
import com.programming.frank.client_microservice.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import net.sf.jasperreports.engine.JRException;

/**
 * @author <a href="mailto:4softwaredevelopers@gmail.com">Jordy Rodríguezr</a>
 * @project demo-spring-boot-jasper
 * @class ReporteVentasServiceImpl
 * @description
 * @HU_CU_REQ
 * @date 24 sep. 2021
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private JasperReportManager reportManager;

    @Autowired
    private DataSource dataSource;

    /**
     * @author <a href="mailto:4softwaredevelopers@gmail.com">Jordy Rodríguezr</a>
     * @date 24 sep. 2021
     * @param params
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws JRException
     * @see com.programming.frank.client_microservice.service.ReportService#getReportClient(java.util.Map)
     */
    @Override
    public Report getReportClient(Map<String, Object> params)
            throws JRException, IOException, SQLException {
        String fileName = "Clientes";
        Report dto = new Report();
        String extension = params.get("tipo").toString().equalsIgnoreCase(TypeReport.EXCEL.name()) ? ".xlsx"
                : ".pdf";
        dto.setFileName(fileName + extension);

        ByteArrayOutputStream stream = reportManager.export(fileName, params.get("tipo").toString(), params,
                dataSource.getConnection());

        byte[] bs = stream.toByteArray();
        dto.setStream(new ByteArrayInputStream(bs));
        dto.setLength(bs.length);

        return dto;
    }

}
