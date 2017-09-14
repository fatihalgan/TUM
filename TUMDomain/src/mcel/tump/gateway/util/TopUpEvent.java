package mcel.tump.gateway.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TopUpEvent {

private static final String strDateFormat = "yyyy-MM-dd'T'HH:mm:ss";
	
	private String msisdn = null;
	private Integer transactionAmount;
	private Date edgeTimestamp;
	private String edgeDealerId;
	
	public TopUpEvent(String msisdn, Integer transactionAmount, Date edgeTimestamp, String edgeDealerId) {
		this.msisdn = msisdn;
		this.transactionAmount = transactionAmount;
		this.edgeTimestamp = edgeTimestamp;
		this.edgeDealerId = edgeDealerId;
	}
	
	public String asXML() {
		StringBuilder builder = new StringBuilder();
		builder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		builder.append("<soapenv:Header/>");
		builder.append("<soapenv:Body>");
		builder.append("<tns:TopUpEvent xmlns:tns=\"http://www.bridge.com/tum\" xmlns:tns1=\"http://co.mcel.mz/types/common\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.bridge.com/tum TUMTopUpEvent.xsd\">");
		builder.append("<tns:msisdn>");
		builder.append("<tns1:internationalPrefix>258</tns1:internationalPrefix>");
		builder.append("<tns1:number>" + msisdn + "</tns1:number>");
		builder.append("</tns:msisdn>");
		builder.append("<tns:transactionAmount>" + String.valueOf(transactionAmount) + "</tns:transactionAmount>");
		builder.append("<tns:edge_timestamp>" + convertDate(edgeTimestamp) + "</tns:edge_timestamp>");
		builder.append("<tns:edge_dealer_id>" + edgeDealerId + "</tns:edge_dealer_id>");
		builder.append("</tns:TopUpEvent>");
		builder.append("</soapenv:Body>");
		builder.append("</soapenv:Envelope>");
		return builder.toString();
	}
	
	private String convertDate(Date date) {
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(strDateFormat);
			return fmt.format(date);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
