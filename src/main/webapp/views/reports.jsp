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
										id="current-year-report">
										Iscritti dell'anno corrente
									</button>
              						<button type="button" class="btn btn-block btn-default"
										id="certificate-report">
										Iscritti dell'anno corrente senza certificato
									</button>
								</div>
	            			</div>
    					</div>
    					<div class="box box-warning">
	            			<div class="box-body">
              					<div class="col-xs-12 col-sm-12 col-md-12">
              						<button type="button" class="btn btn-block btn-default"
										id="general-report">
										Report generale (tutti i climber del database)
									</button>
								</div>
	            			</div>
    					</div>
	    			</div>        				
	    		</div>
				<div class="row">
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-4 col-xl-4">
						<div id="import-box" class="box box-primary">
							<div class="box-header with-border">
								<h3 class="box-title">Importa iscritti</h3>
							</div>
							<!-- /.box-header -->
							<!-- form start -->
							<form role="form" action="${pageContext.servletContext.contextPath}/report?method=importPersons" method="post" enctype="multipart/form-data">
								<!-- <input type="hidden" name="method" value="importPersons"/> -->
								<div class="box-body">
									<div class="form-group">
										<label for="file">File excel</label> 
										<input type="file" name="file" id="file">
										<p class="help-block">
											Attenzione: il formato del file (ordine delle colonne e tipo di dati) deve essere lo stesso del <i>Report generale</i>
										</p>
									</div>
								</div>
								<!-- /.box-body -->
								<div class="box-footer">
									<button type="submit" class="btn btn-primary">Importa</button>
								</div>
							</form>
						</div>
						<div id="import-result-box" style="display:none;" class="box box-primary">
							<div class="box-header with-border">
								<h3 class="box-title">Risultato import</h3>
							</div>
							<!-- /.box-header -->
							<div class="box-body">
								Record totali excel: <strong>${total}</strong>
								<br>
								Nuovi iscritti inseriti: <strong>${inserted}</strong>
								<br>
								Iscritti aggiornati: <strong>${updated}</strong>
          						<c:if test="${not empty errors}">
          							<div class="callout callout-danger">
                						<h4>Errori:</h4>
                						<p>
                							<c:forEach var="e" items="${errors}">
											  <strong>Riga: <c:out value="${e.key}"/></strong>: <c:out value="${e.value}"/>
											  <br/>
											</c:forEach>
                						</p>
              						</div>
          						</c:if>
          						<p>
									<button id="show-import" type="button" class="btn bg-olive btn-flat margin">Ok</button>          						
          						</p>
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
				$("#current-year-report").on("click", function(){
					var url = "${pageContext.servletContext.contextPath}/report?method=currentYear";
					var win = window.open(url, '_blank');
					win.focus();
				});
				$("#show-import").on("click", function(){
					$("#import-result-box").hide();
					$("#import-box").show();
				});
			});
		</script>
		<c:if test="${importDone}">
			<script>
				$(document).ready(function(){
					$("#import-result-box").show();
					$("#import-box").hide();
				});
			
			</script>
		</c:if>
	</tiles:putAttribute>
	
</tiles:insertDefinition>

