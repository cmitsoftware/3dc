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
					<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
            			<div class="box box-success">
            				<c:choose>
	            				<c:when test="${empty param.result or param.result eq ''}">
		            				<div class="box-body">
		            					<form id="mailing-form" role="form" method="post" enctype="multipart/form-data" 
		            						action="${pageContext.servletContext.contextPath}/mailing?method=send">
		            						<div class="form-group">
							                  	<label>Oggetto</label>
							                  	<input id="mailing-subject" type="text" class="form-control" name="subject"/>
							                </div>
											<div class="form-group">
												<label>Corpo della mail</label>
								                <textarea id="mailing-text" name="text"  class="textarea" style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;"></textarea>
								            </div>
											<div class="form-group">
							                  	<label>Destinatari (separati da virgola)</label>
							                  	<input id="mailing-recipients" type="text" class="form-control" name="recipients"/>
							                </div>
							                <div class="form-group">
							                	<label for="mailing-attachment-1">Allegati</label>
							                  	<input id="mailing-attachment-1" name="mailing-attachment-1" type="file">
							                  	<input id="mailing-attachment-2" name="mailing-attachment-2" type="file">
							                  	<input id="mailing-attachment-3" name="mailing-attachment-3" type="file">
							                </div>
							                
							                <input id="mailing-type" type="hidden" value="recipients" name="type"/>
		            					</form>
			            			</div>
			            			<div class="box-footer">
					                	<button type="submit" mailing-type="recipients" class="btn btn-primary mailing-form-submit">Invia mail a indirizzi specificati</button>
					                	<button type="submit" mailing-type="all" class="btn btn-warning mailing-form-submit">Invia a tutti i climber</button>
					                	<button type="submit" mailing-type="subscribed" class="btn btn-success mailing-form-submit">Invia a tutti gli iscritti dell'anno corrente</button>
					                	<button type="submit" mailing-type="nocertificate" class="btn btn-danger mailing-form-submit">Invia agli iscritti dell'anno corrente senza certificato</button>
					              	</div>
	            				</c:when>
								<c:otherwise>
									<div class="box-body">
										${param.result}			
									</div>
									<div class="box-footer">
					                	<button type="submit" class="btn btn-primary" onclick="location.href='${pageContext.servletContext.contextPath}/mailing'">Ok</button>
					                	<button type="submit" class="btn btn-primary" onclick="window.history.back();">Indietro</button>
					              	</div>
								</c:otherwise>            				
            				</c:choose>
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
				
				$("#mailing-text").wysihtml5({
					"locale": "it-IT",
					"toolbar": {
						"image": false,
						"color": true,  
					    "blockquote": false
					}
				});
				
				$(".mailing-form-submit").on("click", function(){
					
					if($("#mailing-subject").val() == "") {
						var n = noty({
							text: "Inserire l'oggetto della mail",
		       				layout: "bottom",
		       				theme: "climbing",
		       				modal: false,
		       				timeout: 3000,
		       				type: "error"
		       			});
						return;
					}
					if($("#mailing-text").val() == "") {
						var n = noty({
							text: "Inserire il testo della mail",
		       				layout: "bottom",
		       				theme: "climbing",
		       				modal: false,
		       				timeout: 3000,
		       				type: "error"
		       			});
						return;
					}
					$("#mailing-type").val($(this).attr("mailing-type"));
					if($("#mailing-type").val() == "recipients") {
						if($("#mailing-recipients").val() == "") {
							var n = noty({
								text: "Specificare i destinatari",
			       				layout: "bottom",
			       				theme: "climbing",
			       				modal: false,
			       				timeout: 3000,
			       				type: "error"
			       			});
							return;
						} else {
							$("#mailing-form").submit();
						}
					} else if($("#mailing-type").val() == "all") {
						var n = noty({
							text: "Inviare la mail a tutti i climber?",
		       				layout: "center",
		       				theme: "climbing",
		       				modal: true,
		       				type: "notification",
		       				buttons: [
								{addClass: "btn btn-primary", text: "Si", 
									onClick: function($noty) {
										$("#mailing-form").submit();
									}
								},
								{addClass: "btn btn-danger", text: "No", onClick: function($noty) {$noty.close();}}
							]
		       			});
					} else if($("#mailing-type").val() == "subscribed") {
						var n = noty({
							text: "Inviare la mail a tutti gli iscritti?",
		       				layout: "center",
		       				theme: "climbing",
		       				modal: true,
		       				type: "notification",
		       				buttons: [
								{addClass: "btn btn-primary", text: "Si",
									onClick: function($noty) {
										$("#mailing-form").submit();
									}
								},
								{addClass: "btn btn-danger", text: "No", onClick: function($noty) {$noty.close();}}
							]
		       			});
					} else if($("#mailing-type").val() == "nocertificate") {
                        var n = noty({
                            text: "Inviare la mail a tutti gli iscritti senza certificato?",
                            layout: "center",
                            theme: "climbing",
                            modal: true,
                            type: "notification",
                            buttons: [
                                {addClass: "btn btn-primary", text: "Si",
                                    onClick: function($noty) {
                                        $("#mailing-form").submit();
                                    }
                                },
                                {addClass: "btn btn-danger", text: "No", onClick: function($noty) {$noty.close();}}
                            ]
                        });
                    }
				});
			});
		</script>
	</tiles:putAttribute>
	
</tiles:insertDefinition>

