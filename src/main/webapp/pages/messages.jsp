<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<t:template>
    <jsp:attribute name="title">Messages</jsp:attribute>
    <jsp:body>
        <div class="container-fluid panel-group">
            <c:forEach items="${messagePage.content}" var="msg">
                <div class="card row">
                    <div class="card-body row">
                        <span class="badge badge-success col-auto">
                                ${msg.user.fullName}, ${msg.user.username}
                        </span>
                        <span class="col">
                                ${msg.text}
                        </span>
                        <span class="badge badge-info col-auto">
                            <fmt:formatDate type="both" value="${msg.time.time}" pattern="dd MMM yyyy HH:mm:ss"/>
                        </span>
                    </div>
                </div>
            </c:forEach>
        </div>
        <t:pagenav pageable="${messagePage}"/>
    </jsp:body>
</t:template>