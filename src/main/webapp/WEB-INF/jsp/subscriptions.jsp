<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="title">Subscriptions</jsp:attribute>
    <jsp:body>
        <table class="table table-striped table-bordered">
            <c:forEach items="${usersPage.content}" var="user">
                <tr>
                    <td>${user.fullName}, ${user.username} - ${user.language}</td>
                    <td>
                        <a data-toggle="collapse" href="#a${user.id}">${user.subscriptions.size()} subscriptions</a>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="border: 0;margin: 0; padding: 0">
                        <div id="a${user.id}" class="collapse" style="padding: 0;margin-bottom: 0">
                            <c:forEach items="${user.subscriptions}" var="subscription">
                                <div class="card">
                                    <div class="card-body">${subscription.topic.id}</div>
                                </div>
                            </c:forEach>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <t:pagenav pageable="${usersPage}"/>
    </jsp:body>
</t:template>