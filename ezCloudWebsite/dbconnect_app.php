<?php
include_once 'keys.php';
$con = mysqli_connect("mysql.hostinger.in","u386866041_dam",$password,"u386866041_ez");
if (mysqli_connect_errno())
{
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
	exit();
}
?>