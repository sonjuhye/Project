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
		String strType = req.getParameter("type"); //나누기 범위
		int nRange = Integer.parseInt(req.getParameter("range")); //출력 단위 묶음
		StringBuilder result = new StringBuilder(); //결과 문자열
		
		//HTML 소스 추출
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
        		
        		//a. 공백 제거
        		tmpResult = tmpResult.replace(" ", "");
                
                //b. Type에 따른 text 범위 지정
                if(strType.equals("X")) { //HTML 태그 제외를 선택한 경우 
                	tmpResult = tmpResult.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
                	tmpResult = tmpResult.replaceAll("<[^>]*>", "");
                }
                
                //c. 영어 및 숫자만 지정                
                tmpResult = tmpResult.replaceAll("[^0-9a-zA-Z]", ""); //영어 및 숫자를 제외한 문자 제거
                
                //d. 영어 숫자 오름차순 정렬                
                String engTemp = tmpResult.replaceAll("[^a-zA-Z]", ""); //영어만 추출
                String numTemp = tmpResult.replaceAll("[^0-9]", ""); //숫자만 추출
                
                //e. 오름차순 정렬
                String engTmpArr[] = engTemp.split("");
                String numTmpArr[] = numTemp.split("");                      
                
                Arrays.sort(engTmpArr, String.CASE_INSENSITIVE_ORDER); //영어의 경우, 대소문자 구분없이 정렬
                Arrays.sort(numTmpArr);
                
                //f. 영어 숫자를 교차하여 Mix
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
                
                //g. 출력 묶음 단위 처리
                String strQuotient = ""; //몫
                String strRemainder = ""; //나머지
                
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
                model.addAttribute("quotient", "ERROR 발생");
                model.addAttribute("remainder", "ERROR 발생");
            }
        } else {
        	model.addAttribute("quotient", "HTML 소스코드가 존재하지 않음");
            model.addAttribute("remainder", "");
        }                
                
		return "home";
	}

}
