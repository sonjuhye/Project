<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<head>
	<title>Program</title>
	<style>				
		fieldset {
			position: absolute;
			top: 50%;
			left: 50%;
		}
		table {
			border-spacing: 0 10px;
		}
		
		input {
			height:30px;
		}
	</style>
</head>
<body>
	<form action="calculate" method="get" name="frm">
		<fieldset>
			<h3>[입력]</h3>
			<table>
				<tr>
					<td><b>URL</b></td>
					<td><input type="text" name="url" /></td>					
				</tr>
				<tr>
					<td><b>Type</b></td>
					<td>
						<select name="type" size="1" style="width:100%; height:30px;">
							<option value="X">HTML 태그 제외</option>
							<option value="Y">Text 전체</option>
						</select>
					</td>
				</tr>
				<tr>
					<td><b>출력 단위 묶음</b></td>
					<td><input type="text" name="range" /></td>					
				</tr>
			</table>
			<input type="submit" value="출력" style="float: right; width:50px;">
			<br />
			<h3>[출력]</h3>
			<table>
				<tr>
					<td><b>몫 : </b></td>
					<td>${quotient}</td>					
				</tr>
				<tr>
					<td><b>나머지 : </b></td>
					<td>${remainder}</td>					
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>
