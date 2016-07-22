<?php
session_start();
include_once 'dbconnect.php';
// username is used at all places for username and password is used at all places for password
if(isset($_SESSION['username'])!="")
{
	header("Location: home.php");
}

$username = mysql_real_escape_string($_POST['username']);
$password = mysql_real_escape_string($_POST['password']);
$res = mysqli_query("SELECT * FROM users WHERE username='$username' and password = '$password'");
if(mysqli_num_rows($res) == 0)
{
	// username / password Mismatch
	$res = mysqli_query("SELECT * FROM users WHERE username='$username'")
	if (mysqli_num_rows($res) == 0)
		echo "No such user Found Please create New Account";
	else
		echo "password incorrect";
	header("Location: index.php");
}
else
{
	$_SESSION['username'] = $username;
	$_SESSION['password'] = $password;
	echo "true";
	header("Location: home.php");
}

?>