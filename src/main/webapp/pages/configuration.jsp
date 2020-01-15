<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<t:template>
    <jsp:attribute name="title">Configuration page</jsp:attribute>
    <jsp:body>
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <th colspan="2">Current settings</th>
            </tr>
            </thead>
            <tr>
                <td>Configuration class:</td>
                <td>${configuration.type}</td>
            </tr>
            <tr>
                <td>Announcement check:</td>
                <td>${configuration.announcementCheckEnabled}</td>
            </tr>
            <tr>
                <td>Bot token:</td>
                <td>${configuration.botToken}</td>
            </tr>
            <tr>
                <td>Master chat id:</td>
                <td>${configuration.masterChatId}</td>
            </tr>
            <tr>
                <td>Default language:</td>
                <td>${configuration.defaultLanguage}</td>
            </tr>
            <tr>
                <td>Webhook url:</td>
                <td>${configuration.webhookUrl}</td>
            </tr>
            <tr>
                <td>Webhook token:</td>
                <td>${configuration.webhookToken}</td>
            </tr>
            <tr>
                <td>Announcement clearing:</td>
                <td>${configuration.cleaningEnabled}</td>
            </tr>
            <tr>
                <td>Certificate:</td>
                <td><a data-toggle="collapse" href="#certificate">Toggle</a>
                    <div style="white-space: pre;" class="collapse" id="certificate">${configuration.certificate}</div>
                </td>
            </tr>
        </table>
        <hr>
        <spring:form method="post" action="configuration">
            <table class="table table-striped table-bordered">
                <thead>
                <tr>
                    <th colspan="2">Change settings</th>
                </tr>
                </thead>
                <tr>
                    <td><label for="configurationType">Configuration class:</label></td>
                    <td><select id="configurationType" name="configurationType">
                        <option value=""></option>
                        <option value="performant">Performant</option>
                        <option value="dynamic">Dynamic</option>
                    </select></td>
                </tr>
                <tr>
                    <td><label for="botToken">Bot token:</label></td>
                    <td><input id="botToken" name="botToken"></td>
                </tr>
                <tr>
                    <td><label for="masterChatId">Master chat id:</label></td>
                    <td><input id="masterChatId" name="masterChatId"></td>
                </tr>
                <tr>
                    <td><label for="defaultLanguage">Default language:</label></td>
                    <td><input id="defaultLanguage" name="defaultLanguage"></td>
                </tr>
                <tr>
                    <td><label for="wekhookUrl">Webhook url:</label></td>
                    <td><input id="wekhookUrl" name="webhookUrl"></td>
                </tr>
                <tr>
                    <td><label for="webhookToken">Webhook token:</label></td>
                    <td><input id="webhookToken" name="webhookToken"></td>
                </tr>
                <tr>
                    <td><label for="announcementCleaning">Announcement clearing:</label></td>
                    <td><select id="announcementCleaning" name="announcementCleaning">
                        <option value=""></option>
                        <option value="true">Enabled</option>
                        <option value="false">Disabled</option>
                    </select></td>
                </tr>
                <tr>
                    <td><label for="certificateInpt">Certificate:</label></td>
                    <td><textarea class="form-control" id="certificateInpt" name="certificate"></textarea></td>
                </tr>
            </table>
            <input class="form-control btn-danger" type="submit">
        </spring:form>
    </jsp:body>
</t:template>