<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<spring:url value="/resources/js/jquery-3.3.1.min.js" var="jquery"/>
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs"/>
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss"/>
<spring:url value="/resources/css/bootstrap-grid.min.css" var="bootstrapGridCss"/>
<spring:url value="/resources/css/main.css" var="mainCss"/>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#2f4d6d">
    <link rel="icon" href="resources/icon.png" type="image/png" sizes="192x192">
    <link href="${bootstrapCss}" rel="stylesheet"/>
    <link href="${bootstrapGridCss}" rel="stylesheet"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <title>
        <jsp:invoke fragment="title"/>
    </title>
    <jsp:invoke fragment="head"/>
</head>
<body class="container-fluid">
<nav class="col-xs-12 col-sm-3 navbar navbar-default container-fluid">
    <ul class="nav navbar-nav">
        <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/'}">active </c:if>col-xs-6 col-12"><a href="<%= request.getContextPath() %>/">Home</a></li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/statistics'}">active </c:if>col-xs-6 col-12"><a href="statistics">Statistics</a></li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/announcements'}">active </c:if>col-xs-6 col-12"><a href="announcements">Announcements</a></li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/messages'}">active </c:if>col-xs-6 col-12"><a href="messages">Messages</a></li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/subscriptions'}">active </c:if>col-xs-6 col-12"><a href="subscriptions">Subscriptions</a></li>
        <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/configuration'}">active </c:if>col-xs-6 col-12"><a href="configuration">Configuration</a></li>
    </ul>
</nav>
<div class="col-xs-12 col-sm-9 panel-group">
    <jsp:useBean id="messageBox" scope="session" class="com.noname.duyuru.app.mvc.message.MessageBox"/>
    <c:forEach items="${messageBox.messages}" var="message">
        <div class="alert ${message.htmlClass} alert-dismissible">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            <strong>${message.header}</strong><br>${message.message}
        </div>
    </c:forEach>
</div>
<jsp:doBody/>
<script src="${jquery}"></script>
<script src="${bootstrapJs}"></script>
</body>
</html>