<?php
include_once 'dbconnect_app.php';
{	
	session_id($_POST['sess_ID']); 
	session_start();
	$username = $_POST['username'];
	$password =	$_POST['password'];
	$res = mysqli_query($con,"UPDATE users SET password = '$password' WHERE username = '$username';");
	mysqli_store_result($con);
	$_SESSION['password'] = $_POST['password'];
	echo "Changed";
	echo "<br>";
	echo "$username";
}
?>