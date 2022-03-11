<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@attribute name="title" fragment="true" %>
<%@attribute name="head" fragment="true" %>
<spring:url value="/" var="index"/>
<spring:url value="/resources/icon.png" var="favicon"/>
<spring:url value="/resources/css/main.css" var="mainCss"/>
<spring:url value="https://bootswatch.com/4/darkly/bootstrap.min.css" var="bootstrapCss"/>
<spring:url value="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap-grid.min.css"
            var="bootstrapGridCss"/>
<spring:url value="https://code.jquery.com/jquery-3.4.1.slim.min.js" var="jquery"/>
<spring:url value="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" var="bootstrapJs"/>
<spring:url value="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" var="popperJs"/>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#2f4d6d">
    <link rel="icon" href="${favicon}" type="image/png" sizes="192x192">
    <link href="${bootstrapCss}" rel="stylesheet"/>
    <link href="${bootstrapGridCss}" rel="stylesheet"/>
    <link href="${mainCss}" rel="stylesheet"/>
    <title>
        <jsp:invoke fragment="title"/>
    </title>
    <jsp:invoke fragment="head"/>
</head>
<body class="container-fluid">
<div class="row">
    <nav class="col-12 col-md-3 navbar navbar-dark bg-primary container-fluid">
        <ul class="nav navbar-nav">
            <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/'}">active </c:if>nav-item">
                <a class="nav-link" href="${index}">Home</a>
            </li>
            <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/statistics'}">active </c:if>nav-item">
                <a class="nav-link" href="statistics">Statistics</a>
            </li>
            <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/announcements'}">active </c:if>nav-item">
                <a class="nav-link" href="announcements">Announcements</a>
            </li>
            <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/messages'}">active </c:if>nav-item">
                <a class="nav-link" href="messages">Messages</a>
            </li>
            <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/subscriptions'}">active </c:if>nav-item">
                <a class="nav-link" href="subscriptions">Subscriptions</a>
            </li>
            <li class="<c:if test="${requestScope['javax.servlet.forward.servlet_path']=='/configuration'}">active </c:if>nav-item">
                <a class="nav-link" href="configuration">Configuration</a>
            </li>
        </ul>
    </nav>
    <div class="col-12 col-md-9">
        <jsp:useBean id="messageBox" scope="session" class="aytackydln.duyuru.mvc.message.MessageBox"/>
        <c:forEach items="${messageBox.messages}" var="messageEntity">
            <div class="alert ${messageEntity.htmlClass} alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <strong>${messageEntity.header}</strong><br>${messageEntity.messageEntity}
            </div>
        </c:forEach>
        <jsp:doBody/>
    </div>
</div>
<script src="${jquery}"></script>
<script src="${bootstrapJs}"></script>
<script src="${popperJs}"></script>
</body>
</html>