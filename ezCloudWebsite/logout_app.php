<?php
	session_id($_POST['sess_id']);
	session_start();
	session_destroy();
	unset($_SESSION['sess_id']);
	unset($_SESSION['username']);
	unset($_SESSION['password']);
	echo "Logged Out";
?>