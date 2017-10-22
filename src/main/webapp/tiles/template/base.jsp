<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html>
	<head>
		<title>
			<tiles:insertAttribute name="title" ignore="true"/>
		</title>
		<tiles:insertAttribute name="head"/>
	</head>
	<body class="hold-transition skin-blue sidebar-collapse sidebar-mini">
		<div class="wrapper">
			<tiles:insertAttribute name="header"/>
			<tiles:insertAttribute name="navigation"/>
			<tiles:insertAttribute name="content" />
			<tiles:insertAttribute name="footer" />
			<tiles:insertAttribute name="lateLoadScripts" />
			<tiles:insertAttribute name="pageScripts" />
		</div>
	</body>
</html>