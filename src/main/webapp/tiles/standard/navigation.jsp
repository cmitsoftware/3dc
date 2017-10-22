<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

      <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      
      <sec:authorize access="isAuthenticated()" var="isAuthenticated"/>
      
      <!-- sidebar menu: : style can be found in sidebar.less -->
	      <ul class="sidebar-menu">
	        <li class="header">Menu</li>
	        <c:if test="${not isAuthenticated}">
				<li>
		          <a href="login">
		            <i class="fa fa-user"></i> <span>Login</span>
		          </a>
		        </li>
			</c:if>
			 <c:if test="${isAuthenticated}">
	        <li class="treeview <c:if test='${requestScope[\"javax.servlet.forward.servlet_path\"] == \"/persons\"}'>active</c:if>">
	          <a href="${pageContext.servletContext.contextPath}/persons">
	            <i class="fa fa-table"></i> <span>Palestra</span>
	          </a>
	        </li>
	        <!-- <li class="treeview">
	          <a href="#">
	            <i class="fa fa-dashboard"></i> <span>Dashboard</span>
	          </a>
	        </li> -->
	        <li>
	          <a href="${pageContext.servletContext.contextPath}/mailing">
	            <i class="fa fa-envelope"></i> <span>Mailing</span>
	          </a>
	        </li>
	        <li class="treeview <c:if test='${requestScope[\"javax.servlet.forward.servlet_path\"] == \"/report\"}'>active</c:if>">
	          <a href="${pageContext.servletContext.contextPath}/report">
	            <i class="fa fa-files-o"></i>
	            <span>Report</span>
<!-- 	            <i class="fa fa-angle-left pull-right"></i> -->
	          </a>
	          <%-- <ul class="treeview-menu">
	            <li><a href="${pageContext.servletContext.contextPath}/report"><i class="fa fa-circle-o"></i> Report certificati</a></li>
	            <li><a href="pages/layout/boxed.html"><i class="fa fa-circle-o"></i> Report FASI</a></li>
	          </ul> --%>
	        </li>
	          </c:if>
	      </ul>
    
    </section>
    <!-- /.sidebar -->
  </aside>