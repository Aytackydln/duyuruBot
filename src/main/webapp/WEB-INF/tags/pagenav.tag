<%@tag description="Page Navigator" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="pageable" type="org.springframework.data.domain.PageImpl" %>
<div class="text-center">
    <ul class="pagination justify-content-center center">
        <c:choose>
        <c:when test="${pageable.hasPrevious()}">
        <li class="page-item">
            </c:when>
            <c:otherwise>
        <li class="disabled page-item">
            </c:otherwise>
            </c:choose>
            <a class="page-link"
               href="${pageContext.request.contextPath}${requestScope['javax.servlet.forward.servlet_path']}?page=${pageable.number-1}">Previous</a>
        </li>
        <li class="disabled page-item">
            <a class="page-link">${pageable.number}</a>
        </li>
        <c:choose>
        <c:when test="${pageable.hasNext()}">
        <li class="page-item">
            </c:when>
            <c:otherwise>
        <li class="disabled">
            </c:otherwise>
            </c:choose>
            <a class="page-link"
               href="${pageContext.request.contextPath}${requestScope['javax.servlet.forward.servlet_path']}?page=${pageable.number+1}">Next</a>
        </li>
    </ul>
</div>
