package com.summer.evento;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
	public static String parseLocation(String recognizedText) {
		Pattern p_loc = Pattern.compile("[0-9]{4}\\s*(BBB|EECS|DOW|FXB)|Dude Connector");
		Matcher m_loc = p_loc.matcher(recognizedText);
		if (m_loc.find())
			return m_loc.group();
		else
			return "";
	}

	public static String parseDate(String recognizedText) {
		Pattern p_date = Pattern.compile("/?(january|jan|february|feb|march|mar|april|apr|may|june|july|august|aug|september|sep"
				+ "|october|oct|november|nov|december|dec)\\s*[0-9]{1,2}/?");
		Matcher m_date = p_date.matcher(recognizedText.toLowerCase());
		if (m_date.find()) {
			return m_date.group();
		}
		else
			return "";
	}
	/*
	public static int parseMonth (String time) {
		String lower_case_time = time.toLowerCase();
		Pattern p_mon = Pattern.compile("/?(january|jan|february|feb|march|mar|april|apr|may|june|july|august|aug|september|sep"
				+ "|october|oct|november|nov|december|dec)/?");
		Matcher m_mon = p_mon.matcher(lower_case_time);
		if (m_mon.find()) {
			switch(m_mon.group()) {
			case "janur"
			}
		}
	}
	*/
	public static String[] parseTime(String recognizedText) {
		String[] times = new String[2];
		Pattern p_time = Pattern.compile("[0-9]{1,2}:[0-9]{0,2}");
		Matcher m_time = p_time.matcher(recognizedText);
		int i = 0;
		while (m_time.find() && i < 2) {
			times[i] = m_time.group();
			++i;
		}
		Pattern p_ampm = Pattern.compile("[0-9]{1}+\\s*[aApP]+[mM]+");
		Matcher m_ampm = p_ampm.matcher(recognizedText);
		if (m_ampm.find()) {
			if (m_ampm.group().contains("a") || m_ampm.group().contains("A")) {	// TODO start & end time may have different am/pm
				times[0] += " AM";
				times[1] += " AM";
			}
			else if (m_ampm.group().contains("p") || m_ampm.group().contains("P")) {
				times[0] += " PM";
				times[1] += " PM";
			}
		}

		//Pattern p_ampm = Pattern.compile("")()
		return times;
	}
}
