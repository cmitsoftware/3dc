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
   				<div class="row">
        			<div class="col-xs-12">
          				<div class="box">
            				<div class="box-header">
 								<div class="col-xs-12 col-sm-4 col-md-2">
              						<button type="button" class="btn btn-block btn-primary"
              							onclick="detail()">
              							Aggiungi
              						</button>
	            				</div>
							   <div class="col-xs-0 col-sm-8 col-md-10"></div>
							</div>
	            			<!-- /.box-header -->
	            			<div class="box-body">
		            			<div class="">
		              				<table id="persons-table" class="table table-bordered table-hover" style="width:100%">
										<thead>
											<tr>
												<th class="all">N.</th>
												<th class="all">Nome</th>
												<th class="all">Cognome</th>
												<th class="min-phone-l">Iscrizione</th>
												<th class="min-phone-l">Abbonamento</th>
												<th class="min-phone-l">Certificato</th>
												<th class="min-phone-l">Entrata libera</th>
												<th class="min-phone-l"></th>
											</tr>
										</thead>
										<tbody>
		     							</tbody>
		    						</table>
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
			var personsTable;
			$(document).ready(function(){
				//alert("ok");
				personsTable = $("#persons-table").DataTable({
					"processing": true,
	       			"serverSide": true,
	       			"responsive": true,
	       			"pageLength": 50,
	       			"lengthMenu": [5, 10, 50, 100],
	       			"dom": "frtpl",
	       			"ajax": {
	        			"url": "${pageContext.request.contextPath}/persons?method=search"
	       			},
	       			"columns": [
						/*{ "data": "id" },*/
						{ "data": "number" },
		    			{ "data": "name" },
		    			{ "data": "surname"},
		    			{ "data": "registrationValid" },
		    			{ "data": "subscriptionValid" },
		    			{ "data": "certificationValid" },
		    			{ "data": "freeEntryAvailable" },
		    			//{ "data": null },
		    			{ "data": null }
		    		],
		    		"language": {    
						"paginate": {      
							"next": "&gt;",
							"previous": "&lt;"
						},
						"search": "",
						"searchPlaceholder": "Cerca...",
						"lengthMenu": "Mostra _MENU_",
						"emptyTable": "-"
					},
		    		"columnDefs": [
					{	
						"targets": 0,
						"responsivePriority": 1
					},
					{	
						"targets": 1,
						"responsivePriority": 2
					},
					{	
						"targets": [3,4,5,6], 
						"render": function ( data, type, row ) {
							if(data) {
		  						return "<span class='glyphicon glyphicon-ok' aria-hidden='true'></span>";
							} else {
								return "<span class='glyphicon glyphicon-remove' aria-hidden='true'></span>";
							}
		                },
		                "createdCell": function (td, cellData, rowData, row, col) {
		                	if ( cellData == false ) {
		                    	$(td).addClass('danger');
		                    } else {
		                    	$(td).addClass('success')
		                    }
	                	}	
					},
					/*
					{ 
		    			"targets": -2,
		    			"searchable": false,
	     				"orderable": false,
	     				"render": function ( data, type, row ) {
	  							return "<span onclick='editPerson(" + data.id + ")' class='glyphicon glyphicon-edit' aria-hidden='true'></span>";
		                }
					},
					*/
					{ 
		    			"targets": -1,
		    			"searchable": false,
	     				"orderable": false,
	     				"render": function ( data, type, row ) {
	  							return "<a style='cursor:pointer;'><span style='margin-left:30px;' onclick='detail(" + data.id + ")' class='glyphicon glyphicon-edit' aria-hidden='true'></span></a>"
	  							+ "  <a style='cursor:pointer;'><span style='margin-left:30px;' onclick='deletePerson(" + data.id + ",&quot;" + data.name + "&quot;,&quot;" + data.surname + "&quot;)' class='glyphicon glyphicon-trash' aria-hidden='true'></span></a>";
		                }
					}
					]
				});
			});
			
			function detail(id){
				
				var url = "${pageContext.servletContext.contextPath}/persons?method=detail";
				if(typeof(id) != 'undefined'){
					url += "&id=" + id;
				}
				location.href = url;
			}
			
			function deletePerson(id, name, surname) {
				var n = noty({
					text: "Eliminare " + name + " " + surname + "?",
       				layout: "center",
       				theme: "climbing",
       				modal: true,
       				type: "notification",
       				buttons: [
						{addClass: "btn btn-primary", text: "Si", 
							onClick: function($noty) {
								location.href = "${pageContext.servletContext.contextPath}/persons?method=delete&id=" + id;
							}
						},
						{addClass: "btn btn-danger", text: "No", onClick: function($noty) {$noty.close();}}
					]
       			});
			}
		</script>
	</tiles:putAttribute>
	
</tiles:insertDefinition>

