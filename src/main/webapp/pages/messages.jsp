<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<t:template>
    <jsp:attribute name="title">Messages</jsp:attribute>
    <jsp:body>
        <div class="container-fluid panel-group">
            <c:forEach items="${messagePage.content}" var="msg">
                <div class="panel panel-default row">
                    <div class="panel-heading col col-md-auto">
                            ${msg.user.fullName}, ${msg.user.username}
                    </div>
                    <div class="panel-body col-9 col-md col-xs-12">
                            ${msg.text}
                    </div>
                    <div class="panel-footer col col-md-auto">
                        <fmt:formatDate type="both" value="${msg.time.time}" pattern="dd MMM yyyy HH:mm:ss"/>
                    </div>
                </div>
            </c:forEach>
        </div>
        <t:pagenav pageable="${messagePage}"/>
    </jsp:body>
</t:template>