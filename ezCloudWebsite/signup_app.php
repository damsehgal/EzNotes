<?php
include_once 'dbconnect_app.php';
session_start();
{	
	$username = $_POST['username'];
	$password =	$_POST['password'];
	$device   = $_POST['device'];
	$salt 	  = uniqid(mt_rand(), true);

	$res = mysqli_query($con,"SELECT * FROM users WHERE username='$username'");
	mysqli_store_result($con);
	if(mysqli_num_rows($res) != 0)
		echo "User Already exists";
	else
	{
		$sessionID = session_id();  
		$res = mysqli_query($con,"INSERT INTO users (username, password, lastDevice , lastSession , salt) VALUES ('$username', '$password', '$device' , '$sessionID' , '$salt');");
		mysqli_store_result($con);
		echo "Inserted";
	}
}
?>
