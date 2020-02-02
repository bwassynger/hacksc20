<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<title>Ceres</title>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

	<link href="https://fonts.googleapis.com/css?family=Courgette&display=swap" rel="stylesheet">

	<link href="css/styles.css" rel="stylesheet">
	<script>
      function logout() {
    	  var xhr = new XMLHttpRequest();
    	  xhr.open("GET", "logoutServlet", false);
    	  xhr.send();
    	  <% loggedIn = false; %>
      }
      </script>
</head>
	<body>
  <%! 
  	boolean loggedIn = false;
  %>
  <%
  	if( session.getAttribute("user") != null ) {
  		loggedIn = true;
  	}
  %>
		<nav class="navbar navbar-light navbar-expand-md">
		  <a class="navbar-brand" href="#">CERES</a>
		  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar1">
		    <span class="navbar-toggler-icon"></span>
		  </button>
		  <div class="collapse navbar-collapse" id="navbar1">
		    <ul class="navbar-nav mr-auto">
		      <li class="nav-item active">
		        <a class="nav-link" href="home.jsp">Home</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="map.html">Map</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="quiz.jsp">Quiz</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="aboutus.html">Our Story</a>
		      </li>
		      <li class="nav-item">
	  <% if(loggedIn) {%>
	   			<a id="login" class="nav-link" href="home.jsp" onclick="logout()">Log Out</a>
	  <% } else { %>
				<a id="login" class="nav-link" href="login.jsp">Login</a>
			  </li>
			  <li>
			  	<a id="logout" class="nav-link" href="register.jsp">Register</a>
			  </li>
	  <% } %>
		      </li>
		    </ul>
		  </div>
		</nav>

		<div class="container-fluid">
			<div class = "row" style = "border-top: solid 1px grey;">
				<div class = "col-1"></div>
				<div class = "col-10 col-custom">
					<div class="jumbotron jumbotron-fluid" id="header">
					  <div class="container header_title">
					    <h1 class="display-2">Ceres</h1>
					    <p class="lead">How environmentally friendly are we?</p>
					  </div>
					</div>
					
				</div>
				<div class = "col-1"></div>
			</div>
			<div class="row">
				<div class = "col-6 col-quiz">
					<button class="quiz quiz-1" id="quizButton">Take the quiz now!</button>
				</div>
				<div class = "col-6 col-quiz quiz-2">
					<button class="quiz" id="resultButton">Explore Results</button>
				</div>
				
			</div>
			<div class="row">
				<!-- must use col or similar -->
				
			</div>
			<div class="foreground"></div>

		</div>


		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
		<script>
			document.getElementById("quizButton").addEventListener("click", function(){
				window.location.href= "quiz.jsp";
			});

			document.getElementById("resultButton").addEventListener("click", function(){
				window.location.href= "map.html";
			});
		</script>

	</body>
</html>

