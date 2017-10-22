<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<header class="main-header">
	<!-- Logo -->
	<a href="persons" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
		<span class="logo-mini"><b>3d</b> c</span> <!-- logo for regular state and mobile devices -->
		<span class="logo-lg"><b>3d</b> climbing</span>
	</a>

	<!-- Header Navbar: style can be found in header.less -->
	<nav class="navbar navbar-static-top" role="navigation">
		<!-- Sidebar toggle button-->
		<a href="#" class="sidebar-toggle" data-toggle="offcanvas"
			role="button"> <span class="sr-only">Toggle navigation</span>
		</a>
		<sec:authorize access="hasRole('ROLE_USER')">
			<sec:authentication var="currentUser" property="principal.user" />
			<div class="navbar-custom-menu">
				<ul class="nav navbar-nav">
					<!-- User Account: style can be found in dropdown.less -->
					<li class="dropdown user user-menu">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
							<img src="${pageContext.servletContext.contextPath}/resources/img/climber.png"
								class="user-image" alt="${currentUser.username}"> 
							<span class="hidden-xs">${currentUser.username}</span>
						</a>
						<ul class="dropdown-menu">
							<!-- User image -->
							<li class="user-header" style="height: auto;">
								<p>
									${currentUser.username} <small>Staff 3dc</small>
								</p>
							</li>
							<!-- Menu Footer-->
							<li class="user-footer">
								<div class="pull-right">
									<a href="j_spring_security_logout" class="btn btn-default btn-flat">Logout</a>
								</div>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</sec:authorize>
	</nav>
</header>