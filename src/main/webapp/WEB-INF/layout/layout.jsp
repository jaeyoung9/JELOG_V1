<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Jelog Page</title>
        <link rel="shortcut icon" href="/static/favicon/favicon.ico">
        <link rel="stylesheet" type="text/css" href="/static/css/headerStyle.css"/>
        <script type="application/javascript" src="/static/js/headerJs.js"></script>
    </head>
    <body>
        <tiles:insertAttribute name="header" />
            <div id="content">
                <tiles:insertAttribute name="body" />
            </div>
        <tiles:insertAttribute name="footer" />
    </body>
</html>