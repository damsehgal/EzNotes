<?php
session_start();
include_once 'dbconnect.php';

if(isset($_SESSION['user'])!="")
{
	header("Location: home.php");
}
?>
<!-- 
	HTML WORK CAN BE DONE LATER 
	<!DOCTYPE html>
	<html>
	<head>
		<title>
			Index Page
		</title>
	</head>
	<body>
		<form method="post" action="index2.php">
			<input type="text" name="username" placeholder="username" required />
			<input type="password" name="password" placeholder="password" required/>
			<input type="submit">
		</form>
	</body>
	</html>
 -->