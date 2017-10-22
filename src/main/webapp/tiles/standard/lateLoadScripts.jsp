<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!-- jQuery 2.1.4 -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<!-- Bootstrap 3.3.5 -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/bootstrap/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/fastclick/fastclick.js"></script>
<!-- Datatables -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/datatables/extensions/Responsive/js/dataTables.responsive.min.js"></script>
<!-- Datepicker -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/datepicker/locales/bootstrap-datepicker.it.js" charset="UTF-8"></script>
<!-- Wysihtml5 -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/bootstrap-wysihtml5/locales/bootstrap-wysihtml5.it-IT.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/dist/js/app.min.js"></script>
<!-- Sparkline -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/sparkline/jquery.sparkline.min.js"></script>
<!-- jvectormap -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- SlimScroll 1.3.0 -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- iCheck - same checkbox on all devices -->
<script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/iCheck/icheck.min.js"></script>
<!-- ChartJS 1.0.1 -->
<%-- <script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/plugins/chartjs/Chart.min.js"></script> --%>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<%-- <script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/dist/js/pages/dashboard2.js"></script> --%>
<!-- AdminLTE for demo purposes -->
<%-- <script src="${pageContext.servletContext.contextPath}/resources/admin-lte-2.3/dist/js/demo.js"></script> --%>

<script src="${pageContext.request.contextPath}/resources/noty/jquery.noty.packaged.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/noty/themes/climbing.js"></script>

<c:if test="${not empty message}">
	<script>
		$(document).ready(function (){
			var n = noty({
				text: "${message}",
				layout: "bottom",
				theme: "climbing",
				modal: false,
				timeout: 5000,
				type: "information"
			});
		});
	</script>
</c:if>
