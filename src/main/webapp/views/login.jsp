<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<tiles:insertDefinition name="3dc.home">

	<tiles:putAttribute name="title" value="Login" />
	
	<tiles:putAttribute name="header" value="/tiles/standard/header.jsp"/>
<%-- 	<tiles:putAttribute name="navigation" value="/tiles/standard/navigation.jsp"/> --%>
	<tiles:putAttribute name="navigation" value="/tiles/standard/navigation.jsp"/>

	<tiles:putAttribute name="content">	
		
		<div class="login-box">
		  	<div class="login-logo">
	  			<a href="http://3dclimbing.it/" target="_blank"><b>3d</b> climbing</a>
		  	</div>
		  	<!-- /.login-logo -->
		  	<div class="login-box-body">
		    	<p class="login-box-msg">Accedi</p>
		
		    	<form action="j_spring_security_check" method="post">
		      		<div class="form-group has-feedback">
		        		<input type="text" class="form-control" name="username" placeholder="Username">
		        		<span class="fa fa-user form-control-feedback"></span>
		      		</div>
		      		<div class="form-group has-feedback">
				        <input type="password" name="password" class="form-control" placeholder="Password">
				        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
		      		</div>
		      		<div class="row">
		        		<div class="col-xs-8">
				          	<div class="form-group">
								<input type="checkbox"  name="_spring_security_remember_me" id="remember-me">
								<label for="remember"> Ricordami</label>
							</div>
				        </div>
			        	<!-- /.col -->
		        		<div class="col-xs-4">
			          		<button type="submit" class="btn btn-primary btn-block btn-flat">Accedi</button>
		        		</div>
		        		<!-- /.col -->
			      	</div>
			    </form>
			</div>
			  <!-- /.login-box-body -->
		</div>
		<!-- /.login-box -->
	</tiles:putAttribute>
  	<!-- /.content-wrapper -->

  	<tiles:putAttribute name="footer" value="/tiles/standard/footer.jsp"/>

	<tiles:putAttribute name="lateLoadScripts" value="/tiles/standard/lateLoadScripts.jsp"/>

	<tiles:putAttribute name="pageScripts">
		<script type="text/javascript">
			$(document).ready(function(){
				
			});
		</script>
	</tiles:putAttribute>
	
</tiles:insertDefinition>

