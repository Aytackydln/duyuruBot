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
                        <span class="bg-primary col-md-auto col">
                                ${ann.topic}
                        </span>
                        <span class="bg-info col-md-auto col">
                                ${ann.date}
                        </span>
                        <span class="col-md col-12">
                            <a
                                    <c:if test="${!ann.link.equals(\"\")}"> href="${ann.url}" target="_blank"</c:if>>${ann.title}</a>
                        </span>
                    </div>
                </div>
            </c:forEach>
        </div>
        <t:pagenav pageable="${announcementList}"/>
    </jsp:body>
</t:template>