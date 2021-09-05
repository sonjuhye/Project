package com.work.project.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value="/calculate", method=RequestMethod.GET)	
	public String main(HttpServletRequest req, Model model) {
				
		String strURL = req.getParameter("url"); //URL
		String strType = req.getParameter("type"); //������ ����
		int nRange = Integer.parseInt(req.getParameter("range")); //��� ���� ����
		StringBuilder result = new StringBuilder(); //��� ���ڿ�
		
		//HTML �ҽ� ����
		String pageContents = "";
        StringBuilder contents = new StringBuilder();
        
                
        try{ 
            URL url = new URL(strURL);
            URLConnection conn = (URLConnection)url.openConnection();
            InputStreamReader reader = new InputStreamReader (conn.getInputStream(), "utf-8"); 
            BufferedReader buff = new BufferedReader(reader);
 
            while((pageContents = buff.readLine())!=null){
                contents.append(pageContents);
            } 
            buff.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
                
        if(contents.length() > 0) {
        	try {                
        		String tmpResult = contents.toString();
        		
        		//a. ���� ����
        		tmpResult = tmpResult.replace(" ", "");
                
                //b. Type�� ���� text ���� ����
                if(strType.equals("X")) { //HTML �±� ���ܸ� ������ ��� 
                	tmpResult = tmpResult.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                	tmpResult = tmpResult.replaceAll("<[^>]*>", "");
                }
                
                //c. ���� �� ���ڸ� ����                
                tmpResult = tmpResult.replaceAll("[^0-9a-zA-Z]", ""); //���� �� ���ڸ� ������ ���� ����
                
                //d. ���� ���� �������� ����                
                String engTemp = tmpResult.replaceAll("[^a-zA-Z]", ""); //��� ����
                String numTemp = tmpResult.replaceAll("[^0-9]", ""); //���ڸ� ����
                
                //e. �������� ����
                String engTmpArr[] = engTemp.split("");
                String numTmpArr[] = numTemp.split("");                      
                
                Arrays.sort(engTmpArr, String.CASE_INSENSITIVE_ORDER); //������ ���, ��ҹ��� ���о��� ����
                Arrays.sort(numTmpArr);
                
                //f. ���� ���ڸ� �����Ͽ� Mix
                int engCnt = 0;
                int numCnt = 0;               
                
                do {
                	if(engCnt < engTmpArr.length) {
                		result.append(engTmpArr[engCnt]);
                		engCnt++;
                	}
                	
                	if(numCnt < numTmpArr.length) {
                		result.append(numTmpArr[numCnt]);
                		numCnt++;
                	}
                } while(engCnt < engTmpArr.length || numCnt < numTmpArr.length);               
                
                //g. ��� ���� ���� ó��
                String strQuotient = ""; //��
                String strRemainder = ""; //������
                
                if(nRange >= result.length()) {
                	strQuotient = result.toString();
                } else {
                	strQuotient = result.substring(0, nRange);
                	strRemainder = result.substring(nRange);
                }
                
                model.addAttribute("quotient", strQuotient);
                model.addAttribute("remainder", strRemainder);            
            } catch(Exception e) {
                e.printStackTrace();
                model.addAttribute("quotient", "ERROR �߻�");
                model.addAttribute("remainder", "ERROR �߻�");
            }
        } else {
        	model.addAttribute("quotient", "HTML �ҽ��ڵ尡 �������� ����");
            model.addAttribute("remainder", "");
        }                
                
		return "home";
	}

}
