package com.chrisimoni.workitemprocessor.util;

import com.chrisimoni.workitemprocessor.dto.ItemReportDto;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;

import java.util.List;

public class ReportUtil {

    public static byte[] generateJasperReport(List<ItemReportDto> items) throws JRException {
        // Generate the report template dynamically
        JasperDesign jasperDesign = createReportTemplate();

        // Compile the JasperDesign into a JRXML file
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        // Create the data source for the report
        JRDataSource dataSource = new JRBeanCollectionDataSource(items);

        // Fill the report with data
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        // Export the report to PDF
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return pdfBytes;
    }

    private static JasperDesign createReportTemplate() throws JRException {
        // Create a new JasperDesign
        JasperDesign jasperDesign = new JasperDesign();

        // Set properties of the JasperDesign
        jasperDesign.setName("ItemReport");

        // Create fields for item report
        createReportFields(jasperDesign);
        // Create a column header band
        createColumnHeaders(jasperDesign);

        // Create a detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(30);

        CreateDetailBand(detailBand);

        // Set the detail band to the JasperDesign
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        return jasperDesign;
    }

    private static void CreateDetailBand(JRDesignBand detailBand) {
        // Create text fields in the detail band for displaying item data
        JRDesignTextField valueTextField = new JRDesignTextField();
        valueTextField.setExpression(new JRDesignExpression("$F{value}"));
        valueTextField.setX(10);
        valueTextField.setY(10);
        valueTextField.setWidth(100);
        valueTextField.setHeight(20);
        detailBand.addElement(valueTextField);

        JRDesignTextField totalItemsTextField = new JRDesignTextField();
        totalItemsTextField.setExpression(new JRDesignExpression("$F{totalItems}"));
        totalItemsTextField.setX(120);
        totalItemsTextField.setY(10);
        totalItemsTextField.setWidth(100);
        totalItemsTextField.setHeight(20);
        detailBand.addElement(totalItemsTextField);

        JRDesignTextField processedItemsTextField = new JRDesignTextField();
        processedItemsTextField.setExpression(new JRDesignExpression("$F{processedItems}"));
        processedItemsTextField.setX(230);
        processedItemsTextField.setY(10);
        processedItemsTextField.setWidth(100);
        processedItemsTextField.setHeight(20);
        detailBand.addElement(processedItemsTextField);
    }

    private static void createColumnHeaders(JasperDesign jasperDesign) {
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(30);

        // Create text fields for headers
        columnHeaderBand.addElement(createTextFieldsForHeaders("Value"));
        columnHeaderBand.addElement(createTextFieldsForHeaders("Total Items"));

        //processedItemsHeader.setX(230);
        columnHeaderBand.addElement(createTextFieldsForHeaders("Processed Items"));

        // Set the column header band to the JasperDesign
        jasperDesign.setColumnHeader(columnHeaderBand);
    }

    private static JRDesignStaticText createTextFieldsForHeaders(String textField) {
        int x = textField.equalsIgnoreCase("Value") ? 10
                : (textField.equalsIgnoreCase("Total Items") ? 120 : 230);
        JRDesignStaticText textFieldHeader = new JRDesignStaticText();
        textFieldHeader.setText(textField);
        textFieldHeader.setX(x);
        textFieldHeader.setY(0);
        textFieldHeader.setWidth(100);
        textFieldHeader.setHeight(20);

        return textFieldHeader;
    }

    private static void createReportFields(JasperDesign jasperDesign) throws JRException {
        JRDesignField valueField = new JRDesignField();
        valueField.setName("value");
        valueField.setValueClass(Integer.class);

        JRDesignField totalItemsField = new JRDesignField();
        totalItemsField.setName("totalItems");
        totalItemsField.setValueClass(Long.class);

        JRDesignField processedItemField = new JRDesignField();
        processedItemField.setName("processedItems");
        processedItemField.setValueClass(Long.class);

        // Add fields to the JasperDesign
        jasperDesign.addField(valueField);
        jasperDesign.addField(totalItemsField);
        jasperDesign.addField(processedItemField);
    }
}
