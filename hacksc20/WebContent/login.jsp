<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>User Login</title>
 <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

	<link href="https://fonts.googleapis.com/css?family=Courgette&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css">
<script src="scripts.js"></script>
<script>
function login() {
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "loginServlet", true);
	xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xhr.onload = function() {
		if(xhr.readyState == 4 && xhr.status == 200) {
			window.location.href="home.jsp";
		}
		else if(xhr.readyState == 4 && xhr.status == 403) {
			document.getElementById("formError").innerHTML = "Incorrect Password";
		}
		else if(xhr.readyState == 4 && xhr.status == 404) {
			document.getElementById("formError").innerHTML = "User Not Found";
		}
	};
	var user = document.userLogin.userName.value;
	var pass = document.userLogin.userPass.value;
	if(user && pass) {
		xhr.send(encodeURI("userName="+user+"&userPass="+pass));
	} else {
		document.getElementById("formError").innerHTML = "Fill out all fields";
		return false;
	}
	
}
</script>
</head>
<body>
<%
	//int RespCode = request.
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
		        <a class="nav-link" href="results.html">Map</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="#">Profile</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="#">Our Story</a>
		      </li>
		    </ul>
		  </div>
		</nav>
<div class="container-fluid">
			<div class="row">
				<div class="col-3"></div>
				<div class="col question col-6">
					<div id="login_panel">
					    <form name="userLogin" method="POST" onsubmit="event.preventDefault(); login();">
					    	<div id="formError"></div>
					    	<div>Username</div>
					    	<input class="userForm" name="userName" type="text">
					    	<div>Password</div>
					    	<input class="userForm" name="userPass" type="text">
					    	<div id="submit-btn">
					    		<button id="loginButton">Sign In</button>
					    	</div>
					    </form>
					</div>
				</div>
				<div class="col-3"></div>

			</div>
		</div>
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</body>
</html>