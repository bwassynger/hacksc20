<!DOCTYPE html>
<html>
<head>
	<title>Ceres</title>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

	<link href="https://fonts.googleapis.com/css?family=Courgette&display=swap" rel="stylesheet">

	<link href="css/styles.css" rel="stylesheet">

	<style>
		#quizHeader{
			text-align: center;
			margin-top: 25px;
			color: #014268;
			font-family: 'Courgette', cursive;
		}

		.questionBox {
			margin-top: 40px;
			text-align: center;
			background-color: lightblue;
			padding-top: 100px;
			padding-bottom: 100px;
			box-shadow: 0px 0px 2px 2px;
		}

		#next {
			position: relative;
			z-index: 101;
			border-radius: 5px;
			background-color: #014268;
			color: white;
			margin-top: 10px;
			padding: 10px;
		}
		
		#send-btn {
			position: relative;
			z-index: 101;
			border-radius: 5px;
			background-color: #014268;
			color: white;
			margin-top: 10px;
			padding: 10px;
		}

		#myQuestion {
			position: relative;
			z-index: 101;

		}

		.choices {
			font-size: 20px;
			position: relative;
			z-index: 101;
		}

	</style>

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
		      <li class="nav-item">
		        <a class="nav-link" href="home.jsp">Home</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="results.html">Map</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="profile.html">Profile</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="aboutus.html">Our Story</a>
		      </li>
		      <li class="nav-item">
	  <% if(loggedIn) {%>
	   			<a id="login" class="nav-link" href="home.jsp" onclick="logout()">Log Out</a>
	  <% } else { %>
				<a id="login" class="nav-link" href="login.jsp">Login</a>
	  <% } %>
		      </li>
		    </ul>
		  </div>
		</nav>
		<h1 id="quizHeader">Quiz</h1>

		<div class="foreground"></div>

		<div class="container-fluid">
			<div class="row">
				<div class="col-3"></div>
				<div class="col col-6 questionBox">
					<h3 class="question" id="myQuestion"></h3>
					<div class="choices">
						<input type="radio" name="answer" value = "1" id="question1" checked="checked"><span>Choice 1</span><br>
						<input type="radio" name = "answer" value = "2" id="question2"><span>Choice 2</span><br>
						<input type="radio" name = "answer" value = "3" id="question3"><span>Choice 3</span><br>
						<input type="radio" name = "answer" value = "4" id="question4"><span>Choice 4</span><br>
					</div>
					<button id="next">Next Question</button>
					<form id="resultForm" action="quizServlet" method="post">
					<div id="zip" style="display: none">Zip Code
					<input name="zip-input">
					</div>
					<button id="send-btn" type="submit" style="display: none;">Compare my results!</button>
					<input id="result" name="result" type=text style="display: none;">
					</form>
				</div>

				<div class="col-3"></div>

			</div>
		</div>

		<script>
			var questions = [
				"How do you get to work?",
				"How's the mileage of your car (combined city/highway mpg)?",
				"How much do you drive per year?",
				"What is your diet?",
				"What are your trash habits?",
				"How much do you spend on electricity ($/month)?",
				"What are your bathing habits most like (adjust for how often you shower)?"
			]

			var answers = [
				"Car", "Car Pool", "Public Transit", "Bike/Walk",
				"less than 20", "20-32", "32-40", "40+",
				"less than 5000 miles", "10,000 miles", "15,000 miles", "20,000+ miles",
				"I eat meat often", "I sometimes eat meat", "I am vegetarian", "I am vegan",
				"I rarely recycle", "I sometimes recycle", "I usually recycle", "I recycle and compost",
				"less than 60", "60-85", "85-100", "100+",
				"I take baths at least once a week", "I take showers over 10 minutes most days", "I take showers 5-10 minutes most days", "I take showers less than 5 minutes most days"
			]

			var qNum = 0;
			var score = 0;

			function displayQuestion(){
				document.querySelector(".question").innerHTML = questions[qNum];
				document.querySelector("#question1").nextSibling.innerHTML = answers[qNum*4];
				document.querySelector("#question2").nextSibling.innerHTML = answers[qNum*4+1];
				document.querySelector("#question3").nextSibling.innerHTML = answers[qNum*4+2];
				document.querySelector("#question4").nextSibling.innerHTML = answers[qNum*4+3];
				document.querySelector("#question1").checked = true;

			}

			function displayResult(){
				//score calculations
				score = score + 4;
				var co2 = 45 - score;
				score = score * 25 - 1;
				
				document.querySelector(".question").innerHTML = "Your environmental index is " + score + "<br />" + 
								"Your estimated CO2 output is " + co2 + " tons/year";

				document.querySelector(".choices").style.display = "none";
				document.querySelector("#next").style.display = "none";
				
				document.querySelector("#send-btn").style.display = "inline";
				document.querySelector("#result").value = score;
				
				<% if(!loggedIn) {%>
				document.querySelector("#zip").style.display = "inline";
				<% } %>
				//send to database and stuffs
			}

			document.getElementById("next").addEventListener("click", function(){
				if(qNum < 6){
					//ADD THEIR ANSWER TO THE SCORE AND RESET THE QUESTIONS
					if(document.getElementById("question1").checked){
						score = score;
					}else if(document.getElementById("question2").checked){
						score = score + 2;
					}else if(document.getElementById("question3").checked){
						score = score + 4;
					}else{
						score = score + 6;
					}
					qNum++;
					console.log("button pressed");
					displayQuestion();
				}else{
					//add to score first
					displayResult();
				}
			});

			displayQuestion();



		</script>
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

	</body>
	</html>