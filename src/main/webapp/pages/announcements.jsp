<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="title">Announcements</jsp:attribute>
    <jsp:body>
        <div class="container-fluid">
            <c:forEach items="${announcementList.content}" var="ann">
                <div class="card">
                    <div class="card-body row">
                        <span class="badge badge-primary col-auto">
                                ${ann.topic}
                        </span>
                        <span class="col">
                            <a<c:if test="${!ann.link.equals(\"\")}"> href="${ann.url}"</c:if>>${ann.title}</a>
                        </span>
                        <span class="badge badge-info col-auto">
                                ${ann.date}
                        </span>
                    </div>
                </div>
            </c:forEach>
        </div>
        <t:pagenav pageable="${announcementList}"/>
    </jsp:body>
</t:template>