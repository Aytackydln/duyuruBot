<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="title">Announcements</jsp:attribute>
    <jsp:body>
        <div class="container-fluid panel-group">
            <c:forEach items="${announcementList.content}" var="ann">
                <div class="panel panel-default row">
                    <div class="panel-heading col col-md-auto">
                            ${ann.topic}
                    </div>
                    <div class="panel-body col-9 col-md col-xs-12">
                        <a<c:if test="${!ann.link.equals(\"\")}"> href="${ann.url}"</c:if>>${ann.title}</a>
                    </div>
                    <div class="panel-footer col col-md-auto">
                            ${ann.date}
                    </div>
                </div>
            </c:forEach>
        </div>
        <t:pagenav pageable="${announcementList}"/>
    </jsp:body>
</t:template>