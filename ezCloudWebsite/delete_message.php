<?php
	include_once 'dbconnect_app.php';
	$message_id = $_POST['message_id'];
	$sql = "DELETE FROM `messages` WHERE message_id = '$message_id'";
	$result=mysqli_query($con,$sql);
	mysqli_store_result($con);
	echo "success";
?>