<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="title">Subscriptions</jsp:attribute>
    <jsp:body>
        <table class="table table-striped table-bordered">
            <caption>Users and subscriptions</caption>
            <tr>
                <th scope="col">User</th>
                <th scope="col">Subscriptions</th>
            </tr>
            <c:forEach items="${usersPage.content}" var="chatUser">
                <tr>
                    <td <c:if test="${chatUser.status != null}"> class="text-muted" </c:if>>
                            ${chatUser.fullName}, ${chatUser.username} - ${chatUser.language}
                        <c:if test="${chatUser.status != null}">
                            , ${chatUser.status}
                        </c:if>
                    </td>
                    <td>
                        <a data-toggle="collapse" href="#a${chatUser.id}">${chatUser.subscriptions.size()} subscriptions</a>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="border: 0;margin: 0; padding: 0">
                        <div id="a${chatUser.id}" class="collapse" style="padding: 0;margin-bottom: 0">
                            <c:forEach items="${chatUser.subscriptions}" var="subscription">
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