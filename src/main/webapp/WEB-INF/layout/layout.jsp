<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Jelog Page</title>

        <%-- meta Setting --%>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <meta name="description" content="" />
        <meta name="author" content="" />

        <link rel="shortcut icon" href="/static/favicon/favicon.ico">

        <link href="https://fonts.googleapis.com/css?family=Saira+Extra+Condensed:500,700" rel="stylesheet" type="text/css" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>

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