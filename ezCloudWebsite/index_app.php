<?php
include_once 'dbconnect_app.php';
if(isset($_POST['sess_id']))
{

	$session_from_shared = $_POST['sess_id'];
	$device = $_POST['device'];
	$res = mysqli_query($con,"SELECT * FROM users WHERE lastSession = '$session_from_shared' and lastDevice = '$device' ");
	mysqli_store_result($con);
	if (mysqli_num_rows($res) == 0)
	{
		echo "No Previous User";
	}
	else
	{ 
		$row = mysqli_fetch_array($res,MYSQLI_NUM);	
		echo $row[0].'<br>'.$row[1];
		session_start();
		$session_id_current = session_id();
		$_SESSION['username'] = $row[0];
		$_SESSION['password'] = $row[1];
		$username = $_SESSION['username'];
		$query = "UPDATE users SET  lastSession = '$session_id_current' where username = '$username' ";
		$res = mysqli_query($con,$query);
		mysqli_store_result($con);
		echo "<br>";
		echo "$session_id_current";
	}
}
else
{	
	session_start();
	$username = $_POST['username'];
	$password =	$_POST['password'];
	$device   = $_POST['device'] ; 
	$_SESSION['username'] = $username;
	$_SESSION['password'] = $password;
	$res = mysqli_query($con,"SELECT * FROM users WHERE username='$username' and password = '$password'");
	mysqli_store_result($con);
	if(mysqli_num_rows($res) == 0)
	{
		$res = mysqli_query($con , "SELECT * FROM users WHERE username='$username'" );
		if (mysqli_num_rows($res) == 0)
			echo "No User Found";
		else
			echo "password incorrect";
	}
	else
	{
		$session_id = session_id();
		$res = mysqli_query($con , "UPDATE users SET lastDevice = '$device' , lastSession = '$session_id' where username = '$username' ");
		echo "true";
		echo "<br>";
		echo session_id();
	}
}
?>
