<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertDefinition name="3dc.home">

	<tiles:putAttribute name="title" value="${label}" />
	
	<tiles:putAttribute name="header" value="/tiles/standard/header.jsp"/>
	<tiles:putAttribute name="navigation" value="/tiles/standard/navigation.jsp"/>

	<tiles:putAttribute name="content">	
	
		<!-- Content Wrapper. Contains page content -->
	  	<div class="content-wrapper">
	  	
	    	<!-- Content Header (Page header) -->
	    	<section class="content-header">
		    </section>

			<!-- Main content -->
   			<section class="content">
   				<div class="row">
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-4 col-xl-4">
            			<div class="box box-success">
            				<div class="box-body">
              					<div class="col-xs-12 col-sm-12 col-md-12">
              						<button type="button" class="btn btn-block btn-default"
										id="certificate-report">
										Iscritti senza certificato
									</button>
								</div>
	            			</div>
    					</div>
    					<div class="box box-warning">
	            			<div class="box-body">
              					<div class="col-xs-12 col-sm-12 col-md-12">
              						<button type="button" class="btn btn-block btn-default"
										id="general-report">
										Tutti gli iscritti
									</button>
								</div>
	            			</div>
    					</div>
	    			</div>        				
	    		</div>
			</section>
   			<!-- /.content -->
		</div>
 
	</tiles:putAttribute>
  	<!-- /.content-wrapper -->

  	<tiles:putAttribute name="footer" value="/tiles/standard/footer.jsp"/>

	<tiles:putAttribute name="lateLoadScripts" value="/tiles/standard/lateLoadScripts.jsp"/>

	<tiles:putAttribute name="pageScripts">
		<script type="text/javascript">
			
			$(document).ready(function(){
				$("#general-report").on("click", function(){
					var url = "${pageContext.servletContext.contextPath}/report?method=general";
					var win = window.open(url, '_blank');
					win.focus();
				});
				$("#certificate-report").on("click", function(){
					var url = "${pageContext.servletContext.contextPath}/report?method=certificate";
					var win = window.open(url, '_blank');
					win.focus();
				});
			});
		</script>
	</tiles:putAttribute>
	
</tiles:insertDefinition>

