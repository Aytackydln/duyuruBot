<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:template>
    <jsp:attribute name="title">Index</jsp:attribute>
    <jsp:body>
        <main class="container-fluid panel-group col-xs-12 col-sm-9">
            <c:choose>
                <c:when test='${configuration.announcementCheckEnabled}'>
                    <div class="panel panel-success">
                        <div class="panel-heading">Announcements are enabled</div>
                        <div class="panel-body">
                            <a href="disableAnnouncements">Disable Announcements</a>
                        </div>
                        <div class="panel-body">
                            <a href="triggerCheck">Trigger Announcements Check</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="panel panel-danger">
                        <div class="panel-heading">Announcements are disabled</div>
                        <div class="panel-body">
                            <a href="enableAnnouncements">Enable Announcements</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="panel panel-info">
                <div class="panel-body">
                    <a href="updateTopics">Update topics</a>
                </div>
            </div>
            <div class="panel panel-info">
                <div class="panel-body">
                    <a href="updateDictionary">Update dictionary</a>
                </div>
            </div>
            <c:choose>
                <c:when test='${configuration.webhookUrl.equals("")}'>
                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            Web hook url is not set. Cannot enable web hook.
                        </div>
                    </div>
                </c:when>
                <c:when test='${configuration.webhookToken.equals("")}'>
                    <div class="panel panel-warning">
                        <div class="panel-heading">Webhook can be enabled</div>
                        <div class="panel-body">
                            <a href="setWebhook">
                                enable web hook on: ${configuration.webhookUrl}webhook/???????
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="panel panel-success">
                        <div class="panel-heading">
                            Web hook is set on: ${configuration.webhookUrl}webhook/${configuration.webhookToken}
                        </div>
                        <div class="panel-body">
                            <a href="deleteWebhook">Delete Webhook</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>
        ${versionKeeper.versionFull}
    </jsp:body>
</t:template>
