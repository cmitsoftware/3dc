<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<tiles:insertDefinition name="3dc.home">

	<tiles:putAttribute name="title" value="Dettaglio persona" />
	
	<tiles:putAttribute name="header" value="/tiles/standard/header.jsp"/>
	<tiles:putAttribute name="navigation" value="/tiles/standard/navigation.jsp"/>

	<tiles:putAttribute name="content">	
	
		<!-- Content Wrapper. Contains page content -->
	  	<div class="content-wrapper">
	    	<!-- Content Header (Page header) -->
	    	<section class="content-header">
		      <!-- <h1>
		        Palestra
		        <small></small>
		      </h1> -->
		      <!-- <ol class="breadcrumb">
		        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
		        <li class="active">Palestra</li>
		      </ol> -->
		    </section>

			<!-- Main content -->
   			<section class="content">
   			
   				<div class="row" style="margin-bottom:10px;">
		    		<div class="col-xs-12 col-sm-12 col-md-12">
		    			<div class="btn-group">
							<button type="button" class="btn btn-primary"
								onclick="$('#person-form').submit()">
								Salva
							</button>
							<button type="button" class="btn btn-primary"
								onclick="location.href='${pageContext.servletContext.contextPath}/persons?reload=true'">
								Indietro
							</button>
						</div>
			   		</div>
					<!-- <div class="col-xs-0 col-sm-8 col-md-10"></div> -->
				</div>
   			
       			<form id="person-form" method="post"
        			action="${pageContext.servletContext.contextPath}/persons?method=save">
   					<div class="row">
	        			<div class="col-xs-12 col-md-12">
	            			<div class="box box-success">
	            				<div class="box-header with-border">
	              					<h4 class="box-title">
	              						Climber n. <input id="number" name="number" type="text" class=""
	              							style="width: 50px;text-align: right;margin-left: 7px;" 
	              							value="${person.number}">
	              					</h4>
	              					<c:if test="${empty person.number}">
		              					<h4>Associa numero automaticamente
											<input id="generateNumber" name="generateNumber" type="checkbox" style="margin-left: 5px;">
										</h4>
									</c:if>
		            			</div>
		            			<div class="box-body">
		            				<input name="id" type="hidden" value="${person.id}">
	              					<div class="form-group col-xs-12 col-md-4">
					               		<label>Nome</label>
					                 	<input id="name" name="name" type="text" 
					                 		class="form-control pull-right active"
					                 		value="${person.name}">
						        	</div>
						        	<div class="form-group col-xs-12 col-md-4">
					               		<label>Cognome</label>
					                 	<input id="surname" name="surname" type="text" 
					                 		class="form-control pull-right active"
					                 		value="${person.surname}">
						        	</div>
						        	<div class="form-group col-xs-12 col-md-4">
					               		<label>Telefono</label>
					                 	<input id="phone" name="phone" type="text" 
					                 		class="form-control pull-right active"
					                 		value="${person.phone}">
						        	</div>
	              					<div class="form-group col-xs-12 col-md-3">
					               		<label>Data iscrizione 3dc annuale</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="registrationDate" name="registrationDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.registrationDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div>
						        	<div class="form-group col-xs-12 col-md-3">
					               		<label>Data certificato medico</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="certificationDate" name="certificationDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.certificationDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div>
						        	<div class="form-group col-xs-12 col-md-3">
					               		<label>Data abbonamento</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="subscriptionDate" name="subscriptionDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.subscriptionDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div>
						        	<div class="form-group col-xs-12 col-md-3">
					               		<label>Data affiliazione FASI</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="affiliationDate" name="affiliationDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.affiliationDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
					               		<!-- /.input group -->
						        	</div>
						        	<%-- <div class="form-group col-xs-12 col-md-6">
					               		<label>Data prova gratuita</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="freeEntryDate" name="freeEntryDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.freeEntryDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div> --%>
	    						</div>
	    					</div>
	    				</div>
	    			</div>
	    			<div class="row">
	    				<div class="col-xs-12 col-md-12">
	    					<div class="box box-warning">
		            			<div class="box-body">
	              					<div class="form-group col-xs-12 col-md-4">
					               		<label>Codice fiscale</label>
					                 	<input id="cf" name="cf" type="text" 
					                 		class="form-control pull-right active"
					                 		value="${person.cf}">
						        	</div>
	              					<div class="form-group col-xs-12 col-md-4">
					               		<label>Email</label>
					                 	<input id="email" name="email" type="text" 
					                 		class="form-control pull-right active"
					                 		value="${person.email}">
						        	</div>
						        	<div class="form-group col-xs-12 col-md-4">
					               		<label>Paese</label>
					                 	<input id="city" name="city" type="text"
					                 		class="form-control pull-right active"
					                 		value="${person.city}">
						        	</div>
						        	<div class="form-group col-xs-12 col-md-12">
					               		<label>Indirizzo</label>
					                 	<input id="address" name="address" 
					                 		type="text" class="form-control pull-right active"
					                 		value="${person.address}">
						        	</div>
	              					<div class="form-group col-xs-12 col-md-3">
					               		<label>Data di nascita</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="birthDate" name="birthDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.birthDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
					               		<!-- /.input group -->
						        	</div>
						        	
						        	<div class="form-group col-xs-12 col-md-3">
					               		<label>Data richiesta prima iscrizione</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="firstRegistrationDate" name="firstRegistrationDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.firstRegistrationDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div>
						        	<div class="form-group col-xs-12 col-md-3">
					               		<label>Data di approvazione</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="firstRegistrationDate" name="firstRegistrationDate" 
						                 		type="text" class="form-control pull-right active 3dc-date"
						                 		value="<fmt:formatDate value='${person.firstRegistrationDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div>
						        	<div class="form-group col-xs-12 col-md-3">
					               		<label>Data creazione anagrafica</label>
						               	<div class="input-group">
						                	<div class="input-group-addon">
						                   		<i class="fa fa-calendar"></i>
						                 	</div>
						                 	<input id="creationDate" name="creationDate" 
						                 		type="text" class="form-control pull-right active 3dc-date" readonly="readonly"
						                 		value="<fmt:formatDate value='${person.creationDate}' pattern='dd/MM/yyyy'/>">
					               		</div>
						        	</div>
						        	<div class="form-group col-xs-12 col-md-12 checkbox">
					                  	<label>
					                    	<input id="mailing" name="mailing" type="checkbox"
					                    		<c:if test="${person.mailing eq true or empty person.id}">checked="checked"</c:if>>
					                    		 Riceve le email
					                  	</label>
					                </div>
	    						</div>
	    					</div>
		    			</div>
		    		</form>
	    		</div>
	    		<div class="row">
		    		<div class="col-xs-12 col-sm-12 col-md-12">
		    			<div class="btn-group">
							<button type="button" class="btn btn-primary"
								onclick="$('#person-form').submit()">
								Salva
							</button>
							<button type="button" class="btn btn-primary"
								onclick="location.href='${pageContext.servletContext.contextPath}/persons?reload=true'">
								Indietro
							</button>
						</div>
			   		</div>
					<!-- <div class="col-xs-0 col-sm-8 col-md-10"></div> -->
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
				$(".3dc-date").datepicker({
					language: "it",
					autoclose: true,
					format: "dd/mm/yyyy"
				});
			});
		</script>
	</tiles:putAttribute>
	
</tiles:insertDefinition>

