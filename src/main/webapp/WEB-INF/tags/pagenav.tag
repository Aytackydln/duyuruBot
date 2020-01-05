<%@tag description="Page Navigator" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="pageable" type="org.springframework.data.domain.PageImpl" %>
<div class="text-center">
    <ul class="pagination justify-content-center">
        <c:choose>
            <c:when test="${pageable.hasPrevious()}">
                <li class="page-item"><a href="${pageContext.request.contextPath}${requestScope['javax.servlet.forward.servlet_path']}?page=${pageable.number-1}">Previous</a></li>
            </c:when>
            <c:otherwise>
                <li class="disabled page-item"><a>Previous</a></li>
            </c:otherwise>
        </c:choose>
        <li class="disabled page-item"><a>${pageable.number}</a></li>
        <c:choose>
            <c:when test="${pageable.hasNext()}">
                <li class="page-item page-item"><a href="${pageContext.request.contextPath}${requestScope['javax.servlet.forward.servlet_path']}?page=${pageable.number+1}">Next</a></li>
            </c:when>
            <c:otherwise>
                <li class="disabled"><a>Next</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
