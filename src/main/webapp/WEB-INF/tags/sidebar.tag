<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="isLogin" type="java.lang.Boolean" %>
<%@ attribute name="isAdmin" type="java.lang.Boolean" %>

<%@ tag import="java.util.List" %>
<%
    List<String> departments = (List<String>) request.getAttribute("departments");
%>

<div class="sidebar">
    <div class="filter">
        <span class="title">필터</span>
        <% for(String deparment : departments) { %>
            <div>
                <input type="checkbox" id=<%=deparment%> >
                <label for=<%=deparment%> ><span><%= deparment %></span></label>
            </div>
        <% } %>
    </div>
    <div>
        <c:if test="${isAdmin}">
            <div class="userFeatureContainer">
                <a><span>회원 관리</span></a>
                <a><span>공통 코드 관리</span></a>
                <a><span>전체 휴가 관리</span></a>
            </div>
        </c:if>
        <div class="userFeatureContainer">
            <a><span>내 정보</span></a>
            <a><span>휴가 내역</span></a>
            <a><span>휴가 등록</span></a>
            <c:if test="${!isLogin}">
                <div class="blurBox">
                    <a class="loginButton" href="/login">로그인 / 회원가입</a>
                </div>
            </c:if>
        </div>
    </div>
</div>