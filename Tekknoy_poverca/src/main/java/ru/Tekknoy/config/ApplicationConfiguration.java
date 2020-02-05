package ru.Tekknoy.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class ApplicationConfiguration {
    private String notCountDays;
    private String formatDateResult;
    private String formatDateBefore;
    private String infoByOrder;
    private String infoByPoverca;
    private String infoByDataShipment;
    private String infoByAcknoledg;
    private String infoByDelivery;
    private String orderExpired;
    private String orderShipment;

    private String fileHTMLForParser;
    private String charsetName;
    private String nameEdit;
    private String tagName;
    private String resultParserNameXLSX;
    private String resultParserNameXLS;

    private int valueDaysForPoverc;


    public String getResultParserNameXLSX() {
        return resultParserNameXLSX;
    }

    public void setResultParserNameXLSX(String resultParserNameXLSX) {
        this.resultParserNameXLSX = resultParserNameXLSX;
    }

    public String getResultParserNameXLS() {
        return resultParserNameXLS;
    }

    public void setResultParserNameXLS(String resultParserNameXLS) {
        this.resultParserNameXLS = resultParserNameXLS;
    }

    public String getNotCountDays() {
        return notCountDays;
    }

    public void setNotCountDays(String notCountDays) {
        this.notCountDays = notCountDays;
    }

    public String getFormatDateResult() {
        return formatDateResult;
    }

    public void setFormatDateResult(String formatDateResult) {
        this.formatDateResult = formatDateResult;
    }

    public String getFormatDateBefore() {
        return formatDateBefore;
    }

    public void setFormatDateBefore(String formatDateBefore) {
        this.formatDateBefore = formatDateBefore;
    }

    public String getInfoByOrder() {
        return infoByOrder;
    }

    public void setInfoByOrder(String infoByOrder) {
        this.infoByOrder = infoByOrder;
    }

    public String getInfoByPoverca() {
        return infoByPoverca;
    }

    public void setInfoByPoverca(String infoByPoverca) {
        this.infoByPoverca = infoByPoverca;
    }

    public String getInfoByDataShipment() {
        return infoByDataShipment;
    }

    public void setInfoByDataShipment(String infoByDataShipment) {
        this.infoByDataShipment = infoByDataShipment;
    }

    public String getInfoByAcknoledg() {
        return infoByAcknoledg;
    }

    public void setInfoByAcknoledg(String infoByAcknoledg) {
        this.infoByAcknoledg = infoByAcknoledg;
    }

    public String getInfoByDelivery() {
        return infoByDelivery;
    }

    public void setInfoByDelivery(String infoByDelivery) {
        this.infoByDelivery = infoByDelivery;
    }

    public String getOrderExpired() {
        return orderExpired;
    }

    public void setOrderExpired(String orderExpired) {
        this.orderExpired = orderExpired;
    }

    public String getOrderShipment() {
        return orderShipment;
    }

    public void setOrderShipment(String orderShipment) {
        this.orderShipment = orderShipment;
    }

    public String getFileHTMLForParser() {
        return fileHTMLForParser;
    }

    public void setFileHTMLForParser(String fileHTMLForParser) {
        this.fileHTMLForParser = fileHTMLForParser;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getNameEdit() {
        return nameEdit;
    }

    public void setNameEdit(String nameEdit) {
        this.nameEdit = nameEdit;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getValueDaysForPoverc() {
        return valueDaysForPoverc;
    }

    public void setValueDaysForPoverc(int valueDaysForPoverc) {
        this.valueDaysForPoverc = valueDaysForPoverc;
    }
}
