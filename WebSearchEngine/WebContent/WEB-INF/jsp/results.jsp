<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Data Lake Homepage</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/dropzone/4.3.0/min/dropzone.min.css" rel="stylesheet">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/dropzone/4.3.0/min/dropzone.min.js"></script>
	<link href="<c:url value="/resources/css/index.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/results.css" />" rel="stylesheet">
	<link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/vis/4.16.1/vis.min.js"></script>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/vis/4.16.1/vis.min.css" rel="stylesheet">
	
</head>
<body>
	
	<div id="top-bar">
		<h2 id="header-title">
			550 Data Lake 
		</h2>
		<c:choose>
	  		<c:when test="${!isLoggedIn}">
				<div class="bar-button-box">
					<button class="bar-button" data-toggle="modal" data-target="#logInModal">
						Log In
					</button>
				</div>
				<div class="bar-button-box">
					<button class="bar-button" data-toggle="modal" data-target="#signUpModal">
						 Sign Up
					</button>
				</div>
			</c:when>
			<c:otherwise>
			  <div class="bar-button-box">
			  		<form action="/logout" method="post"> 
						<button class="bar-button" data-toggle="modal" data-target="#signUpModal">
							 Log Out
						</button>
					</form>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-12 text-center">
	
				<!-- Flash Messages -->
				<c:if test="${resultMessage != null}">
					<div class="text-center alert alert-success alert-dismissible custom-alert" role="alert">
					    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
					  	    <span aria-hidden="true">&times;</span>
					    </button>
					    ${resultMessage}
					</div>
				</c:if>
				
				<!-- Search Input Panel -->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h1 class="panel-title">Search the Data Lake</h1>
					</div>
				  	<div class="panel-body">
				    	<div>
							<form action="/search" method="get">
								<input type="text" name="query" placeholder="Search" id="search-entry-box">
								<br>
							  	<button type="submit" class="search-button btn btn-default">Search</button
							</form>
						</div>
				  	</div>
				</div>
				
				RESULTS HERE
				<div id="mynetwork"></div>
					
			</div>
		</div>
	</div>
	
	

	<script type="text/javascript">
	    // create an array with nodes
	    var nodes = new vis.DataSet([
	        {id: 1, label: 'Node 1'},
	        {id: 2, label: 'Node 2'},
	        {id: 3, label: 'Node 3'},
	        {id: 4, label: 'Node 4'},
	        {id: 5, label: 'Node 5'}
	    ]);
	
	    // create an array with edges
	    var edges = new vis.DataSet([
	        {from: 1, to: 3, arrows:'to'},
	        {from: 1, to: 2, arrows:'to'},
	        {from: 2, to: 4, arrows:'to'},
	        {from: 2, to: 5, arrows:'to'}
	    ]);
	
	    // create a network
	    var container = document.getElementById('mynetwork');
	
	    // provide the data in the vis format
	    var data = {
	        nodes: nodes,
	        edges: edges
	    };
	    var options = {};
	
	    // initialize your network!
	    var network = new vis.Network(container, data, options);
	</script>
		
	
</body>
</html>